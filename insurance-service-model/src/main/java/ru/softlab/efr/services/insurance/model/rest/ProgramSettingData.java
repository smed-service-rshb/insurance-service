package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.CalendarUnit;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplate;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.PremiumMethod;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RequiredField;
import ru.softlab.efr.services.insurance.model.rest.Risk;
import ru.softlab.efr.services.insurance.model.rest.RiskDocument;
import ru.softlab.efr.services.insurance.model.rest.Strategy;
import ru.softlab.efr.services.insurance.model.rest.UnderwritingKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Описание параметров программы страхования
 */
@ApiModel(description = "Описание параметров программы страхования")
@Validated
public class ProgramSettingData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("staticDate")
    private LocalDate staticDate = null;

    @JsonProperty("strategy")
    @Valid
    private List<Strategy> strategy = null;

    @JsonProperty("policyholderInsured")
    private Boolean policyholderInsured = null;

    @JsonProperty("minimumTerm")
    private Integer minimumTerm = null;

    @JsonProperty("maximumTerm")
    private Integer maximumTerm = null;

    @JsonProperty("paymentTerm")
    private Integer paymentTerm = null;

    @JsonProperty("calendarUnit")
    private CalendarUnit calendarUnit = null;

    @JsonProperty("currency")
    private Long currency = null;

    @JsonProperty("minSum")
    private BigDecimal minSum = null;

    @JsonProperty("maxSum")
    private BigDecimal maxSum = null;

    @JsonProperty("minPremium")
    private BigDecimal minPremium = null;

    @JsonProperty("maxPremium")
    private BigDecimal maxPremium = null;

    @JsonProperty("premiumMethod")
    private PremiumMethod premiumMethod = null;

    @JsonProperty("coefficient")
    private BigDecimal coefficient = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("sum")
    private BigDecimal sum = null;

    @JsonProperty("tariff")
    private BigDecimal tariff = null;

    @JsonProperty("periodicity")
    private PaymentPeriodicity periodicity = null;

    @JsonProperty("underwriting")
    private UnderwritingKind underwriting = null;

    @JsonProperty("contractTemplate")
    @Valid
    private List<ContractTemplate> contractTemplate = null;

    @JsonProperty("risks")
    @Valid
    private List<Risk> risks = null;

    @JsonProperty("optionalRisks")
    @Valid
    private List<Risk> optionalRisks = null;

    @JsonProperty("documents")
    @Valid
    private List<RiskDocument> documents = null;

    @JsonProperty("requiredFields")
    @Valid
    private List<RequiredField> requiredFields = null;

    @JsonProperty("minAgeHolder")
    private Integer minAgeHolder = null;

    @JsonProperty("maxAgeHolder")
    private Integer maxAgeHolder = null;

    @JsonProperty("minAgeInsured")
    private Integer minAgeInsured = null;

    @JsonProperty("maxAgeInsured")
    private Integer maxAgeInsured = null;

    @JsonProperty("minGrowth")
    private Integer minGrowth = null;

    @JsonProperty("maxGrowth")
    private Integer maxGrowth = null;

    @JsonProperty("minWeight")
    private Integer minWeight = null;

    @JsonProperty("maxWeight")
    private Integer maxWeight = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("maxUpperPressure")
    private Integer maxUpperPressure = null;

    @JsonProperty("minUpperPressure")
    private Integer minUpperPressure = null;

    @JsonProperty("maxLowerPressure")
    private Integer maxLowerPressure = null;

    @JsonProperty("minLowerPressure")
    private Integer minLowerPressure = null;

    @JsonProperty("guaranteeLevel")
    private BigDecimal guaranteeLevel = null;

    @JsonProperty("specialRate")
    private Boolean specialRate = null;

    @JsonProperty("specialRateValue")
    private BigDecimal specialRateValue = null;

    @JsonProperty("individualRate")
    private Boolean individualRate = null;

    @JsonProperty("discount")
    private BigDecimal discount = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ProgramSettingData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор набора параметров программы страхования
     * @param programKind Вид программы страхования
     * @param programId Идентификатор программы страхования
     * @param startDate Дата начала действия программы страхования
     * @param endDate Дата окончания действия программы страхования
     * @param staticDate Статичная дата начала действия программы страхования
     * @param strategy Список стратегий. Только для ИСЖ (programKind == ISJ)
     * @param policyholderInsured Признак того, что страхователь является застрахованным лицом
     * @param minimumTerm Минимальный срок страхования
     * @param maximumTerm Максимальный срок страхования
     * @param paymentTerm Срок выплаты в годах
     * @param calendarUnit Календарная единица срока страхования
     * @param currency Идентификатор валюты программы страхования
     * @param minSum Минимальная страховая сумма по договору
     * @param maxSum Максимальная страховая сумма по договору
     * @param minPremium Минимальная страховая премия по договору
     * @param maxPremium Максимальная страховая премия по договору
     * @param premiumMethod Тип расчета страховой премии
     * @param coefficient Коэффициент для расчета премии, присылается если premiumMethod == MULTIPLIED
     * @param premium Страховая премия по договору, присылается если premiumMethod == FIXED
     * @param sum Страховая сумма по договору, присылается если тип расчёта страховой суммы == FIXED
     * @param tariff Тариф для расчета, присылается если тип расчёта страховой суммы == по формуле
     * @param periodicity Периодичность уплаты взносов
     * @param underwriting Вид андеррайтинга
     * @param contractTemplate Список шаблонов документов для печати
     * @param risks Список обязательных рисков
     * @param optionalRisks Список дополнительных рисков
     * @param documents Список обязательных документов
     * @param requiredFields Поля, обязательные для заполнения
     * @param minAgeHolder Страхователь, возраст минимум
     * @param maxAgeHolder Страхователь, возраст максимум
     * @param minAgeInsured Застрахованный, возраст минимум
     * @param maxAgeInsured Застрахованный, возраст максимум
     * @param minGrowth Минимальный рост застрахованного
     * @param maxGrowth Максимальный рост застрахованного
     * @param minWeight Минимальный вес застрахованного
     * @param maxWeight Максимальный вес застрахованного
     * @param gender Пол застрахованного
     * @param maxUpperPressure Максимальное значение верхнего артериального давления Застрахованного
     * @param minUpperPressure Минимальное значение верхнего артериального давления Застрахованного
     * @param maxLowerPressure Максимальное значение нижнего артериального давления Застрахованного
     * @param minLowerPressure Минимальное значение нижнего артериального давления Застрахованного
     * @param guaranteeLevel Уровень гарантии (%)(Только для ИСЖ)
     * @param specialRate Признак специального курса валюты для программы
     * @param specialRateValue Добавочный процент специального курса валют
     * @param individualRate Признак возможности использования индивидуального курса валют
     * @param discount Размер скидки
     */
    public ProgramSettingData(Long id, ProgramKind programKind, Long programId, LocalDate startDate, LocalDate endDate, LocalDate staticDate, List<Strategy> strategy, Boolean policyholderInsured, Integer minimumTerm, Integer maximumTerm, Integer paymentTerm, CalendarUnit calendarUnit, Long currency, BigDecimal minSum, BigDecimal maxSum, BigDecimal minPremium, BigDecimal maxPremium, PremiumMethod premiumMethod, BigDecimal coefficient, BigDecimal premium, BigDecimal sum, BigDecimal tariff, PaymentPeriodicity periodicity, UnderwritingKind underwriting, List<ContractTemplate> contractTemplate, List<Risk> risks, List<Risk> optionalRisks, List<RiskDocument> documents, List<RequiredField> requiredFields, Integer minAgeHolder, Integer maxAgeHolder, Integer minAgeInsured, Integer maxAgeInsured, Integer minGrowth, Integer maxGrowth, Integer minWeight, Integer maxWeight, Gender gender, Integer maxUpperPressure, Integer minUpperPressure, Integer maxLowerPressure, Integer minLowerPressure, BigDecimal guaranteeLevel, Boolean specialRate, BigDecimal specialRateValue, Boolean individualRate, BigDecimal discount) {
        this.id = id;
        this.programKind = programKind;
        this.programId = programId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.staticDate = staticDate;
        this.strategy = strategy;
        this.policyholderInsured = policyholderInsured;
        this.minimumTerm = minimumTerm;
        this.maximumTerm = maximumTerm;
        this.paymentTerm = paymentTerm;
        this.calendarUnit = calendarUnit;
        this.currency = currency;
        this.minSum = minSum;
        this.maxSum = maxSum;
        this.minPremium = minPremium;
        this.maxPremium = maxPremium;
        this.premiumMethod = premiumMethod;
        this.coefficient = coefficient;
        this.premium = premium;
        this.sum = sum;
        this.tariff = tariff;
        this.periodicity = periodicity;
        this.underwriting = underwriting;
        this.contractTemplate = contractTemplate;
        this.risks = risks;
        this.optionalRisks = optionalRisks;
        this.documents = documents;
        this.requiredFields = requiredFields;
        this.minAgeHolder = minAgeHolder;
        this.maxAgeHolder = maxAgeHolder;
        this.minAgeInsured = minAgeInsured;
        this.maxAgeInsured = maxAgeInsured;
        this.minGrowth = minGrowth;
        this.maxGrowth = maxGrowth;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.gender = gender;
        this.maxUpperPressure = maxUpperPressure;
        this.minUpperPressure = minUpperPressure;
        this.maxLowerPressure = maxLowerPressure;
        this.minLowerPressure = minLowerPressure;
        this.guaranteeLevel = guaranteeLevel;
        this.specialRate = specialRate;
        this.specialRateValue = specialRateValue;
        this.individualRate = individualRate;
        this.discount = discount;
    }

    /**
     * Идентификатор набора параметров программы страхования
    * @return Идентификатор набора параметров программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор набора параметров программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getProgramKind() {
        return programKind;
    }

    public void setProgramKind(ProgramKind programKind) {
        this.programKind = programKind;
    }


    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }


    /**
     * Дата начала действия программы страхования
    * @return Дата начала действия программы страхования
    **/
    @ApiModelProperty(value = "Дата начала действия программы страхования")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания действия программы страхования
    * @return Дата окончания действия программы страхования
    **/
    @ApiModelProperty(value = "Дата окончания действия программы страхования")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Статичная дата начала действия программы страхования
    * @return Статичная дата начала действия программы страхования
    **/
    @ApiModelProperty(value = "Статичная дата начала действия программы страхования")
    
  @Valid


    public LocalDate getStaticDate() {
        return staticDate;
    }

    public void setStaticDate(LocalDate staticDate) {
        this.staticDate = staticDate;
    }


    public ProgramSettingData addStrategyItem(Strategy strategyItem) {
        if (this.strategy == null) {
            this.strategy = new ArrayList<Strategy>();
        }
        this.strategy.add(strategyItem);
        return this;
    }

    /**
     * Список стратегий. Только для ИСЖ (programKind == ISJ)
    * @return Список стратегий. Только для ИСЖ (programKind == ISJ)
    **/
    @ApiModelProperty(value = "Список стратегий. Только для ИСЖ (programKind == ISJ)")
    
  @Valid


    public List<Strategy> getStrategy() {
        return strategy;
    }

    public void setStrategy(List<Strategy> strategy) {
        this.strategy = strategy;
    }


    /**
     * Признак того, что страхователь является застрахованным лицом
    * @return Признак того, что страхователь является застрахованным лицом
    **/
    @ApiModelProperty(value = "Признак того, что страхователь является застрахованным лицом")
    


    public Boolean isPolicyholderInsured() {
        return policyholderInsured;
    }

    public void setPolicyholderInsured(Boolean policyholderInsured) {
        this.policyholderInsured = policyholderInsured;
    }


    /**
     * Минимальный срок страхования
    * @return Минимальный срок страхования
    **/
    @ApiModelProperty(value = "Минимальный срок страхования")
    


    public Integer getMinimumTerm() {
        return minimumTerm;
    }

    public void setMinimumTerm(Integer minimumTerm) {
        this.minimumTerm = minimumTerm;
    }


    /**
     * Максимальный срок страхования
    * @return Максимальный срок страхования
    **/
    @ApiModelProperty(value = "Максимальный срок страхования")
    


    public Integer getMaximumTerm() {
        return maximumTerm;
    }

    public void setMaximumTerm(Integer maximumTerm) {
        this.maximumTerm = maximumTerm;
    }


    /**
     * Срок выплаты в годах
    * @return Срок выплаты в годах
    **/
    @ApiModelProperty(value = "Срок выплаты в годах")
    


    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }


    /**
     * Календарная единица срока страхования
    * @return Календарная единица срока страхования
    **/
    @ApiModelProperty(value = "Календарная единица срока страхования")
    
  @Valid


    public CalendarUnit getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnit calendarUnit) {
        this.calendarUnit = calendarUnit;
    }


    /**
     * Идентификатор валюты программы страхования
    * @return Идентификатор валюты программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор валюты программы страхования")
    


    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }


    /**
     * Минимальная страховая сумма по договору
    * @return Минимальная страховая сумма по договору
    **/
    @ApiModelProperty(value = "Минимальная страховая сумма по договору")
    
  @Valid


    public BigDecimal getMinSum() {
        return minSum;
    }

    public void setMinSum(BigDecimal minSum) {
        this.minSum = minSum;
    }


    /**
     * Максимальная страховая сумма по договору
    * @return Максимальная страховая сумма по договору
    **/
    @ApiModelProperty(value = "Максимальная страховая сумма по договору")
    
  @Valid


    public BigDecimal getMaxSum() {
        return maxSum;
    }

    public void setMaxSum(BigDecimal maxSum) {
        this.maxSum = maxSum;
    }


    /**
     * Минимальная страховая премия по договору
    * @return Минимальная страховая премия по договору
    **/
    @ApiModelProperty(value = "Минимальная страховая премия по договору")
    
  @Valid


    public BigDecimal getMinPremium() {
        return minPremium;
    }

    public void setMinPremium(BigDecimal minPremium) {
        this.minPremium = minPremium;
    }


    /**
     * Максимальная страховая премия по договору
    * @return Максимальная страховая премия по договору
    **/
    @ApiModelProperty(value = "Максимальная страховая премия по договору")
    
  @Valid


    public BigDecimal getMaxPremium() {
        return maxPremium;
    }

    public void setMaxPremium(BigDecimal maxPremium) {
        this.maxPremium = maxPremium;
    }


    /**
     * Тип расчета страховой премии
    * @return Тип расчета страховой премии
    **/
    @ApiModelProperty(value = "Тип расчета страховой премии")
    
  @Valid


    public PremiumMethod getPremiumMethod() {
        return premiumMethod;
    }

    public void setPremiumMethod(PremiumMethod premiumMethod) {
        this.premiumMethod = premiumMethod;
    }


    /**
     * Коэффициент для расчета премии, присылается если premiumMethod == MULTIPLIED
    * @return Коэффициент для расчета премии, присылается если premiumMethod == MULTIPLIED
    **/
    @ApiModelProperty(value = "Коэффициент для расчета премии, присылается если premiumMethod == MULTIPLIED")
    
  @Valid


    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }


    /**
     * Страховая премия по договору, присылается если premiumMethod == FIXED
    * @return Страховая премия по договору, присылается если premiumMethod == FIXED
    **/
    @ApiModelProperty(value = "Страховая премия по договору, присылается если premiumMethod == FIXED")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Страховая сумма по договору, присылается если тип расчёта страховой суммы == FIXED
    * @return Страховая сумма по договору, присылается если тип расчёта страховой суммы == FIXED
    **/
    @ApiModelProperty(value = "Страховая сумма по договору, присылается если тип расчёта страховой суммы == FIXED")
    
  @Valid


    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }


    /**
     * Тариф для расчета, присылается если тип расчёта страховой суммы == по формуле
    * @return Тариф для расчета, присылается если тип расчёта страховой суммы == по формуле
    **/
    @ApiModelProperty(value = "Тариф для расчета, присылается если тип расчёта страховой суммы == по формуле")
    
  @Valid


    public BigDecimal getTariff() {
        return tariff;
    }

    public void setTariff(BigDecimal tariff) {
        this.tariff = tariff;
    }


    /**
     * Периодичность уплаты взносов
    * @return Периодичность уплаты взносов
    **/
    @ApiModelProperty(value = "Периодичность уплаты взносов")
    
  @Valid


    public PaymentPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PaymentPeriodicity periodicity) {
        this.periodicity = periodicity;
    }


    /**
     * Вид андеррайтинга
    * @return Вид андеррайтинга
    **/
    @ApiModelProperty(value = "Вид андеррайтинга")
    
  @Valid


    public UnderwritingKind getUnderwriting() {
        return underwriting;
    }

    public void setUnderwriting(UnderwritingKind underwriting) {
        this.underwriting = underwriting;
    }


    public ProgramSettingData addContractTemplateItem(ContractTemplate contractTemplateItem) {
        if (this.contractTemplate == null) {
            this.contractTemplate = new ArrayList<ContractTemplate>();
        }
        this.contractTemplate.add(contractTemplateItem);
        return this;
    }

    /**
     * Список шаблонов документов для печати
    * @return Список шаблонов документов для печати
    **/
    @ApiModelProperty(value = "Список шаблонов документов для печати")
    
  @Valid


    public List<ContractTemplate> getContractTemplate() {
        return contractTemplate;
    }

    public void setContractTemplate(List<ContractTemplate> contractTemplate) {
        this.contractTemplate = contractTemplate;
    }


    public ProgramSettingData addRisksItem(Risk risksItem) {
        if (this.risks == null) {
            this.risks = new ArrayList<Risk>();
        }
        this.risks.add(risksItem);
        return this;
    }

    /**
     * Список обязательных рисков
    * @return Список обязательных рисков
    **/
    @ApiModelProperty(value = "Список обязательных рисков")
    
  @Valid


    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }


    public ProgramSettingData addOptionalRisksItem(Risk optionalRisksItem) {
        if (this.optionalRisks == null) {
            this.optionalRisks = new ArrayList<Risk>();
        }
        this.optionalRisks.add(optionalRisksItem);
        return this;
    }

    /**
     * Список дополнительных рисков
    * @return Список дополнительных рисков
    **/
    @ApiModelProperty(value = "Список дополнительных рисков")
    
  @Valid


    public List<Risk> getOptionalRisks() {
        return optionalRisks;
    }

    public void setOptionalRisks(List<Risk> optionalRisks) {
        this.optionalRisks = optionalRisks;
    }


    public ProgramSettingData addDocumentsItem(RiskDocument documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<RiskDocument>();
        }
        this.documents.add(documentsItem);
        return this;
    }

    /**
     * Список обязательных документов
    * @return Список обязательных документов
    **/
    @ApiModelProperty(value = "Список обязательных документов")
    
  @Valid


    public List<RiskDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<RiskDocument> documents) {
        this.documents = documents;
    }


    public ProgramSettingData addRequiredFieldsItem(RequiredField requiredFieldsItem) {
        if (this.requiredFields == null) {
            this.requiredFields = new ArrayList<RequiredField>();
        }
        this.requiredFields.add(requiredFieldsItem);
        return this;
    }

    /**
     * Поля, обязательные для заполнения
    * @return Поля, обязательные для заполнения
    **/
    @ApiModelProperty(value = "Поля, обязательные для заполнения")
    
  @Valid


    public List<RequiredField> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(List<RequiredField> requiredFields) {
        this.requiredFields = requiredFields;
    }


    /**
     * Страхователь, возраст минимум
    * @return Страхователь, возраст минимум
    **/
    @ApiModelProperty(value = "Страхователь, возраст минимум")
    


    public Integer getMinAgeHolder() {
        return minAgeHolder;
    }

    public void setMinAgeHolder(Integer minAgeHolder) {
        this.minAgeHolder = minAgeHolder;
    }


    /**
     * Страхователь, возраст максимум
    * @return Страхователь, возраст максимум
    **/
    @ApiModelProperty(value = "Страхователь, возраст максимум")
    


    public Integer getMaxAgeHolder() {
        return maxAgeHolder;
    }

    public void setMaxAgeHolder(Integer maxAgeHolder) {
        this.maxAgeHolder = maxAgeHolder;
    }


    /**
     * Застрахованный, возраст минимум
    * @return Застрахованный, возраст минимум
    **/
    @ApiModelProperty(value = "Застрахованный, возраст минимум")
    


    public Integer getMinAgeInsured() {
        return minAgeInsured;
    }

    public void setMinAgeInsured(Integer minAgeInsured) {
        this.minAgeInsured = minAgeInsured;
    }


    /**
     * Застрахованный, возраст максимум
    * @return Застрахованный, возраст максимум
    **/
    @ApiModelProperty(value = "Застрахованный, возраст максимум")
    


    public Integer getMaxAgeInsured() {
        return maxAgeInsured;
    }

    public void setMaxAgeInsured(Integer maxAgeInsured) {
        this.maxAgeInsured = maxAgeInsured;
    }


    /**
     * Минимальный рост застрахованного
    * @return Минимальный рост застрахованного
    **/
    @ApiModelProperty(value = "Минимальный рост застрахованного")
    


    public Integer getMinGrowth() {
        return minGrowth;
    }

    public void setMinGrowth(Integer minGrowth) {
        this.minGrowth = minGrowth;
    }


    /**
     * Максимальный рост застрахованного
    * @return Максимальный рост застрахованного
    **/
    @ApiModelProperty(value = "Максимальный рост застрахованного")
    


    public Integer getMaxGrowth() {
        return maxGrowth;
    }

    public void setMaxGrowth(Integer maxGrowth) {
        this.maxGrowth = maxGrowth;
    }


    /**
     * Минимальный вес застрахованного
    * @return Минимальный вес застрахованного
    **/
    @ApiModelProperty(value = "Минимальный вес застрахованного")
    


    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }


    /**
     * Максимальный вес застрахованного
    * @return Максимальный вес застрахованного
    **/
    @ApiModelProperty(value = "Максимальный вес застрахованного")
    


    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }


    /**
     * Пол застрахованного
    * @return Пол застрахованного
    **/
    @ApiModelProperty(value = "Пол застрахованного")
    
  @Valid


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    /**
     * Максимальное значение верхнего артериального давления Застрахованного
    * @return Максимальное значение верхнего артериального давления Застрахованного
    **/
    @ApiModelProperty(value = "Максимальное значение верхнего артериального давления Застрахованного")
    


    public Integer getMaxUpperPressure() {
        return maxUpperPressure;
    }

    public void setMaxUpperPressure(Integer maxUpperPressure) {
        this.maxUpperPressure = maxUpperPressure;
    }


    /**
     * Минимальное значение верхнего артериального давления Застрахованного
    * @return Минимальное значение верхнего артериального давления Застрахованного
    **/
    @ApiModelProperty(value = "Минимальное значение верхнего артериального давления Застрахованного")
    


    public Integer getMinUpperPressure() {
        return minUpperPressure;
    }

    public void setMinUpperPressure(Integer minUpperPressure) {
        this.minUpperPressure = minUpperPressure;
    }


    /**
     * Максимальное значение нижнего артериального давления Застрахованного
    * @return Максимальное значение нижнего артериального давления Застрахованного
    **/
    @ApiModelProperty(value = "Максимальное значение нижнего артериального давления Застрахованного")
    


    public Integer getMaxLowerPressure() {
        return maxLowerPressure;
    }

    public void setMaxLowerPressure(Integer maxLowerPressure) {
        this.maxLowerPressure = maxLowerPressure;
    }


    /**
     * Минимальное значение нижнего артериального давления Застрахованного
    * @return Минимальное значение нижнего артериального давления Застрахованного
    **/
    @ApiModelProperty(value = "Минимальное значение нижнего артериального давления Застрахованного")
    


    public Integer getMinLowerPressure() {
        return minLowerPressure;
    }

    public void setMinLowerPressure(Integer minLowerPressure) {
        this.minLowerPressure = minLowerPressure;
    }


    /**
     * Уровень гарантии (%)(Только для ИСЖ)
    * @return Уровень гарантии (%)(Только для ИСЖ)
    **/
    @ApiModelProperty(value = "Уровень гарантии (%)(Только для ИСЖ)")
    
  @Valid


    public BigDecimal getGuaranteeLevel() {
        return guaranteeLevel;
    }

    public void setGuaranteeLevel(BigDecimal guaranteeLevel) {
        this.guaranteeLevel = guaranteeLevel;
    }


    /**
     * Признак специального курса валюты для программы
    * @return Признак специального курса валюты для программы
    **/
    @ApiModelProperty(value = "Признак специального курса валюты для программы")
    


    public Boolean isSpecialRate() {
        return specialRate;
    }

    public void setSpecialRate(Boolean specialRate) {
        this.specialRate = specialRate;
    }


    /**
     * Добавочный процент специального курса валют
    * @return Добавочный процент специального курса валют
    **/
    @ApiModelProperty(value = "Добавочный процент специального курса валют")
    
  @Valid


    public BigDecimal getSpecialRateValue() {
        return specialRateValue;
    }

    public void setSpecialRateValue(BigDecimal specialRateValue) {
        this.specialRateValue = specialRateValue;
    }


    /**
     * Признак возможности использования индивидуального курса валют
    * @return Признак возможности использования индивидуального курса валют
    **/
    @ApiModelProperty(value = "Признак возможности использования индивидуального курса валют")
    


    public Boolean isIndividualRate() {
        return individualRate;
    }

    public void setIndividualRate(Boolean individualRate) {
        this.individualRate = individualRate;
    }


    /**
     * Размер скидки
    * @return Размер скидки
    **/
    @ApiModelProperty(value = "Размер скидки")
    
  @Valid


    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramSettingData programSettingData = (ProgramSettingData) o;
        return Objects.equals(this.id, programSettingData.id) &&
            Objects.equals(this.programKind, programSettingData.programKind) &&
            Objects.equals(this.programId, programSettingData.programId) &&
            Objects.equals(this.startDate, programSettingData.startDate) &&
            Objects.equals(this.endDate, programSettingData.endDate) &&
            Objects.equals(this.staticDate, programSettingData.staticDate) &&
            Objects.equals(this.strategy, programSettingData.strategy) &&
            Objects.equals(this.policyholderInsured, programSettingData.policyholderInsured) &&
            Objects.equals(this.minimumTerm, programSettingData.minimumTerm) &&
            Objects.equals(this.maximumTerm, programSettingData.maximumTerm) &&
            Objects.equals(this.paymentTerm, programSettingData.paymentTerm) &&
            Objects.equals(this.calendarUnit, programSettingData.calendarUnit) &&
            Objects.equals(this.currency, programSettingData.currency) &&
            Objects.equals(this.minSum, programSettingData.minSum) &&
            Objects.equals(this.maxSum, programSettingData.maxSum) &&
            Objects.equals(this.minPremium, programSettingData.minPremium) &&
            Objects.equals(this.maxPremium, programSettingData.maxPremium) &&
            Objects.equals(this.premiumMethod, programSettingData.premiumMethod) &&
            Objects.equals(this.coefficient, programSettingData.coefficient) &&
            Objects.equals(this.premium, programSettingData.premium) &&
            Objects.equals(this.sum, programSettingData.sum) &&
            Objects.equals(this.tariff, programSettingData.tariff) &&
            Objects.equals(this.periodicity, programSettingData.periodicity) &&
            Objects.equals(this.underwriting, programSettingData.underwriting) &&
            Objects.equals(this.contractTemplate, programSettingData.contractTemplate) &&
            Objects.equals(this.risks, programSettingData.risks) &&
            Objects.equals(this.optionalRisks, programSettingData.optionalRisks) &&
            Objects.equals(this.documents, programSettingData.documents) &&
            Objects.equals(this.requiredFields, programSettingData.requiredFields) &&
            Objects.equals(this.minAgeHolder, programSettingData.minAgeHolder) &&
            Objects.equals(this.maxAgeHolder, programSettingData.maxAgeHolder) &&
            Objects.equals(this.minAgeInsured, programSettingData.minAgeInsured) &&
            Objects.equals(this.maxAgeInsured, programSettingData.maxAgeInsured) &&
            Objects.equals(this.minGrowth, programSettingData.minGrowth) &&
            Objects.equals(this.maxGrowth, programSettingData.maxGrowth) &&
            Objects.equals(this.minWeight, programSettingData.minWeight) &&
            Objects.equals(this.maxWeight, programSettingData.maxWeight) &&
            Objects.equals(this.gender, programSettingData.gender) &&
            Objects.equals(this.maxUpperPressure, programSettingData.maxUpperPressure) &&
            Objects.equals(this.minUpperPressure, programSettingData.minUpperPressure) &&
            Objects.equals(this.maxLowerPressure, programSettingData.maxLowerPressure) &&
            Objects.equals(this.minLowerPressure, programSettingData.minLowerPressure) &&
            Objects.equals(this.guaranteeLevel, programSettingData.guaranteeLevel) &&
            Objects.equals(this.specialRate, programSettingData.specialRate) &&
            Objects.equals(this.specialRateValue, programSettingData.specialRateValue) &&
            Objects.equals(this.individualRate, programSettingData.individualRate) &&
            Objects.equals(this.discount, programSettingData.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, programKind, programId, startDate, endDate, staticDate, strategy, policyholderInsured, minimumTerm, maximumTerm, paymentTerm, calendarUnit, currency, minSum, maxSum, minPremium, maxPremium, premiumMethod, coefficient, premium, sum, tariff, periodicity, underwriting, contractTemplate, risks, optionalRisks, documents, requiredFields, minAgeHolder, maxAgeHolder, minAgeInsured, maxAgeInsured, minGrowth, maxGrowth, minWeight, maxWeight, gender, maxUpperPressure, minUpperPressure, maxLowerPressure, minLowerPressure, guaranteeLevel, specialRate, specialRateValue, individualRate, discount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProgramSettingData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    staticDate: ").append(toIndentedString(staticDate)).append("\n");
        sb.append("    strategy: ").append(toIndentedString(strategy)).append("\n");
        sb.append("    policyholderInsured: ").append(toIndentedString(policyholderInsured)).append("\n");
        sb.append("    minimumTerm: ").append(toIndentedString(minimumTerm)).append("\n");
        sb.append("    maximumTerm: ").append(toIndentedString(maximumTerm)).append("\n");
        sb.append("    paymentTerm: ").append(toIndentedString(paymentTerm)).append("\n");
        sb.append("    calendarUnit: ").append(toIndentedString(calendarUnit)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    minSum: ").append(toIndentedString(minSum)).append("\n");
        sb.append("    maxSum: ").append(toIndentedString(maxSum)).append("\n");
        sb.append("    minPremium: ").append(toIndentedString(minPremium)).append("\n");
        sb.append("    maxPremium: ").append(toIndentedString(maxPremium)).append("\n");
        sb.append("    premiumMethod: ").append(toIndentedString(premiumMethod)).append("\n");
        sb.append("    coefficient: ").append(toIndentedString(coefficient)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    sum: ").append(toIndentedString(sum)).append("\n");
        sb.append("    tariff: ").append(toIndentedString(tariff)).append("\n");
        sb.append("    periodicity: ").append(toIndentedString(periodicity)).append("\n");
        sb.append("    underwriting: ").append(toIndentedString(underwriting)).append("\n");
        sb.append("    contractTemplate: ").append(toIndentedString(contractTemplate)).append("\n");
        sb.append("    risks: ").append(toIndentedString(risks)).append("\n");
        sb.append("    optionalRisks: ").append(toIndentedString(optionalRisks)).append("\n");
        sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
        sb.append("    requiredFields: ").append(toIndentedString(requiredFields)).append("\n");
        sb.append("    minAgeHolder: ").append(toIndentedString(minAgeHolder)).append("\n");
        sb.append("    maxAgeHolder: ").append(toIndentedString(maxAgeHolder)).append("\n");
        sb.append("    minAgeInsured: ").append(toIndentedString(minAgeInsured)).append("\n");
        sb.append("    maxAgeInsured: ").append(toIndentedString(maxAgeInsured)).append("\n");
        sb.append("    minGrowth: ").append(toIndentedString(minGrowth)).append("\n");
        sb.append("    maxGrowth: ").append(toIndentedString(maxGrowth)).append("\n");
        sb.append("    minWeight: ").append(toIndentedString(minWeight)).append("\n");
        sb.append("    maxWeight: ").append(toIndentedString(maxWeight)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    maxUpperPressure: ").append(toIndentedString(maxUpperPressure)).append("\n");
        sb.append("    minUpperPressure: ").append(toIndentedString(minUpperPressure)).append("\n");
        sb.append("    maxLowerPressure: ").append(toIndentedString(maxLowerPressure)).append("\n");
        sb.append("    minLowerPressure: ").append(toIndentedString(minLowerPressure)).append("\n");
        sb.append("    guaranteeLevel: ").append(toIndentedString(guaranteeLevel)).append("\n");
        sb.append("    specialRate: ").append(toIndentedString(specialRate)).append("\n");
        sb.append("    specialRateValue: ").append(toIndentedString(specialRateValue)).append("\n");
        sb.append("    individualRate: ").append(toIndentedString(individualRate)).append("\n");
        sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

