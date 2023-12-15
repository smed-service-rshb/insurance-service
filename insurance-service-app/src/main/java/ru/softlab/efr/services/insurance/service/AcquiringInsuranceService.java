package ru.softlab.efr.services.insurance.service;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.clients.model.Client;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.EmploeeDataWithOrgUnits;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.converter.ProgramSettingConverter;
import ru.softlab.efr.services.insurance.exception.AcquiringException;
import ru.softlab.efr.services.insurance.model.db.AcquiringInfo;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.OrderEntity;
import ru.softlab.efr.services.insurance.model.enums.AcquiringStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.enums.SourceEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.pojo.CheckAcquiringResult;
import ru.softlab.efr.services.insurance.pojo.CheckResult;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@PropertySource(value = {"classpath:update-contract.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
public class AcquiringInsuranceService {

    private static final Logger LOGGER = Logger.getLogger(AcquiringInsuranceService.class);
    private static final String SYSTEM_EMPLOYEE_ID = "systemEmployeeId";

    @Value("${insurance.exist.error}")
    private String insuranceExistError;
    @Value("${client.identification.error}")
    private String clientIdentificationError;
    @Value("${acquiring.error}")
    private String acquiringError;
    @Value("${acquiring.mail.subject}")
    private String mailSubject;
    @Value("${acquiring.mail.body}")
    private String mailBody;

    private final SettingsService settingsService;
    private final ClientService clientService;
    private final InsuranceService insuranceService;
    private final JsonHelper jsonHelper;
    private final CheckService checkService;
    private final NotifyService notifyService;
    private final EmployeesClient employeeService;
    private final AcquiringInfoService acquiringInfoService;
    private final InsuranceConverter insuranceConverter;
    private final TemplateService templateService;
    private final ProgramSettingConverter programSettingConverter;
    private final CalculationService calculationService;
    private final BuyoutService buyoutService;
    private final OrderService orderService;
    private final AcquiringProgramService acquiringProgramService;
    private final InsuranceRepository insuranceRepository;

    public AcquiringInsuranceService(SettingsService settingsService, ClientService clientService, InsuranceService insuranceService,
                                     JsonHelper jsonHelper, CheckService checkService, NotifyService notifyService, EmployeesClient employeeService,
                                     AcquiringInfoService acquiringInfoService, InsuranceConverter insuranceConverter, TemplateService templateService, ProgramSettingConverter programSettingConverter, CalculationService calculationService, BuyoutService buyoutService, OrderService orderService, AcquiringProgramService acquiringProgramService, InsuranceRepository insuranceRepository) {
        this.settingsService = settingsService;
        this.clientService = clientService;
        this.insuranceService = insuranceService;
        this.jsonHelper = jsonHelper;
        this.checkService = checkService;
        this.notifyService = notifyService;
        this.employeeService = employeeService;
        this.acquiringInfoService = acquiringInfoService;
        this.insuranceConverter = insuranceConverter;
        this.templateService = templateService;
        this.programSettingConverter = programSettingConverter;
        this.calculationService = calculationService;
        this.buyoutService = buyoutService;
        this.orderService = orderService;
        this.acquiringProgramService = acquiringProgramService;
        this.insuranceRepository = insuranceRepository;
    }

    /**
     * Проверка доступности оформления договора клиентом
     *
     * @param surName     фамилия клиента
     * @param firstName   имя клиента
     * @param middleName  отчество клиента
     * @param birthDate   дата рождения клиента
     * @param phoneNumber номер мобильного телефона клиента
     * @return результат проверки
     */
    public CheckAcquiringResult checkAvailabilityAcquiring(String surName, String firstName, String middleName,
                                                           LocalDate birthDate, String phoneNumber) {
        List<Insurance> activeByHoldersData = insuranceService
                .findActiveByHoldersData(surName, firstName, middleName, birthDate, phoneNumber);
        if (activeByHoldersData != null && !activeByHoldersData.isEmpty()) {
            return new CheckAcquiringResult(true, activeByHoldersData.get(0).getHolder(), insuranceExistError);
        }
        List<ClientEntity> clients = clientService.findClient(surName, firstName, middleName, birthDate, phoneNumber,
                null, null, null, null);
        if (clients != null && clients.size() > 1) {
            LOGGER.warn(String.format(clientIdentificationError, surName, firstName, middleName, birthDate, phoneNumber));
            return new CheckAcquiringResult(false, acquiringError);
        }
        return new CheckAcquiringResult(true, clients != null && !clients.isEmpty() ? clients.get(0) : null);
    }

    /**
     * Создание договора клиентом
     *
     * @param model    данные договора страхования
     * @param uuid     идентификатор процесса оформления договора
     * @param isMobile признак оформления с мобильного устройства
     * @throws AcquiringException исключение выбрасываемое при ошибки оформления договора
     */
    @Transactional(rollbackFor = AcquiringException.class)
    public AcquiringInfo issueInsurance(BaseInsuranceModel model, String uuid, Long insuranceId, boolean isMobile, Long acquiringProgramId) throws AcquiringException {
        Client client = jsonHelper.deserialize(jsonHelper.serialize(model.getHolderData()), Client.class);
        try {
            CheckResult checkResult = checkService.checkClientByBlockage(client);
            if (!checkResult.isResponseReceived() || checkResult.getBlockedPersonnelData() != null) {
                throw new AcquiringException("Клиент не прошел проверку по справочнику блокировок");
            }
            checkResult = checkService.checkClientByTerrorist(client);
            if (!checkResult.isResponseReceived() || !checkResult.isCheckSuccess()) {
                throw new AcquiringException("Клиент не прошел проверку по справочнику террористов/экстремистов");
            }
            checkResult = checkService.checkClientByPassport(client);
            if (checkResult != null && (!checkResult.isResponseReceived() || !checkResult.isCheckSuccess())) {
                throw new AcquiringException("Клиент не прошел проверку по справочнику недействительных паспортов");
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
            throw new AcquiringException(ex.getMessage());
        }
        Insurance insurance = new Insurance();
        if (insuranceId != null) {
            insurance = insuranceService.findById(insuranceId);
        } else {
            insuranceConverter.updateInsuranceFromBaseModel(model, insurance, null, null, null, null);
        }
        PrincipalDataImpl principalData = getSystemEmployee();
        if (principalData != null) {
            if (insurance.getCode() == null) {
                insurance.setSource(isMobile ? SourceEnum.MOBILE_CLIENT : SourceEnum.INTERNET_CLIENT);
                insurance.setAcquiringProgram(acquiringProgramId == null ? null : acquiringProgramService.findById(acquiringProgramId));
            }
            insuranceService.setStatus(insurance, InsuranceStatusCode.MADE_NOT_COMPLETED,
                    principalData.getBranch(),
                    principalData.getId(),
                    getFullName(principalData),
                    principalData.getOfficeId(),
                    principalData.getOffice(),
                    null, null);
        } else {
            throw new AcquiringException("Произошла ошибка при оформлении договора, не задан системный сотрудник");
        }
        sendDocumentEmail(insurance);
        return acquiringInfoService.saveAsCreated(uuid, insurance);
    }

    public void setMadeStatus(Insurance insurance) {
        PrincipalDataImpl principalData = getSystemEmployee();
        if (principalData != null) {
            insuranceService.setStatus(insurance, InsuranceStatusCode.MADE,
                    principalData.getBranch(),
                    principalData.getId(),
                    getFullName(principalData),
                    principalData.getOfficeId(),
                    principalData.getOffice(),
                    null, null);
        } else {
            //так как оплата была произведена, переводим в статус оформлен в любом случае, даже если системный работник не был найден
            insuranceService.setStatus(insurance, InsuranceStatusCode.MADE,
                    insurance.getBranchName(),
                    insurance.getEmployeeId(),
                    "Система",
                    insurance.getBranchId(),
                    insurance.getSubdivisionName(),
                    null, null);
        }
    }

    /**
     * Отправка клиенту списка документов по договору.
     *
     * @param insurance договор
     * @throws AcquiringException исключение выбрасываемое при неуспешной отправки документов
     */
    public void sendDocumentEmail(Insurance insurance) throws AcquiringException {
        ClientEntity client = insurance.getHolder();
        String body = String.format(mailBody, insurance.getContractNumber());
        if (!notifyService.sendEmail(client.getEmail(), String.format(mailSubject, insurance.getContractNumber()), body, getTemplate(insurance), "text/plain")) {
            throw new AcquiringException("Не удалось отправить пакет документов на email пользователя");
        }
    }

    private Map<String, byte[]> getTemplate(Insurance insurance) throws AcquiringException {
        ClientEntity client = insurance.getHolder();
        List<String> templates;
        if (insurance.getSource() != SourceEnum.OFFICE) {
            templates = insurance.getAcquiringProgram() != null ? insurance.getAcquiringProgram().getDocumentTemplateList() : new ArrayList<>();
        } else {
            templates = insurance.getProgramSetting().getDocumentTemplateList();
        }
        Map<String, byte[]> templatesByteList = new HashMap<>();
        if (templates != null) {
            String currency = programSettingConverter.getCurrencyById(insurance.getCurrency());
            for (String templateId : templates) {
                try {
                    Resource template = templateService.getTemplateContent(templateId);
                    templatesByteList.put(templateService.getPrintTemplate(templateId).getFileName().concat(".pdf"),
                            templateService.buildAndMergeTemplates(
                                    Collections.singletonList(template),
                                    ReportableContract.construct(insurance, client,
                                            templateService.isTestInstance(),
                                            currency,
                                            insurance.getAmount(),
                                            insurance.getPremium() != null ? insurance.getPremium().multiply(new BigDecimal(
                                                    calculationService.periodCount(insurance.getCalendarUnit(),
                                                            insurance.getDuration(),
                                                            insurance.getPeriodicity()))) : null,
                                            buyoutService.getRedemptionByContract(insurance, currency),
                                            buyoutService.getPayDateString(insurance)
                                    ), new JRPdfExporter()));
                } catch (Exception e) {
                    throw new AcquiringException("Ошибка при получении шаблона");
                }
            }
            return templatesByteList;
        }
        return new HashMap<>();
    }

    private String getFullName(PrincipalDataImpl principalData) {
        return principalData.getSecondName()
                .concat(" ".concat(principalData.getFirstName()))
                .concat((principalData.getMiddleName() == null) || principalData.getMiddleName().trim().isEmpty()
                        ? ""
                        : " ".concat(principalData.getMiddleName()));
    }

    public PrincipalDataImpl getSystemEmployee() {
        SettingEntity settingEntity = settingsService.get(SYSTEM_EMPLOYEE_ID);
        PrincipalDataImpl principalData = null;
        if (settingEntity != null && settingEntity.getValue() != null && StringUtils.isNumeric(settingEntity.getValue())) {
            try {
                EmploeeDataWithOrgUnits system = employeeService.getEmployeeByIdWithOutPermission(Long.valueOf(settingEntity.getValue()), 10L);
                if (system != null && system.getOrgUnits() != null && !system.getOrgUnits().isEmpty()) {
                    OrgUnitData orgUnit = system.getOrgUnits().get(0);
                    principalData = new PrincipalDataImpl();
                    principalData.setOfficeId(orgUnit.getId());
                    principalData.setOffice(orgUnit.getOffice());
                    principalData.setBranch(orgUnit.getBranch());
                    principalData.setId(system.getId());
                    principalData.setFirstName(system.getFirstName());
                    principalData.setMiddleName(system.getMiddleName());
                    principalData.setSecondName(system.getSecondName());

                }
            } catch (RestClientException ex) {
                LOGGER.warn("При получении информации, для заполнения данных по сотруднику оформившему договор произошла ошибка: ", ex);
            }
        }
        return principalData;
    }

    /**
     * Обновление статуса договора после оплаты
     *
     * @param order заказ на оплату
     */
    @Transactional
    public AcquiringInfo updateInsurance(OrderEntity order) {
        order = orderService.update(order.getId(), order);
        AcquiringInfo info = acquiringInfoService.findByInsurance(order.getContract());
        if (order.getOrderCode() != null && order.getOrderCode() == 2 &&
                info != null && info.getStatus() == AcquiringStatus.CREATED &&
                info.getInsurance().getStatus().getCode() == InsuranceStatusCode.MADE_NOT_COMPLETED) {
            setMadeStatus(info.getInsurance());
            info = acquiringInfoService.saveAsIssued(info.getUuid());
        }
        return info;
    }

    /**
     * Запуск проверки договоров КСП в статусе оформление не завершено.
     * Если такие договора найдены, их необходимо перевести в статус анулирован
     */
    @Scheduled(cron = "${share.quotes.update.schedule.cron}")
    @Transactional
    public void checkKspContract() {
        PrincipalDataImpl principalData = getSystemEmployee();
        List<Insurance> kspContract = insuranceRepository.findByStatusAndKindAndCreationDateAfter(InsuranceStatusCode.MADE_NOT_COMPLETED, ProgramKind.KSP, LocalDate.now().atStartOfDay());
        kspContract.forEach(insurance -> {
            if (principalData != null) {
                insuranceService.setStatus(insurance, InsuranceStatusCode.REVOKED,
                        principalData.getBranch(),
                        principalData.getId(),
                        getFullName(principalData),
                        principalData.getOfficeId(),
                        principalData.getOffice(),
                        null, null);
            } else {
                insuranceService.setStatus(insurance, InsuranceStatusCode.REVOKED,
                        insurance.getBranchName(),
                        insurance.getEmployeeId(),
                        "Система",
                        insurance.getBranchId(),
                        insurance.getSubdivisionName(),
                        null, null);
            }
        });
    }
}
