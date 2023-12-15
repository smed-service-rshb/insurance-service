package ru.softlab.efr.services.insurance.task;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.DecisionAnswer;
import ru.softlab.efr.clients.model.rest.DecisionOption;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.DecisionInsuranceRequest;
import ru.softlab.efr.services.insurance.model.rest.FoundInsurance;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.ProgramSettingService;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode.*;
import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.*;


/**
 * Проверить наличие существующих контрактов по клиенту
 */
@Component
public class CheckExistingContractsTask implements JavaDelegate {

    private static final int MIN_DAY_PERIOD = 30;
    private final static String DOCUMENT_CHECK_SETTINGS = "documentDataChecks";
    private static final Logger LOGGER = Logger.getLogger(CheckExistingContractsTask.class);
    private JsonHelper jsonHelper;
    private InsuranceService insuranceService;
    private ProgramSettingService programSettingService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    @Autowired
    public void setInsuranceService(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Autowired
    public void setProgramSettingService(ProgramSettingService programSettingService) {
        this.programSettingService = programSettingService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if (Boolean.TRUE.equals(Boolean.valueOf(settingsService.get(DOCUMENT_CHECK_SETTINGS).getValue()))) {
            setDefaultValues(execution);
            SetStatusInsuranceModel model = jsonHelper.deserialize((String) execution.getVariable(ContextVariablesNames.CONTRACT_DATA), SetStatusInsuranceModel.class);
            //Вычисляем ID клиента
            Long clientId = model.getInsuredId();
            if (clientId == null && model.getInsuredData() != null) {
                if (model.getInsuredData().getId() != null) {
                    clientId = Long.valueOf(model.getInsuredData().getId());
                }
            }
            if (clientId == null && model.isHolderEqualsInsured()) {
                clientId = model.getHolderId();
                if (clientId == null && model.getHolderData() != null && model.getHolderData().getId() != null) {
                    clientId = Long.valueOf(model.getHolderData().getId());
                }
            }
            if (clientId == null) {
                return;
            }
            //Вычислить программу страхования и тип программы
            Long currentInsuranceId = model.getId();
            Long programSettingId = model.getProgramSettingId();
            if (programSettingId == null) return;
            ProgramSetting programSetting = programSettingService.findById(programSettingId);
            //Поиск договоров с той же программой
            List<Insurance> foundInsurances = insuranceService.findAllByProgramAndClientAndEndDateMoreThan(programSetting.getProgram().getId(), clientId, LocalDate.now().plusDays(MIN_DAY_PERIOD));
            foundInsurances = filter(currentInsuranceId, foundInsurances);
            if (!CollectionUtils.isEmpty(foundInsurances)) {
                //•	У найденных договоров срок окончания больше или равен (текущей дате + 90 дней). У найденных договоров срок окончания больше или равен (текущей дате + 90 дней)
                LocalDate nowPlus89Days = LocalDate.now().plusDays(89);
                LocalDate nowPlus30Days = LocalDate.now().plusDays(30);
                LocalDate nowPlus90Days = LocalDate.now().plusDays(90);
                if (foundInsurances.stream().anyMatch(insurance -> insurance.getEndDate().isAfter(nowPlus89Days))) {
                    Insurance first = foundInsurances.get(0);
                    execution.setVariable(ERROR_MESSAGE, String.format("До окончания срока действия действующего договора № %s осталось более 3х месяцев." +
                            " Оформление договора невозможно.", first.getContractNumber()));
                    return;
                } else if (foundInsurances.stream().anyMatch(insurance -> insurance.getEndDate().isAfter(nowPlus30Days) && insurance.getEndDate().isBefore(nowPlus90Days))) {
                    Insurance first = foundInsurances.get(0);
                    if (!first.getEndDate().plusDays(1).isEqual(model.getStartDate())) {
                        //У найденных договоров срок окончания меньше (текущей даты + 90 дней) и больше (текущей даты + 30 дней).
                        String msg = String.format("В отношении Застрахованного действует договор страхования № %s со сроком окончания %s." +
                                        " Оформление договора возможно с даты следующей за датой окончания действия указанного договора",
                                first.getContractNumber(),
                                first.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
                        );
                        DecisionInsuranceRequest decisionRequest = new DecisionInsuranceRequest(msg, DecisionOption.CONTINUE, DecisionOption.CANCEL);
                        execution.setVariable(INSURANCE_DECISION_DATA, jsonHelper.serialize(decisionRequest));
                        execution.setVariable(ContextVariablesNames.FIXED_DATE_START, first.getEndDate().plusDays(1));
                        return;
                    }
                }
            }
            if (model.getNewStatus() == InsuranceStatusType.MADE) {
                //Ищем договора по следующим условиям:
                //Застрахованное лицо аналогичное застрахованному лицу, указанному в новом договоре (сравнивается по идентификаторам клиентов).
                //Программа страхования у оформляемого договора отличается от программы страхования действующего договора (сравнивается по идентификатору программы страхования),
                // при этом вид программы страхования у действующего договора «КСП».
                foundInsurances = insuranceService.findAllByProgramKindAndClient(programSetting.getProgram().getType(), clientId);
                foundInsurances = filter(currentInsuranceId, foundInsurances);
                if (!CollectionUtils.isEmpty(foundInsurances)) {
                    String msg = "Для Застрахованного найдены действующие договоры. Необходимо подтверждение, что при их наличии возможно оформление текущего договора";
                    List<FoundInsurance> insuranceList = foundInsurances.stream()
                            .map(i -> new FoundInsurance(
                                    i.getId(),
                                    i.getContractNumber(),
                                    i.getStartDate(),
                                    i.getEndDate(),
                                    i.getCloseDate(),
                                    i.getProgramSetting().getProgram().getName()
                            )).collect(Collectors.toList());
                    DecisionInsuranceRequest decisionRequest = new DecisionInsuranceRequest(msg, DecisionOption.CONTINUE, DecisionOption.CANCEL);
                    decisionRequest.setInsuranceList(insuranceList);
                    execution.setVariable(INSURANCE_DECISION_DATA, jsonHelper.serialize(decisionRequest));
                    return;
                }
            }
        }
    }

    private void setDefaultValues(DelegateExecution execution) {
        execution.setVariable(ERROR_MESSAGE, null);
        execution.setVariable(INSURANCE_DECISION_DATA, null);
        execution.setVariable(EMPLOYEE_DECISION, DecisionAnswer.CONTINUE);
        execution.setVariable(ContextVariablesNames.FIXED_DATE_START, null);
    }

    private List<Insurance> filter(Long currentInsuranceId, List<Insurance> insurances) {
        if (CollectionUtils.isEmpty(insurances)) return insurances;
        //Убираем ID текущей страховки
        if (currentInsuranceId != null) {
            insurances = insurances.stream()
                    .filter(insurance -> !insurance.getId().equals(currentInsuranceId))
                    .filter(insurance -> insurance.getEndDate() != null)
                    .collect(Collectors.toList());
        }
        Set<InsuranceStatusCode> statusCodes = Stream.of(
                MADE, //Оформлен,
                CHANGING_APPLICATION_RECEIVED, // Получено заявление на изменение договора,
                NEED_WITHDRAW_APPLICATION, // Требуется заявление о выплате,
                WITHDRAW_APPLICATION_RECEIVED, // Получено заявление о выплате,
                PAYMENT_FULFILLED, // Выплата произведена,
                REFUSING_APPLICATION_RECEIVED, // Получено заявление об отказе,
                CANCELLATION_APPLICATION_RECEIVED // Получено заявление о расторжении договора,
        ).collect(Collectors.toSet());
        //Убираем с пустой датой окончания и неподходящими статусами
        insurances = insurances.stream()
                .filter(insurance -> insurance.getEndDate() != null && insurance.getStatus() != null)
                .filter(insurance -> statusCodes.contains(insurance.getStatus().getCode()))
                .collect(Collectors.toList());
        return insurances;
    }

}
