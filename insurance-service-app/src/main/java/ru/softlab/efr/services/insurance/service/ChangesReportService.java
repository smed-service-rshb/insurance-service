package ru.softlab.efr.services.insurance.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.db.ChangeReportItem;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.repositories.ChangesReportRepository;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static ru.softlab.efr.services.insurance.controllers.Constants.CHANGE_REPORT_STORE_PATH;

/**
 * Сервис, формирующий отчёт по изменениям.
 *
 * @author olshansky
 * @since 30.01.2019
 */

@Service
public class ChangesReportService {

    private static final Logger LOGGER = Logger.getLogger(ChangesReportService.class);
    private static final String TITLE_DESCRIPTION = "Отчет об изменениях договоров за период с %s по %s";
    private static final List<String> TITLE_COLUMNS = asList("№ записи", "Номер договора", "Дата оформления договора", "ФИО Страхователя", "Дата изменения", "Время изменения", "Статус до изменения", "Статус после изменения", "Наименование вида прикрепленного документа", "Комплект обязательных документов полный", "Пользователь, который внес изменение");
    private static final String DEFAULT_CHANGES_REPORT_FILE_PATH = System.getProperty("java.io.tmpdir");

    @Autowired
    private ChangesReportRepository changesReportRepository;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ExtractProcessInfoService extractProcessInfoService;

    @Async("extractThreadPoolTaskExecutor")
    @Transactional
    public void createReportAsync(LocalDateTime startDate, LocalDateTime endDate, String uuid) {
        String fileName = getFileName();
        String storePath = getReportStoreFolder();
        String filePath = storePath.concat(fileName);
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), getChangeReportByCreateDateOutputStream(startDate, endDate, uuid));
        } catch (IOException e) {
            LOGGER.error(String.format("Не удалось сохранить отчёт в папку: %s", filePath), e);
        }
    }

    @Transactional
    public byte[] getChangeReportByCreateDateOutputStream(LocalDateTime createDate, LocalDateTime endDate, String extractUuid) {
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            String title = String.format(TITLE_DESCRIPTION,
                    ReportableContract.presentLocalDateTime(createDate, "dd.MM.yyyy HH.mm.ss"),
                    ReportableContract.presentLocalDateTime(endDate, "dd.MM.yyyy HH.mm.ss"));
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(title, TITLE_COLUMNS));

            Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(extractUuid);
            extractEntity.setStatus(ExtractStatus.SAVE);
            Sort sortByContractNumberAndCreationDate = new Sort(Sort.Direction.ASC, "contractNumber", "changeDate");
            byte[] byteArrayFromExcel = new byte[0];
            try (Stream<ChangeReportItem> changeReportItems = changesReportRepository.findByChangeDateBetween(createDate, endDate, sortByContractNumberAndCreationDate)) {
                changeReportItems.forEach(item ->
                        extractor.addRow(convertInsuranceContractToTemplate(item, index), index.getAndIncrement()));
                byteArrayFromExcel = extractor.getByteArrayFromExcel();
            } catch (Exception ex) {
                LOGGER.error(String.format("Произошла ошибка при формировании отчета по изменениям." +
                                " Дата начала периода %s, дата окончания периода %s",
                        createDate, endDate), ex);
                extractEntity.setStatus(ExtractStatus.ERROR);
            } finally {
                extractProcessInfoService.saveContentAndExtract(extractEntity, byteArrayFromExcel);
            }
            return byteArrayFromExcel;
        }
    }

    private List<Object> convertInsuranceContractToTemplate(ChangeReportItem changeReportItems, AtomicInteger index) {
        List<Object> result = new ArrayList<>();
        result.add(index.intValue() - 2);
        result.add(changeReportItems.getContractNumber());
        result.add(ReportableContract.presentLocalDate(changeReportItems.getConclusionDate()));
        result.add(changeReportItems.getHolderName());

        String date = "";
        String time = "";
        if (changeReportItems.getChangeDate() != null) {
            date = ReportableContract.presentLocalDate(changeReportItems.getChangeDate().toLocalDate());
            time = ReportableContract.presentLocalDateTime(changeReportItems.getChangeDate(), "HH:mm:ss");
        }
        result.add(date);
        result.add(time);
        result.add(changeReportItems.getPrevStatus());
        result.add(changeReportItems.getNextStatus());
        result.add(changeReportItems.getAttachedDocType());
        result.add(changeReportItems.isFullSetDocument() ? "Да" : "Нет");
        result.add(changeReportItems.getEmployeeName());
        return result;
    }

    public String getFileName() {
        return String.format("Отчёт по изменениям_%s.xlsx", ReportableContract.presentLocalDateTime(LocalDateTime.now(), "dd.MM.yyyy_HH.mm.ss"));
    }

    private String getReportStoreFolder() {
        String result = getValueOrDefault(CHANGE_REPORT_STORE_PATH, DEFAULT_CHANGES_REPORT_FILE_PATH);
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
