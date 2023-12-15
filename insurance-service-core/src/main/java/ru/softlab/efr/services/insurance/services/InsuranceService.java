package ru.softlab.efr.services.insurance.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.client.CurrencyRateClient;
import ru.softlab.efr.common.dict.exchange.model.ShortCurrencyRateData;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.InsuranceExtract;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;
import ru.softlab.efr.services.insurance.services.contructnumbers.ContractNumberSequenceApi;
import ru.softlab.efr.services.insurance.services.crmexport.CrmExportStatusService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static ru.softlab.efr.services.insurance.utils.NotificationHelper.getGreetingsByMiddleName;

/**
 * Сервис для работы с договорами.
 *
 * @author Krivenko
 * @since 26.07.2018
 */
@Service
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class InsuranceService {

    static final ArrayList<String> UNEXIST_USER_GROUP = new ArrayList<>(Collections.singleton("UNEXIST_USER_GROUP"));
    private static final Logger LOGGER = Logger.getLogger(InsuranceService.class);
    private final InsuranceRepository insuranceRepository;
    @Value("${insurance.email.subject}")
    private String emailSubjectTemplate;
    @Value("${contract.email.body}")
    private String emailBodyTemplate;

    private StatusHistoryService statusHistoryService;
    private NotifyService notifyService;
    private AttachmentService attachmentService;
    private StatusService statusService;
    private CurrencyRateClient currencyRateClient;
    private ContractNumberSequenceApi contractNumberSequenceService;
    private CrmExportStatusService crmExportStatusService;

    @Autowired
    public InsuranceService(InsuranceRepository insuranceRepository,
                            ContractNumberSequenceApi contractNumberSequenceService,
                            StatusHistoryService statusHistoryService,
                            NotifyService notifyService,
                            AttachmentService attachmentService,
                            StatusService statusService, CurrencyRateClient currencyRateClient,
                            CrmExportStatusService crmExportStatusService) {
        this.insuranceRepository = insuranceRepository;
        this.contractNumberSequenceService = contractNumberSequenceService;
        this.statusHistoryService = statusHistoryService;
        this.notifyService = notifyService;
        this.attachmentService = attachmentService;
        this.statusService = statusService;
        this.currencyRateClient = currencyRateClient;
        this.crmExportStatusService = crmExportStatusService;
    }

    /**
     * Найти договор по идентификатору.
     *
     * @param id идентификатор договора
     * @return договор
     */
    @Transactional(readOnly = true)
    public Insurance findById(long id) {
        return insuranceRepository.findContractByIdAndDeleted(id, false);
    }

    public Revision<Integer, Insurance> findLastChangeRevision(long id) {
        if (insuranceRepository.existsByIdAndDeleted(id, false)) {
            Revision<Integer, Insurance> lastChangeRevision = insuranceRepository.findLastChangeRevision(id);
            if (Objects.nonNull(lastChangeRevision)) {
                return lastChangeRevision;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Найти договор по идентификатору и фильтрам.
     *
     * @param id идентификатор договора
     * @return договор
     */
    @Transactional(readOnly = true)
    public Insurance findById(long id, boolean viewAllContracts, boolean isAdmin, Set<Long> employeeOfficesFilter,
                              Long employeeIdFilter, List<String> employeeGroups,
                              Set<Long> offices) {
        if (CollectionUtils.isEmpty(employeeGroups)) {
            employeeGroups = UNEXIST_USER_GROUP;
        }
        if (CollectionUtils.isEmpty(employeeOfficesFilter)) {
            return insuranceRepository.findContractByIdAndDeleted(id, viewAllContracts, isAdmin, employeeIdFilter, employeeGroups, offices, false);
        }
        return insuranceRepository.findContractByIdAndDeleted(id, viewAllContracts, isAdmin, employeeOfficesFilter, employeeIdFilter, employeeGroups, offices, false);
    }

    /**
     * Найти договор по коду.
     *
     * @param code код договора
     * @return договор
     */
    @Transactional(readOnly = true)
    public Insurance findByCode(String code) {
        return insuranceRepository.findContractByCodeAndDeleted(code, false);
    }

    /**
     * Получить список договоров в соответствии с условиями разбивки результата на страницы.
     *
     * @param pageable Условия разбивки результата на страницы.
     * @return список договоров
     */
    @Transactional(readOnly = true)
    public Page<InsuranceSummary> findAll(boolean isAdmin, Pageable pageable, List<String> employeeGroups, Long employeeId) {
        if (CollectionUtils.isEmpty(employeeGroups)) {
            employeeGroups = UNEXIST_USER_GROUP;
        }
        return insuranceRepository.findAll(isAdmin, false, pageable, employeeGroups, employeeId);
    }

    /**
     * Получить список договоров в соответствии с условиями разбивки результата на страницы.
     *
     * @param employeeOfficesFilter Наименования ВСП. Если значение указано, то в результат выборки попадут все договора,
     *                              относящиеся к указанным ВСП.
     * @param employeeIdFilter      Идентификатор сотрудника. Если значение указано, то в результат выборки попадут все договора,
     *                              созданные содрудником с указанным идентификатором.
     * @param employeeGroups        Названия групп пользователей. Значение обязательно для заполнения. В выборку попадают
     *                              только те договоры, программа страхования которых связана хотя бы с одной из перечисленных групп
     *                              пользователей.
     * @param pageable              Условия разбивки результата на страницы.
     * @return список договоров
     */
    @Transactional(readOnly = true)
    public Page<InsuranceSummary> findAllByOfficeAndEmployeeIdAndEmployeeGroups(boolean isAdmin, Set<Long> employeeOfficesFilter,
                                                                                Long employeeIdFilter, List<String> employeeGroups,
                                                                                Set<Long> offices,
                                                                                Pageable pageable) {
        if (CollectionUtils.isEmpty(employeeGroups)) {
            employeeGroups = UNEXIST_USER_GROUP;
        }
        if (CollectionUtils.isEmpty(employeeOfficesFilter)) {
            return insuranceRepository.findAllWithoutRevoked(isAdmin, false, employeeIdFilter, pageable);
        }
        return insuranceRepository.findAllWithoutRevoked(isAdmin, false, employeeOfficesFilter, employeeIdFilter, employeeGroups, offices, pageable);
    }

    /**
     * Получить список договоров по фрагменту номера в соответствии с условиями разбивки результата на страницы.
     *
     * @param clientFirstName   Имя страхователя.
     * @param clientSurname     Фамилия страхователя.
     * @param clientMiddleName  Отчество страхователя.
     * @param number            Фрагмент номера для фильтрации.
     * @param statusCode        Код статуса договора
     * @param programKind       Вид программы страхования
     * @param programId         Идентификатор программы страхования
     * @param startCreationDate Начало периода, в который должна попасть дата создания договора
     * @param endCreationDate   Окончание периода, в который должна попасть дата создания договора
     * @param fullSetDocument   Признак получения полного комплекта документов
     * @param pageable          Условия разбивки результата на страницы.
     * @return список договоров
     */
    @Transactional(readOnly = true)
    public Page<InsuranceSummary> findByNumber(boolean isAdmin, Long clientId, String clientFirstName, String clientSurname, String clientMiddleName,
                                               String number, InsuranceStatusCode statusCode, ProgramKind programKind,
                                               Long programId, LocalDate startCreationDate, LocalDate endCreationDate,
                                               LocalDate startConclusionDate, LocalDate endConclusionDate,
                                               List<String> groups, Boolean fullSetDocument,
                                               Long employeeId,
                                               Pageable pageable) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }
        LocalDateTime startDate = (startCreationDate != null) ? startCreationDate.atStartOfDay() : null;
        LocalDateTime endDate = (endCreationDate != null) ? endCreationDate.plusDays(1).atStartOfDay() : null;
        return insuranceRepository.findAll(isAdmin, clientId, clientFirstName, clientSurname, clientMiddleName, number,
                statusCode, programKind, programId, startDate, endDate, startConclusionDate,
                endConclusionDate, groups, fullSetDocument, employeeId, false, pageable);
    }

    @Transactional(readOnly = true)
    public Insurance findByNumber(String contractNumber) {
        return insuranceRepository.findByContractNumber(contractNumber);
    }

    /**
     * Получить список договоров по фрагменту номера в соответствии с условиями разбивки результата на страницы.
     * Договоры в статусе "Аннулирован по замене" в результат выборки не попадут.
     *
     * @param clientFirstName       Имя страхователя.
     * @param clientSurname         Фамилия страхователя.
     * @param clientMiddleName      Отчество страхователя.
     * @param number                Фрагмент номера для фильтрации
     * @param employeeOfficesFilter Наименования ВСП. Если значение указано, то в результат выборки попадут все договора,
     *                              относящиеся к указанным ВСП.
     * @param employeeIdFilter      Идентификатор сотрудника. Если значение указано, то в результат выборки попадут все договора,
     *                              созданные содрудником с указанным идентификатором.
     * @param statusCode            Код статуса договора
     * @param programKind           Вид программы страхования
     * @param programId             Идентификатор программы страхования
     * @param startCreationDate     Начало периода, в который должна попасть дата создания договора
     * @param endCreationDate       Окончание периода, в который должна попасть дата создания договора
     * @param employeeGroups        Названия групп пользователей. Значение обязательно для заполнения. В выборку попадают
     *                              только те договоры, программа страхования которых связана хотя бы с одной из
     *                              перечисленных групп пользователей.
     * @param fullSetDocument       Признак получения полного комплекта документов
     * @param pageable              Условия разбивки результата на страницы.
     * @return список договоров
     */
    @Transactional(readOnly = true)
    public Page<InsuranceSummary> findByNumberOfficeAndEmployeeIdAndEmployeeGroupsWithoutRevoked(boolean isAdmin, Long clientId, String clientFirstName, String clientSurname,
                                                                                                 String clientMiddleName, String number,
                                                                                                 Set<Long> employeeOfficesFilter, Long employeeIdFilter,
                                                                                                 InsuranceStatusCode statusCode, ProgramKind programKind,
                                                                                                 Long programId, LocalDate startCreationDate,
                                                                                                 LocalDate endCreationDate,
                                                                                                 LocalDate startConclusionDate,
                                                                                                 LocalDate endConclusionDate,
                                                                                                 List<String> employeeGroups,
                                                                                                 Set<Long> offices,
                                                                                                 Boolean fullSetDocument,
                                                                                                 Pageable pageable) {
        LocalDateTime startDate = (startCreationDate != null) ? startCreationDate.atStartOfDay() : null;
        LocalDateTime endDate = (endCreationDate != null) ? endCreationDate.plusDays(1).atStartOfDay() : null;
        if (CollectionUtils.isEmpty(employeeGroups)) {
            employeeGroups = UNEXIST_USER_GROUP;
        }
        if (CollectionUtils.isEmpty(employeeOfficesFilter)) {
            return insuranceRepository.findAllWithoutRevoked(isAdmin, clientId, clientFirstName, clientSurname, clientMiddleName, number,
                    statusCode, programKind, programId, startDate, endDate,
                    false, employeeIdFilter, startConclusionDate, endConclusionDate,
                    fullSetDocument, pageable);
        }
        return insuranceRepository.findAllWithoutRevoked(isAdmin, clientId, clientFirstName, clientSurname, clientMiddleName, number,
                statusCode, programKind, programId, startDate, endDate,
                false, employeeOfficesFilter, employeeIdFilter, startConclusionDate, endConclusionDate, employeeGroups, offices,
                fullSetDocument, pageable);
    }

    /**
     * Обновление информации по договору.
     *
     * @param insurance договор
     * @return обновлённый договор
     */
    @Transactional
    public Insurance update(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    /**
     * Создать договор.
     *
     * @param insurance договор
     * @return договор
     */
    @Transactional
    public Insurance create(Insurance insurance, String branchName, String officeName, Long employeeId, String employeeName, Long officeId) {
        insurance.setBranchName(branchName);
        insurance.setSubdivisionName(officeName);
        insurance.setEmployeeId(employeeId);
        insurance.setEmployeeName(employeeName);
        insurance.setCreationDate(LocalDateTime.now());
        insurance.setDeleted(false);
        insurance.setSubdivisionId(officeId);
        if (insurance.getIndividualRate() != null) {
            insurance.setIndividualRateDate(LocalDate.now());
            insurance.setSetRateEmployeeId(employeeId);
        }
        insurance = insuranceRepository.save(insurance);
        updateAttachment(insurance);
        return insurance;
    }

    /**
     * Получение списка договоров, оформленных в указанный период.
     * В выборку не попадут договоры в статусе "Аннулирован по замене".
     *
     * @param employeeOfficesFilter Наименования ВСП. Если значение указано, то в результат выборки попадут все договоры,
     *                              относящиеся к указанным ВСП.
     * @param employeeGroupFilter   Наименования группы пользователя. Если значение указано, то в результат выборки попадут все договоры,
     *                              относящиеся к указанным группам пользователей.
     * @param employeeIdFilter      Идентификатор сотрудника. Если значение указано, то в результат выборки попадут все договора,
     *                              созданные содрудником с указанным идентификатором.
     * @param startDate             Дата начала периода.
     * @param endDate               Дата окончания периода.
     * @return Список договоров, оформленных в указанный период.
     */
    @Transactional(readOnly = true)
    public Stream<InsuranceExtract> findAllByConclusionDateBetween(boolean isAdmin, Set<Long> employeeOfficesFilter, List<String> employeeGroupFilter, Set<Long> offices, Long employeeIdFilter, LocalDate startDate, LocalDate endDate,
                                                                   ProgramKind programKind) {
        if (CollectionUtils.isEmpty(employeeGroupFilter)) {
            employeeGroupFilter = UNEXIST_USER_GROUP;
        }
        if (CollectionUtils.isEmpty(employeeOfficesFilter)) {
            return insuranceRepository.findAllByConclusionDateBetween(
                    isAdmin,
                    false,
                    employeeIdFilter,
                    startDate,
                    endDate,
                    programKind,
                    employeeGroupFilter,
                    offices,
                    InsuranceRepository.CONTRACT_SORT_BY_ID);
        }
        return insuranceRepository.findAllByConclusionDateBetween(
                isAdmin,
                false,
                employeeOfficesFilter,
                employeeIdFilter,
                employeeGroupFilter,
                offices,
                startDate,
                endDate,
                programKind,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }

    /**
     * Получение списка договоров, оформленных в указанный период.
     * В выборку не попадут договоры в статусе "Аннулирован по замене".
     *
     * @param employeeOfficesFilter Наименования ВСП. Если значение указано, то в результат выборки попадут все договора,
     *                              относящиеся к указанным ВСП.
     * @param employeeIdFilter      Идентификатор сотрудника. Если значение указано, то в результат выборки попадут все договора,
     *                              созданные содрудником с указанным идентификатором.
     * @param startDate             Дата начала периода.
     * @param endDate               Дата окончания периода.
     * @return Списко договоров, оформленных в указанный период.
     */
    @Transactional(readOnly = true)
    public Stream<InsuranceExtract> findAllFullByConclusionDateBetween(boolean isAdmin, Set<Long> employeeOfficesFilter, List<String> employeeGroupFilter,
                                                                       Set<Long> offices, Long employeeIdFilter, LocalDate startDate, LocalDate endDate, List<ProgramKind> programKinds) {
        if (CollectionUtils.isEmpty(employeeGroupFilter)) {
            employeeGroupFilter = UNEXIST_USER_GROUP;
        }
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        if (CollectionUtils.isEmpty(employeeOfficesFilter)) {
            return insuranceRepository.findAllFullByConclusionDateBetween(
                    isAdmin,
                    false,
                    employeeIdFilter,
                    employeeGroupFilter,
                    offices,
                    startDateTime,
                    endDateTime,
                    startDate,
                    endDate,
                    programKinds,
                    InsuranceRepository.CONTRACT_SORT_BY_ID);
        }
        return insuranceRepository.findAllFullByConclusionDateBetween(
                isAdmin,
                false,
                employeeOfficesFilter,
                employeeIdFilter,
                employeeGroupFilter,
                offices,
                startDateTime,
                endDateTime,
                startDate,
                endDate,
                programKinds,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }

    /**
     * Получение списка договоров, оформленных в указанный период.
     * В выборку не попадут договоры в статусе "Аннулирован по замене".
     *
     * @param startDate Дата начала периода.
     * @param endDate   Дата окончания периода.
     * @return Списко договоров, оформленных в указанный период.
     */
    @Transactional(readOnly = true)
    public Stream<InsuranceExtract> findAllByConclusionDateBetween(boolean isAdmin, LocalDate startDate, LocalDate endDate, ProgramKind programKind, List<String> employeeGroupFilter, Set<Long> offices) {
        if (CollectionUtils.isEmpty(employeeGroupFilter)) {
            employeeGroupFilter = UNEXIST_USER_GROUP;
        }
        return insuranceRepository.findAllByConclusionDateDateBetween(
                isAdmin,
                false,
                startDate,
                endDate,
                programKind,
                employeeGroupFilter,
                offices,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }

    /**
     * Получение списка договоров, оформленных в указанный период.
     * В выборку не попадут договоры в статусе "Аннулирован по замене".
     *
     * @param startDate Дата начала периода.
     * @param endDate   Дата окончания периода.
     * @return Списко договоров, оформленных в указанный период.
     */
    @Transactional(readOnly = true)
    public Stream<InsuranceExtract> findAllFullByConclusionDateBetween(boolean isAdmin, LocalDate startDate, LocalDate endDate, List<ProgramKind> programKinds, List<String> employeeGroupFilter, Set<Long> offices) {
        if (CollectionUtils.isEmpty(employeeGroupFilter)) {
            employeeGroupFilter = UNEXIST_USER_GROUP;
        }
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        return insuranceRepository.findAllFullByConclusionDateDateBetween(
                isAdmin,
                false,
                startDateTime,
                endDateTime,
                startDate,
                endDate,
                programKinds,
                employeeGroupFilter,
                offices,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }


    /**
     * Получение списка договоров по застрахованному лицу и программе с датой окончания больше заданной.
     *
     * @param clientId  Идентификатор клиента, по которому надо найти договоры.
     * @param programId Идентификатор программы страхования.
     * @param endDate   Дата окончания действия договора.
     * @return Список договоров, оформленных в указанный период.
     */
    @Transactional(readOnly = true)
    public List<Insurance> findAllByProgramAndClientAndEndDateMoreThan(Long programId, Long clientId, LocalDate endDate) {
        return insuranceRepository.findAllByProgramAndClientAndEndDateMoreThan(
                false,
                programId,
                clientId,
                endDate,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }


    /**
     * Получить все неудаленные и действующие договоры по клиенту и типу программы.
     *
     * @param programKind Тип программы страхования.
     * @param clientId    Идентифкатор клиента, по которому надо найти договоры.
     * @return Список найденных договоров.
     */
    public List<Insurance> findAllByProgramKindAndClient(ProgramKind programKind, Long clientId) {
        return insuranceRepository.findAllByProgramKindAndClient(
                false,
                programKind,
                clientId,
                InsuranceRepository.CONTRACT_SORT_BY_ID);
    }

    /**
     * Получить все неудаленные договоры по клиенту.
     *
     * @param clientId Идентификатор клиента
     * @return Список найденных договоров.
     */
    public List<Insurance> findAllByClient(Long clientId) {
        return insuranceRepository.findAllByClient(clientId);
    }

    /**
     * Получить дату первого оформленного договор клиента
     *
     * @param clientId Идентификатор клиента
     * @return Список найденных договоров.
     */
    public LocalDate getFirstContractDateByClient(Long clientId) {
        return insuranceRepository.getFirstContractDateByClient(clientId);
    }

    /**
     * Удалить договор.
     *
     * @param insurance договор
     * @return договор
     */
    @Transactional
    public Insurance delete(Insurance insurance) {
        insurance.setDeleted(true);
        return insuranceRepository.save(insurance);
    }

    /**
     * Изменение статуса по договору по договору.
     *
     * @param insurance             договор
     * @param insuranceContractCode параметр заранее созданного номера договора
     */
    @Transactional
    public Insurance setStatus(Insurance insurance, InsuranceStatusCode newStatus, String branch, Long employeeId, String employeeName, Long subdivisionId,
                               String subdivisionName, String description, String insuranceContractCode) {
        InsuranceStatus status = statusService.getByCode(newStatus);
        if (status == null) {
            throw new EntityNotFoundException();
        }
        if (insurance.getId() != null) {
            Insurance updatedEntity =
                    insuranceRepository.findContractByIdAndDeleted(insurance.getId(), false);
            if (updatedEntity == null) {
                throw new EntityNotFoundException();
            }
        }
        if (insurance.getEmployeeId() == null || insurance.getCode() != null) {
            insurance.setEmployeeId(employeeId);
            insurance.setEmployeeName(employeeName);
            insurance.setSubdivisionId(subdivisionId);
            insurance.setSubdivisionName(subdivisionName);
            insurance.setBranchName(branch);
        }
        insurance.setDeleted(false);
        if (InsuranceStatusCode.MADE == newStatus) {
            insurance.setCode(null);
        }
        if (insurance.getStatus() == null && InsuranceStatusCode.MADE == newStatus) {
            LocalDate today = LocalDate.now();
            insurance.setConclusionDate(today);
            //            Integer coolingPeriod = insurance.getProgramSetting().getProgram().getCoolingPeriod();
            insurance.setStartDate(today);
            insurance.setEndDate(insurance.calculateEndDate());
        }
        if (isClosingStatus(newStatus)) {
            insurance.setCloseDate(LocalDate.now());
        } else {
            insurance.setCloseDate(null);
        }
        insurance.setStatus(status);
        if (InsuranceStatusCode.MADE.equals(newStatus)) {
            if (Objects.isNull(insuranceContractCode)) {
                insurance = contractNumberSequenceService.generateContractNumberAndSave(insurance, null);
            } else if (insurance.getContractNumber() == null) {
                insurance.setContractNumber(insuranceContractCode);
            }
        }
        insurance = insuranceRepository.save(insurance);
        updateAttachment(insurance);
        if (ProgramKind.SMS.equals(insurance.getProgramSetting().getProgram().getType())) {
            if (InsuranceStatusCode.CRM_IMPORTED.equals(newStatus) && !statusHistoryService.isCrmImported(insurance)) {
                crmExportStatusService.exportInsuranceByChangeStatus(insurance);
            }
        }
        //Сохраняем изменения в истории статусов
        statusHistoryService.save(insurance, newStatus, employeeId, employeeName, subdivisionId, subdivisionName, description);

        return insurance;
    }

    /**
     * Обновление сущности договора при импорте из универсального отчета
     * с обновлением истории статусов в случае изменения статуса
     *
     * @param insurance договор с новыми значениями из файла отчета
     * @param oldStatus статус договора в БД
     */
    @Transactional
    public void updateFromReport(Insurance insurance, InsuranceStatus oldStatus) {
        insuranceRepository.saveAndFlush(insurance);
        if (!oldStatus.getCode().equals(insurance.getStatus().getCode())) {
            statusHistoryService.save(insurance, insurance.getStatus().getCode(), insurance.getEmployeeId(),
                    insurance.getEmployeeName(), insurance.getSubdivisionId(),
                    insurance.getSubdivisionName(), null);
        }
    }

    @Transactional
    public Insurance copyContract(Insurance insuranceContract, Insurance copyContract, InsuranceStatusCode newStatus, String branch, Long employeeId, String employeeName, Long officeId, String office, String description) {
        //todo организовать блокировку таблицы при получении технического номера
        String number = insuranceContract.getContractNumber();
        insuranceContract.setInitialContractNumber(number);
        insuranceContract.setContractNumber(getReplacementNumber());
        insuranceContract = setStatus(insuranceContract, newStatus, branch, employeeId, employeeName, officeId, office, description, null);
        copyContract.setVersion(0L);
        copyContract = create(copyContract, insuranceContract.getBranchName(), insuranceContract.getSubdivisionName(),
                insuranceContract.getEmployeeId(), insuranceContract.getEmployeeName(), insuranceContract.getSubdivisionId());
        copyAttachments(copyContract, insuranceContract);
        copyContract.setContractNumber(number);
        copyContract.setCreationDate(insuranceContract.getCreationDate());
        copyContract.setStartDate(insuranceContract.getStartDate());
        copyContract.setConclusionDate(insuranceContract.getConclusionDate());
        copyContract.setEndDate(insuranceContract.getEndDate());
        copyContract.setParent(insuranceContract);
        copyContract.setAcquiringProgram(insuranceContract.getAcquiringProgram());
        copyContract.setSource(insuranceContract.getSource());
        return insuranceRepository.save(copyContract);
    }

    /**
     * Получить список договоров клиента
     *
     * @param clientEntity клиент (страхователь)
     * @return список догоаоров
     */
    public List<Insurance> findAllByHolder(ClientEntity clientEntity) {
        return insuranceRepository.findAllByHolder(clientEntity);
    }

    /**
     * Получить список договоров по данным клиента в статусах черновик, проект, анулирован, отказ клиента
     *
     * @param surName     фамилия клиента
     * @param firstName   имя клиента
     * @param middleName  отчество клиента
     * @param birthDate   дата рождения клиента
     * @param phoneNumber номер мобильного телефона клиента
     * @return список страховок
     */
    public List<Insurance> findActiveByHoldersData(String surName, String firstName, String middleName, LocalDate birthDate, String phoneNumber) {
        List<InsuranceStatus> statuses = new ArrayList<>();
        statuses.add(statusService.getByCode(InsuranceStatusCode.DRAFT));
        statuses.add(statusService.getByCode(InsuranceStatusCode.PROJECT));
        statuses.add(statusService.getByCode(InsuranceStatusCode.REVOKED));
        statuses.add(statusService.getByCode(InsuranceStatusCode.CLIENT_REFUSED));
        return insuranceRepository.findAllByHolder(surName, firstName, middleName, birthDate, phoneNumber, statuses);
    }

    /**
     * Получить список договоров клиента
     *
     * @param holders  клиент (страхователь)
     * @param pageable pageable
     * @return список догоаоров
     */
    public Page<Insurance> findAllByHoldersPageable(List<ClientEntity> holders, Pageable pageable) {
        List<InsuranceStatus> statuses = new ArrayList<>();
        statuses.add(statusService.getByCode(InsuranceStatusCode.DRAFT));
        statuses.add(statusService.getByCode(InsuranceStatusCode.PROJECT));
        statuses.add(statusService.getByCode(InsuranceStatusCode.REVOKED));
        statuses.add(statusService.getByCode(InsuranceStatusCode.REVOKED_REPLACEMENT));
        return insuranceRepository.findAllByHoldersPageable(holders, statuses, pageable);
    }

    @Transactional
    public boolean notifyClient(Insurance insurance, String personalNumber, String branchName, String subdivisionName, Map<String, byte[]> templates) {
        insurance.setCode(insuranceRepository.generateCode().trim());
        insurance.setCallCenterEmployeeName(insurance.getEmployeeName());
        insurance.setCallCenterEmployeeNumber(personalNumber);
        insurance.setCallCenterEmployeeId(insurance.getEmployeeId());
        insurance.setCallCenterBranchName(branchName);
        insurance.setCallCenterSubdivisionName(subdivisionName);
        insuranceRepository.save(insurance);
        return sendNotification(insurance, templates);
    }

    /**
     * Получить объект страховок с определенной ревизией
     *
     * @param insuranceId    ID договора страхования
     * @param revisionNumber номер ревизии
     * @return объект страховок по ревизии
     */
    public Revision<Integer, Insurance> findRevision(Long insuranceId, int revisionNumber) {
        Revision<Integer, Insurance> revision = insuranceRepository.findRevision(insuranceId, revisionNumber);
        if (revision != null && revision.getEntity() != null) {
            Insurance historicalInsurance = revision.getEntity();
            Insurance originInsurance = insuranceRepository.findOne(insuranceId);
            historicalInsurance.setProgramSetting(originInsurance.getProgramSetting());
        }
        return revision;
    }

    public Revisions<Integer, Insurance> findRevisions(Long insuranceId) {
        Revisions<Integer, Insurance> revisions = insuranceRepository.findRevisions(insuranceId);
        Insurance originInsurance = insuranceRepository.findOne(insuranceId);
        if (revisions != null && !CollectionUtils.isEmpty(revisions.getContent())) {
            revisions.getContent().forEach(r ->
                    r.getEntity().setProgramSetting(originInsurance.getProgramSetting())
            );
        }
        return revisions;
    }

    private void updateAttachment(Insurance insurance) {
        if (insurance.getUuid() != null) {
            attachmentService.updateAttachment(insurance);
        }
    }

    private synchronized String getReplacementNumber() {
        Long number = insuranceRepository.getReplacementNumber();
        if (number == null) {
            return "000000000000001";
        } else {
            return String.format("%015d", ++number);
        }
    }

    /**
     * Отправить уведомление клинету с информацией о договоре.
     *
     * @param insurance договор
     * @return статус отправки уведомления, true - уведомление было отправлено
     */
    private boolean sendNotification(Insurance insurance, Map<String, byte[]> templates) {

        if (insurance.getCode() != null) {
            try {
                ClientEntity client = insurance.getHolder();
                String clientName = client.getSurName()
                        .concat(" ".concat(client.getFirstName()))
                        .concat((client.getMiddleName() == null) || client.getMiddleName().trim().isEmpty()
                                ? ""
                                : " ".concat(client.getMiddleName()));
                String programName = insurance.getProgramSetting().getProgram().getName();
                String body = String.format(emailBodyTemplate,
                        getGreetingsByMiddleName(client.getGender(), client.getMiddleName()),
                        clientName, programName, insurance.getCode());

                return notifyService.sendEmail(client.getEmail(),
                        String.format(emailSubjectTemplate, programName),
                        body,
                        templates,
                        "text/plain");
            } catch (Exception ex) {
                LOGGER.error("Произошла ошибка при отправке email после сохранения договора!", ex);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Определение финального статуса, при котором требуется установить дату закрытия договора
     *
     * @param newStatus статус который будет установлен договору
     * @return true -  статус является финальным
     */
    private boolean isClosingStatus(InsuranceStatusCode newStatus) {
        switch (newStatus) {
            case FINISHED:
            case CLIENT_REFUSED:
            case REVOKED:
            case CANCELED_IN_HOLD_PERIOD:
            case CANCELED:
                return true;
            default:
                return false;
        }
    }

    private void copyAttachments(Insurance copyContract, Insurance insuranceContract) {
        List<Attachment> attachments = attachmentService.findAllExceptDeletedByContract(insuranceContract.getId());
        attachments.forEach(i -> {
            Attachment attachment = new Attachment();
            attachment.setContract(copyContract);
            attachment.setKind(i.getKind());
            attachment.setDocumentType(i.getDocumentType());
            attachment.setCreateDate(i.getCreateDate());
            attachment.setFileName(i.getFileName());
            attachment.setOwner(i.getOwner());
            attachment.setVerified(i.getVerified());
            attachment.setDeleted(i.getDeleted());
            attachmentService.save(attachment, attachmentService.getContent(i.getId()));
        });
    }

    @Transactional
    public void updateIndividualRate(Long insuranceId, BigDecimal rate, Long employeeId) throws RestClientException {

        Insurance insurance = insuranceRepository.getById(insuranceId);
        if (insurance == null) {
            throw new EntityNotFoundException();
        }
        ProgramSetting programSetting = insurance.getProgramSetting();
        if (BigDecimal.ZERO.compareTo(rate) == 0) {
            ShortCurrencyRateData active = currencyRateClient.getActiveCurrencyRate(insurance.getCurrency(), 10);
            if (active == null) {
                throw new EntityNotFoundException();
            }
            if (programSetting.getSpecialRate()) {
                insurance.setExchangeRate(active.getRate().add(active.getRate()
                        .multiply(programSetting.getSpecialRateValue().divide(BigDecimal.valueOf(100)))));
            } else {
                insurance.setExchangeRate(active.getInnerRate());
            }
            insurance.setIndividualRate(false);
            insurance.setIndividualRateDate(null);
        } else {
            insurance.setIndividualRate(true);
            insurance.setExchangeRate(rate);
            insurance.setIndividualRateDate(LocalDate.now());
        }
        insurance.setSetRateEmployeeId(employeeId);
        insuranceRepository.save(insurance);
    }

    /**
     * Вернуть признак наличия оформленного договора страхования для клиента
     *
     * @param clientId идентификатор клиента
     * @return признак наличия оформленного договора
     */
    public boolean existsByClient(Long clientId) {
        return insuranceRepository.existsByClient(clientId, InsuranceStatusCode.PAYED);
    }
}
