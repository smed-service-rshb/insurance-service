package ru.softlab.efr.services.insurance.process;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataStore;
import ru.softlab.efr.services.authorization.ThreadLocalPrincipalDataStore;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.Phone;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.pojo.Result;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.*;

/**
 * Сохранить данные клиента и договора
 */
@Component
public class SaveContractAndClientDataTask implements JavaDelegate {

    private static final Logger LOGGER = Logger.getLogger(SaveContractAndClientDataTask.class);

    private JsonHelper jsonHelper;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceConverter insuranceConverter;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ClientShortDataService clientShortDataService;

    @Autowired
    private EmployeesClient employeesClient;

    private ClientCheckService clientCheckService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrgUnitCachedClientService orgUnitCachedClientService;

    @Autowired
    public void setClientCheckService(ClientCheckService clientCheckService) {
        this.clientCheckService = clientCheckService;
    }

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        setDefaultValues(execution);

        SetStatusInsuranceModel model = jsonHelper.deserialize((String) execution.getVariable(ContextVariablesNames.CONTRACT_DATA), SetStatusInsuranceModel.class);
        PrincipalDataImpl principalData = jsonHelper.deserialize((String) execution.getVariable(ContextVariablesNames.PRINCIPAL_DATA), PrincipalDataImpl.class);
        String insuranceContractNumber = (String) execution.getVariable(ContextVariablesNames.CONTRACT_NUMBER);
        //Для сохранения в истории автора изменений
        PrincipalDataStore ds = new ThreadLocalPrincipalDataStore();

        String branch = null, office = null, employeeName = null;
        Long employeeId = null, officeId = null;
        if (model.getEmployeeId() == null || principalData.getId().equals(model.getEmployeeId())) {
            if (ds.getPrincipalData() == null) {
                ds.setPrincipalData(principalData);
            }
            branch = principalData.getBranch();
            office = principalData.getOffice();
            employeeId = principalData.getId();
            employeeName = principalData.getSecondName() + " "
                    + principalData.getFirstName() + " "
                    + principalData.getMiddleName();
            officeId = principalData.getOfficeId();
        } else {
            EmploeeDataWithOrgUnits employee = employeesClient.getEmployeeByIdWithOutPermission(model.getEmployeeId(), 10L);
            employeeId = employee.getId();
            employeeName = employee.getSecondName() + " "
                    + employee.getFirstName() + " "
                    + employee.getMiddleName();

            if (employee.getOrgUnits() != null && employee.getOrgUnits().size() > 0) {
                office = employee.getOrgUnits().get(0).getOffice();
                officeId = employee.getOrgUnits().get(0).getId();
                branch = employee.getOrgUnits().get(0).getBranch();
            }
        }

        List<String> errorsMsg = new ArrayList<>();

        Long id = model.getId();
        Insurance insurance;
        if (id != null) {
            insurance = insuranceService.findById(id);
        } else {
            insurance = new Insurance();
        }
        if (insurance == null) {
            errorsMsg.add("Не удается найти объект с id=" + id);
            throw new ValidationException(errorsMsg);
        }

        if (insuranceContractNumber == null) {
            insuranceContractNumber = insurance.getContractNumber();
        }

        InsuranceStatusType frontStatus = model.getNewStatus();
        InsuranceStatusCode backStatus = InsuranceStatusCode.DRAFT;
        if (frontStatus != null) {
            backStatus = InsuranceStatusCode.valueOf(frontStatus.name());
        }
        String oldNumber = insurance.getContractNumber();
        Long clientIdToUpdate = clientService.getAuthClientIdToUpdate(insurance.getHolder(), model.getHolderData());
        if (execution.getVariable(ContextVariablesNames.FIXED_DATE_START) != null || null == insurance.getStatus()
                || InsuranceStatusCode.DRAFT == insurance.getStatus().getCode()
                || InsuranceStatusCode.PROJECT == insurance.getStatus().getCode()) {
            BaseInsuranceModel baseInsuranceModel = insuranceConverter.convertSetStatusInsuranceModelToBaseInsuranceModel(model);
            insurance.setFixedStartDate((LocalDate) execution.getVariable(ContextVariablesNames.FIXED_DATE_START));
            insuranceConverter.updateInsuranceFromBaseModel(baseInsuranceModel, insurance, employeeName, officeId, office, branch);
        }

        Result transitionAbility = statusService.isTransitionAvailable(insurance, principalData, backStatus);
        if (transitionAbility.isFail()) {
            errorsMsg.add(transitionAbility.getDescription());
            throw new ValidationException(errorsMsg);
        }
        setStatus(execution, insurance, backStatus, branch, employeeId, employeeName, officeId, office, insuranceContractNumber);

        if (clientIdToUpdate != null) {
            String newPhone = model.getHolderData().getPhones().stream().filter(Phone::isMain).map(Phone::getNumber).findFirst().orElse("");
            String newEmail = model.getHolderData().getEmail() != null ? model.getHolderData().getEmail() : "";
            employeesClient.updateUserWithOutPermissions(clientIdToUpdate,
                    new UpdateUserRq(
                            model.getHolderData().getFirstName(),
                            model.getHolderData().getSurName(),
                            model.getHolderData().getMiddleName(),
                            newPhone, model.getHolderData().getBirthDate(), newEmail), 10L);
        }

        execution.setVariable(ContextVariablesNames.ERROR_NOTIFY, false);
        if (principalData.getRights().contains(Right.CREATE_CONTRACT_IN_CALL_CENTER) && backStatus == InsuranceStatusCode.PROJECT
                && insurance.getProgramSetting().getProgram().getType() == ProgramKind.KSP) {
            execution.setVariable(ContextVariablesNames.ERROR_NOTIFY,
                    !insuranceService.notifyClient(insurance, principalData.getPersonnelNumber(), principalData.getBranch(), principalData.getOffice(),
                            templateService.getAllTemplates(insurance)));
        }
        model.setNewStatus(InsuranceStatusType.valueOf(backStatus.name()));
        model.setId(insurance.getId());
        execution.setVariable(ContextVariablesNames.NEED_SHOW_CHANGE_STATUS,
                oldNumber != null && !oldNumber.equals(insurance.getContractNumber()));
        execution.setVariable(ContextVariablesNames.CONTRACT_ID, insurance.getId());
        execution.setVariable(ContextVariablesNames.CONTRACT_DATA, jsonHelper.serialize(model));
        //Заполняем ID клиента в проведенных ранее проверках по страхователю
        Set<Long> holderCheckIds = (Set<Long>) execution.getVariable(HOLDER_CHECK_IDS);
        if (insurance.getHolder() != null && holderCheckIds != null) {
            ClientShortData holderShortData = clientShortDataService.findById(insurance.getHolder().getId());
            setClientToChecks(holderCheckIds, holderShortData);
        }
        //Заполняем ID клиента в проведенных ранее проверках по застрахованному
        Set<Long> insuredCheckIds = (Set<Long>) execution.getVariable(INSURED_CHECK_IDS);
        if (insurance.getInsured() != null && insuredCheckIds != null) {
            ClientShortData insuredShortData = clientShortDataService.findById(insurance.getInsured().getId());
            setClientToChecks(insuredCheckIds, insuredShortData);
        }
    }

    private Insurance setStatus(DelegateExecution execution,
                                Insurance insuranceContract, InsuranceStatusCode newStatus,
                                String branch, Long employeeId, String employeeName, Long officeId,
                                String office, String contructNumber) {
        try {
            return insuranceService.setStatus(insuranceContract, newStatus, branch, employeeId, employeeName,
                    officeId, office, null, contructNumber);
        } catch (DataIntegrityViolationException constraintException) {
            LOGGER.warn("Нарушена уникальность номера договора, сейчас будет произведена попытка перегенерировать номер договора...");
            LOGGER.error(constraintException);
            Insurance newInsurance = setStatus(execution,
                    insuranceContract, newStatus, branch, employeeId, employeeName, officeId, office, null);
            execution.setVariable(ERROR_MESSAGE, String.format("Нарушена уникальность номера договора, сгенерирован новый код %s. " +
                    "\n Код договора %s отправлен некорректно, обратитесь к администратору ТМ \n" +
                    "попытка отправки нового кода на сервер ТМ", newInsurance.getContractNumber(), contructNumber));
            execution.setVariable(ContextVariablesNames.CONTRACT_NUMBER, newInsurance.getContractNumber());
            execution.setVariable(ContextVariablesNames.IS_FINISH, Boolean.TRUE.toString());
            return newInsurance;
        }
    }

    private void setClientToChecks(Set<Long> checkIds, ClientShortData clientShortData) {
        if (CollectionUtils.isEmpty(checkIds)) return;
        checkIds.forEach(checkId -> {
            ClientCheck check = clientCheckService.findById(checkId);
            if (check.getClient() == null) {
                check.setClient(clientShortData);
                clientCheckService.save(check);
            }
        });
    }

    private void setDefaultValues(DelegateExecution execution) {
        execution.setVariable(ERROR_MESSAGE, null);
        execution.setVariable(IS_FINISH, null);
    }

}
