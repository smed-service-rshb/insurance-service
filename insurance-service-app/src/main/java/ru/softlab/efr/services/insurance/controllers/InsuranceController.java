package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.UpdateUserRq;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.pojo.Result;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;
import ru.softlab.efr.services.insurance.service.EmployeeFilterService;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.services.models.PrintFormServiceResponse;
import ru.softlab.efr.services.insurance.services.promocode.PromocodeApiConnectorService;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isProgramAllowable;

/**
 * Контроллер для работы с договором.
 *
 * @author Krivenko
 * @since 25.07.2018
 */
@RestController
@PropertySource(value = {"classpath:application.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
public class InsuranceController implements InsuranceApi {

    private static final Logger LOGGER = Logger.getLogger(InsuranceController.class);

    private final InsuranceService insuranceService;
    private final InsuranceCheckService insuranceCheckService;
    private final InsuranceConverter insuranceConverter;
    private final PrincipalDataSource principalDataSource;
    private final EmployeeFilterService employeeFilterService;
    private final ClientService clientService;
    private final InsuranceExtractor insuranceExtractor;
    private final StatusService statusService;
    private final OperationLogService operationLogService;
    private final ExtractProcessInfoService extractProcessInfoService;
    private final InsuranceAvailabilityService insuranceAvailabilityService;
    private final InsurancePrintFormService insurancePrintFormService;
    private final EmployeesClient employeesClient;
    private final ProgramSettingService programSettingService;
    @Value("${insurance.error.not.found}")
    private String INSURANCE_NOT_FOUND;
    @Value("${insurance.error.not.found.by.code}")
    private String INSURANCE_NOT_FOUND_BY_CODE;
    @Value("${clients.error.filter}")
    private String CLIENTS_FILTER_ERROR;
    @Value("${insurance.file.formCertification}")
    private String FILE_FORM_CERTIFICATION;
    @Value("${insurance.converter.static.date.notvalid}")
    private String STATIC_DATE_LESS_CURRENT_DATE;

    private PromocodeApiConnectorService promocodeApiConnectorService;

    @Autowired
    public InsuranceController(InsuranceService insuranceService, InsuranceCheckService insuranceCheckService,
                               InsuranceConverter insuranceConverter, PrincipalDataSource principalDataSource,
                               EmployeeFilterService employeeFilterService, ClientService clientService,
                               InsuranceExtractor insuranceExtractor, StatusService statusService,
                               OperationLogService operationLogService, ExtractProcessInfoService extractProcessInfoService,
                               InsuranceAvailabilityService insuranceAvailabilityService, InsurancePrintFormService insurancePrintFormService, ProgramSettingService programSettingService, EmployeesClient employeesClient,
                               PromocodeApiConnectorService promocodeApiConnectorService) {
        this.insuranceService = insuranceService;
        this.insuranceCheckService = insuranceCheckService;
        this.insuranceConverter = insuranceConverter;
        this.principalDataSource = principalDataSource;
        this.employeeFilterService = employeeFilterService;
        this.clientService = clientService;
        this.insuranceExtractor = insuranceExtractor;
        this.statusService = statusService;
        this.operationLogService = operationLogService;
        this.extractProcessInfoService = extractProcessInfoService;
        this.insuranceAvailabilityService = insuranceAvailabilityService;
        this.insurancePrintFormService = insurancePrintFormService;
        this.programSettingService = programSettingService;
        this.employeesClient = employeesClient;
        this.promocodeApiConnectorService = promocodeApiConnectorService;
    }

    private boolean hasClients(List<FoundClient> clients) {
        return (!CollectionUtils.isEmpty(clients));
    }

    private ListClientsResponse getEmptyListClientResponse() {
        return new ListClientsResponse(null, 0);
    }

    private FoundMultipleClients getMultipleClientsResponse(BaseInsuranceModel insuranceModel) {

        List<FoundClient> foundHolderClients = insuranceModel.getHolderId() != null || Boolean.TRUE.equals(insuranceModel.isNewHolder()) ?
                new ArrayList<>() : clientService.findSimilarClients(insuranceModel.getHolderData());
        List<FoundClient> foundInsuredClients = (insuranceModel.isHolderEqualsInsured() || insuranceModel.getInsuredId() != null
                || Boolean.TRUE.equals(insuranceModel.isNewInsured())) ?
                new ArrayList<>() : clientService.findSimilarClients(insuranceModel.getInsuredData());

        boolean hasHolder = hasClients(foundHolderClients);
        boolean hasInsured = hasClients(foundInsuredClients);

        if (hasInsured || hasHolder) {

            FoundMultipleClients foundMultipleClientsResponse = new FoundMultipleClients();

            if (hasHolder) {
                foundMultipleClientsResponse.setHoldered(new ListClientsResponse(foundHolderClients, foundHolderClients.size()));
            } else {
                foundMultipleClientsResponse.setHoldered(getEmptyListClientResponse());
            }

            if (hasInsured) {
                foundMultipleClientsResponse.setInsured(new ListClientsResponse(foundInsuredClients, foundInsuredClients.size()));
            } else {
                foundMultipleClientsResponse.setInsured(getEmptyListClientResponse());
            }

            return foundMultipleClientsResponse;
        }
        return null;
    }


    @Override
    @HasRight(Right.CREATE_CONTRACT)
    public ResponseEntity createContractV2(@Valid @RequestBody BaseInsuranceModel insuranceModel) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("CREATE_CONTRACT");
        operationLogEntry.setOperationDescription("Создание договора страхования");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        ProgramSetting programSetting = programSettingService.findById(insuranceModel.getProgramSettingId());
        if (programSetting != null && programSetting.getStaticDate() != null
                && programSetting.getStaticDate().compareTo(new Date()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(STATIC_DATE_LESS_CURRENT_DATE);
        }
        try {
            PrincipalData principalData = principalDataSource.getPrincipalData();
            String branch = principalData.getBranch();
            String office = principalData.getOffice();
            Long employeeId = principalData.getId();
            String employeeName = principalData.getSecondName() + " "
                    + principalData.getFirstName() + " "
                    + principalData.getMiddleName();
            Long officeId = principalData.getOfficeId();
            Insurance insuranceContract = new Insurance();
            insuranceContract.setVersion(0L);
            insuranceConverter.updateInsuranceFromBaseModel(insuranceModel, insuranceContract, null, null, null, null);
            insuranceContract.setIndividualRate(insuranceModel.isIndividualRate());
            FoundMultipleClients foundMultipleClients = getMultipleClientsResponse(insuranceModel);
            if (ProgramKind.SMS.equals(insuranceContract.getProgramSetting().getProgram().getType())) {
                insuranceContract = insuranceService.setStatus(
                        insuranceContract,
                        InsuranceStatusCode.MADE,
                        branch, employeeId, employeeName, officeId, office,
                        "Создание нового договора в системе СМС", null);
            }
            if (foundMultipleClients != null
                    && (foundMultipleClients.getHoldered().getTotalFounds() > 0
                    || foundMultipleClients.getInsured().getTotalFounds() > 0)) {
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(foundMultipleClients);
            } else {
                insuranceContract = insuranceService.create(insuranceContract, branch, office, employeeId, employeeName, officeId);
            }
            operationLogEntry.setOperationParameter("id", insuranceContract.getId());
            operationLogEntry.setOperationParameter("contractNumber", insuranceContract.getContractNumber());
            operationLogEntry.setOperationParameter("creationDate", insuranceContract.getCreationDate());
            operationLogEntry.setOperationState(OperationState.SUCCESS);

            CreateInsuranceResponse response = new CreateInsuranceResponse(insuranceContract.getId(),
                    insuranceContract.getContractNumber(),
                    getClientId(insuranceContract.getHolder()),
                    getClientId(insuranceContract.getInsured()));

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);

        }
    }

    private Long getClientId(ClientEntity clientEntity) {
        return clientEntity != null ? clientEntity.getId() : null;
    }

    @Override
    @HasRight(Right.DELETE_CONTRACT)
    public ResponseEntity<Void> deleteContractV2(@PathVariable("id") Long id) throws Exception {
        //todo
        return ResponseEntity.ok().build();
    }

    @HasRight(Right.CREATE_CONTRACT)
    @Override
    public ResponseEntity<Boolean> existsContractByClient(@PathVariable("id") Long id) {
        boolean exists = insuranceService.existsByClient(id);
        return ResponseEntity.ok(exists);
    }

    @Override
    @HasRight(Right.VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER)
    public ResponseEntity<ViewInsuranceModel> findContractsByCode(@PathVariable("code") String code) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Insurance insurance = insuranceService.findByCode(code);
        if (insurance == null) {
            LOGGER.warn(String.format(INSURANCE_NOT_FOUND_BY_CODE, code));
            return ResponseEntity.notFound().build();
        }
        if (!isProgramAllowable(insurance.getProgramSetting().getProgram(), principalDataSource.getPrincipalData())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(insuranceConverter.convertInsuranceToViewModel(insurance, principalData));
    }

    @Override
    @HasRight(Right.UPDATE_FULL_SET_DOCUMENT)
    public ResponseEntity<Void> fullSetDocument(@PathVariable("id") Long id, @RequestBody FullSetDocumentData fullSetDocumentData) throws Exception {
        Insurance insurance = insuranceService.findById(id);
        if (insurance == null) {
            LOGGER.warn(String.format(INSURANCE_NOT_FOUND, id));
            return ResponseEntity.notFound().build();
        }
        if (!isProgramAllowable(insurance.getProgramSetting().getProgram(), principalDataSource.getPrincipalData())) {
            return ResponseEntity.notFound().build();
        }
        insurance.setFullSetDocument(fullSetDocumentData.isFullSetDocument());
        insurance.setCommentForNotFullSetDocument(fullSetDocumentData.getCommentForNotFullSetDocument());
        insuranceService.update(insurance);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT)
    public ResponseEntity<ViewInsuranceModel> getContractV2(@PathVariable("id") Long id) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        Insurance insurance = insuranceService.findById(id, filter.canViewAllContract(), filter.isAdmin(), filter.getEmployeeOfficesFilter(),
                filter.getEmployeeIdFilter(), filter.getEmployeeGroupFilter(), employeeFilterService.getOfficeIdsByNames(principalData.getOffices()));

        if (insurance == null) {
            LOGGER.warn(String.format(INSURANCE_NOT_FOUND, id));
            return ResponseEntity.notFound().build();
        }

        ViewInsuranceModel viewInsuranceModel = insuranceConverter.convertInsuranceToViewModel(insurance, principalData);
        if (principalData.getRights().contains(Right.CREATE_CONTRACT_IN_CALL_CENTER) ||
                principalData.getRights().contains(Right.VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER)) {
            viewInsuranceModel.setCode(insurance.getCode());
        }
        return ResponseEntity.ok(viewInsuranceModel);
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT_REPORT)
    public ResponseEntity<UuidRs> getContractsExtract(@Valid @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                      @Valid @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate,
                                                      @RequestParam(value = "kind") String kind) throws Exception {
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        ProgramKind programKind;
        try {
            programKind = StringUtils.isNotBlank(kind) ? ProgramKind.valueOf(kind) : null;
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        String name = "Отчёт по продажам с " + startDate + " по " + endDate + ".xlsx";
        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.SALE_REPORT, name, filter);
        if (!createProcessResult.isAlreadyExists()) {
            insuranceExtractor.createExtractAsync(filter.isAdmin(), startDate, endDate, programKind,
                    filter.canViewAllContract() ? null : filter.getEmployeeIdFilter(),
                    filter.canViewAllContract() ? null : filter.getEmployeeOfficesFilter(),
                    filter.getEmployeeGroupFilter(),
                    employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                    createProcessResult.getExtract().getUuid());
        }

        return ResponseEntity.ok().body(new UuidRs(createProcessResult.getExtract().getUuid()));
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT_REPORT)
    public ResponseEntity<UuidRs> getContractsUniversalExtract(@NotNull @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                               @NotNull @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate,
                                                               @RequestParam(value = "kind") String kind) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        ProgramKind programKind;
        try {
            programKind = !StringUtils.isEmpty(kind) ? ProgramKind.valueOf(kind) : null;
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        String name = "Универсальный отчет " + (programKind == null ? "с " : (programKind.toString() + " с ")) + startDate + " по " + endDate + ".xlsx";
        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.UNIVERSAL_REPORT, name, filter);
        if (!createProcessResult.isAlreadyExists()) {
            insuranceExtractor.createUniversalExtractAsync(filter.isAdmin(), startDate, endDate, programKind,
                    filter.canViewAllContract() ? null : filter.getEmployeeIdFilter(),
                    filter.canViewAllContract() ? null : filter.getEmployeeOfficesFilter(),
                    filter.getEmployeeGroupFilter(),
                    employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                    createProcessResult.getExtract().getUuid());
        }

        return ResponseEntity.ok().body(new UuidRs(createProcessResult.getExtract().getUuid()));
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT_REPORT)
    public ResponseEntity<ExtractStatusRs> getExtractStatus(@PathVariable("uuid") String uuid) {
        Extract extract = extractProcessInfoService.getExtractProcessInfo(uuid);
        if (extract == null) {
            return ResponseEntity.badRequest().build();
        }
        ExtractStatusRs rs = new ExtractStatusRs();
        rs.setStatus(ru.softlab.efr.services.insurance.model.rest.ExtractStatus.valueOf(extract.getStatus().name()));
        return ResponseEntity.ok().body(rs);
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT_REPORT)
    public ResponseEntity<byte[]> getExtractContent(@PathVariable("uuid") String uuid) throws Exception {
        try {
            Extract extract = extractProcessInfoService.getContent(uuid);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
                            + URLEncoder.encode(extract.getName(), "UTF-8").replaceAll("\\+", " "))
                    .body(extract.getContent());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    @HasPermission(Permissions.VIEW_CONTRACT)
    public ResponseEntity<Page<ListInsuranceModel>> listContractV2(Pageable pageable) throws Exception {
        Pageable pageableDesc = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                InsuranceRepository.CONTRACT_SORT_BY_ID);
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        Page<InsuranceSummary> insurances;
        if (filter.canViewAllContract()) {
            // У пользователя есть права на просмотр всех договоров
            insurances = insuranceService.findAll(filter.isAdmin(), pageableDesc, filter.getEmployeeGroupFilter(),
                    filter.getEmployeeIdFilter());
        } else {
            insurances = insuranceService.findAllByOfficeAndEmployeeIdAndEmployeeGroups(filter.isAdmin(), filter.getEmployeeOfficesFilter(),
                    filter.getEmployeeIdFilter(), filter.getEmployeeGroupFilter(), employeeFilterService.getOfficeIdsByNames(principalData.getOffices()), pageableDesc);
        }
        return ResponseEntity.ok(insurances.map(insuranceConverter::convertInsuranceToListModel));
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT)
    public ResponseEntity<Page<ListInsuranceModel>> listFilteredContractsV2(
            @PageableDefault(value = 50) Pageable pageable, @Valid @RequestBody FilterContractsRq filterData,
            @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) throws Exception {
        if ((hasFilter == null) || !hasFilter || (filterData == null)) {
            return listContractV2(pageable);
        }

        Pageable pageableDesc = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                InsuranceRepository.CONTRACT_SORT_BY_ID);
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);

        String clientSecondName = (!StringUtils.isEmpty(filterData.getSecondName()))
                ? filterData.getSecondName().trim().toLowerCase()
                : null;

        String clientFirstName = (!StringUtils.isEmpty(filterData.getFirstName()))
                ? filterData.getFirstName().trim().toLowerCase()
                : null;

        String clientMiddleName = (!StringUtils.isEmpty(filterData.getMiddleName()))
                ? filterData.getMiddleName().trim().toLowerCase()
                : null;

        String contractNumberFilter = (!StringUtils.isEmpty(filterData.getContractNumber()))
                ? filterData.getContractNumber().trim()
                : "";

        ProgramKind programKindFilter = (filterData.getProgramKind() != null)
                ? ProgramKind.valueOf(filterData.getProgramKind().name())
                : null;

        InsuranceStatusCode statusCodeFilter = (filterData.getStatus() != null)
                ? InsuranceStatusCode.valueOf(filterData.getStatus().name())
                : null;

        Page<InsuranceSummary> insurances;
        if (filter.canViewAllContract()) {
            // У пользователя есть права на просмотр всех договоров
            insurances = insuranceService.findByNumber(
                    filter.isAdmin(), filterData.getId(),
                    clientFirstName, clientSecondName, clientMiddleName,
                    contractNumberFilter, statusCodeFilter, programKindFilter,
                    filterData.getProgramId(), filterData.getStartDate(), filterData.getEndDate(),
                    filterData.getStartConclusionDate(), filterData.getEndConclusionDate(), filter.getEmployeeGroupFilter(),
                    filterData.isFullSetDocument(),
                    filter.getEmployeeIdFilter(), pageableDesc);
        } else {
            insurances = insuranceService.findByNumberOfficeAndEmployeeIdAndEmployeeGroupsWithoutRevoked(
                    filter.isAdmin(), filterData.getId(),
                    clientFirstName, clientSecondName, clientMiddleName,
                    contractNumberFilter, filter.getEmployeeOfficesFilter(), filter.getEmployeeIdFilter(),
                    statusCodeFilter, programKindFilter,
                    filterData.getProgramId(), filterData.getStartDate(), filterData.getEndDate(),
                    filterData.getStartConclusionDate(), filterData.getEndConclusionDate(), filter.getEmployeeGroupFilter(),
                    employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                    filterData.isFullSetDocument(), pageableDesc);
        }
        return ResponseEntity.ok(insurances.map(insuranceConverter::convertInsuranceToListModel));
    }

    @Override
    @HasRight(Right.EDIT_CONTRACT)
    public ResponseEntity putContractV2(@PathVariable("id") Long id, @Valid @RequestBody BaseInsuranceModel insuranceModel) throws Exception {
        Insurance insuranceContract = insuranceService.findById(id);
        if (insuranceContract == null) {
            LOGGER.error(String.format(INSURANCE_NOT_FOUND, id));
            return ResponseEntity.notFound().build();
        }
        Timestamp staticDate = insuranceContract.getProgramSetting().getStaticDate();
        if (staticDate != null && staticDate.compareTo(new Date()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(STATIC_DATE_LESS_CURRENT_DATE);
        }
        if (!isProgramAllowable(insuranceContract.getProgramSetting().getProgram(), principalDataSource.getPrincipalData())) {
            return ResponseEntity.notFound().build();
        }
        Long clientIdToUpdate = clientService.getAuthClientIdToUpdate(insuranceContract.getHolder(), insuranceModel.getHolderData());
        insuranceConverter.updateInsuranceFromBaseModel(insuranceModel, insuranceContract, null, null, null, null);

        FoundMultipleClients foundMultipleClients = getMultipleClientsResponse(insuranceModel);
        if (foundMultipleClients != null
                && (foundMultipleClients.getHoldered().getTotalFounds() > 0
                || foundMultipleClients.getInsured().getTotalFounds() > 0)) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(foundMultipleClients);
        }

        ContractValidationResult validationResult = new ContractValidationResult();
        validationResult.setValidationErrors(insuranceCheckService.getContractErrors(insuranceModel, insuranceContract, null));
        if (canBeSave(validationResult.getValidationErrors())) {
            validationResult.setContractId(insuranceService.update(insuranceContract).getId());
        }
        if (clientIdToUpdate != null) {
            String newPhone = insuranceModel.getHolderData().getPhones().stream().filter(Phone::isMain).map(Phone::getNumber).findFirst().orElse("");
            String newEmail = insuranceModel.getHolderData().getEmail() != null ? insuranceModel.getHolderData().getEmail() : "";
            employeesClient.updateUserWithOutPermissions(clientIdToUpdate,
                    new UpdateUserRq(
                            insuranceModel.getHolderData().getFirstName(),
                            insuranceModel.getHolderData().getSurName(),
                            insuranceModel.getHolderData().getMiddleName(),
                            newPhone, insuranceModel.getHolderData().getBirthDate(), newEmail), 10L);
        }
        if (!CollectionUtils.isEmpty(validationResult.getValidationErrors())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.SET_INDIVIDUAL_CURRENCY_RATE)
    public ResponseEntity<Void> setIndividualRate(@PathVariable("insuranceId") Long insuranceId, @Valid @RequestBody IndividualRateRq setIndividualRateRq) throws Exception {
        Insurance insurance = insuranceService.findById(insuranceId);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }
        insuranceService.updateIndividualRate(insuranceId, setIndividualRateRq.getRate(), principalDataSource.getPrincipalData().getId());
        return ResponseEntity.ok().build();
    }

    private boolean canNotBeConclusion(List<CheckModel> errors) {
        return CollectionUtils.isEmpty(errors) && errors.stream().anyMatch(n ->
                n.getErrorType().equals(CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION)
                        || n.getErrorType().equals(CheckModelErrorType.CRITICAL));
    }

    private boolean canBeSave(List<CheckModel> errors) {
        return CollectionUtils.isEmpty(errors) || !CollectionUtils.isEmpty(errors) && errors.stream().noneMatch(n -> n.getErrorType().equals(CheckModelErrorType.CRITICAL));
    }

    @Override
    @HasRight(Right.EDIT_CONTRACT)
    public ResponseEntity setStatusContractV2(@PathVariable("id") Long id, @Valid @RequestBody SetStatusInsuranceModel insuranceModel) throws Exception {
        Insurance insuranceContract = insuranceService.findById(id);
        if (insuranceContract == null) {
            LOGGER.error(String.format(INSURANCE_NOT_FOUND, id));
            return ResponseEntity.notFound().build();
        }
        ProgramSetting program = insuranceContract.getProgramSetting();
        if (program.getStaticDate() != null && program.getStaticDate().compareTo(new Date()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(STATIC_DATE_LESS_CURRENT_DATE);
        }
        if (!isProgramAllowable(insuranceContract.getProgramSetting().getProgram(), principalDataSource.getPrincipalData())) {
            return ResponseEntity.notFound().build();
        }
        InsuranceStatusType newStatusModel = insuranceModel.getNewStatus();
        if (newStatusModel == null) {
            LOGGER.error("Во время смены статуса договора произошла ошибка, причина: Статус не может быть определён");
            return ResponseEntity.notFound().build();
        }
        PrincipalData principalData = principalDataSource.getPrincipalData();
        String branch = principalData.getBranch();
        String office = principalData.getOffice();
        Long employeeId = principalData.getId();
        String employeeName = principalData.getSecondName() + " "
                + principalData.getFirstName() + " "
                + principalData.getMiddleName();
        Long officeId = principalData.getOfficeId();
        InsuranceStatusCode newStatus = InsuranceStatusCode.valueOf(newStatusModel.name());
        BaseInsuranceModel baseInsuranceModel = null;
        //Обновляем данные при переходе из статуса "Черновик"
        if (InsuranceStatusCode.DRAFT == insuranceContract.getStatus().getCode()) {
            baseInsuranceModel = insuranceConverter.convertSetStatusInsuranceModelToBaseInsuranceModel(insuranceModel);
            insuranceConverter.updateInsuranceFromBaseModel(baseInsuranceModel, insuranceContract, null, null, null, null);
        }

        //Выполняем проверки доступности программы и параметров программы
        ContractValidationResult availabilityResult = new ContractValidationResult();
        availabilityResult.setValidationErrors(insuranceAvailabilityService.getContractErrors(insuranceContract, newStatus.name(), principalData));
        if (!CollectionUtils.isEmpty(availabilityResult.getValidationErrors())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(availabilityResult);
        }

        //Выполняем проверки при переходе в статус Проект или Оформлен
        if (InsuranceStatusCode.PROJECT == newStatus || InsuranceStatusCode.MADE == newStatus || InsuranceStatusCode.MADE_NOT_COMPLETED == newStatus) {
            if (baseInsuranceModel == null) {
                baseInsuranceModel = insuranceConverter.convertSetStatusInsuranceModelToBaseInsuranceModel(insuranceModel);
            }
            if (newStatusModel == InsuranceStatusType.MADE && insuranceContract.getProgramSetting().getProgram().getType().equals(ProgramKind.KSP)) {
                List<Insurance> kspContract = insuranceService.findAllByProgramKindAndClient(ProgramKind.KSP, insuranceModel.getHolderId());

                List<Insurance> existContract = kspContract.stream().filter(contract ->
                        contract.getProgramSetting().getProgram().getId().equals(insuranceContract.getProgramSetting().getProgram().getId()) &&
                                !contract.getId().equals(id)).collect(Collectors.toList());
                if (!existContract.isEmpty()) {
                    ContractValidationResult validationResult = new ContractValidationResult();
                    String error = String.format("Переход в статус \"Оформлен\" не возможен. У клиента уже есть действующий договор %s с данным видом страхования",
                            existContract.get(0).getContractNumber());
                    CheckModel checkModel = new CheckModel("status", error, CheckModelErrorType.CRITICAL);
                    List<CheckModel> list = new ArrayList<>();
                    list.add(checkModel);
                    validationResult.setValidationErrors(list);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
                }
            }
            ContractValidationResult validationResult = new ContractValidationResult();
            validationResult.setValidationErrors(insuranceCheckService.getContractErrors(insuranceModel, insuranceContract, newStatus.name()));
            if (canNotBeConclusion(validationResult.getValidationErrors())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
            }
        }
        //Выполняем проверку возможности перехода в заданный статус
        Result statusCanBeChanged = statusService.isTransitionAvailable(insuranceContract, principalData, newStatus);
        if (statusCanBeChanged.isFail()) {
            ContractValidationResult validationResult = new ContractValidationResult();
            CheckModel checkModel = new CheckModel("status", statusCanBeChanged.getDescription(), CheckModelErrorType.CRITICAL);
            List<CheckModel> list = new ArrayList<>();
            list.add(checkModel);
            validationResult.setValidationErrors(list);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
        if (newStatus == InsuranceStatusCode.REVOKED_REPLACEMENT) {
            if (baseInsuranceModel == null) {
                baseInsuranceModel = insuranceConverter.convertSetStatusInsuranceModelToBaseInsuranceModel(insuranceModel);
            }
            Insurance copyContract = new Insurance();
            insuranceConverter.updateInsuranceFromBaseModel(baseInsuranceModel, copyContract, null, null, null, null);
            copyContract = insuranceService.copyContract(insuranceContract, copyContract, newStatus, branch, employeeId, employeeName, officeId, office, insuranceModel.getComment());
            return ResponseEntity.ok().body(new CreateInsuranceResponse(copyContract.getId(), null, null, null));
        } else {
            setStatus(insuranceContract, newStatus, branch, employeeId, employeeName, officeId, office, insuranceModel.getComment());
        }
        return ResponseEntity.ok().build();
    }

    private void setStatus(Insurance insuranceContract, InsuranceStatusCode newStatus, String branch, Long employeeId, String employeeName, Long officeId, String office, String description) {
        try {
            insuranceService.setStatus(insuranceContract, newStatus, branch, employeeId, employeeName, officeId, office, description, null);
        } catch (DataIntegrityViolationException constraintException) {
            LOGGER.warn("Нарушена уникальность номера договора, сейчас будет произведена попытка перегенерировать номер договора...", constraintException);
            setStatus(insuranceContract, newStatus, branch, employeeId, employeeName, officeId, office, description);
        }
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT)
    public ResponseEntity<Resource> printContractV2(@PathVariable("id") Long id, @PathVariable("templateId") String templateId) throws Exception {
        PrintFormServiceResponse response = insurancePrintFormService.printContractByInsuranceIdAndTemplateId(id, templateId,
                employeeFilterService.getFilterForPrincipal(principalDataSource.getPrincipalData()),
                employeeFilterService.getOfficeIdsByNames(principalDataSource.getPrincipalData().getOffices()));
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        }
        String encodedFileName = URLEncoder.encode(response.getFilename(), StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .body(response.getByteArrayResource());
    }

    @Override
    @HasPermission(Permissions.VIEW_CONTRACT)
    public ResponseEntity<byte[]> printFormCertification() throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(FILE_FORM_CERTIFICATION, StandardCharsets.UTF_8.name());
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(IOUtils.toByteArray(InsuranceController.class.getResourceAsStream("/templates/formCertificationForForeignResident.pdf")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}