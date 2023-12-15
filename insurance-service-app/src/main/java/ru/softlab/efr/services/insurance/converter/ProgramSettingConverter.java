package ru.softlab.efr.services.insurance.converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.common.client.CurrenciesClient;
import ru.softlab.efr.common.client.PrintTemplatesClient;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.RequiredDocumentSetting;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Конвертер представления сущности параметров программ страхования.
 */
@Service
@Transactional
public class ProgramSettingConverter {

    private static final Logger LOGGER = Logger.getLogger(ProgramSettingConverter.class);
    private static final long GET_CURRENCIES_TIMEOUT = 10L;
    private static final long GET_TEMPLATES_TIMEOUT = 10L;

    private final CurrenciesClient currenciesClient;
    private final PrintTemplatesClient printTemplatesClient;
    private final ProgramService programService;
    private final ProgramSettingService programSettingService;
    private final RequiredDocumentService requiredDocumentService;
    private final RequiredFieldService requiredFieldService;
    private final RiskService riskService;
    private final StrategyService strategyService;
    private final CalculationService calculationService;
    private final StrategyConverter converter;

    @Autowired
    public ProgramSettingConverter(CurrenciesClient currenciesClient, PrintTemplatesClient printTemplatesClient, ProgramService programService, ProgramSettingService programSettingService, RequiredDocumentService requiredDocumentService, RequiredFieldService requiredFieldService, RiskService riskService, StrategyService strategyService, CalculationService calculationService, StrategyConverter converter) {
        this.currenciesClient = currenciesClient;
        this.printTemplatesClient = printTemplatesClient;
        this.programService = programService;
        this.programSettingService = programSettingService;
        this.requiredDocumentService = requiredDocumentService;
        this.requiredFieldService = requiredFieldService;
        this.riskService = riskService;
        this.strategyService = strategyService;
        this.calculationService = calculationService;
        this.converter = converter;
    }

    public static Predicate<RiskDocument> isNewDocument(List<RequiredDocumentSetting> backDocList) {
        return doc -> backDocList.stream().noneMatch(a -> a.getRequiredDocument().getId().equals(doc.getId()));
    }

    public static Predicate<RiskDocument> getFrontDocByBackId(RequiredDocumentSetting backDocument) {
        return front -> front.getId().equals(backDocument.getRequiredDocument().getId());
    }

    public static Predicate<Risk> isNewRisk(List<RiskSetting> backRiskList) {
        return frontRiskEntity -> backRiskList.stream().noneMatch(backRiskEntity -> backRiskEntity.getRisk().getId().equals(frontRiskEntity.getId()));
    }

    public static Predicate<Risk> getFrontRiskByBackId(RiskSetting backRisk) {
        return front -> front.getId().equals(backRisk.getRisk().getId());
    }

    public static Predicate<RiskSetting> filterDeleted(List<Risk> frontRiskList) {
        return backRiskEntity -> frontRiskList.stream().anyMatch(frontRiskEntity -> frontRiskEntity.getId().equals(backRiskEntity.getRisk().getId()));
    }

    /**
     * Преобразование параметров программы страхования из представления БД в представление API.
     *
     * @param programSetting Сущность параметров программы страхования в представлении БД.
     * @return Сущность параметров программы страхования в представлении API.
     */
    public ProgramSettingDataForList convert(ProgramSetting programSetting) {
        ProgramSettingDataForList converted = new ProgramSettingDataForList();
        converted.setId(programSetting.getId());
        converted.setProgramId(programSetting.getProgram().getId());
        converted.setPolicyCode(programSetting.getProgram().getPolicyCode());
        converted.setProgram(programSetting.getProgram().getName());
        converted.setProgramKind(programSetting.getProgram() != null ? ProgramKind.valueOf(programSetting.getProgram().getType().name()) : null);
        converted.setCalendarUnit(programSetting.getCalendarUnit() != null ? CalendarUnit.valueOf(programSetting.getCalendarUnit().name()) : null);
        converted.setStartDate(programSetting.getStartDate().toLocalDateTime().toLocalDate());
        converted.setEndDate(programSetting.getEndDate().toLocalDateTime().toLocalDate());
        converted.setMinimumTerm(programSetting.getMinimumTerm());
        converted.setMaximumTerm(programSetting.getMaximumTerm());
        converted.setMinSum(programSetting.getMinSum());
        converted.setMaxSum(programSetting.getMaxSum());
        converted.setMinPremium(programSetting.getMinPremium());
        converted.setMaxPremium(programSetting.getMaxPremium());
        converted.setCurrency(getCurrencyById(programSetting.getCurrency()));

        return converted;
    }

    public FindProgramSettingResult convertFindProgram(ProgramSetting programSetting, FindProgramSettingRq findProgramSettingRq) {

        //для поиска программ без указания срока страхования используем минимальный срок страхования программы
        if (findProgramSettingRq.getTerm() == null) {
            findProgramSettingRq.setTerm(programSetting.getMinimumTerm());
        }

        // считаем итоговую страховую сумму, страховую премию за один период внесения взноса и страховую премию за весь период действия договора
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal premiumPerPayPeriod = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;

        int periodCount = calculationService.periodCount(CalendarUnitEnum.valueOf(findProgramSettingRq.getCalendarUnit().name()),
                findProgramSettingRq.getTerm(), programSetting.getPeriodicity());


        if (findProgramSettingRq.getAmount() == null) {
            //для поиска программ без указания суммы или премии страхования возвращаем фиксированные значения настроек программы
            sum = programSetting.getInsuranceAmount();
            totalPremium = programSetting.getBonusAmount();
            premiumPerPayPeriod = programSetting.getBonusAmount();
        } else if (FindProgramType.SUM.equals(findProgramSettingRq.getType())) {
            sum = findProgramSettingRq.getAmount();
            totalPremium = calculationService.getInsurancePremiumByProgramSetting(programSetting, sum);
            premiumPerPayPeriod = calculationService.getInsurancePremiumPerPeriodByProgramSetting(programSetting, sum,
                    CalendarUnitEnum.valueOf(findProgramSettingRq.getCalendarUnit().name()),
                    findProgramSettingRq.getTerm());

        } else if (FindProgramType.PREMIUM.equals(findProgramSettingRq.getType())) {
            premiumPerPayPeriod = findProgramSettingRq.getAmount();
            totalPremium = premiumPerPayPeriod.multiply(BigDecimal.valueOf(periodCount));
            sum = calculationService.getInsuranceAmountByProgramSetting(programSetting, findProgramSettingRq.getAmount(),
                    CalendarUnitEnum.valueOf(findProgramSettingRq.getCalendarUnit().name()),
                    findProgramSettingRq.getTerm());

        }

        // рассчитываем страховые суммы и страховые премии по рискам
        List<FindProgramRisk> requiredRisks = new ArrayList<>();
        List<FindProgramRisk> additionalRisks = new ArrayList<>();
        List<RequiredField> requiredFields = new ArrayList<>();
        List<RiskDocument> documents = new ArrayList<>();
        List<ContractTemplate> templates = new ArrayList<>();
        List<StrategyData> strategies = new ArrayList<>();
        if (!CollectionUtils.isEmpty(programSetting.getRequiredRiskSettingList())) {
            requiredRisks = convertBackRiskList2FrontRiskList(programSetting.getRequiredRiskSettingList(), programSetting,
                    sum, totalPremium, periodCount);
        }
        if (!CollectionUtils.isEmpty(programSetting.getAdditionalRiskSettingList())) {
            additionalRisks = convertBackRiskList2FrontRiskList(programSetting.getAdditionalRiskSettingList(), programSetting,
                    sum, totalPremium, periodCount);
        }
        if (!CollectionUtils.isEmpty(programSetting.getRequiredFieldList())) {
            requiredFields = programSetting.getRequiredFieldList().stream().map(field ->
                    new RequiredField(field.getName(), field.getStrId())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(programSetting.getRequiredDocumentList())) {
            documents = programSetting.getRequiredDocumentList().stream()
                    .filter(document -> !Boolean.TRUE.equals(document.getRequiredDocument().getDeleted()))
                    .map(document ->
                            new RiskDocument(document.getRequiredDocument().getId(), document.getRequiredDocument().getType(), document.getStatus()))
                    .collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(programSetting.getDocumentTemplateList())) {
            templates = getDocumentTemplateById(programSetting.getDocumentTemplateList());
        }

        if (!CollectionUtils.isEmpty(programSetting.getStrategyList())) {
            strategies = programSetting.getStrategyList().stream()
                    .filter(strategy -> !Boolean.TRUE.equals(strategy.getDeleted()))
                    .map(converter::convert).collect(Collectors.toList());
        }

        return new FindProgramSettingResult(
                programSetting.getProgram().getId(),
                programSetting.getProgram().getName(),
                programSetting.getProgram().getType() != null ? ProgramKind.valueOf(programSetting.getProgram().getType().name()) : null,
                programSetting.getProgram().getNumber(),
                programSetting.getId(),
                programSetting.getPremiumMethod() != null ? PremiumMethod.valueOf(programSetting.getPremiumMethod().name()) : null,
                programSetting.getCoefficient(),
                premiumPerPayPeriod,
                sum,
                programSetting.getMinimumTerm(),
                programSetting.getMaximumTerm(),
                programSetting.getCalendarUnit() != null ? CalendarUnit.valueOf(programSetting.getCalendarUnit().name()) : null,
                programSetting.getCurrency(),
                programSetting.getPeriodicity() != null ? PaymentPeriodicity.valueOf(programSetting.getPeriodicity().name()) : null,
                programSetting.getProgram().getPolicyCode(),
                programSetting.getProgram().getVariant(),
                programSetting.getProgram().getCoolingPeriod(),
                requiredRisks,
                additionalRisks,
                requiredFields,
                documents,
                templates,
                strategies,
                programSetting.getGuaranteeLevel(),
                programSetting.getPaymentTerm(),
                programSetting.getPolicyHolderInsured(),
                programSetting.getSpecialRate(),
                programSetting.getSpecialRateValue(),
                programSetting.getIndividualRate()
        );
    }

    private List<FindProgramRisk> convertBackRiskList2FrontRiskList(List<RiskSetting> backRisks, ProgramSetting programSetting,
                                                                    BigDecimal sum, BigDecimal totalPremium, int periodCount) {
        return backRisks.stream().map(risk -> {
            BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(risk);
            BigDecimal riskSum = calculationService.getInsuranceAmountByRiskSetting(risk, sum, totalPremium, periodCount);
            return new FindProgramRisk(
                    risk.getRisk().getId(),
                    risk.getRisk().getName(),
                    risk.getSignAmount(),
                    riskSum,
                    riskPremium,
                    risk.getRecordAmount() != null ? RiskRecordAmountType.valueOf(risk.getRecordAmount().name()) : null,
                    risk.getType() != null ? MethodCalcRisk.valueOf(risk.getType().name()) : null,
                    null, /*TODO: переделать underwritingRate Андеррайтинский коэффициент*/
                    calculationService.getRiskAmounts(risk, totalPremium, periodCount),
                    risk.getRisk().getBenefitsInsured(),
                    RiskCalculationTypeEnum.DEPENDS_ON_TERM == risk.getCalculationType() ? risk.getOtherRiskParam() : null
            );
        }).collect(Collectors.toList());
    }

    private Risk convertBackRisk2FrontRisk(RiskSetting backRisk) {
        return new Risk(backRisk.getRisk().getId(),
                backRisk.getRisk() != null ? backRisk.getRisk().getName() : null,
                backRisk.getSignAmount(),
                backRisk.getMinRiskAmount(),
                backRisk.getMaxRiskAmount(),
                backRisk.getCalculationType() != null ? RiskCalculationType.valueOf(backRisk.getCalculationType().name()) : null,
                backRisk.getRiskAmount(),
                backRisk.getRiskDependence() != null ? backRisk.getRiskDependence().getId() : null,
                backRisk.getCalculationCoefficient(),
                backRisk.getRiskPremium(),
                backRisk.getCalculationCoefficientPremium(),
                backRisk.getRiskReturnRate(),
                backRisk.getOtherRiskParam(),
                backRisk.getRulesDetails(),
                backRisk.getInsuranceRule(),
                backRisk.getInsuranceKind(),
                backRisk.getSortPriority(),
                backRisk.getRecordAmount() != null ? RiskRecordAmountType.valueOf(backRisk.getRecordAmount().name()) : null,
                backRisk.getType() != null ? MethodCalcRisk.valueOf(backRisk.getType().name()) : null
        );
    }

    public ProgramSettingData convertDetail(ProgramSetting backEntity) {

        List<ContractTemplate> frontTemplates = new ArrayList<>();
        List<Strategy> frontStrategies = new ArrayList<>();
        List<Risk> frontRisks = new ArrayList<>();
        List<Risk> optionalRisks = new ArrayList<>();
        List<RiskDocument> documents = new ArrayList<>();
        List<RequiredField> requiredFields = new ArrayList<>();

        if (!CollectionUtils.isEmpty(backEntity.getDocumentTemplateList())) {
            frontTemplates = getDocumentTemplateById(backEntity.getDocumentTemplateList());
        }
        if (!CollectionUtils.isEmpty(backEntity.getStrategyList())) {
            frontStrategies = backEntity.getStrategyList().stream()
                    .filter(strategy -> !Boolean.TRUE.equals(strategy.getDeleted()))
                    .map(m -> new Strategy(m.getId(), m.getName())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(backEntity.getRequiredRiskSettingList())) {
            frontRisks = backEntity.getRequiredRiskSettingList().stream()
                    .map(this::convertBackRisk2FrontRisk).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(backEntity.getAdditionalRiskSettingList())) {
            optionalRisks = backEntity.getAdditionalRiskSettingList().stream()
                    .map(this::convertBackRisk2FrontRisk).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(backEntity.getRequiredDocumentList())) {
            documents = backEntity.getRequiredDocumentList().stream()
                    .filter(document -> !Boolean.TRUE.equals(document.getRequiredDocument().getDeleted()))
                    .map(m -> new RiskDocument(m.getRequiredDocument().getId(), m.getRequiredDocument().getType(), m.getStatus())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(backEntity.getRequiredFieldList())) {
            requiredFields = backEntity.getRequiredFieldList().stream()
                    .map(m -> new RequiredField(m.getName(), m.getStrId())).collect(Collectors.toList());
        }

        return new ProgramSettingData(
                backEntity.getId(),
                backEntity.getProgram() != null && backEntity.getProgram().getType() != null ? ProgramKind.valueOf(backEntity.getProgram().getType().name()) : null,
                backEntity.getProgram().getId(),
                AppUtils.mapTimestamp2LocalDate(backEntity.getStartDate()),
                AppUtils.mapTimestamp2LocalDate(backEntity.getEndDate()),
                backEntity.getStaticDate() != null ? AppUtils.mapTimestamp2LocalDate(backEntity.getStaticDate()) : null,
                frontStrategies,
                backEntity.getPolicyHolderInsured(),
                backEntity.getMinimumTerm(),
                backEntity.getMaximumTerm(),
                backEntity.getPaymentTerm(),
                backEntity.getCalendarUnit() != null ? CalendarUnit.valueOf(backEntity.getCalendarUnit().name()) : null,
                backEntity.getCurrency(),
                backEntity.getMinSum(),
                backEntity.getMaxSum(),
                backEntity.getMinPremium(),
                backEntity.getMaxPremium(),
                backEntity.getPremiumMethod() != null ? PremiumMethod.valueOf(backEntity.getPremiumMethod().name()) : null,
                backEntity.getCoefficient(),
                backEntity.getBonusAmount(),
                backEntity.getInsuranceAmount(),
                backEntity.getTariff(),
                backEntity.getPeriodicity() != null ? PaymentPeriodicity.valueOf(backEntity.getPeriodicity().name()) : null,
                backEntity.getUnderwriting() != null ? UnderwritingKind.valueOf(backEntity.getUnderwriting().name()) : null,
                frontTemplates,
                frontRisks,
                optionalRisks,
                documents,
                requiredFields,
                backEntity.getMinAgeHolder(),
                backEntity.getMaxAgeHolder(),
                backEntity.getMinAgeInsured(),
                backEntity.getMaxAgeInsured(),
                backEntity.getMinGrowth(),
                backEntity.getMaxGrowth(),
                backEntity.getMinWeight(),
                backEntity.getMaxWeight(),
                backEntity.getGender() != null ? Gender.valueOf(backEntity.getGender().name()) : null,
                backEntity.getMaxUpperPressure(),
                backEntity.getMinUpperPressure(),
                backEntity.getMaxLowerPressure(),
                backEntity.getMinLowerPressure(),
                backEntity.getGuaranteeLevel(),
                backEntity.getSpecialRate(),
                backEntity.getSpecialRateValue(),
                backEntity.getIndividualRate(),
                backEntity.getDiscount()
        );
    }

    public void convertDetail(ProgramSettingData frontEntity, ProgramSetting backEntity) {
        ProgramSetting storedEntity = backEntity.getId() != null ? programSettingService.findById(backEntity.getId()) : null;

        Program program = programService.findById(frontEntity.getProgramId());
        if (backEntity.getVersion() == null) {
            backEntity.setVersion(0L);
        }
        backEntity.setProgram(program);
        backEntity.setStartDate(AppUtils.mapLocalDate2Timestamp(frontEntity.getStartDate()));
        backEntity.setEndDate(AppUtils.mapLocalDate2Timestamp(frontEntity.getEndDate()));
        backEntity.setStaticDate(frontEntity.getStaticDate() != null
                ? AppUtils.mapLocalDate2Timestamp(frontEntity.getStaticDate()) : null);
        backEntity.setPolicyHolderInsured(frontEntity.isPolicyholderInsured());
        backEntity.setMinimumTerm(frontEntity.getMinimumTerm());
        backEntity.setMaximumTerm(frontEntity.getMaximumTerm());
        backEntity.setCalendarUnit(frontEntity.getCalendarUnit() != null
                ? CalendarUnitEnum.valueOf(frontEntity.getCalendarUnit().name()) : null);
        backEntity.setCurrency(frontEntity.getCurrency());
        backEntity.setMinSum(frontEntity.getMinSum());
        backEntity.setMaxSum(frontEntity.getMaxSum());
        backEntity.setMinPremium(frontEntity.getMinPremium());
        backEntity.setMaxPremium(frontEntity.getMaxPremium());
        backEntity.setPremiumMethod(frontEntity.getPremiumMethod() != null
                ? PremiumMethodEnum.valueOf(frontEntity.getPremiumMethod().name()) : null);
        backEntity.setCoefficient(frontEntity.getCoefficient());
        backEntity.setBonusAmount(frontEntity.getPremium());
        backEntity.setInsuranceAmount(frontEntity.getSum());
        backEntity.setTariff(frontEntity.getTariff());
        backEntity.setPeriodicity(frontEntity.getPeriodicity() != null
                ? PeriodicityEnum.valueOf(frontEntity.getPeriodicity().name()) : null);
        backEntity.setUnderwriting(frontEntity.getUnderwriting() != null
                ? UnderwritingEnum.valueOf(frontEntity.getUnderwriting().name()) : null);
        backEntity.setMinAgeHolder(frontEntity.getMinAgeHolder());
        backEntity.setMaxAgeHolder(frontEntity.getMaxAgeHolder());
        backEntity.setMinAgeInsured(frontEntity.getMinAgeInsured());
        backEntity.setMaxAgeInsured(frontEntity.getMaxAgeInsured());
        backEntity.setMinGrowth(frontEntity.getMinGrowth());
        backEntity.setMaxGrowth(frontEntity.getMaxGrowth());
        backEntity.setMinWeight(frontEntity.getMinWeight());
        backEntity.setMaxWeight(frontEntity.getMaxWeight());
        backEntity.setGender(frontEntity.getGender() != null ? GenderTypeEnum.valueOf(frontEntity.getGender().name()) : null);
        backEntity.setMaxUpperPressure(frontEntity.getMaxUpperPressure());
        backEntity.setMinUpperPressure(frontEntity.getMinUpperPressure());
        backEntity.setMaxLowerPressure(frontEntity.getMaxLowerPressure());
        backEntity.setMinLowerPressure(frontEntity.getMinLowerPressure());
        backEntity.setGuaranteeLevel(frontEntity.getGuaranteeLevel());
        backEntity.setPaymentTerm(frontEntity.getPaymentTerm());
        backEntity.setDeleted(false);


        if (!CollectionUtils.isEmpty(frontEntity.getRequiredFields())) {
            backEntity.setRequiredFieldList(frontEntity.getRequiredFields().stream()
                    .map(m -> requiredFieldService.findFirstByStrId(m.getStrId())).collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(frontEntity.getContractTemplate())) {
            backEntity.setDocumentTemplateList(frontEntity.getContractTemplate().stream()
                    .map(ContractTemplate::getId).collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(frontEntity.getStrategy())) {
            backEntity.setStrategyList(frontEntity.getStrategy().stream().map(m ->
                    strategyService.findById(m.getId())).collect(Collectors.toList()));
        }

        backEntity.setRequiredDocumentList(getRequiredDocumentList(getStoredDocListList(storedEntity), frontEntity.getDocuments()));
        backEntity.setRequiredRiskSettingList(getRiskList(getStoredRequiredRiskList(storedEntity), frontEntity.getRisks()));
        backEntity.setAdditionalRiskSettingList(getRiskList(getStoredAdditionalRiskList(storedEntity), frontEntity.getOptionalRisks()));

        backEntity.setSpecialRate(frontEntity.isSpecialRate());
        backEntity.setSpecialRateValue(frontEntity.getSpecialRateValue());
        backEntity.setIndividualRate(frontEntity.isIndividualRate());

        backEntity.setDeleted(false);
    }

    private List<RequiredDocumentSetting> getStoredDocListList(ProgramSetting storedEntity) {
        return storedEntity != null && storedEntity.getRequiredDocumentList() != null ? storedEntity.getRequiredDocumentList() : null;
    }

    private List<RiskSetting> getStoredRequiredRiskList(ProgramSetting storedEntity) {
        return storedEntity != null && storedEntity.getRequiredRiskSettingList() != null ? storedEntity.getRequiredRiskSettingList() : null;
    }

    private List<RiskSetting> getStoredAdditionalRiskList(ProgramSetting storedEntity) {
        return storedEntity != null && storedEntity.getAdditionalRiskSettingList() != null ? storedEntity.getAdditionalRiskSettingList() : null;
    }

    public Function<RequiredDocumentSetting, RequiredDocumentSetting> mapBackDocument(List<RiskDocument> frontDocList) {
        return doc -> {
            Optional<RiskDocument> frontElement = AppUtils.getFirst(frontDocList, getFrontDocByBackId(doc));
            if (frontElement.isPresent()) {
                doc.setStatus(frontElement.get().getState());
                return doc;
            }
            return null;
        };
    }

    private List<RequiredDocumentSetting> getRequiredDocumentList(List<RequiredDocumentSetting> backDocList, List<RiskDocument> frontDocList) {
        if (CollectionUtils.isEmpty(frontDocList)) {
            return new ArrayList<>();
        }
        List<RiskDocument> newFrontEntities = frontDocList;
        if (!CollectionUtils.isEmpty(backDocList)) {
            newFrontEntities = AppUtils.filter(frontDocList, isNewDocument(backDocList));
        }
        List<RequiredDocumentSetting> newBackEntities = AppUtils.apply(newFrontEntities, createBackDocuments());
        if (!CollectionUtils.isEmpty(backDocList)) {
            newBackEntities.addAll(AppUtils.apply(backDocList, mapBackDocument(frontDocList)));
        }
        return newBackEntities;
    }

    public Function<RiskSetting, RiskSetting> mapBackRisk(List<Risk> frontRiskList) {
        return backRisk -> {
            Optional<Risk> frontElement = AppUtils.getFirst(frontRiskList, getFrontRiskByBackId(backRisk));
            return frontElement.map(risk -> mapFrontRisk2BackRisk(backRisk, risk, RiskTypeEnum.REQUIRED)).orElse(null);
        };
    }

    public Function<RiskDocument, RequiredDocumentSetting> createBackDocuments() {
        return doc -> new RequiredDocumentSetting(0L,
                requiredDocumentService.findById(doc.getId()),
                doc.getState(), false);
    }

    public Function<Risk, RiskSetting> createBackRisks() {
        return backRisk -> mapFrontRisk2BackRisk(new RiskSetting(), backRisk, RiskTypeEnum.ADDITIONAL);
    }

    private List<RiskSetting> getRiskList(List<RiskSetting> backRiskList, List<Risk> frontRiskList) {
        if (CollectionUtils.isEmpty(frontRiskList)) {
            return new ArrayList<>();
        }
        List<Risk> newFrontEntities = frontRiskList;
        if (!CollectionUtils.isEmpty(backRiskList)) {
            newFrontEntities = AppUtils.filter(frontRiskList, isNewRisk(backRiskList));
        }
        List<RiskSetting> newBackEntities = AppUtils.apply(newFrontEntities, createBackRisks());
        if (!CollectionUtils.isEmpty(backRiskList)) {
            newBackEntities.addAll(AppUtils.apply(AppUtils.filter(backRiskList, filterDeleted(frontRiskList)), mapBackRisk(frontRiskList)));
        }
        return newBackEntities;
    }

    private RiskSetting mapFrontRisk2BackRisk(RiskSetting result, Risk frontRisk, RiskTypeEnum riskType) {
        Long id = result.getId();
        AppUtils.mapSimilarObjects(new RiskSetting(
                0L,
                riskType,
                riskService.findById(frontRisk.getId()),
                frontRisk.isSignAmount(), frontRisk.getMinRiskAmount()
                , frontRisk.getMaxRiskAmount(),
                frontRisk.getCalculationType() != null ? RiskCalculationTypeEnum.valueOf(frontRisk.getCalculationType().name()) : null,
                frontRisk.getRiskAmount(),
                /*m.getRiskDependence() != null ? riskSettingService.findById(m.getRiskDependence()) : */
                null,
                frontRisk.getCalculationCoefficient(),
                frontRisk.getRiskPremium(),
                frontRisk.getCalculationCoefficientPremium(),
                null,
                frontRisk.getRiskReturnRate(),
                frontRisk.getOtherRiskParam(),
                frontRisk.getInsuranceRule(),
                frontRisk.getRulesDetails(),
                null, frontRisk.getRecordAmount() != null ? RiskRecordAmountTypeEnum.valueOf(frontRisk.getRecordAmount().name()) : null,
                frontRisk.getType() != null ? RiskCalculationSumTypeEnum.valueOf(frontRisk.getType().name()) : null,
                frontRisk.getInsuranceKind(),
                false, frontRisk.getSortPriority()), result);
        result.setId(id);
        return result;
    }

    public ProgramSetting convertDetail(ProgramSettingData frontEntity) {
        ProgramSetting programSetting = new ProgramSetting();
        convertDetail(frontEntity, programSetting);
        return programSetting;
    }

    public List<ContractTemplate> getDocumentTemplateById(List<String> documentTemplateIds) {
        List<ContractTemplate> printTemplates = new ArrayList<>();
        String errorText = "Во время запроса шаблона документа c ID='%s' из сервиса common-dict произошла ошибка, причина: %s";
        documentTemplateIds.forEach(f -> {
            try {
                ru.softlab.efr.common.dict.exchange.model.PrintTemplate printTemplate = printTemplatesClient.get(f).get(GET_TEMPLATES_TIMEOUT);
                if (printTemplate != null && AppUtils.isNotNullOrWhitespace(printTemplate.getFileName())) {
                    printTemplates.add(new ContractTemplate(printTemplate.getType(), printTemplate.getFileName()));
                } else {
                    ContractTemplate emptyPrintTemplate = new ContractTemplate();
                    String emptyResult = "Получен пустой шаблон документа";
                    emptyPrintTemplate.setId("ERROR");
                    emptyPrintTemplate.setName(emptyResult);
                    LOGGER.error(String.format(errorText, f, emptyResult));
                    printTemplates.add(emptyPrintTemplate);
                }
            } catch (RestClientException e) {
                ContractTemplate emptyPrintTemplate = new ContractTemplate();
                emptyPrintTemplate.setId(f);
                emptyPrintTemplate.setName(String.format("Произошла ошибка при получении шаблона документа %s", f));
                printTemplates.add(emptyPrintTemplate);
                LOGGER.error(String.format(errorText, f, e.getMessage()), e);
            }
        });
        return printTemplates;
    }

    public String getCurrencyById(Long currencyId) {
        String result = "Нет информации";
        try {
            Currency currency = currenciesClient.getCurrency(currencyId).get(GET_CURRENCIES_TIMEOUT);
            if (currency != null && currency.getLiteralISO() != null) {
                result = currency.getLiteralISO();
            }
        } catch (RestClientException e) {
            result = "Произошла ошибка при получении валюты";
            LOGGER.error(String.format("Во время запроса валюты c ID='%s' из сервиса common-dict произошла ошибка, причина: %s", currencyId, e), e);
        }
        return result;
    }
}
