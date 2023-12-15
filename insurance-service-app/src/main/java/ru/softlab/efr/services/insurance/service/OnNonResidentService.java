package ru.softlab.efr.services.insurance.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.NonResidentDTO;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static ru.softlab.efr.services.insurance.controllers.Constants.CHANGE_REPORT_STORE_PATH;

/**
 * Сервис, формирующий отчёт по налоговым нерезидентам
 *
 * @author Dzhemaletdinov
 * @since 03.02.2019
 */
@Service
public class OnNonResidentService {

    private static final Logger LOGGER = Logger.getLogger(OnNonResidentService.class);
    private static final String DEFAULT_NON_RESIDENT_REPORT_FILE_PATH = System.getProperty("java.io.tmpdir");
    private static final List<String> TITLE_COLUMNS = asList("ФИО клиента/\n" + "застрахованного лица/\n" + "выгодоприобретателя\n", "Номер договора", "Дата оформления договора", "Дата окончания действия договора", "Сумма договора", "Страна налогового резидентства", "ИНН/TIN");
    private static final String TITLE_DESCRIPTION = "Отчет о налоговых резидентах";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String NOT_SPECIFIED = "(не указано)";

    @Autowired
    private InsuranceRepository insuranceRepository;

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
            FileUtils.writeByteArrayToFile(new File(filePath), generateReportForNonResident(startDate, endDate, uuid));
        } catch (IOException e) {
            LOGGER.error(String.format("Не удалось сохранить отчёт о налоговых нерезидентах в папку: %s", filePath), e);
        }
    }

    @Transactional
    public byte[] generateReportForNonResident(LocalDateTime startDate, LocalDateTime endDate, String uuid) {

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(titleWithInfo(), TITLE_COLUMNS));

            Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(uuid);
            extractEntity.setStatus(ExtractStatus.SAVE);
            byte[] byteArrayFromExcel = new byte[0];
            try {
                extractor.addRow(emptyRow(0), index.getAndIncrement());
                processHistoricalData(insuranceRepository.findAllNonResidentHolders(startDate, endDate), extractor, index);

                extractor.addRow(emptyRow(1), index.getAndIncrement());
                processHistoricalData(insuranceRepository.findAllNonResidentInsureds(startDate, endDate), extractor, index);

                extractor.addRow(emptyRow(2), index.getAndIncrement());
                index.getAndIncrement();
                processHistoricalData(insuranceRepository.findAllNonResidentRecipients(startDate, endDate), extractor, index);

                byteArrayFromExcel = extractor.getByteArrayFromExcel();
            } catch (Exception ex) {
                LOGGER.error(String.format("Произошла ошибка при формировании отчета о налоговых нерезидентах." +
                                " Дата начала периода %s, дата окончания периода %s",
                        startDate, endDate), ex);
                extractEntity.setStatus(ExtractStatus.ERROR);
            } finally {
                extractProcessInfoService.saveContentAndExtract(extractEntity, byteArrayFromExcel);
            }

            return byteArrayFromExcel;
        }
    }

    /**
     * @return наименование + информация о проверке
     */
    private String titleWithInfo() {
        return String.format("%s %n" + "Дата проведенной проверки: %s%n Время проведенной проверки: %s",
                TITLE_DESCRIPTION, LocalDateTime.now().format(DATE_FORMATTER), LocalTime.now().format(TIME_FORMATTER));
    }

    public String getFileName() {
        return String.format("Отчёт о налоговых нерезидентах %s.xlsx", presentLocalDate(LocalDateTime.now()));
    }

    private static String presentLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return NOT_SPECIFIED;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss"));
    }

    private List<Object> convertNonResidentToTemplate(NonResidentDTO residentDTO) {
        List<Object> result = new ArrayList<>();

        result.add(StringUtils.getClientFullName(residentDTO.getSurName(), residentDTO.getFirstName(), residentDTO.getMiddleName()));
        result.add(residentDTO.getContractNumber() != null ? residentDTO.getContractNumber() : NOT_SPECIFIED);
        result.add(residentDTO.getStartDate() != null ? residentDTO.getStartDate().format(DATE_FORMATTER) : NOT_SPECIFIED);
        result.add(residentDTO.getCloseDate() != null ? residentDTO.getCloseDate().format(DATE_FORMATTER) : NOT_SPECIFIED);
        result.add(residentDTO.getAmount() != null ? residentDTO.getAmount() : NOT_SPECIFIED);
        result.add(residentDTO.getCitizenshipCountry() != null
                && residentDTO.getTaxResidenceEnum() == null ? residentDTO.getCitizenshipCountry() : NOT_SPECIFIED);
        if (residentDTO.getTaxResidenceEnum() != null) {
            result.add(NOT_SPECIFIED);
        }
        result.add(residentDTO.getInn());
        return result;
    }

    private String getReportStoreFolder() {
        String result = getValueOrDefault(CHANGE_REPORT_STORE_PATH, DEFAULT_NON_RESIDENT_REPORT_FILE_PATH);
        if (result.endsWith("/")) {
            return result;
        }
        return result.concat("/");
    }

    private String getValueOrDefault(String key, String defaultVal) {
        SettingEntity settingEntity = settingsService.get(key);
        if (settingEntity == null || org.apache.commons.lang.StringUtils.isBlank(settingEntity.getValue())) {
            LOGGER.warn(String.format("Не удалось прочитать значение настройки %s, будет использовано значение по-умолчанию: %s",
                    key, defaultVal));
            return defaultVal;
        }
        return settingEntity.getValue();
    }

    private List<Object> emptyRow(int i) {
        List<Object> result = new ArrayList<>();
        if (i == 0) {
            result.add("Данные о клиентах");
        } else if (i == 1) {
            result.add("Данные о застрахованных");
        } else if (i == 2) {
            result.add("Данные о выгодоприобретателях");
        }
        return result;
    }

    private void processHistoricalData(Stream<NonResidentDTO> modifyStream, ExcelReportExtractor extractor, AtomicInteger index) {
        try {
            modifyStream.forEach(item -> {
                NonResidentDTO dto = new NonResidentDTO(item);
                Revision<Integer, Insurance> insuranceRevision = insuranceRepository.findLastChangeRevision(item.getInsuranceId());
                if (Objects.nonNull(insuranceRevision)) {
                    dto.setFirstName(insuranceRevision.getEntity().getHolder().getFirstName());
                    dto.setSurName(insuranceRevision.getEntity().getHolder().getSurName());
                    dto.setMiddleName(insuranceRevision.getEntity().getHolder().getMiddleName());
                    dto.setCitizenshipCountry(insuranceRevision.getEntity().getHolder().getCitizenshipCountry());
                    dto.setInn(insuranceRevision.getEntity().getHolder().getInn());
                }
                extractor.addRow(convertNonResidentToTemplate(dto), index.getAndIncrement());
            });
        } finally {
            modifyStream.close();
        }
    }
}
