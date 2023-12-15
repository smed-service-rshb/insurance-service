package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.Quote;
import ru.softlab.efr.services.insurance.model.db.ShareEntity;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;
import ru.softlab.efr.services.insurance.pojo.ParseShareResult;
import ru.softlab.efr.services.insurance.repositories.QuoteRepository;
import ru.softlab.efr.services.insurance.repositories.ShareRepository;
import ru.softlab.efr.services.insurance.repositories.StrategyRepository;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;
import ru.softlab.efr.services.insurance.utils.InvestmentDictUpdateHelper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PropertySource(value = {"classpath:dict-update.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
@Service
public class ShareService {

    private static final Logger LOGGER = Logger.getLogger(ShareService.class);
    private static final String SHARE_SETTING_PATH_NAME = "SHARE_PATH";
    private static final String REPLICATE_ENABLED = "replicateShareEnable";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${replicate.share.error.file.not.exist}")
    private String replicateShareErrorFileNotExist;
    @Value("${replicate.share.error}")
    private String replicateShareError;

    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StrategyRepository strategyRepository;

    /**
     * Получить список всех акций, у которых присутствуют котировки за заданный период со списком котировок за заданный период.
     *
     * @param strategyId идентификатор стратегии
     * @param startDate  дата начала периода
     * @return список акций
     */
    @Transactional(readOnly = true)
    public List<ShareEntity> getShareWithQuoteByDate(Long strategyId, LocalDate startDate) {

        List<Quote> quotes = quoteRepository.findAllByStrategyIdAndDateBetween(strategyId, startDate, LocalDate.now(), new Sort("date"));
        Map<Long, ShareEntity> shareEntityMap = new LinkedHashMap<>();
        quotes.forEach(q -> {
            if (shareEntityMap.containsKey(q.getShare().getId())) {
                shareEntityMap.get(q.getShare().getId()).getQuotes().add(q);
            } else {
                ShareEntity shareEntity = new ShareEntity(q.getShare().getId(), q.getShare().getName(), q.getShare().getDescription());
                shareEntity.getQuotes().add(q);
                shareEntityMap.put(q.getShare().getId(), shareEntity);
            }
        });
        return new ArrayList<>(shareEntityMap.values());
    }

    /**
     * Репликация справочника котировок акций
     *
     * @throws IOException
     */
    private void parseAndSave() throws IOException {
        ParseShareResult result = InvestmentDictUpdateHelper.getShareEntityListFromXLSX(getPath(),
                shareRepository.findAll(),
                strategyRepository.findByTypeAndDeleted(StrategyType.COUPON, false));
        quoteRepository.save(result.getQuotes());
        shareRepository.save(result.getShares());
    }

    /**
     * Запуск репликации справочника котировок акций
     */
    @Scheduled(cron = "${share.quotes.update.schedule.cron}")
    @Transactional
    public void runUpdate() {
        if (needReplicate()) {
            if (isExistStoredFile()) {
                try {
                    parseAndSave();
                } catch (Exception e) {
                    LOGGER.warn(replicateShareError, e);
                }
            } else {
                LOGGER.warn(String.format(replicateShareErrorFileNotExist, getPath()));
            }
        }
    }

    protected String getPath() {
        SettingEntity settingEntity = settingsService.get(SHARE_SETTING_PATH_NAME);
        if (settingEntity != null) {
            return settingEntity.getValue();
        } else {
            return "";
        }
    }

    protected boolean needReplicate() {
        SettingEntity settingEntity = settingsService.get(REPLICATE_ENABLED);
        if (settingEntity != null) {
            return Boolean.valueOf(settingEntity.getValue());
        } else {
            return true;
        }
    }

    private boolean isExistStoredFile() {
        File f = new File(getPath());
        return f.exists() && !f.isDirectory();
    }

    @Transactional
    public byte[] getExcelReport(Insurance insurance) {
        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            List<ShareEntity> shares = getShareWithQuoteByDate(insurance.getStrategy().getId(), insurance.getStartDate());
            List<String> shareNames = new ArrayList<>(Collections.singletonList("Дата"));
            shareNames.addAll(shares.stream().map(i -> i.getName() + (i.getDescription() != null ? (" (" + i.getDescription() + ")") : "")).collect(Collectors.toList()));
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(String.format("Отчет о котировках акций за период: %s - %s",
                    insurance.getStartDate().format(FORMATTER), LocalDate.now().format(FORMATTER)),
                    shareNames));
            for (LocalDate date = insurance.getStartDate(); date.isBefore(LocalDate.now()); date = date.plusDays(1L)) {
                List<Object> data = new ArrayList<>();
                final LocalDate currentDateValue = date;
                data.add(currentDateValue.format(FORMATTER));
                shares.forEach(shareEntity -> {
                    Quote quoteValue = shareEntity.getQuotes()
                            .stream()
                            .filter(quote -> quote.getDate().equals(currentDateValue))
                            .findFirst().orElse(new Quote());
                    data.add(quoteValue.getValue() != null ? quoteValue.getValue() : "");
                });
                extractor.addRow(data, index.getAndIncrement());
            }

            return extractor.getByteArrayFromExcel();
        }
    }
}
