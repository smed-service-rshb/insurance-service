package ru.softlab.efr.services.insurance.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.exception.ImportInsuranceException;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.pojo.InsuranceParseResult;
import ru.softlab.efr.services.insurance.services.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.controllers.Constants.UPLOAD_CHANGES_RESULT_STORE_PATH;

/**
 * Сервис импорта изменений из учётной системы.
 * <p>
 * Задание составленное на основании устных ответов Михайлец Илоны:
 * <p>
 * Необходимо предоставить пользователю возможность загрузки в систему файла, аналогично формату "Универсальный отчёт"
 * В файле могут быть изменения персональных данных клиента - страхователя и / или застрахованного, статусов договора,
 * данные по выгодоприобретателям, размер страховой суммы и размер страховой премии
 * <p>
 * В персональные данные входит ФИО, адреса, данные документов, удостоверяющих личность.
 * <p>
 * Должны изменится актуальные данные в соответствующих таблицах, при смене статусов - должны быть добавлены записи в
 * таблицу истории смены статусов по договору.
 * <p>
 * Загрузка должна происходить на отдельной странице, где должно отображаться поле с выбором файла через кнопку "Обзор"
 * После осуществления загрузки, пользователь должен иметь возможность получить результат импорта изменений в виде
 * всплывающего окна с надписью "Импорт документов завершён, результат выгружен в папку: "(путь к файлу)"
 * <p>
 * В выгружаемом файле должны находится результаты по каждой записи: если произошла ошибка, то необходимо напротив
 * строки указать её причину.
 *
 * @author olshansky
 * @author Kalantaev
 * @since 06.03.2019
 */

@Service
public class UploadChangesService {

    private static final Logger LOGGER = Logger.getLogger(UploadChangesService.class);
    private static final List<String> addressesNames = Arrays.asList("Регион", "Район", "Город", "Населенный пункт", "Улица", "Дом", "Корпус", "Строение", "Квартира");
    private static final String DEFAULT_UPLOAD_CHANGES_RESULT_STORE_PATH = System.getProperty("java.io.tmpdir");
    private static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final int KSP_RUR_AMOUNT_CELL_NUMBER = 32;
    private static final int KSP_AMOUNT_CELL_NUMBER = 31;
    private static final int KSP_RUR_PREMIUM_CELL_NUMBER = 30;
    private static final int KSP_PREMIUM_CELL_NUMBER = 29;
    private static final int HOLDER_KSP_CELL_NUMBER = 6;
    private static final int CURRENCY_KSP_CELL_NUMBER = 28;
    private static final int KSP_STATUS_CELL_NUMBER = 22;
    private static final int HOLDER_PHONE_KSP_CELL_NUMBER = 8;
    private static final int HOLDER_EMAIL_KSP_CELL_NUMBER = 9;
    private static final int HOLDER_BIRTHDAY_KSP_CELL_NUMBER = 7;
    private static final int CONTRACT_ID_CELL_NUMBER = 0;
    private static final int CONTRACT_NUMBER_CELL_NUMBER = 1;
    private static final int CONTRACT_NUMBER_KSP_CELL_NUMBER = 4;
    private static final int CONTRACT_KIND_KSP_CELL_NUMBER = 16;
    private static final int TITLE_ROW_INDEX = 2;
    private static final int CONTRACT_KIND_CELL_INDEX = 2;
    private static final int CONTRACT_STATUS_CELL_NUMBER = 5;
    private static final int CONTRACT_AMOUNT_ISJ_CELL_NUMBER = 170;
    private static final int CONTRACT_AMOUNT_RENT_CELL_NUMBER = 209;
    private static final int CONTRACT_CURRENCY_CELL_NUMBER = 16;
    private static final int CONTRACT_PREMIUM_CELL_NUMBER = 15;
    private static final int CONTRACT_PREMIUM_RUR_CELL_NUMBER = 18;
    private static final int HOLDER_CELL_NUMBER = 26;
    private static final int INSURED_CELL_NUMBER = 84;
    private static final int MAIN_DOC_SERIES_SHIFT_CELL = 1;
    private static final int MAIN_DOC_NUMBER_SHIFT_CELL = 2;
    private static final int MAIN_DOC_ISSUED_DATE_SHIFT_CELL = 3;
    private static final int MAIN_DOC_ISSUED_BY_SHIFT_CELL = 4;
    private static final int MAIN_DOC_DIVISION_CODE_SHIFT_CELL = 5;
    private static final int FIRST_RECIPIENT_ISJ_CELL_NUMBER = 184;
    private static final int FIRST_RECIPIENT_RENT_CELL_NUMBER = 170;
    private static final int STATUS_UPDATE_CELL_NUMBER = 244;
    private static final int REGISTRATION_ADDRESS_SHIFT = 10;
    private static final int ADDRESS_REGION_SHIFT_SELL = 1;
    private static final int ADDRESS_AREA_SHIFT_SELL = 2;
    private static final int ADDRESS_CITY_SHIFT_SELL = 3;
    private static final int ADDRESS_LOCALITY_SHIFT_SELL = 4;
    private static final int ADDRESS_STREET_SHIFT_SELL = 5;
    private static final int ADDRESS_HOUSE_SHIFT_SELL = 6;
    private static final int ADDRESS_CONSTRUCTION_SHIFT_SELL = 7;
    private static final int ADDRESS_HOUSING_SHIFT_SELL = 8;
    private static final int ADDRESS_APARTMENT_SHIFT_SELL = 9;
    private static final int CONTRACT_EXCHANGE_RATE_CELL_NUMBER = 17;
    private static final int ADDRESS_SHIFT_CELL = 11;
    private static final int MAIN_DOC_SHIFT_CELL = 31;
    private static final int RESIDENT_SHIFT_CELL = 1;
    private static final int TAX_RESIDENCE_SHIFT_CELL = 2;
    private static final int BIRTH_DATE_SHIFT_CELL = 3;
    private static final int GENDER_SHIFT_CELL = 4;
    private static final int INN_SHIFT_SELL = 5;
    private static final int SNILS_SHIFT_SELL = 6;
    private static final int BIRTH_COUNTRY_SHIFT_SELL = 7;
    private static final int BIRTH_PLACE_SHIFT_SELL = 8;
    private static final int EMAIL_SHIFT_SELL = 9;
    private static final int PHONE_SHIFT_SELL = 10;
    private static final int MIGRATION_CARD_SHIFT = 37;
    private static final int MIGRATION_CARD_NUMBER_SHIFT = 1;
    private static final int MIGRATION_CARD_START_DATE_SHIFT = 2;
    private static final int MIGRATION_CARD_END_DATE_SHIFT = 3;
    private static final int FOREIGN_DOC_SHIFT = 41;
    private static final int FOREIGN_DOC_END_DATE_SHIFT = 2;
    private static final int FOREIGN_DOC_START_DATE_SHIFT = 1;
    private static final int BENEFICIAL_OWNER_SHIFT = 48;
    private static final int BUSINESS_RELATIONS_SHIFT = 49;
    private static final int ACTIVITIES_GOAL_SHIFT = 50;
    private static final int BUSINESS_RELATIONS_GOAL_SHIFT = 51;
    private static final int RISK_LEVEL_DESC_SHIFT = 52;
    private static final int BUSINESS_REPUTATION_SHIFT = 53;
    private static final int FINANCIAL_STABILITY_SHIFT = 54;
    private static final int FINANCES_SOURCE_SHIFT = 55;
    private static final int RECIPIENT_BLOCK_LENGTH = 7;
    private static final int RECIPIENT_BIRTH_DATE_SHIFT = 1;
    private static final int RECIPIENT_BIRTH_PLACE_SHIFT = 2;
    private static final int RECIPIENT_RESIDENCE_SHIFT = 4;
    private static final int SHARE_SHIFT = 6;
    private static final int ID_INSURED_CELL = 83;
    private static final int ID_HOLDER_CELL = 24;
    private static final String IMPORT_ERROR_MESSAGE = "Во время импорта изменений произошла ошибка. Номер строки %s причина: %s";
    private static final String ERR_TEXT_CONTRACT_NUMBER_NOT_FOUND = "Указанный номер договора %s не найден";
    private static final String CONTRACT_NUMBER_NOT_SPECIFIED = "Невозможно идентифицировать договор - не указан номер договора";


    private final SettingsService settingsService;
    private final InsuranceService insuranceService;
    private final StatusService statusService;
    private final ExtractProcessInfoService extractProcessInfoService;
    private final CurrencyCachedClientService currencyService;
    private final StatusHistoryService statusHistoryService;

    public UploadChangesService(SettingsService settingsService, InsuranceService insuranceService, StatusService statusService, ExtractProcessInfoService extractProcessInfoService, CurrencyCachedClientService currencyService, StatusHistoryService statusHistoryService) {
        this.settingsService = settingsService;
        this.insuranceService = insuranceService;
        this.statusService = statusService;
        this.extractProcessInfoService = extractProcessInfoService;
        this.currencyService = currencyService;
        this.statusHistoryService = statusHistoryService;
    }

    @Async("extractThreadPoolTaskExecutor")
    @Transactional
    public void importInsuranceAsync(byte[] content, String uuid) {
        Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(uuid);
        extractEntity.setStatus(ExtractStatus.SAVE);
        String fileName = getFileName();
        try {
            Executors.newSingleThreadExecutor().submit((Callable<Void>) () -> {
                LOGGER.info(String.format("Получен файл для импорта изменений: %s. Сейчас будет осуществлена попытка " +
                                "сохранить его в папку: %s", extractEntity.getName(), getReportStoreFolder()));
                boolean fileSaved = saveReceivedFile(content, fileName);
                if (fileSaved) {
                    InsuranceParseResult insuranceParseResult = loadChangesFromXLSX(content, fileName);
                    setSyncErrors2XLSX(getReportStoreFolder().concat(fileName), insuranceParseResult.getErrors());
                    LOGGER.info(String.format("Работа над загрузкой изменений завершена. Сохранено в папку: %s",
                            getReportStoreFolder()));
                } else {
                    extractEntity.setStatus(ExtractStatus.ERROR);
                }
                return null;
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.error("Произошла ошибка при импорте договоров", e.getCause());
            extractEntity.setStatus(ExtractStatus.ERROR);
        } finally {
            extractProcessInfoService.update(extractEntity);
        }
    }

    private InsuranceParseResult loadChangesFromXLSX(byte[] frontFile, String fileName) throws Exception {

        List<Currency> allCurrency = currencyService.getAllCurrency();
        Map<String, Currency> currencyMap = new HashMap<>();
        allCurrency.forEach(currency -> currencyMap.put(currency.getCurrencyName(), currency));

        InsuranceParseResult result = new InsuranceParseResult();

        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new ByteArrayInputStream(frontFile))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);

            for (Row row : myExcelSheet) {
                if (row.getRowNum() >= 3 && row.getCell(CONTRACT_ID_CELL_NUMBER) != null) {
                    try {
                        String contractNumber = getString(row.getCell(getContractNumberCellIndex(row)));
                        if (StringUtils.isBlank(contractNumber)) {
                            throw new ImportInsuranceException(CONTRACT_NUMBER_NOT_SPECIFIED);
                        }
                        Insurance insuranceContract = insuranceService.findByNumber(contractNumber);
                        if (insuranceContract == null) {
                            throw new ImportInsuranceException(String.format(ERR_TEXT_CONTRACT_NUMBER_NOT_FOUND, contractNumber));
                        } else {
                            isActualData(fileName, insuranceContract);
                            if ((ProgramKind.fromValue(row.getCell(CONTRACT_KIND_CELL_INDEX).getStringCellValue()) != null) &&
                                    ProgramKind.fromValue(row.getCell(CONTRACT_KIND_KSP_CELL_NUMBER).getStringCellValue()) == ProgramKind.KSP) {
                                updateKSP(insuranceContract, row, currencyMap);
                            } else {
                                updateISJAndRent(insuranceContract, row, currencyMap);
                            }
                            result.getParsedInsurances().add(insuranceContract);
                        }
                    } catch (Exception ex) {
                        LOGGER.error(String.format(IMPORT_ERROR_MESSAGE, row.getRowNum(), ex.getMessage()), ex);
                        result.getErrors().add(new AbstractMap.SimpleEntry<>((long) row.getRowNum(), ex.getMessage()));
                    }
                }
            }
        }
        List<Insurance> duplicates = getDuplicates(result.getParsedInsurances());
        duplicates.forEach(f -> result.getErrors().add(new AbstractMap.SimpleEntry<>(f.getId(),
                String.format("Найден дублирующий договор %s", f.getContractNumber()))));
        return result;
    }

    private void updateISJAndRent(Insurance insuranceContract, Row row, Map<String, Currency> currencyMap) throws ImportInsuranceException {
        InsuranceStatus oldStatus = insuranceContract.getStatus();
        insuranceContract.setStatus(statusService.getByName(getString(row.getCell(CONTRACT_STATUS_CELL_NUMBER))));

        Currency currency = currencyMap.get(getString(row.getCell(CONTRACT_CURRENCY_CELL_NUMBER)));
        if (currency != null) {
            insuranceContract.setCurrency(currency.getId());
        } else {
            throw new ImportInsuranceException("Не удалось определить валюту договора," +
                    "  допустимые значения для валют: " + currencyMap.keySet());
        }

        BigDecimal exchangeRate = getBigDecimalFromString(getNumber(row.getCell(CONTRACT_EXCHANGE_RATE_CELL_NUMBER)));
        insuranceContract.setAmount(getBigDecimalFromString(getNumber(row.getCell(getAmountCellIndex(row)))));
        if (currency.getLiteralISO().equals("RUB")) {
            insuranceContract.setRurAmount(insuranceContract.getAmount());
        } else if (exchangeRate.compareTo(BigDecimal.ZERO) != 0) {
            insuranceContract.setExchangeRate(exchangeRate);
            insuranceContract.setRurAmount(insuranceContract.getAmount().multiply(exchangeRate));
        } else {
            throw new ImportInsuranceException("Для валютного договора не указан курс валюты договора на дату заключения");
        }
        insuranceContract.setPremium(getBigDecimalFromString(getNumber(row.getCell(CONTRACT_PREMIUM_CELL_NUMBER))));
        insuranceContract.setRurPremium(getBigDecimalFromString(getNumber(row.getCell(CONTRACT_PREMIUM_RUR_CELL_NUMBER))));
        boolean holderEqualsInsured = getString(row.getCell(ID_HOLDER_CELL)).equals(getString(row.getCell(ID_INSURED_CELL)));
        insuranceContract.setHolderEqualsInsured(holderEqualsInsured);
        if (holderEqualsInsured) {
            insuranceContract.setInsured(null);
        } else {
            if (insuranceContract.getInsured() == null) {
                insuranceContract.setInsured(new ClientEntity());
            }
            updateClient(insuranceContract.getInsured(), row, INSURED_CELL_NUMBER, 0);
        }
        updateClient(insuranceContract.getHolder(), row, HOLDER_CELL_NUMBER, 1);
        updateRecipient(insuranceContract, row);

        insuranceService.updateFromReport(insuranceContract, oldStatus);
    }

    private void updateKSP(Insurance insuranceContract, Row row, Map<String, Currency> currencyMap) throws ImportInsuranceException {

        InsuranceStatus oldStatus = insuranceContract.getStatus();
        insuranceContract.setStatus(statusService.getByName(getString(row.getCell(KSP_STATUS_CELL_NUMBER))));
        Currency currency = currencyMap.get(getString(row.getCell(CURRENCY_KSP_CELL_NUMBER)));
        if (currency != null) {
            insuranceContract.setCurrency(currency.getId());
        } else {
            throw new ImportInsuranceException("Не удалось определить валюту договора," +
                    "  допустимые значения для валют: " + currencyMap.keySet());
        }
        insuranceContract.setRurAmount(getBigDecimalFromString(getNumber(row.getCell(KSP_RUR_AMOUNT_CELL_NUMBER))));
        insuranceContract.setAmount(getBigDecimalFromString(getNumber(row.getCell(KSP_AMOUNT_CELL_NUMBER))));
        insuranceContract.setRurPremium(getBigDecimalFromString(getNumber(row.getCell(KSP_RUR_PREMIUM_CELL_NUMBER))));
        insuranceContract.setPremium(getBigDecimalFromString(getNumber(row.getCell(KSP_PREMIUM_CELL_NUMBER))));
        ClientEntity holder = insuranceContract.getHolder();
        updateClientName(holder, getString(row.getCell(HOLDER_KSP_CELL_NUMBER)));
        holder.setBirthDate(getDateValue(row, HOLDER_BIRTHDAY_KSP_CELL_NUMBER));
        String mainPhone = getString(row.getCell(HOLDER_PHONE_KSP_CELL_NUMBER));
        holder.getPhones().stream().filter(PhoneForClaimEntity::isMain).forEach(phone -> phone.setNumber(mainPhone));
        holder.setEmail(getString(row.getCell(HOLDER_EMAIL_KSP_CELL_NUMBER)));

        insuranceService.updateFromReport(insuranceContract, oldStatus);
    }

    private void updateClient(ClientEntity clientEntity, Row row, int cellIndex, int additionalShift) throws ImportInsuranceException {
        updateClientName(clientEntity, getString(row.getCell(cellIndex)));
        updateResidentField(clientEntity, row, cellIndex + RESIDENT_SHIFT_CELL);
        updateTaxResidenceField(clientEntity, row, cellIndex + TAX_RESIDENCE_SHIFT_CELL);
        clientEntity.setBirthDate(getDateValue(row, cellIndex + BIRTH_DATE_SHIFT_CELL));
        updateGenderField(clientEntity, row, cellIndex + GENDER_SHIFT_CELL);
        clientEntity.setTaxPayerNumber(getString(row.getCell(cellIndex + INN_SHIFT_SELL)));
        clientEntity.setSnils(getString(row.getCell(cellIndex + SNILS_SHIFT_SELL)));
        cellIndex += additionalShift;
        updateClientAddresses(clientEntity, row, cellIndex + ADDRESS_SHIFT_CELL);
        updateClientDocs(clientEntity, row, cellIndex + MAIN_DOC_SHIFT_CELL);
        clientEntity.setBirthCountry(getString(row.getCell(cellIndex + BIRTH_COUNTRY_SHIFT_SELL)));
        clientEntity.setBirthPlace(getString(row.getCell(cellIndex + BIRTH_PLACE_SHIFT_SELL)));
        clientEntity.setEmail(getString(row.getCell(cellIndex + EMAIL_SHIFT_SELL)));
        String mainPhone = getString(row.getCell(cellIndex + PHONE_SHIFT_SELL));
        clientEntity.getPhones().stream().filter(PhoneForClaimEntity::isMain).forEach(phone -> phone.setNumber(mainPhone));
        updateMigrationCard(clientEntity, row, cellIndex + MIGRATION_CARD_SHIFT);
        updateForeignDoc(clientEntity, row, cellIndex + FOREIGN_DOC_SHIFT);
        clientEntity.setBeneficialOwner(getString(row.getCell(cellIndex + BENEFICIAL_OWNER_SHIFT)));
        clientEntity.setBusinessRelations(getString(row.getCell(cellIndex + BUSINESS_RELATIONS_SHIFT)));
        clientEntity.setActivitiesGoal(getString(row.getCell(cellIndex + ACTIVITIES_GOAL_SHIFT)));
        clientEntity.setBusinessRelationsGoal(getString(row.getCell(cellIndex + BUSINESS_RELATIONS_GOAL_SHIFT)));
        clientEntity.setRiskLevelDesc(getString(row.getCell(cellIndex + RISK_LEVEL_DESC_SHIFT)));
        clientEntity.setBusinessReputation(getString(row.getCell(cellIndex + BUSINESS_REPUTATION_SHIFT)));
        clientEntity.setFinancialStability(getString(row.getCell(cellIndex + FINANCIAL_STABILITY_SHIFT)));
        clientEntity.setFinancesSource(getString(row.getCell(cellIndex + FINANCES_SOURCE_SHIFT)));
    }

    private void updateMigrationCard(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        List<DocumentForClientEntity> documents = clientEntity.getDocuments();
        if (documents != null) {
            DocumentForClientEntity migrationCard = documents.stream().filter(document -> document.getDocType() == IdentityDocTypeEnum.MIGRATION_CARD).findFirst().orElse(null);
            if (migrationCard != null) {
                migrationCard.setDocSeries(getString(row.getCell(cellIndex)));
                migrationCard.setDocNumber(getString(row.getCell(cellIndex + MIGRATION_CARD_NUMBER_SHIFT)));
                migrationCard.setStayStartDate(getDateValue(row, cellIndex + MIGRATION_CARD_START_DATE_SHIFT));
                migrationCard.setStayEndDate(getDateValue(row, cellIndex + MIGRATION_CARD_END_DATE_SHIFT));
            }
        }
    }

    private void updateForeignDoc(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        List<DocumentForClientEntity> documents = clientEntity.getDocuments();
        if (documents != null) {
            DocumentForClientEntity foreignDoc = documents.stream().filter(document -> document.getDocType() == IdentityDocTypeEnum.FOREIGN_DOCUMENT).findFirst().orElse(null);
            if (foreignDoc != null) {
                foreignDoc.setDocName(getString(row.getCell(cellIndex)));
                foreignDoc.setStayStartDate(getDateValue(row, cellIndex + FOREIGN_DOC_START_DATE_SHIFT));
                foreignDoc.setStayEndDate(getDateValue(row, cellIndex + FOREIGN_DOC_END_DATE_SHIFT));
            }
        }
    }

    private void updateGenderField(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        GenderTypeEnum gender = GenderTypeEnum.fromValue(getString(row.getCell(cellIndex)));
        if (gender != null) {
            clientEntity.setGender(gender);
        } else {
            throw new ImportInsuranceException("Поле \"" + getCellName(row, cellIndex) + "\" должно содержать одно из значений: " + Arrays.toString(GenderTypeEnum.values()));
        }
    }

    private LocalDate getDateValue(Row row, int cellIndex) throws ImportInsuranceException {
        try {
            //при создании отчета дата сохраняется в виде строки, после ручного редактирования ячейки может принять значение даты
            if (row.getCell(cellIndex).getCellTypeEnum() == CellType.NUMERIC) {
                Date date = row.getCell(cellIndex).getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                String date = row.getCell(cellIndex).getStringCellValue();
                if (StringUtils.isNotEmpty(date)) {
                    return LocalDate.parse(date, DDMMYYYY);
                }
            }
            return null;
        } catch (Exception ex) {
            LOGGER.error("Во время импорта изменений произошла ошибка, причина: ", ex);
            throw new ImportInsuranceException("Поле \"" + getCellName(row, cellIndex) + "\" должно содержать значение в формате: dd.mm.yyyy");
        }
    }

    private void updateResidentField(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        CitizenshipTypeEnum citizenship = CitizenshipTypeEnum.fromValue(getString(row.getCell(cellIndex)));
        if (citizenship != null) {
            clientEntity.setResident(citizenship.name().toLowerCase());
        } else {
            throw new ImportInsuranceException("Поле \"" + getCellName(row, cellIndex) + "\" должно содержать одно из значений: " + Arrays.toString(CitizenshipTypeEnum.values()));
        }
    }

    private void updateTaxResidenceField(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        TaxResidenceTypeEnum taxResidence = TaxResidenceTypeEnum.fromValue(getString(row.getCell(cellIndex)));
        if (taxResidence != null) {
            clientEntity.setResident(taxResidence.name().toLowerCase());
        } else {
            throw new ImportInsuranceException("Поле \"" + getCellName(row, cellIndex) + "\" должно содержать одно из значений: " + Arrays.toString(TaxResidenceTypeEnum.values()));
        }
    }

    private String getCellName(Row row, int cellIndex) {
        return row.getSheet().getRow(TITLE_ROW_INDEX).getCell(cellIndex).getStringCellValue();
    }

    private void updateRecipient(Insurance insuranceContract, Row row) throws ImportInsuranceException {
        int recipientCellIndex = getRecipientIndex(row);

        List<InsuranceRecipientEntity> existRecipientList = null;
        if (insuranceContract.getRecipientList() != null) {
            existRecipientList = insuranceContract.getRecipientList()
                    .stream().sorted(Comparator.comparing(InsuranceRecipientEntity::getId))
                    .collect(Collectors.toList());
        }
        BigDecimal allShare = BigDecimal.ZERO;
        List<InsuranceRecipientEntity> recipientList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InsuranceRecipientEntity recipientEntity = new InsuranceRecipientEntity();
            String recipientName = getString(row.getCell(recipientCellIndex + (RECIPIENT_BLOCK_LENGTH * i)));
            if (StringUtils.isNotEmpty(recipientName)) {
                if (existRecipientList != null && existRecipientList.size() > i) {
                    recipientEntity.setId(existRecipientList.get(i).getId());
                    recipientEntity.setBirthCountry(existRecipientList.get(i).getBirthCountry());
                }
                recipientEntity.setInsurance(insuranceContract);
                updateRecipientName(recipientEntity, recipientName);
                recipientEntity.setBirthDate(getDateValue(row, recipientCellIndex + RECIPIENT_BIRTH_DATE_SHIFT + (RECIPIENT_BLOCK_LENGTH * i)));
                recipientEntity.setBirthPlace(getString(row.getCell(recipientCellIndex + RECIPIENT_BIRTH_PLACE_SHIFT + (RECIPIENT_BLOCK_LENGTH * i))));
                TaxResidenceTypeEnum residenceTypeEnum = TaxResidenceTypeEnum.fromValue(getString(row.getCell(recipientCellIndex + RECIPIENT_RESIDENCE_SHIFT + (RECIPIENT_BLOCK_LENGTH * i))));
                if (residenceTypeEnum != null) {
                    recipientEntity.setTaxResidence(TaxResidenceEnum.valueOf(residenceTypeEnum.name()));
                }
                recipientList.add(recipientEntity);
            }

            BigDecimal share = getBigDecimalFromString(getNumber(row.getCell(recipientCellIndex + SHARE_SHIFT + (RECIPIENT_BLOCK_LENGTH * i))));
            recipientEntity.setShare(share);
            allShare = allShare.add(share);
        }
        if (!recipientList.isEmpty()) {
            insuranceContract.setRecipientList(recipientList);
        }
        if (allShare.compareTo(BigDecimal.ZERO) != 0 && allShare.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new ImportInsuranceException("Общая доля выгодоприобритателей должна быть равна 100");
        }
    }

    private void updateClientDocs(ClientEntity clientEntity, Row row, int cellIndex) throws ImportInsuranceException {
        if (clientEntity.getDocuments() != null) {
            DocumentForClientEntity document = clientEntity.getDocuments().stream().filter(DocumentForClientEntity::isMain).findFirst().orElse(null);
            if (document != null) {
                document.setDocType(IdentityDocTypeEnum.fromValue(getString(row.getCell(cellIndex))));
                document.setDocSeries(getString(row.getCell(cellIndex + MAIN_DOC_SERIES_SHIFT_CELL)));
                document.setDocNumber(getString(row.getCell(cellIndex + MAIN_DOC_NUMBER_SHIFT_CELL)));
                document.setIssuedBy(getString(row.getCell(cellIndex + MAIN_DOC_ISSUED_BY_SHIFT_CELL)));
                document.setIssuedDate(getDateValue(row, cellIndex + MAIN_DOC_ISSUED_DATE_SHIFT_CELL));
                document.setDivisionCode(getString(row.getCell(cellIndex + MAIN_DOC_DIVISION_CODE_SHIFT_CELL)).replace("-", ""));
            }
        }
    }

    private void updateClientAddresses(ClientEntity clientEntity, Row row, int cellIndex) {
        clientEntity.getAddresses().forEach(address -> {
            if (AddressTypeEnum.RESIDENCE == address.getAddressType()) {
                updateAddress(address, row, cellIndex);
            } else if (AddressTypeEnum.REGISTRATION == address.getAddressType()) {
                updateAddress(address, row, cellIndex + REGISTRATION_ADDRESS_SHIFT);
            }
        });

    }

    private void updateAddress(AddressForClientEntity address, Row row, int cellIndex) {
        address.setIndex(row.getCell(cellIndex).getStringCellValue());
        address.setRegion(getAddressValue(row, cellIndex, ADDRESS_REGION_SHIFT_SELL));
        address.setArea(getAddressValue(row, cellIndex, ADDRESS_AREA_SHIFT_SELL));
        address.setCity(getAddressValue(row, cellIndex, ADDRESS_CITY_SHIFT_SELL));
        address.setLocality(getAddressValue(row, cellIndex, ADDRESS_LOCALITY_SHIFT_SELL));
        address.setStreet(getAddressValue(row, cellIndex, ADDRESS_STREET_SHIFT_SELL));
        address.setHouse(getAddressValue(row, cellIndex, ADDRESS_HOUSE_SHIFT_SELL));
        address.setConstruction(getAddressValue(row, cellIndex, ADDRESS_CONSTRUCTION_SHIFT_SELL));
        address.setHousing(getAddressValue(row, cellIndex, ADDRESS_HOUSING_SHIFT_SELL));
        address.setApartment(getAddressValue(row, cellIndex, ADDRESS_APARTMENT_SHIFT_SELL));
    }

    private String getAddressValue(Row row, int cellIndex, int shift) {
        return getString(row.getCell(cellIndex + shift)).replaceFirst(addressesNames.get(shift - 1), "").trim();
    }

    private void updateClientName(ClientEntity clientEntity, String fullName) {
        clientEntity.setSurName(getSurNameFromFullName(fullName).trim());
        clientEntity.setFirstName(getFirstNameFromFullName(fullName).trim());
        clientEntity.setMiddleName(getMiddleNameFromFullName(fullName).trim());
    }

    private void updateRecipientName(InsuranceRecipientEntity recipientEntity, String fullName) {
        recipientEntity.setSurName(getSurNameFromFullName(fullName).trim());
        recipientEntity.setFirstName(getFirstNameFromFullName(fullName).trim());
        recipientEntity.setMiddleName(getMiddleNameFromFullName(fullName).trim());
    }

    /**
     * Определяем, что данные из универсального отчёта новее, чем изменения в БД (дата универсального отчёта меньше чем дата изменений по договору)
     */
    private void isActualData(String fileName, Insurance insuranceContract) throws IOException, ImportInsuranceException {

        InsuranceStatusHistory history = statusHistoryService.getByInsurance(insuranceContract);
        if (history == null) {
            return;
        }
        LocalDateTime changeDate = history.getChangeDate();
        File file = new File(getReportStoreFolder().concat(fileName));
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        if (LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault()).isBefore(changeDate)) {
            throw new ImportInsuranceException("Изменения не приняты: Дата универсального отчёта меньше, чем дата изменений по договору");
        }
    }

    private String getSurNameFromFullName(String fullName) {
        if (StringUtils.isNotBlank(fullName)) {
            return fullName.split(" ")[0];
        }
        return "";
    }

    private String getFirstNameFromFullName(String fullName) {
        if (StringUtils.isNotBlank(fullName)) {
            return fullName.split(" ")[1];
        }
        return "";
    }

    private String getMiddleNameFromFullName(String fullName) {
        if (StringUtils.isNotBlank(fullName)) {
            return fullName.replace(getSurNameFromFullName(fullName).concat(" ").concat(getFirstNameFromFullName(fullName)), "");
        }
        return "";
    }

    private int getRecipientIndex(Row row) {
        ProgramKind kind = ProgramKind.fromValue(row.getCell(CONTRACT_KIND_CELL_INDEX).getStringCellValue());
        switch (kind) {
            case ISJ:
                return FIRST_RECIPIENT_ISJ_CELL_NUMBER;
            case RENT:
                return FIRST_RECIPIENT_RENT_CELL_NUMBER;
            default:
                return 0;
        }
    }

    private int getAmountCellIndex(Row row) {
        ProgramKind kind = ProgramKind.fromValue(row.getCell(CONTRACT_KIND_CELL_INDEX).getStringCellValue());
        switch (kind) {
            case ISJ:
                return CONTRACT_AMOUNT_ISJ_CELL_NUMBER;
            case RENT:
                return CONTRACT_AMOUNT_RENT_CELL_NUMBER;
            default:
                return 0;
        }
    }

    private int getContractNumberCellIndex(Row row) {
        ProgramKind kind = ProgramKind.fromValue(row.getCell(CONTRACT_KIND_CELL_INDEX).getStringCellValue());
        if (kind == ProgramKind.KSP) {
            return CONTRACT_NUMBER_KSP_CELL_NUMBER;
        }
        return CONTRACT_NUMBER_CELL_NUMBER;
    }

    private boolean saveReceivedFile(byte[] frontFile, String fileName) {
        try {
            FileUtils.writeByteArrayToFile(new File(getReportStoreFolder().concat(fileName)), frontFile);
        } catch (IOException ex) {
            LOGGER.error("Произошла ошибка во время сохранения файла, причина: ", ex);
            return false;
        }
        return true;
    }

    private static BigDecimal getBigDecimalFromString(String string) {
        if (StringUtils.isBlank(string)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(Double.valueOf(string));
    }

    private static List<Insurance> getDuplicates(List<Insurance> insurances) {
        Set<Long> uniques = new HashSet<>();
        return insurances.stream().filter(e -> !uniques.add(e.getId())).collect(Collectors.toList());
    }

    private static String getNumber(Cell cell) {
        if (cell != null) {
            String value = "";
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = String.valueOf(cell.getNumericCellValue());
            } else {
                value = String.valueOf(cell).trim();
            }
            return value;
        }
        return "";
    }

    private static String getString(Cell cell) {
        if (cell != null) {
            String value = "";
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = String.valueOf((long) cell.getNumericCellValue());
            } else {
                value = String.valueOf(cell).trim();
            }
            return value.replace(".0", "");
        }
        return "";
    }

    private static void setSyncErrors2XLSX(String path, List<AbstractMap.SimpleEntry<Long, String>> errors) throws Exception {
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            errors.forEach(err -> {
                for (Row row : myExcelSheet) {
                    if (row.getRowNum() > 0) {
                        String id = getString(row.getCell(CONTRACT_ID_CELL_NUMBER));
                        if (StringUtils.isNotBlank(id)) {
                            if (Long.valueOf(row.getRowNum()).equals(err.getKey())) {
                                setErrorText(row, err.getValue());
                            }
                        }
                    }
                }
            });
            try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
                myExcelBook.write(fileOutputStream);
            }
        }
    }

    private static void setErrorText(Row row, String errorText) {
        Cell cellResponse = row.getCell(STATUS_UPDATE_CELL_NUMBER);
        String prevCellValue = getString(cellResponse);
        if (cellResponse == null) {
            cellResponse = row.createCell(STATUS_UPDATE_CELL_NUMBER);
        }
        cellResponse.setCellValue(
                StringUtils.isBlank(prevCellValue) ?
                        errorText : prevCellValue.concat(";\n").concat(errorText));
    }

    public String getFileName() {
        return String.format("Результаты импорта изменений_%s.xlsx", ReportableContract.presentLocalDateTime(LocalDateTime.now(), "dd.MM.yyyy_HH.mm.ss"));
    }

    public String getReportStoreFolder() {
        String result = getValueOrDefault(UPLOAD_CHANGES_RESULT_STORE_PATH, DEFAULT_UPLOAD_CHANGES_RESULT_STORE_PATH);
        if (result.endsWith("/")) {
            return result;
        }
        return result.concat("/");
    }

    private String getValueOrDefault(String key, String defaultVal) {
        SettingEntity settingEntity = settingsService.get(key);
        if (settingEntity == null || StringUtils.isBlank(settingEntity.getValue())) {
            LOGGER.warn(String.format("Не удалось прочитать значение настройки %s, будет использовано значение по-умолчанию: %s",
                    key, defaultVal));
            return defaultVal;
        }
        return settingEntity.getValue();
    }
}
/* Универсальный отчёт по изменениям
  Номер поля	Наименование поля
  0	id договора (CONTRACT_ID_CELL_NUMBER)
  1	Номер договора (CONTRACT_NUMBER_CELL_NUMBER)
  2	Вид страхования
  3	Программа страхования
  4	Код продукта
  5	Статус договора (CONTRACT_STATUS_CELL_NUMBER)
  6	Дата заключения договора
  7	Дата начала договора
  8	Дата окончания договора
  9	Срок действия договора
  10	Дата анулирования договора
  11	Дата расторжения договора
  12	Дата расторжения в период охлаждения договора
  13	Размер возврата (выплаты выкупной суммы) при расторжении договора
  14	Дата возврата (выплаты выкупной суммы)
  15	Премия в валюте договора (CONTRACT_PREMIUM_CELL_NUMBER)
  16	Валюта договора (CONTRACT_CURRENCY_CELL_NUMBER)
  17	Курс валюты договора на дату заключения, руб.
  18	Премия в рублях (CONTRACT_PREMIUM_RUR_CELL_NUMBER)
  19	Дата начала периода оплаты взносов
  20	Дата окончания периода оплаты взносов
  21	Периодичность оплаты взносов
  22	Плановая дата оплаты взносов
  23	Фактическая дата поступления страхового взноса
  24	id Страхователя
  25	Тип страхователя
  26	ФИО/Наименование Страхователя
  27	Гражданство Страхователя
  28	Налоговое резидентство Страхователя
  29	Дата рождения Страхователя
  30	Пол Страхователя
  31	ИНН/TIN Страхователя
  32	СНИЛС (для ФЛ)
  33	КПП Страхователя (для ЮЛ и ИП)
  34	Страна рождения  Страхователя
  35	Место рождения Страхователя
  36	E-mail Страхователя
  37	Телефон Страхователя
  38	Прописка Страхователя. Индекс
  39	Прописка Страхователя. Республика, край, область
  40	Прописка Страхователя. Район
  41	Прописка Страхователя. Город
  42	Прописка Страхователя. Населенный пункт
  43	Прописка Страхователя. Улица
  44	Прописка Страхователя. Дом
  45	Прописка Страхователя. Копус
  46	Прописка Страхователя. Строение
  47	Прописка Страхователя. Квартира
  48	Место пребывания Страхователя. Индекс
  49	Место пребывания Страхователя. Республика, край, область
  50	Место пребывания Страхователя. Район
  51	Место пребывания Страхователя. Город
  52	Место пребывания Страхователя. Населенный пункт
  53	Место пребывания Страхователя. Улица
  54	Место пребывания Страхователя. Дом
  55	Место пребывания Страхователя. Копус
  56	Место пребывания Страхователя. Строение
  57	Место пребывания Страхователя. Квартира
  58	Тип основного документа Страхователя
  59	Серия документа Страхователя
  60	Номер документа Страхователя
  61	Дата выдачи документа Страхователя
  62	Кем выдан документ  Страхователя
  63	Код подразделения выдачи документа  Страхователя
  64	Данные миграционной карты Страхователя. Серия
  65	Данные миграционной карты Страхователя. Номер
  66	Данные миграционной карты Страхователя. Дата начала срока пребывания
  67	Данные миграционной карты Страхователя. Дата окончания срока пребывания
  68	Наименование документа Страхователя, подтверждающего право пребывания в РФ
  69	Дата начала срока пребывания Страхователя в РФ
  70	Дата окончания срока пребывания Страхователя в РФ
  71	ПДЛ Страхователь (да/нет)
  72	Должность ПДЛ Страхователя
  73	Работодатель ПДЛ Страхователя
  74	Источник денежных средств ПДЛ Страхователя
  75	Сведения о бенефициарном владельце. Страхователь
  76	Сведения о характере деловых отношений. Страхователь
  77	Сведения о целях ФХД. Страхователь
  78	Сведения о целях установления  деловых отношений. Страхователь
  79	Уровень риска. Страхователь
  80	Сведения о деловой репутации. Страхователь
  81	Сведения о финансовом положении. Страхователь
  82	Сведения об источниках происхождения денежных средств. Страхователь
  83	id Застрахованного
  84	ФИО Застрахованного
  85	Гражданство Застрахованного
  86	Налоговое резидентство Застрахованного
  87	Дата рождения Застрахованного
  88	Пол Застрахованного
  89	ИНН/TIN Застрахованного
  90	СНИЛС Застрахованного
  91	Страна рождения  Застрахованного
  92	Место рождения Застрахованного
  93	E-mail Застрахованного
  94	Телефон Застрахованного
  95	Прописка Застрахованного. Индекс
  96	Прописка Застрахованного. Республика, край, область
  97	Прописка Застрахованного. Район
  98	Прописка Застрахованного. Город
  99	Прописка Застрахованного. Населенный пункт
  100	Прописка Застрахованного. Улица
  101	Прописка Застрахованного. Дом
  102	Прописка Застрахованного. Копус
  103	Прописка Застрахованного. Строение
  104	Прописка Застрахованного. Квартира
  105	Место пребывания Застрахованного. Индекс
  106	Место пребывания Застрахованного. Республика, край, область
  107	Место пребывания Застрахованного. Район
  108	Место пребывания Застрахованного. Город
  109	Место пребывания Застрахованного. Населенный пункт
  110	Место пребывания Застрахованного. Улица
  111	Место пребывания Застрахованного. Дом
  112	Место пребывания Застрахованного. Копус
  113	Место пребывания Застрахованного. Строение
  114	Место пребывания Застрахованного. Квартира
  115	Тип основного документа Застрахованного
  116	Серия документа Застрахованного
  117	Номер документа Застрахованного
  118	Дата выдачи документа Застрахованного
  119	Кем выдан документ Застрахованного
  120	Код подразделения выдачи документа Застрахованного
  121	Данные миграционной карты Застрахованного. Серия
  122	Данные миграционной карты Застрахованного. Номер
  123	Данные миграционной карты Застрахованного. Дата начала срока пребывания
  124	Данные миграционной карты Застрахованного. Дата окончания срока пребывания
  125	Наименование документа Застрахованного, подтверждающего право пребывания в РФ
  126	Дата начала срока пребывания Застрахованного в РФ
  127	Дата окончания срока пребывания Застрахованного в РФ
  128	ПДЛ Застрахованный (да/нет)
  129	Должность ПДЛ Застрахованного
  130	Работодатель ПДЛ Застрахованного
  131	Источник денежных средств ПДЛ Застрахованного
  75	Сведения о бенефициарном владельце. Страхователь
  76	Сведения о характере деловых отношений. Страхователь
  77	Сведения о целях ФХД. Страхователь
  78	Сведения о целях установления  деловых отношений. Страхователь
  79	Уровень риска. Страхователь
  80	Сведения о деловой репутации. Страхователь
  81	Сведения о финансовом положении. Страхователь
  82	Сведения об источниках происхождения денежных средств. Страхователь
  140	СС в валюте договора по риску Дожития 1 год
  141	СС в рублях по договору по риску Дожития 1 год
  142	Дата дожития 1 год
  143	СС  в валюте договора  по риску Дожития 2 года
  144	СС в рублях по договору по риску Дожития 2 год
  145	Дата дожития 2 года
  146	СС  в валюте договора  по риску Дожития 3 года
  147	СС в рублях по договору по риску Дожития 3 год
  148	Дата дожития 3 года
  149	СС  в валюте договора по риску Дожития 4 года
  150	СС в рублях по договору по риску Дожития 4 год
  151	Дата дожития 4 года
  152	СС  в валюте договора по риску Дожития 5 лет
  153	СС в рублях по договору по риску Дожития 5 год
  154	Дата дожития 5 лет
  155	СС  в валюте договора по риску Дожития 6 лет
  156	СС в рублях по договору по риску Дожития 6 год
  157	Дата дожития 6 лет
  158	СС  в валюте договора  по риску Дожития 7 лет
  159	СС в рублях по договору по риску Дожития 7 год
  160	Дата дожития 7 лет
  161	СС в валюте договора по риску Дожития 8 лет
  162	СС в рублях по договору по риску Дожития 8 год
  163	Дата дожития 8 лет
  164	СС в валюте договора по риску Дожития 9 лет
  165	СС в рублях по договору по риску Дожития 9 год
  166	Дата дожития 9 лет
  167	СС   в валюте договора по риску Дожития 10 лет
  168	СС в рублях по договору по риску Дожития 10 год
  169	Дата дожития 10 лет
  170	СС по Дожитию (CONTRACT_AMOUNT_CELL_NUMBER)
  171	СС в валюте договора по риску Смерть по любой причине
  172	СС в рублях по договору по риску Смерть по любой причине
  173	СС в валюте договора по риску НС
  174	СС   в рублях по договору по риску НС
  175	СС в валюте договора по риску кораблекрушение
  176	СС в рублях по договору  по риску кораблекрушение
  177	Выгодоприобретатель 1 по риску Дожитие (ВП 1 Дожитие)
  178	Дата рождения ВП1 Дожитие
  179	Место рождения ВП1 Дожитие
  180	Адрес места жительства ВП1 Дожитие
  181	Налоговое резидентство ВП1 Дожитие
  182	Отношение к Застрахованного к ВП1 Дожитие
  183	Доля ВП1 Дожитие
  184	Выгодоприобретатель 1 по риску Смерть по любой причине (ВП 1 СЛП)
  185	Дата рождения ВП1 СЛП
  186	Место рождения ВП1 СЛП
  187	Адрес места жительства ВП1 СЛП
  188	Налоговое резидентство ВП1 СЛП
  189	Отношение к Застрахованного к ВП1 СЛП
  190	Доля ВП1 СЛП
  191	Выгодоприобретатель 2 по риску Смерть по любой причине (ВП 1 СЛП)
  192	Дата рождения ВП2 СЛП
  193	Место рождения ВП2 СЛП
  194	Адрес места жительства ВП2 СЛП
  195	Налоговое резидентство ВП2 СЛП
  196	Отношение к Застрахованного к ВП2 СЛП
  197	Доля ВП2 СЛП
  198	Выгодоприобретатель 3 по риску Смерть по любой причине (ВП 1 СЛП)
  199	Дата рождения ВП3 СЛП
  200	Место рождения ВП3 СЛП
  201	Адрес места жительства ВП3 СЛП
  202	Налоговое резидентство ВП3 СЛП
  203	Отношение к Застрахованного к ВП3 СЛП
  204	Доля ВП3 СЛП
  205	Выгодоприобретатель 4 по риску Смерть по любой причине (ВП 1 СЛП)
  206	Дата рождения ВП4 СЛП
  207	Место рождения ВП4 СЛП
  208	Адрес места жительства ВП4 СЛП
  209	Налоговое резидентство ВП4 СЛП
  210	Отношение к Застрахованного к ВП4 СЛП
  211	Доля ВП4 СЛП
  212	Выгодоприобретатель 5 по риску Смерть по любой причине (ВП 1 СЛП)
  213	Дата рождения ВП5 СЛП
  214	Место рождения ВП5 СЛП
  215	Адрес места жительства ВП5 СЛП
  216	Налоговое резидентство ВП5 СЛП
  217	Отношение к Застрахованного к ВП5 СЛП
  218	Доля ВП5 СЛП
  219	Стратегия инвестирования
  220	Коэффициент участия 1
  221	Дата НЗБИ 1
  222	Дата экспирации 1
  223	Коэффициент участия 2
  224	Дата НЗБИ 2
  225	Дата экспирации 2
  226	Валюта гарантийного фонда
  227	Валюта рискового фонда
  228	Величина инвестиционного купона
  229	Наименование агента
  230	% КВ Агенту
  231	КВ Агенту в рублях
  232	ФИО оформившего сотрудника
  233	Таб. номер сотрудника
  234	Категория продавца (ФК/Розница)
  235	ВСП
  236	Город
  237	Филиал
  238	Сегмент продукта
  239	Получено согласие на участие в конкурсе (да/нет)
  240	Документы в ПО прикреплены (да/нет)
  241	Комментарии к прикрепленным документам
  242	Наличие медицинской анкеты
  243	Наличие финансовой анкеты
  244  Служебное поле, которое будет заполняться строковым значением, содержащим результат импорта изменений (STATUS_UPDATE_CELL_NUMBER)
  */
