package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.client.CurrencyRateClient;
import ru.softlab.efr.common.dict.exchange.model.CurrencyCbRateRs;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.db.BaseIndex;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;
import ru.softlab.efr.services.insurance.model.rest.IncomeData;
import ru.softlab.efr.services.insurance.repositories.BaseIndexRepository;
import ru.softlab.efr.services.insurance.repositories.StrategyRepository;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;
import ru.softlab.efr.services.insurance.utils.InvestmentDictUpdateHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PropertySource(value = {"classpath:dict-update.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
@Service
public class StrategyBaseIndexService {

    protected static final Logger LOGGER = Logger.getLogger(ShareService.class);
    private static final String USD_ISO = "USD";
    private static final String BASE_INDEX_SETTING_PATH_NAME = "BASE_INDEX_PATH";
    private static final String REPLICATE_ENABLED = "replicateBaseIndexEnable";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${replicate.base.index.error.file.not.exist}")
    private String replicateBaseIndexErrorFileNotExist;
    @Value("${replicate.base.index.error}")
    private String replicateError;

    @Autowired
    private BaseIndexRepository baseIndexRepository;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StrategyRepository strategyRepository;
    @Autowired
    private CurrencyRateClient currencyRateClient;

    /**
     * Получить список всех базовых индексов за заданный период
     *
     * @param startDate  дата начала периода
     * @param endDate    дата окончания периода
     * @param strategyId id-стратегии
     * @return список базовых индексов
     */
    @Transactional(readOnly = true)
    public List<BaseIndex> getIndexByStrategyAndDateBetween(Long strategyId, LocalDate startDate, LocalDate endDate) {
        List<BaseIndex> indexes = baseIndexRepository.findAllByDateBetweenAndStrategy(startDate, endDate, strategyId, new Sort(Sort.Direction.ASC, "date"));
        indexes.stream().sorted(Comparator.comparing(BaseIndex::getDate)).collect(Collectors.toList());
        return indexes;
    }

    /**
     * Репликация справочника базовых индексов
     *
     * @throws IOException
     */
    private void parseAndSave() throws IOException {
        List<BaseIndex> baseIndexList = InvestmentDictUpdateHelper.getStrategyBaseIndexListFromXLSX(
                getPathOrDefault(),
                strategyRepository.findByTypesAndDeleted(
                        Arrays.asList(StrategyType.CLASSIC, StrategyType.LOCOMOTIVE), false));
        baseIndexRepository.save(baseIndexList);
    }

    /**
     * Запуск репликации справочника базовых индексов
     */
    @Scheduled(cron = "${strategy.base.index.update.schedule.cron}")
    @Transactional
    public void runUpdate() {
        if (needReplicate()) {
            if (isExistStoredFile()) {
                try {
                    parseAndSave();
                } catch (Exception e) {
                    LOGGER.warn(replicateError, e);
                }
            } else {
                LOGGER.warn(String.format(replicateBaseIndexErrorFileNotExist, getPathOrDefault()));
            }
        }
    }

    protected String getPathOrDefault() {
        SettingEntity settingEntity = settingsService.get(BASE_INDEX_SETTING_PATH_NAME);
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
        File f = new File(getPathOrDefault());
        return f.exists() && !f.isDirectory();
    }

    /**
     * Получить данными по динамике инвестиционного дохода
     *
     * @param insurance страховой договор
     * @param indexes   базовые индексы
     * @return список содержащий данные по базовым активам и инвестиционный доход отсортированный по дате в порядке увеличения
     */
    public List<IncomeData> calculateIncomes(Insurance insurance, List<BaseIndex> indexes, StrategyType type) {
        List<IncomeData> incomes = new LinkedList<>();
        if (indexes.isEmpty()) {
            return incomes;
        }
        Map<LocalDate, BigDecimal> usdRates = getUSDRates(insurance.getStartDate(), LocalDate.now());
        BigDecimal startRate = usdRates.get(insurance.getStartDate());
        BigDecimal rate = insurance.getStrategy().getStrategyProperties().get(0).getRate();
        BigDecimal premium = insurance.getPremium();
        BigDecimal startIndex = indexes.get(0).getValue();
        if (insurance.getStrategy().getStrategyProperties().size() > 1) {
            premium = premium.divide(BigDecimal.valueOf(2));
        }
        final BigDecimal finalPremium = premium;
        if (startRate != null && rate != null && premium != null && startIndex != null) {
            indexes.forEach(i -> {
                if (i.getValue() != null && usdRates.get(i.getDate()) != null) {
                    BigDecimal relation = i.getValue().subtract(startIndex).divide(startIndex, 5, RoundingMode.HALF_UP);
                    BigDecimal result = null;
                    if (type == StrategyType.CLASSIC) {
                        result = finalPremium.multiply(rate.divide(BigDecimal.valueOf(100)))
                                .multiply(relation)
                                .multiply(usdRates.get(i.getDate()).divide(startRate, 2, RoundingMode.HALF_UP));
                    }
                    if (result == null || BigDecimal.ZERO.compareTo(result) < 0) {
                        incomes.add(new IncomeData(i.getDate(), result, relation.multiply(BigDecimal.valueOf(100)), i.getValue()));
                    } else {
                        incomes.add(new IncomeData(i.getDate(), BigDecimal.ZERO, relation.multiply(BigDecimal.valueOf(100)), i.getValue()));
                    }
                }
            });
        }
        return incomes;
    }

    public String getLocomotiveIncome(List<BaseIndex> indexes){
        if(indexes.isEmpty()){
            return "";
        }
        BigDecimal first = indexes.get(0).getValue();
        BigDecimal last = indexes.get(indexes.size() - 1).getValue();
        BigDecimal divide = last.divide(first, 2, BigDecimal.ROUND_HALF_UP);
        if(divide.compareTo(BigDecimal.valueOf(2)) > 0){
            return last.subtract(first).divide(first,2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).toString();
        } else if (last.compareTo(first) >0){
            return "10%";
        } else {
            return "0";
        }
    }

    private Map<LocalDate, BigDecimal> getUSDRates(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, BigDecimal> rates = new HashMap<>();
        try {
            CurrencyCbRateRs cbCurrencyRateList = currencyRateClient.getCbCurrencyRateList(USD_ISO,
                    startDate.format(FORMATTER), endDate.format(FORMATTER), 10);
            if (!cbCurrencyRateList.getRates().isEmpty()) {
                cbCurrencyRateList.getRates().forEach(rate -> rates.put(LocalDate.parse(rate.getDate(), FORMATTER), rate.getRate()));
            }
        } catch (Exception e) {
            LOGGER.warn("Произошла ошибка при получении курса валюты", e);
        }

        return rates;
    }

    @Transactional
    public byte[] getExcelReport(Insurance insurance, Strategy strategy) {
        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(String.format("Отчет по базовым активам за период: %s - %s",
                    insurance.getStartDate().format(FORMATTER), LocalDate.now().format(FORMATTER)),
                    Arrays.asList("Дата", "Значение базового актива")));
            List<BaseIndex> indexes = getIndexByStrategyAndDateBetween(strategy.getId(), insurance.getStartDate(), LocalDate.now());
            if(strategy.getStrategyType().equals(StrategyType.LOCOMOTIVE)) {
                List<IncomeData> incomeData = calculateIncomes(insurance, indexes, strategy.getStrategyType());
                incomeData.forEach(i -> extractor.addRow(Arrays.asList(i.getDate().format(FORMATTER), i.getRelativeValue()), index.getAndIncrement()));
            } else {
                indexes.forEach(i -> extractor.addRow(Arrays.asList(i.getDate().format(FORMATTER), i.getValue()), index.getAndIncrement()));
            }
            return extractor.getByteArrayFromExcel();
        }
    }
}

