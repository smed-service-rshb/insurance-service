package ru.softlab.efr.services.insurance.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.insurance.exception.ImportInsuranceException;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.CompanyEnum;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;
import ru.softlab.efr.services.insurance.model.rest.ContractValidationResult;
import ru.softlab.efr.services.insurance.pojo.InsuranceParseResult;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;
import ru.softlab.efr.services.insurance.services.InsuranceService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.utils.AppUtils.*;

/**
 * Сервис загрузки статусов договоров из XLSX
 *
 * @author olshansky
 * @since 10.11.2019
 */

@Service
@PropertySource(value = {"classpath:dict-update.properties"}, encoding = "UTF-8")
public class UploadXLSContractStatusesService extends BaseUploadService {

    private static final Logger LOGGER = Logger.getLogger(UploadXLSContractStatusesService.class);
    private static final int CLIENT_ID_CELL_NUMBER = 0;
    private static final int CONTRACT_STATUS_CELL_NUMBER = 1;
    private static final int DEFAULT_SHEET_NUMBER = 0;
    private static final int DEFAULT_TITLE_ROW_NUMBER = 0;
    private static final int HEAD_COUNT_ROW = 0; // Количество строк - шапка документа, в которую входят наименование отчёта, наименование столбцов и пр, не относящееся к самим данным договора
    private static final int STATUS_UPDATE_CELL_NUMBER = 2;

    private final InsuranceService insuranceService;
    private final ExtractProcessInfoService extractProcessInfoService;
    private final OperationLogService operationLogService;
    private final ClientService clientService;

    @Value("${contract-statuses.xlsx.filename}")
    private String storeFileName;

    @Lazy
    @Autowired
    private UploadXLSContractStatusesService uploadXLSContractStatusesService;

    @Autowired
    public UploadXLSContractStatusesService(InsuranceService insuranceService,
                                            ExtractProcessInfoService extractProcessInfoService,
                                            OperationLogService operationLogService, ClientService clientService) {
        this.insuranceService = insuranceService;
        this.extractProcessInfoService = extractProcessInfoService;
        this.operationLogService = operationLogService;
        this.clientService = clientService;
    }

    @Scheduled(cron = "${contract-statuses.xlsx.update}")
    public void runScheduleTask() {
        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.IMPORT_INSURANCE_CONTRACT_STATUSES, storeFileName, null);
        if (!createProcessResult.isAlreadyExists()) {
            String fullFilePath = getReportStoreFolder().concat(storeFileName);
            try {
                byte[] content = IOUtils.toByteArray(new FileInputStream(fullFilePath));
                runLoadAsync(content, createProcessResult.getExtract().getUuid());
            } catch (IOException e) {
                String errorMsg = String.format("При попытке выполнить загрузку статусов по договорам страхования из xlsx произошла ошибка, причина: Файл %s не найден",
                        fullFilePath);
                LOGGER.error(errorMsg);
                OperationLogEntry operationLogEntry = operationLogService.startLogging();
                operationLogEntry.setOperationKey("PARSE_UPLOAD_CONTRACT_STATUSES");
                operationLogEntry.setOperationDescription("Обработка процесса загрузки статусов по договорам страхования из XLSX файла");
                operationLogEntry.setOperationMode(OperationMode.ACTIVE);
                operationLogEntry.setOperationParameter("uuid", createProcessResult.getExtract().getUuid());
                operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
                Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(createProcessResult.getExtract().getUuid());
                extractEntity.setStatus(ExtractStatus.ERROR);
                extractEntity.setName(getFileName());
                operationLogEntry.setOperationParameter("Причина ошибки",
                        errorMsg + "\n" + e.getMessage());
                operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
                operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
                operationLogService.log(operationLogEntry);
            }
        }
    }

    @Async("extractThreadPoolTaskExecutor")
    @Transactional
    public void runLoadAsync(byte[] content, String uuid) {
        Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(uuid);
        extractEntity.setStatus(ExtractStatus.SAVE);
        extractEntity.setName(getFileName());
        String fileName = getFileName();
        final byte[][] result = new byte[1][1];

        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("PARSE_UPLOAD_CONTRACT_STATUSES");
        operationLogEntry.setOperationDescription("Обработка процесса загрузки статусов по договорам страхования из XLSX файла");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        operationLogEntry.setOperationParameter("uuid", uuid);
        operationLogEntry.setOperationState(OperationState.SUCCESS);

        try {
            Executors.newSingleThreadExecutor().submit((Callable<Void>) () -> {
                LOGGER.info(String.format("Получен файл для загрузки статусов по договорам страхования: %s. Сейчас будет осуществлена попытка " +
                        "сохранить его в папку: %s", extractEntity.getName(), getReportStoreFolder()));
                boolean fileSaved = saveReceivedFile(content, fileName);
                if (fileSaved) {
                    InsuranceParseResult insuranceParseResult = loadContractStatusesFromXLSX(content, fileName);
                    setSyncErrors2XLSX(getReportStoreFolder().concat(fileName), insuranceParseResult.getErrors(),
                            DEFAULT_SHEET_NUMBER, HEAD_COUNT_ROW, STATUS_UPDATE_CELL_NUMBER);
                    result[0] = IOUtils.toByteArray(new FileInputStream(getReportStoreFolder().concat(fileName)));
                    LOGGER.info(String.format("Работа над загрузкой статусов по договорам страхования завершена. Сохранено в папку: %s",
                            getReportStoreFolder()));
                } else {
                    extractEntity.setStatus(ExtractStatus.ERROR);
                    operationLogEntry.setOperationParameter("Причина ошибки",
                            "Произошла ошибка при сохранении файла в настроечную папку");
                    operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
                }
                return null;
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.error("Произошла ошибка при загрузке статусов по договорам страхования", e.getCause());
            extractEntity.setStatus(ExtractStatus.ERROR);
            operationLogEntry.setOperationParameter("Причина ошибки",
                    e.getMessage());
            operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        } finally {
            extractProcessInfoService.saveContentAndExtract(extractEntity, result[0]);
        }
        operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
        operationLogService.log(operationLogEntry);
    }

    public InsuranceParseResult loadContractStatusesFromXLSX(byte[] frontFile, String fileName) throws Exception {
        InsuranceParseResult result = new InsuranceParseResult();
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new ByteArrayInputStream(frontFile))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(DEFAULT_SHEET_NUMBER);

            for (Row row : myExcelSheet) {
                if (row.getRowNum() > HEAD_COUNT_ROW && Objects.nonNull(row.getCell(CLIENT_ID_CELL_NUMBER))) {
                    try {
                        uploadXLSContractStatusesService.validateStatusDataAndUpdateInsuranceContract(row, result);
                    } catch (Exception ex) {
                        LOGGER.error(ex);
                        LOGGER.error(String.format(IMPORT_ERROR_MESSAGE, row.getRowNum(), ex.getMessage()), ex);
                        result.getErrors().add(new AbstractMap.SimpleEntry<>((long) row.getRowNum(), ex.getMessage()));
                    }
                }
            }
        }
        LOGGER.info(String.format("Результаты выполнения загрузки статусов по договорам страхования из xlsx: %s", result));
        return result;
    }

    @Transactional
    public void validateStatusDataAndUpdateInsuranceContract(Row row, InsuranceParseResult result) throws ImportInsuranceException {
        ContractValidationResult validationResult = new ContractValidationResult();
        validationResult.setValidationErrors(new ArrayList<>());
        Insurance insuranceContract = validateAndGetInsurance(row, validationResult);
        if (Objects.isNull(insuranceContract)) {
            mapCheckModel(validationResult, result);
            throw new ImportInsuranceException("Договор не найден - дальнейшая работа над данной записью невозможна.");
        } else {
            validationResult.setContractId(insuranceContract.getId());
            updateInsurance(insuranceContract, row, validationResult);
            result.getParsedInsurances().add(insuranceContract);
        }
        mapCheckModel(validationResult, result);
    }

    private void mapCheckModel(ContractValidationResult validationResult, InsuranceParseResult result) {
        validationResult.getValidationErrors().forEach(validationError ->
                result.getErrors().add(new AbstractMap.SimpleEntry<>(Long.valueOf(validationError.getKey()),
                        validationError.getValue()))
        );
    }

    private Insurance validateAndGetInsurance(Row row, ContractValidationResult validationResult) throws ImportInsuranceException {
        List<CheckModel> checkModel = new ArrayList<>();

        Insurance foundedContract = null;
        String clientId = null;
        String clientIdRaw = getString(row.getCell(CLIENT_ID_CELL_NUMBER));

        if (StringUtils.isBlank(clientIdRaw)) {
            putCriticalError(checkModel, row.getRowNum(),
                    String.format("Значение в поле '%s' должно быть заполнено",
                            getCellName(row, CLIENT_ID_CELL_NUMBER, DEFAULT_TITLE_ROW_NUMBER)));
        } else {
            try {
                clientId = clientIdRaw;
            } catch (NumberFormatException ex) {
                putCriticalError(checkModel, row.getRowNum(),
                        String.format("Значение в поле '%s' должно быть числом",
                                getCellName(row, CLIENT_ID_CELL_NUMBER, DEFAULT_TITLE_ROW_NUMBER)));
            }
        }

        if (Objects.nonNull(clientId)) {
            foundedContract =  insuranceService.findByNumber(clientId);
        }

        if (Objects.isNull(foundedContract)) {
            putCriticalError(checkModel, row.getRowNum(),
                    String.format("Не найдено договора '%s' в системе", clientId));
        }

        String contractStatusName = getString(row.getCell(CONTRACT_STATUS_CELL_NUMBER));

        if (StringUtils.isBlank(contractStatusName)) {
            putCriticalError(checkModel, row.getRowNum(),
                    String.format("Значение в поле '%s' должно быть заполнено",
                            getCellName(row, CONTRACT_STATUS_CELL_NUMBER, DEFAULT_TITLE_ROW_NUMBER)));
        } else {
            InsuranceStatusCode insuranceStatusCode =
                    InsuranceStatusCode.findByValueAndCompany(contractStatusName, CompanyEnum.SMS);
            if (Objects.isNull(insuranceStatusCode)) {
                putCriticalError(checkModel, row.getRowNum(),
                        String.format("Значение '%s' в поле '%s' должно соответствовать любому из списка: [%s]",
                                contractStatusName,
                                getCellName(row, CONTRACT_STATUS_CELL_NUMBER, DEFAULT_TITLE_ROW_NUMBER),
                                InsuranceStatusCode.valuesByCompany(CompanyEnum.SMS).stream()
                                        .map(InsuranceStatusCode::getNameStatus)
                                        .collect(Collectors.joining(", "))));
            }
        }

        validationResult.getValidationErrors().addAll(checkModel);
        return foundedContract;
    }

    private void updateInsurance(Insurance insuranceContract,
                                 Row row,
                                 ContractValidationResult validationResult) {

        String contractStatusName = getString(row.getCell(CONTRACT_STATUS_CELL_NUMBER));

        if (CollectionUtils.isEmpty(validationResult.getValidationErrors())) {
            InsuranceStatusCode insuranceStatusCode =
                    InsuranceStatusCode.findByValueAndCompany(contractStatusName, CompanyEnum.SMS);
            insuranceService.setStatus(insuranceContract,
                    insuranceStatusCode,
                    insuranceContract.getBranchName(),
                    insuranceContract.getEmployeeId(),
                    insuranceContract.getEmployeeName(),
                    insuranceContract.getSubdivisionId(),
                    insuranceContract.getSubdivisionName(),
                    "Автоматический переход при загрузке статусов договоров из XLSX файла",
                    null);
        }
    }

    public String getFileName() {
        return String.format("Результаты загрузки статусов по договорам страхования_%s.xlsx", ReportableContract.presentLocalDateTime(LocalDateTime.now(), "dd.MM.yyyy_HH.mm.ss"));
    }

}
