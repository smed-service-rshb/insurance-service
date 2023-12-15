package ru.softlab.efr.services.insurance.converter;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.common.client.CurrencyRateClient;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.dict.exchange.model.ShortCurrencyRateData;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.StrategyType;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.services.models.InsuranceCalculationInfo;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class InsuranceConverter {

    private static final Logger LOGGER = Logger.getLogger(InsuranceConverter.class);

    public static final BigDecimal PERCENTAGE = BigDecimal.valueOf(100);
    public static final int SCALE_2 = 2;

    private static final String RUB_ISO_CODE = "RUB";
    private final ProgramSettingService programSettingService;
    private final StrategyService strategyService;
    private final ClientService clientService;
    private final StatusService statusService;
    private final StatusHistoryService statusHistoryService;
    private final CalculationService calculationService;
    private final CurrencyCachedClientService currencyCachedClientService;
    private final CurrencyRateClient currencyRateClient;
    @Value("${insurance.converter.client.notfound}")
    private String CLIENT_NOT_EXIST_WARN;
    @Value("${insurance.converter.insured}")
    private String INSURED_NAME;
    @Value("${insurance.converter.holder}")
    private String HOLDER_NAME;
    @Value("${program.settings.strategy.notfound}")
    private String STRATEGY_NOT_FOUND_WARN;
    @Value("${program.settings.notfound}")
    private String PROGRAM_SETTING_NOT_FOUND_WARN;
    @Value("${currency.not.found}")
    private String CURRENCY_NOT_FOUND_WARN;
    @Value("${currency.rate.not.found}")
    private String CURRENCY_RATE_NOT_FOUND_WARN;
    @Value("${insurance.converter.static.date.notvalid}")
    private String STATIC_DATE_LESS_CURRENT_DATE;
    private InsuranceRepository insuranceRepository;

    @Autowired
    public InsuranceConverter(ProgramSettingService programSettingService,
                              StrategyService strategyService,
                              ClientService clientService,
                              StatusService statusService,
                              CalculationService calculationService,
                              CurrencyCachedClientService currencyCachedClientService,
                              CurrencyRateClient currencyRateClient,
                              InsuranceRepository insuranceRepository,
                              StatusHistoryService statusHistoryService) {
        this.programSettingService = programSettingService;
        this.strategyService = strategyService;
        this.clientService = clientService;
        this.statusService = statusService;
        this.calculationService = calculationService;
        this.currencyCachedClientService = currencyCachedClientService;
        this.currencyRateClient = currencyRateClient;
        this.insuranceRepository = insuranceRepository;
        this.statusHistoryService = statusHistoryService;
    }

    public void updateInsuranceFromBaseModel(BaseInsuranceModel model,
                                             Insurance insurance,
                                             String employeeName, Long subdevisionId, String subdivisionName, String branchName) throws ValidationException {
        List<String> errorMessages = new ArrayList<>();

        insurance.setEmployeeId(model.getEmployeeId());
        insurance.setEmployeeName(employeeName);

        if (Objects.nonNull(subdevisionId)) {
            insurance.setSubdivisionId(subdevisionId);
        }

        insurance.setSubdivisionName(subdivisionName);
        insurance.setBranchName(branchName);

        if (model.getHolderId() == null) {
            insurance.setHolder(mapFrontClient2Back(model.getHolderData(), new ClientEntity()));
        } else {
            ClientEntity holderEntity = clientService.get(model.getHolderId());
            if (holderEntity == null) {
                String msg = String.format(CLIENT_NOT_EXIST_WARN, HOLDER_NAME, model.getHolderId());
                errorMessages.add(msg);
            } else {
                if (model.getHolderData() == null) {
                    insurance.setHolder(holderEntity);
                } else {
                    insurance.setHolder(mapFrontClient2Back(model.getHolderData(), holderEntity));
                }
            }
        }

        if (model.getHolderId() != null && model.getHolderId().equals(model.getInsuredId())) {
            model.setHolderEqualsInsured(true);
        }

        insurance.setHolderEqualsInsured(Boolean.TRUE.equals(model.isHolderEqualsInsured()));

        if (!Boolean.TRUE.equals(insurance.getHolderEqualsInsured())) {
            if (model.getInsuredId() == null) {
                insurance.setInsured(mapFrontClient2Back(model.getInsuredData(), new ClientEntity()));
            } else {
                ClientEntity insuredEntity = clientService.get(model.getInsuredId());
                if (insuredEntity == null) {
                    String msg = String.format(CLIENT_NOT_EXIST_WARN, INSURED_NAME, model.getInsuredId());
                    errorMessages.add(msg);
                } else {
                    if (model.getInsuredData() == null) {
                        insurance.setInsured(insuredEntity);
                    } else {
                        insurance.setInsured(mapFrontClient2Back(model.getInsuredData(), insuredEntity));
                    }
                }
            }
        } else {
            insurance.setInsured(null);
        }

        if (model.getPeriodicity() != null) {
            insurance.setPeriodicity(PeriodicityEnum.valueOf(model.getPeriodicity().name()));
        } else {
            insurance.setPeriodicity(null);
        }
        insurance.setGrowth(model.getGrowth());
        insurance.setWeight(model.getWeight());
        insurance.setUpperPressure(model.getUpperPressure());
        insurance.setLowerPressure(model.getLowerPressure());
        insurance.setGuaranteeLevel(model.getGuaranteeLevel());
        insurance.setParticipationRate(model.getParticipationRate());
        insurance.setRecipientEqualsHolder(model.isRecipientEqualsHolder() != null ? model.isRecipientEqualsHolder() : true);
        insurance.setSource(SourceEnum.OFFICE);
        insurance.setPaymentTerm(model.getPaymentTerm());
        insurance.setFullSetDocument(model.isFullSetDocument());
        if (model.getCalendarUnit() != null) {
            insurance.setCalendarUnit(CalendarUnitEnum.valueOf(model.getCalendarUnit().name()));
        } else {
            insurance.setCalendarUnit(CalendarUnitEnum.valueOf(CalendarUnit.MONTH.name()));
        }
        /*if (insurance.getStatus() == null) {
            insurance.setStatus(statusService.getByCode(InsuranceStatusCode.DRAFT));
        }*/

        Long strategyId = model.getStrategyId();
        if (strategyId != null) {
            try {
                Strategy strategy = strategyService.findById(strategyId);
                insurance.setStrategy(strategy);
            } catch (EntityNotFoundException ex) {
                String message = String.format(STRATEGY_NOT_FOUND_WARN, strategyId);
                LOGGER.warn(message);
                errorMessages.add(message);
            }
        } else {
            insurance.setStrategy(null);
        }

        Long programSettingId = model.getProgramSettingId();
        ProgramSetting programSetting = null;
        try {
            programSetting = programSettingService.findById(programSettingId);
            if (Objects.nonNull(programSetting.getProgram().getComulation())) {
                if (model.getRurAmount().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal cumulationSettings = BigDecimal.valueOf(programSetting.getProgram().getComulation());
                    BigDecimal cumulations = insuranceRepository.getCumulationSum(insurance.getHolder().getId(), programSetting.getProgram().getId());
                    if (Objects.isNull(cumulations)) {
                        cumulations = BigDecimal.ZERO;
                    }
                    if (cumulationSettings.compareTo(cumulations.add(model.getRurAmount())) <= 0) {
                        String message = String.format("Страховая сумма по всем действующим договорам клиента с учетом текущего превышает заданный порог %s RUB. " +
                                        "Доступная для оформления сумма: %s RUB. Оформление невозможно.",
                                cumulationSettings, cumulationSettings.subtract(cumulations));
                        LOGGER.debug(message);
                        errorMessages.add(message);
                    }
                }
            }
            insurance.setProgramSetting(programSetting);
        } catch (EntityNotFoundException ex) {
            String message = String.format(PROGRAM_SETTING_NOT_FOUND_WARN, programSettingId);
            LOGGER.warn(message);
            errorMessages.add(message);
        }

        Long currencyId = model.getCurrencyId();
        Currency currency = null;
        try {
            currency = currencyCachedClientService.getById(model.getCurrencyId());
        } catch (RestClientException e) {
            String message = String.format(CURRENCY_NOT_FOUND_WARN, currencyId);
            LOGGER.warn(message);
            errorMessages.add(message);
        }
        BigDecimal exchangeRate = null;
        boolean isRubCurrency = currency != null && RUB_ISO_CODE.equals(currency.getLiteralISO());
        if (!(Boolean.TRUE.equals(model.isIndividualRate()) && Boolean.TRUE.equals(programSetting.getIndividualRate()))
                && (insurance.getStatus() == null
                || insurance.getStatus().getCode() == InsuranceStatusCode.DRAFT
                || insurance.getStatus().getCode() == InsuranceStatusCode.PROJECT)) {
            if (!isRubCurrency && currency != null) {
                try {
                    ShortCurrencyRateData active = currencyRateClient.getActiveCurrencyRate(currency.getId(), 10);
                    if (Boolean.TRUE.equals(programSetting.getSpecialRate())) {
                        exchangeRate = active.getRate().add(active.getRate()
                                .multiply(programSetting.getSpecialRateValue().divide(BigDecimal.valueOf(100))));
                    } else {
                        exchangeRate = active.getInnerRate();
                    }
                } catch (RestClientException e) {
                    String message = String.format(CURRENCY_RATE_NOT_FOUND_WARN, currency.getLiteralISO());
                    LOGGER.warn(message);
                    errorMessages.add(message);
                }
            }
            insurance.setIndividualRate(false);
        } else if (insurance.getId() == null && insurance.getParent() == null) {
            exchangeRate = model.getExchangeRate();
        } else {
            exchangeRate = insurance.getExchangeRate();
        }
        insurance.setExchangeRate(exchangeRate);
        if (programSetting != null) {
            Integer coolingPeriod = programSetting.getProgram().getCoolingPeriod();
            Timestamp staticDate = programSetting.getStaticDate();
            if (staticDate != null && programSetting.getStaticDate() != null) {
                if (programSetting.getStaticDate().compareTo(new Date()) >= 0) {
                    insurance.setConclusionDate(model.getConclusionDate());
                    insurance.setStartDate(staticDate.toLocalDateTime().toLocalDate());
                } else {
                    String msg = STATIC_DATE_LESS_CURRENT_DATE;
                    errorMessages.add(msg);
                }
            }
            //Эта ветка, возможно, при текущей статусной модели и бизнес процессе не нужна. Она перезаписсывает даты, если они уже есть.
            //Нет требования обновлять даты при редактировании данных
            /*else {
                if (insurance.getParent() != null && model.getConclusionDate() != null) {
                    insurance.setConclusionDate(model.getConclusionDate());
                    insurance.setStartDate(model.getConclusionDate().plusDays(coolingPeriod + 1));
                } else if (Objects.nonNull(insurance.getFixedStartDate())) {
                    insurance.setStartDate(insurance.getFixedStartDate());
                } else if (Objects.nonNull(insurance.getStartDate())) {
                    insurance.setStartDate(LocalDate.now().plusDays(coolingPeriod + 1));
                }
            }*/

            insurance.setDuration(model.getDuration());
            insurance.setEndDate(calculateEndDate(insurance));

            InsuranceCalculationInfo insuranceCalculationInfo = buildInsuranceCalculationInfo(model, programSetting);
            insuranceCalculationInfo = buildInsuranceCalculationDiscount(insurance, programSetting, insuranceCalculationInfo);

            BigDecimal totalPremium = insuranceCalculationInfo.getTotalPremium();
            int periodCount = calculationService.periodCount(CalendarUnitEnum.valueOf(model.getCalendarUnit().name()),
                    model.getDuration(), programSetting.getPeriodicity());

            insurance.setCurrency(model.getCurrencyId());
            insurance.setDuration(model.getDuration());
            if (!isRubCurrency && exchangeRate != null) {
                insurance.setRurAmount(insurance.getAmount().multiply(exchangeRate));
                insurance.setRurPremium(insurance.getPremium().multiply(exchangeRate));
            } else {
                insurance.setRurAmount(insurance.getAmount());
                insurance.setRurPremium(insurance.getPremium());
            }
            final BigDecimal rate = exchangeRate;
            //Обновляем список обязательных рисков
            if (!CollectionUtils.isEmpty(programSetting.getRequiredRiskSettingList())) {
                List<RiskSetting> requiredRiskList = programSetting.getRequiredRiskSettingList();
                List<InsuranceRiskEntity> riskInfoList = requiredRiskList.stream().map(riskSetting -> {
                    InsuranceRiskEntity existingEntityRisk = insurance.getRiskInfoList()
                            .stream()
                            .filter(entityRisk -> entityRisk.getRisk().getId().equals(riskSetting.getRisk().getId()))
                            .findFirst()
                            .orElse(null);
                    if (existingEntityRisk != null) {
                        existingEntityRisk.setAmount(calculationService.getInsuranceAmountByRiskSetting(riskSetting, insurance.getAmount(), totalPremium, periodCount));
                        existingEntityRisk.setPremium(calculationService.getInsurancePremiumByRiskSetting(riskSetting));
                        existingEntityRisk.setOtherRiskParam(riskSetting.getOtherRiskParam());
                        setRurAmountToRisk(isRubCurrency, rate, existingEntityRisk);
                        return existingEntityRisk;
                    } else {
                        InsuranceRiskEntity riskEntity = new InsuranceRiskEntity();
                        riskEntity.setInsurance(insurance);
                        riskEntity.setRisk(riskSetting.getRisk());
                        riskEntity.setAmount(calculationService.getInsuranceAmountByRiskSetting(riskSetting, insurance.getAmount(), totalPremium, periodCount));
                        riskEntity.setPremium(calculationService.getInsurancePremiumByRiskSetting(riskSetting));
                        setRurAmountToRisk(isRubCurrency, rate, riskEntity);
                        riskEntity.setOtherRiskParam(riskSetting.getOtherRiskParam());
                        return riskEntity;
                    }
                }).collect(Collectors.toList());
                insurance.getRiskInfoList().clear();
                insurance.getRiskInfoList().addAll(riskInfoList);
            }

            //Обновляем список дополнительных рисков
            if (!CollectionUtils.isEmpty(programSetting.getAdditionalRiskSettingList())) {
                List<RiskSetting> requiredRiskList = programSetting.getAdditionalRiskSettingList();
                List<InsuranceAddRiskEntity> addRiskInfoList = requiredRiskList.stream().map(riskSetting -> {
                    InsuranceAddRiskEntity existingEntityRisk = insurance.getAddRiskInfoList()
                            .stream()
                            .filter(entityRisk -> entityRisk.getRisk().getId().equals(riskSetting.getRisk().getId()))
                            .findFirst()
                            .orElse(null);
                    if (existingEntityRisk != null) {
                        existingEntityRisk.setAmount(calculationService.getInsuranceAmountByRiskSetting(riskSetting, insurance.getAmount(), totalPremium, periodCount));
                        existingEntityRisk.setPremium(calculationService.getInsurancePremiumByRiskSetting(riskSetting));
                        return existingEntityRisk;
                    } else {
                        InsuranceAddRiskEntity riskEntity = new InsuranceAddRiskEntity();
                        riskEntity.setInsurance(insurance);
                        riskEntity.setRisk(riskSetting.getRisk());
                        riskEntity.setAmount(calculationService.getInsuranceAmountByRiskSetting(riskSetting, insurance.getAmount(), totalPremium, periodCount));
                        riskEntity.setPremium(calculationService.getInsurancePremiumByRiskSetting(riskSetting));
                        return riskEntity;
                    }
                }).collect(Collectors.toList());
                insurance.getAddRiskInfoList().clear();
                insurance.getAddRiskInfoList().addAll(addRiskInfoList);
            }
        }

        //Обновляем список выгодоприобретателей
        updateRecipientList(model.getRecipientList(), insurance);

        //Сохранение выбранного типа расчета
        if (model.getType() != null) {
            insurance.setCalcBySum(model.getType() == FindProgramType.SUM);
        }

        if (model.getUuid() != null) {
            insurance.setUuid(model.getUuid());
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    private InsuranceCalculationInfo buildInsuranceCalculationInfo(BaseInsuranceModel model, ProgramSetting programSetting) {
        BigDecimal insuranceAmount;
        BigDecimal premiumPerPeriod;
        BigDecimal totalPremium;
        int periodCount = calculationService.periodCount(CalendarUnitEnum.valueOf(model.getCalendarUnit().name()),
                model.getDuration(), programSetting.getPeriodicity());

        if (FindProgramType.SUM.equals(model.getType())) {
            insuranceAmount = model.getAmount();
            totalPremium = calculationService.getInsurancePremiumByProgramSetting(programSetting, insuranceAmount);
            premiumPerPeriod = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                    programSetting, insuranceAmount, CalendarUnitEnum.valueOf(model.getCalendarUnit().name()), model.getDuration());
        } else {
            premiumPerPeriod = model.getPremium();
            insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                    programSetting, premiumPerPeriod, CalendarUnitEnum.valueOf(model.getCalendarUnit().name()), model.getDuration());
            totalPremium = premiumPerPeriod.multiply(BigDecimal.valueOf(periodCount));
        }

        return new InsuranceCalculationInfo(insuranceAmount, premiumPerPeriod, totalPremium);
    }

    private InsuranceCalculationInfo buildInsuranceCalculationDiscount(Insurance insurance, ProgramSetting programSetting, InsuranceCalculationInfo insuranceCalculationInfo) {
        insurance.setPremiumWithoutDiscount(null);
        insurance.setDiscount(null);

        boolean calcBySum = isTrue(insurance.getCalcBySum());
        BigDecimal discount = programSetting.getDiscount();

        if (discount == null
                || discount.compareTo(BigDecimal.ZERO) == 0
                || insurance.getHolder() == null) {
            return insuranceCalculationInfo;
        }

        boolean existsContract = insuranceRepository.existsByClient(
                insurance.getHolder().getId(),
                InsuranceStatusCode.PAYED);

        if (!existsContract) {
            return insuranceCalculationInfo;
        }

        insurance.setDiscount(discount);
        if (calcBySum) {
            BigDecimal insuranceAmount = insuranceCalculationInfo.getInsuranceAmount() != null
                    ? calcAmountWithDiscount(insuranceCalculationInfo.getInsuranceAmount(), discount)
                    : insuranceCalculationInfo.getInsuranceAmount();

            insurance.setAmount(insuranceAmount);
            insurance.setPremium(insuranceCalculationInfo.getPremiumPerPeriod());
            insurance.setPremiumWithoutDiscount(insuranceCalculationInfo.getInsuranceAmount());
            return new InsuranceCalculationInfo(
                    insuranceAmount,
                    insuranceCalculationInfo.getPremiumPerPeriod(),
                    insuranceCalculationInfo.getTotalPremium());
        } else {
            BigDecimal premiumPerPeriod = insuranceCalculationInfo.getPremiumPerPeriod() != null
                    ? calcAmountWithDiscount(insuranceCalculationInfo.getPremiumPerPeriod(), discount)
                    : insuranceCalculationInfo.getPremiumPerPeriod();
            BigDecimal totalPremium = insuranceCalculationInfo.getTotalPremium() != null
                    ? calcAmountWithDiscount(insuranceCalculationInfo.getTotalPremium(), discount)
                    : insuranceCalculationInfo.getTotalPremium();

            insurance.setAmount(insuranceCalculationInfo.getInsuranceAmount());
            insurance.setPremium(premiumPerPeriod);
            insurance.setPremiumWithoutDiscount(insuranceCalculationInfo.getPremiumPerPeriod());
            return new InsuranceCalculationInfo(
                    insuranceCalculationInfo.getInsuranceAmount(),
                    premiumPerPeriod,
                    totalPremium);
        }
    }

    private static BigDecimal calcAmountWithDiscount(BigDecimal value, BigDecimal discount) {
        return value.subtract(value
                .multiply(discount)
                .divide(PERCENTAGE, SCALE_2, RoundingMode.DOWN));
    }

    private void setRurAmountToRisk(boolean isRubCurrency, BigDecimal rate, InsuranceRiskEntity riskEntity) {
        if (!isRubCurrency && rate != null) {
            if (riskEntity.getAmount() != null) {
                riskEntity.setRurAmount(riskEntity.getAmount().multiply(rate));
            }
            if (riskEntity.getPremium() != null) {
                riskEntity.setRurPremium(riskEntity.getPremium().multiply(rate));
            }
        } else {
            riskEntity.setRurPremium(riskEntity.getPremium());
            riskEntity.setRurAmount(riskEntity.getAmount());
        }
    }

    private ClientEntity mapFrontClient2Back(Client clientFront, ClientEntity clientBack) {
        if (clientFront == null || clientFront.getFirstName() == null || clientFront.getFirstName().length() == 0) {
            return null;
        }
        clientBack.update(clientFront);
        return clientBack;
    }

    /**
     * Конвертация объекта договора страхования в модель представления
     *
     * @param insurance объект договора страхования
     */
    public BaseInsuranceModel convertInsuranceToBaseModel(Insurance insurance) throws RestClientException {
        BaseInsuranceModel model = new BaseInsuranceModel();
        model.setStartDate(insurance.getStartDate());
        Long holderId = insurance.getHolder() != null ? insurance.getHolder().getId() : null;
        model.setHolderId(holderId);
        Long insuredId = insurance.getInsured() != null ? insurance.getInsured().getId() : null;
        model.setInsuredId(insuredId);
        model.setHolderEqualsInsured(insurance.getHolderEqualsInsured());
        model.setAmount(insurance.getAmount());
        model.setRurAmount(insurance.getRurAmount());
        model.setCurrencyId(insurance.getCurrency());
        Currency currency = currencyCachedClientService.getById(insurance.getCurrency());
        if (currency != null) {
            model.setCurrencyCode(currency.getLiteralISO());
        }

        model.setDuration(insurance.getDuration());
        model.setPremium(insurance.getPremium());
        model.setPremiumWithoutDiscount(insurance.getPremiumWithoutDiscount());
        model.setDiscount(insurance.getDiscount());
        model.setRurPremium(insurance.getRurPremium());
        if (insurance.getPeriodicity() != null) {
            model.setPeriodicity(PaymentPeriodicity.valueOf(insurance.getPeriodicity().name()));
        }
        model.setGrowth(insurance.getGrowth());
        model.setWeight(insurance.getWeight());
        model.setUpperPressure(insurance.getUpperPressure());
        model.setLowerPressure(insurance.getLowerPressure());
        model.setGuaranteeLevel(insurance.getGuaranteeLevel());
        model.setParticipationRate(insurance.getParticipationRate());
        model.setRecipientEqualsHolder(insurance.getRecipientEqualsHolder() != null ? insurance.getRecipientEqualsHolder() : true);
        if (insurance.getStrategy() != null) {
            model.setStrategyId(insurance.getStrategy().getId());
        } else {
            model.setStrategyId(null);
        }
        if (insurance.getProgramSetting() != null) {
            model.setProgramSettingId(insurance.getProgramSetting().getId());
        } else {
            model.setProgramSettingId(null);
        }
        // Конвертация обязательных рисков
        if (!CollectionUtils.isEmpty(insurance.getRiskInfoList())) {
            model.setRiskInfoList(insurance.getRiskInfoList().stream().map(InsuranceRiskEntity::convertToModel).collect(Collectors.toList()));
        } else {
            model.setRiskInfoList(new ArrayList<>());
        }
        //Конвертация списока дополнительных рисков
        if (!CollectionUtils.isEmpty(insurance.getAddRiskInfoList())) {
            model.setAddRiskInfoList(insurance.getAddRiskInfoList().stream().map(InsuranceAddRiskEntity::convertToModel).collect(Collectors.toList()));
        } else {
            model.setAddRiskInfoList(new ArrayList<>());
        }
        //Обновляем список выгодоприобретателей
        if (!CollectionUtils.isEmpty(insurance.getRecipientList())) {
            model.setRecipientList(insurance.getRecipientList().stream().map(InsuranceRecipientEntity::convertToModel).collect(Collectors.toList()));
        } else {
            model.setRecipientList(new ArrayList<>());
        }
        model.setHolderData(mapBackClient2FrontClient(holderId));
        model.setInsuredData(mapBackClient2FrontClient(insuredId));
        if (insurance.getCalcBySum() != null && insurance.getCalcBySum()) {
            model.setType(FindProgramType.SUM);
        } else {
            model.setType(FindProgramType.PREMIUM);
        }
        return model;
    }

    private Client mapBackClient2FrontClient(Long clientId) {
        if (clientId == null) {
            return new Client();
        }
        ClientEntity clientBack = clientService.get(clientId);
        return ClientEntity.toClient(clientBack);
    }

    public ListInsuranceModel convertInsuranceToListModel(InsuranceSummary insurance) {
        ListInsuranceModel model = new ListInsuranceModel();
        model.setBranchId(insurance.getBranchId());
        model.setBranchName(insurance.getBranchName());
        model.setSubdivisionId(insurance.getSubdivisionId());
        model.setSubdivisionName(insurance.getSubdivisionName());
        model.setStartDate(insurance.getStartDate());
        model.setEndDate(calculateEndDate(insurance.getPaymentTerm(), insurance.getDuration(), insurance.getStartDate(), insurance.getCreationDate().toLocalDate(), insurance.getCalendarUnit()));
        model.setConclusionDate(insurance.getConclusionDate()); // Дата оформления договора
        model.setCloseDate(insurance.getCloseDate());
        model.setCreationDate(insurance.getCreationDate().toLocalDate());
        if (InsuranceStatusCode.REVOKED_REPLACEMENT == insurance.getStatus()) {
            model.setContractNumber(insurance.getInitialContractNumber());
        } else {
            model.setContractNumber(insurance.getContractNumber());
        }
        model.setDuration(insurance.getDuration());
        model.setEmployeeName(insurance.getEmployeeName());
        model.setId(insurance.getId());
        model.setKind(ru.softlab.efr.services.insurance.model.rest.ProgramKind.valueOf(insurance.getType().name()));
        model.setPremium(insurance.getPremium());
        model.setRurPremium(insurance.getRurPremium());
        Currency currency = getCurrencyById(insurance.getCurrency());
        if (currency != null) {
            model.setCurrency(currency.getLiteralISO());
        }

        if (insurance.getStatus() != null) {
            model.setStatus(InsuranceStatusType.valueOf(insurance.getStatus().name()));
        }
        //Данные клиента
        Gender genderModel = (insurance.getHolderGender() != null) ? Gender.valueOf(insurance.getHolderGender().name().toUpperCase()) : null;
        ShortClientData clientData = new ShortClientData(
                insurance.getHolderId(),
                insurance.getHolderSurName(),
                insurance.getHolderFirstName(),
                insurance.getHolderMiddleName(),
                insurance.getHolderBirthDate(),
                genderModel,
                insurance.getHolderDocType() != null ? DocumentType.valueOf(insurance.getHolderDocType().name()) : null,
                insurance.getHolderDocSeria(),
                insurance.getHolderDocNumber(),
                insurance.getHolderPhoneNumber(),
                insurance.getHolderEmail());
        model.setClientData(clientData);
        model.setProgramName(insurance.getProgramName());
        model.setEmployeeId(insurance.getEmployeeId());
        model.setEmployeeName(insurance.getEmployeeName());
        return model;
    }

    public ListInsuranceModel convertInsuranceToListModel(Insurance insurance) {
        ListInsuranceModel model = new ListInsuranceModel();
        model.setBranchId(insurance.getBranchId());
        model.setBranchName(insurance.getBranchName());
        model.setSubdivisionId(insurance.getSubdivisionId());
        model.setSubdivisionName(insurance.getSubdivisionName());
        model.setStartDate(insurance.getStartDate());
        model.setEndDate(calculateEndDate(insurance.getPaymentTerm(), insurance.getDuration(), insurance.getStartDate(), insurance.getCreationDate().toLocalDate(), insurance.getCalendarUnit()));
        model.setCloseDate(insurance.getCloseDate());
        //дата заключения договора
        model.setCreationDate(insurance.getConclusionDate());
        model.setContractNumber(insurance.getContractNumber());
        model.setDuration(insurance.getDuration());
        model.setEmployeeName(insurance.getEmployeeName());
        model.setId(insurance.getId());
        model.setStatus(InsuranceStatusType.valueOf(insurance.getStatus().getCode().name()));
        if (insurance.getProgramSetting().getProgram() != null) {
            model.setKind(ProgramKind.valueOf(insurance.getProgramSetting().getProgram().getType().name()));
            String strategy = insurance.getStrategy() != null ? " " + insurance.getStrategy().getName() : "";
            String programName = insurance.getProgramSetting().getProgram().getNameForPrint();
            model.setProgramName(programName != null ? programName + " " + strategy : strategy);
        }
        model.setPremium(insurance.getPremium());
        model.setRurPremium(insurance.getRurPremium());
        Currency currency = getCurrencyById(insurance.getCurrency());
        if (currency != null) {
            model.setCurrency(currency.getLiteralISO());
        }

        ClientEntity holder = insurance.getHolder();

        //Данные клиента
        ShortClientData clientData = new ShortClientData();
        clientData.setGender(holder.getGender() != null ? Gender.valueOf(holder.getGender().name().toUpperCase()) : null);

        if (!CollectionUtils.isEmpty(holder.getPhones())) {
            String holderPhone = holder.getPhones().get(0).getNumber();
            for (PhoneForClaimEntity phone : holder.getPhones()) {
                if (phone.isMain()) {
                    holderPhone = phone.getNumber();
                }
            }
            clientData.setPhoneNumber(holderPhone);
        }

        clientData.setId(holder.getId());
        clientData.setSurName(holder.getSurName());
        clientData.setFirstName(holder.getFirstName());
        clientData.setMiddleName(holder.getMiddleName());
        clientData.setBirthDate(holder.getBirthDate());
        clientData.setEmail(holder.getEmail());

        DocumentForClientEntity document = holder.getMainDocument();
        if (document != null) {
            clientData.setDocNumber(document.getDocNumber());
            clientData.setDocSeries(document.getDocSeries());
            clientData.setDocType(document.getDocType() != null ? DocumentType.valueOf(document.getDocType().name()) : null);
        }

        model.setClientData(clientData);
        model.setEmployeeId(insurance.getEmployeeId());
        model.setEmployeeName(insurance.getEmployeeName());
        return model;
    }


    public ViewInsuranceModel convertInsuranceToConsumerViewModel(Insurance insurance) throws RestClientException {
        BaseInsuranceModel baseModel = convertInsuranceToBaseModel(insurance);
        ViewInsuranceModel viewModel = new ViewInsuranceModel();
        BeanUtils.copyProperties(baseModel, viewModel);
        viewModel.setId(insurance.getId());
        viewModel.setHolderEqualsInsured(insurance.getHolderEqualsInsured());
        viewModel.setRecipientEqualsHolder(insurance.getRecipientEqualsHolder());
        //вид программы страхования
        viewModel.setKind(ProgramKind.valueOf(insurance.getProgramSetting().getProgram().getType().name()));
        //Номер договора
        if (InsuranceStatusCode.REVOKED_REPLACEMENT == insurance.getStatus().getCode()) {
            viewModel.setContractNumber(insurance.getInitialContractNumber());
        } else {
            viewModel.setContractNumber(insurance.getContractNumber());
        }
        //Актуальный статус договора
        if (insurance.getStatus() != null) {
            viewModel.setStatus(InsuranceStatusType.valueOf(insurance.getStatus().getCode().name()));
        }
        //Наименовние программы страхования
        Strategy strategy = insurance.getStrategy();
        String strategyName = strategy != null ? strategy.getName() : "";
        String programName = insurance.getProgramSetting().getProgram().getNameForPrint();
        viewModel.setProgramName(programName != null ? programName + " " + strategyName : strategyName);
        viewModel.setStrategyName(strategyName);
        if (strategy != null && strategy.getStrategyProperties() != null) {
            viewModel.setStrategyProperties(insurance.getStrategy().getStrategyProperties().stream().
                    map(StrategyConverter::getStrategyProperty).collect(Collectors.toList()));
        }
        viewModel.setPaymentTerm(insurance.getPaymentTerm());
        //дата заключения договора
        viewModel.setCreationDate(insurance.getConclusionDate());
        //дата начала действия договора
        viewModel.setStartDate(insurance.getStartDate());
        //дата закрытия договора
        viewModel.setCloseDate(insurance.getCloseDate());
        //дата окончания действия договора
        viewModel.setEndDate(insurance.getEndDate());
        if (strategy != null) {
            viewModel.setStrategyId(insurance.getStrategy().getId());
            viewModel.setStrategyType(strategy.getStrategyType() != null ?
                    StrategyType.valueOf(strategy.getStrategyType().name()) : null);
        }

        return viewModel;
    }

    /**
     * Сформировать модель для просмотра по объекту страховок
     *
     * @param insurance объект БД
     * @return модель для презентации
     */
    public ViewInsuranceModel convertInsuranceToViewModel(Insurance insurance, PrincipalData principalData) throws RestClientException {
        BaseInsuranceModel baseModel = convertInsuranceToBaseModel(insurance);
        ViewInsuranceModel viewModel = new ViewInsuranceModel();
        BeanUtils.copyProperties(baseModel, viewModel);
        viewModel.setId(insurance.getId());
        if (insurance.getPeriodicity() != null) {
            viewModel.setPeriodicity(PaymentPeriodicity.valueOf(insurance.getPeriodicity().name()));
        }
        viewModel.setHolderEqualsInsured(insurance.getHolderEqualsInsured());
        viewModel.setRecipientEqualsHolder(insurance.getRecipientEqualsHolder() != null ? insurance.getRecipientEqualsHolder() : true);
        if (insurance.getCalcBySum() != null && insurance.getCalcBySum()) {
            viewModel.setType(FindProgramType.SUM);
        } else {
            viewModel.setType(FindProgramType.PREMIUM);
        }
        //Идентификатор сотрудника создавшего договор
        viewModel.setEmployeeId(insurance.getEmployeeId());
        //вид программы страхования
        viewModel.setKind(ProgramKind.valueOf(insurance.getProgramSetting().getProgram().getType().name()));
        //вариант программы страхования
        if (Objects.nonNull(insurance.getProgramSetting().getProgram().getVariant())) {
            viewModel.setOption(insurance.getProgramSetting().getProgram().getVariant());
        }
        //Номер договора
        if (Objects.nonNull(insurance.getStatus()) && InsuranceStatusCode.REVOKED_REPLACEMENT == insurance.getStatus().getCode()) {
            viewModel.setContractNumber(insurance.getInitialContractNumber());
        } else {
            viewModel.setContractNumber(insurance.getContractNumber());
        }
        //Актуальный статус договора
        if (insurance.getStatus() != null) {
            viewModel.setStatus(InsuranceStatusType.valueOf(insurance.getStatus().getCode().name()));
        }
        //Номер программы страхования
        viewModel.setProgramNumber(insurance.getProgramSetting().getProgram().getNumber());
        //Наименовние программы страхования
        viewModel.setProgramName(insurance.getProgramSetting().getProgram().getName());
        //Кодировка программы
        viewModel.setProgramCode(insurance.getProgramSetting().getProgram().getPolicyCode());
        //Вариант страхования
        CalendarUnitEnum calendarUnit = insurance.getCalendarUnit();
        //единица срока страхования
        if (calendarUnit != null) {
            viewModel.setCalendarUnit(CalendarUnit.valueOf(calendarUnit.name()));
        }
        //дата заключения договора
        viewModel.setCreationDate(insurance.getConclusionDate() == null ?
                insurance.getCreationDate().toLocalDate() : insurance.getConclusionDate());
        //дата начала действия договора
        viewModel.setStartDate(insurance.getStartDate());
        //дата закрытия договора
        viewModel.setCloseDate(insurance.getCloseDate());
        //дата окончания действия договора
        viewModel.setEndDate(insurance.getEndDate());
        //Срок выплаты в годах
        viewModel.setPaymentTerm(insurance.getPaymentTerm());
        //Признак получения полного комплекта документов
        viewModel.setFullSetDocument(Boolean.TRUE.equals(insurance.getFullSetDocument()));
        //Комментарий к признаку получения полного/неполного комплекта документов
        viewModel.setCommentForNotFullSetDocument(insurance.getCommentForNotFullSetDocument() != null ? insurance.getCommentForNotFullSetDocument() : "");
        viewModel.setIsCopy(insurance.getParent() != null);
        //Курс валюты договора к рублю РФ на дату оформления договора
        viewModel.setExchangeRate(insurance.getExchangeRate());

        viewModel.setProgramCode(insurance.getProgramSetting().getProgram().getProgramCode());
        viewModel.setOption(insurance.getProgramSetting().getProgram().getProgramTariff());
        viewModel.setIndividualRate(insurance.getIndividualRate());
        if (insurance.getStrategy() != null) {
            viewModel.setStrategyName(insurance.getStrategy().getName());
            viewModel.setStrategyProperties(insurance.getStrategy().getStrategyProperties().stream().
                    map(StrategyConverter::getStrategyProperty).collect(Collectors.toList()));
        }
        Set<InsuranceStatusCode> availableStatuses = statusService.getAvailableStatuses(insurance, principalData);

        InsuranceStatusHistory lastButOneStatus = statusHistoryService.getLastButOneAssigned(insurance);
        if (lastButOneStatus != null) {
            viewModel.setPreviousStatus(lastButOneStatus.getStatus().getCode().name());
        }
        if (!CollectionUtils.isEmpty(availableStatuses)) {
            List<InsuranceStatusType> statusTypes = availableStatuses.stream().filter(Objects::nonNull)
                    .map(s -> InsuranceStatusType.valueOf(s.name())).collect(Collectors.toList());
            viewModel.setAvailableStatuses(statusTypes);
        }
        return viewModel;
    }

    /**
     * Расчет даты окончания действия договора.
     *
     * @param insurance Договор страхования.
     * @return Дата окончания действия договора страхования.
     */
    private LocalDate calculateEndDate(Insurance insurance) {
        return calculateEndDate(insurance.getPaymentTerm(), insurance.getDuration(),
                insurance.getStartDate(), insurance.getCreationDate().toLocalDate(), insurance.getCalendarUnit());
    }

    private LocalDate calculateEndDate(Integer paymentTerm, Integer duration, LocalDate startDate, LocalDate createDate,
                                       CalendarUnitEnum calendarUnit) {
        if (Objects.isNull(startDate)) {
            startDate = createDate;
        }
        if (paymentTerm != null) {
            startDate = startDate.plusYears(paymentTerm);
        }
        startDate = startDate.plusDays(-1);
        if (duration == null)
            return startDate;
        switch (calendarUnit) {
            case DAY:
                return startDate.plusDays(duration);
            case MONTH:
                return startDate.plusMonths(duration);
            case YEAR:
                return startDate.plusYears(duration);
            default:
                return null;
        }
    }

    //Обновляем список выгодоприобретателей
    private void updateRecipientList(List<InsuranceRecipient> recipientList, Insurance insurance) {
        if (!CollectionUtils.isEmpty(recipientList)) {
            List<InsuranceRecipientEntity> updatedList = recipientList
                    .stream()
                    .filter(model -> model.getFirstName() != null && model.getSurName() != null)
                    .map(model -> {
                        Optional<InsuranceRecipientEntity> orig = insurance.getRecipientList().stream()
                                .filter(recipient -> StringUtils.equalsIgnoreCase(recipient.getFirstName(), model.getFirstName()) &&
                                        StringUtils.equalsIgnoreCase(recipient.getMiddleName(), model.getMiddleName()) &&
                                        StringUtils.equals(recipient.getBirthDate(), model.getBirthDate())
                                ).findFirst();
                        TaxResidenceType taxResidenceModel = model.getTaxResidence();
                        TaxResidenceEnum taxResidence = taxResidenceModel != null ? TaxResidenceEnum.valueOf(taxResidenceModel.name().toUpperCase()) : null;
                        if (orig.isPresent()) {
                            InsuranceRecipientEntity recipient = orig.get();
                            recipient.update(model.getSurName(),
                                    model.getFirstName(), model.getMiddleName(), model.getBirthDate(),
                                    model.getBirthPlace(), taxResidence, model.getRelationship(), model.getShare(), model.getBirthCountry());
                            return recipient;
                        } else {
                            return new InsuranceRecipientEntity(insurance, model.getSurName(),
                                    model.getFirstName(), model.getMiddleName(), model.getBirthDate(),
                                    model.getBirthPlace(), taxResidence, model.getRelationship(), model.getShare(), model.getBirthCountry());
                        }
                    })
                    .collect(Collectors.toList());
            insurance.getRecipientList().clear();
            insurance.getRecipientList().addAll(updatedList);
        } else {
            insurance.getRecipientList().clear();
        }
    }

    /**
     * Конвертация одной модели в другую
     */
    public BaseInsuranceModel convertSetStatusInsuranceModelToBaseInsuranceModel(SetStatusInsuranceModel statusModel) {
        BaseInsuranceModel baseModel = new BaseInsuranceModel();
        BeanUtils.copyProperties(statusModel, baseModel);
        baseModel.setPeriodicity(statusModel.getPeriodicity());
        baseModel.setHolderEqualsInsured(statusModel.isHolderEqualsInsured());
        baseModel.setRecipientEqualsHolder(statusModel.isRecipientEqualsHolder());
        baseModel.setType(statusModel.getType());
        baseModel.setCalendarUnit(statusModel.getCalendarUnit());
        baseModel.setStartDate(statusModel.getStartDate());
        baseModel.setIndividualRate(statusModel.isIndividualRate());
        return baseModel;
    }

    private Currency getCurrencyById(Long currencyId) {
        Currency currency = null;
        try {
            currency = currencyCachedClientService.getById(currencyId);
        } catch (RestClientException e) {
            String message = String.format(CURRENCY_NOT_FOUND_WARN, currencyId);
            LOGGER.warn(message);
        }
        return currency;
    }

    public RedemptionSum convert(ReportableRedemption redemptions) {
        RedemptionSum redemptionSum = new RedemptionSum();
        BeanUtils.copyProperties(redemptions, redemptionSum);
        return redemptionSum;
    }
}
