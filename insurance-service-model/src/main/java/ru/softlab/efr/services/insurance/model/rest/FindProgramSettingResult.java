package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.CalendarUnit;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplate;
import ru.softlab.efr.services.insurance.model.rest.FindProgramRisk;
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.PremiumMethod;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RequiredField;
import ru.softlab.efr.services.insurance.model.rest.RiskDocument;
import ru.softlab.efr.services.insurance.model.rest.StrategyData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Результат подбора программ страхования
 */
@ApiModel(description = "Результат подбора программ страхования")
@Validated
public class FindProgramSettingResult   {
    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("programNumber")
    private String programNumber = null;

    @JsonProperty("programSettingId")
    private Long programSettingId = null;

    @JsonProperty("premiumMethod")
    private PremiumMethod premiumMethod = null;

    @JsonProperty("coefficient")
    private BigDecimal coefficient = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("sum")
    private BigDecimal sum = null;

    @JsonProperty("minimumTerm")
    private Integer minimumTerm = null;

    @JsonProperty("maximumTerm")
    private Integer maximumTerm = null;

    @JsonProperty("calendarUnit")
    private CalendarUnit calendarUnit = null;

    @JsonProperty("currency")
    private Long currency = null;

    @JsonProperty("periodicity")
    private PaymentPeriodicity periodicity = null;

    @JsonProperty("policyCode")
    private String policyCode = null;

    @JsonProperty("option")
    private String option = null;

    @JsonProperty("coolingPeriod")
    private Integer coolingPeriod = null;

    @JsonProperty("risks")
    @Valid
    private List<FindProgramRisk> risks = null;

    @JsonProperty("optionalRisks")
    @Valid
    private List<FindProgramRisk> optionalRisks = null;

    @JsonProperty("requiredFields")
    @Valid
    private List<RequiredField> requiredFields = null;

    @JsonProperty("documents")
    @Valid
    private List<RiskDocument> documents = null;

    @JsonProperty("contractTemplate")
    @Valid
    private List<ContractTemplate> contractTemplate = null;

    @JsonProperty("strategies")
    @Valid
    private List<StrategyData> strategies = null;

    @JsonProperty("guaranteeLevel")
    private BigDecimal guaranteeLevel = null;

    @JsonProperty("paymentTerm")
    private Integer paymentTerm = null;

    @JsonProperty("policyholderInsured")
    private Boolean policyholderInsured = null;

    @JsonProperty("specialRate")
    private Boolean specialRate = null;

    @JsonProperty("specialRateValue")
    private BigDecimal specialRateValue = null;

    @JsonProperty("individualRate")
    private Boolean individualRate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FindProgramSettingResult() {}

    /**
     * Создает экземпляр класса
     * @param programId Идентификатор программы страхования
     * @param programName Наименование программы страхования
     * @param programKind Вид программы страхования
     * @param programNumber Номер программы страхования
     * @param programSettingId Идентификатор набора параметров программы страхования
     * @param premiumMethod Тип расчета страховой премии
     * @param coefficient Коэффициент для расчета премии
     * @param premium Страховая премия по договору
     * @param sum Страховая сумма по договору
     * @param minimumTerm Минимальный срок страхования
     * @param maximumTerm Максимальный срок страхования
     * @param calendarUnit Календарная единица срока страхования
     * @param currency Идентификатор валюты программы страхования
     * @param periodicity Периодичность уплаты взносов
     * @param policyCode Кодировка полиса
     * @param option Вариант программы страхования
     * @param coolingPeriod Период охлаждения
     * @param risks Список обязательных рисков
     * @param optionalRisks Список обязательных рисков
     * @param requiredFields Поля, обязательные для заполнения
     * @param documents Список обязательных документов
     * @param contractTemplate Список шаблонов документов для печати
     * @param strategies Список стратегий программы
     * @param guaranteeLevel Уровень гарантии (%)(Только для ИСЖ)
     * @param paymentTerm Срок выплаты в годах
     * @param policyholderInsured Признак является ли Страхователь и Застрахованный одним физическим лицом
     * @param specialRate Признак специального курса валюты для программы
     * @param specialRateValue Добавочный процент специального курса валют
     * @param individualRate Признак возможности использования индивидуального курса валют
     */
    public FindProgramSettingResult(Long programId, String programName, ProgramKind programKind, String programNumber, Long programSettingId, PremiumMethod premiumMethod, BigDecimal coefficient, BigDecimal premium, BigDecimal sum, Integer minimumTerm, Integer maximumTerm, CalendarUnit calendarUnit, Long currency, PaymentPeriodicity periodicity, String policyCode, String option, Integer coolingPeriod, List<FindProgramRisk> risks, List<FindProgramRisk> optionalRisks, List<RequiredField> requiredFields, List<RiskDocument> documents, List<ContractTemplate> contractTemplate, List<StrategyData> strategies, BigDecimal guaranteeLevel, Integer paymentTerm, Boolean policyholderInsured, Boolean specialRate, BigDecimal specialRateValue, Boolean individualRate) {
        this.programId = programId;
        this.programName = programName;
        this.programKind = programKind;
        this.programNumber = programNumber;
        this.programSettingId = programSettingId;
        this.premiumMethod = premiumMethod;
        this.coefficient = coefficient;
        this.premium = premium;
        this.sum = sum;
        this.minimumTerm = minimumTerm;
        this.maximumTerm = maximumTerm;
        this.calendarUnit = calendarUnit;
        this.currency = currency;
        this.periodicity = periodicity;
        this.policyCode = policyCode;
        this.option = option;
        this.coolingPeriod = coolingPeriod;
        this.risks = risks;
        this.optionalRisks = optionalRisks;
        this.requiredFields = requiredFields;
        this.documents = documents;
        this.contractTemplate = contractTemplate;
        this.strategies = strategies;
        this.guaranteeLevel = guaranteeLevel;
        this.paymentTerm = paymentTerm;
        this.policyholderInsured = policyholderInsured;
        this.specialRate = specialRate;
        this.specialRateValue = specialRateValue;
        this.individualRate = individualRate;
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
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
     * Номер программы страхования
    * @return Номер программы страхования
    **/
    @ApiModelProperty(value = "Номер программы страхования")
    
 @Pattern(regexp="(^[0-9]{3}$)")

    public String getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(String programNumber) {
        this.programNumber = programNumber;
    }


    /**
     * Идентификатор набора параметров программы страхования
    * @return Идентификатор набора параметров программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор набора параметров программы страхования")
    


    public Long getProgramSettingId() {
        return programSettingId;
    }

    public void setProgramSettingId(Long programSettingId) {
        this.programSettingId = programSettingId;
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
     * Коэффициент для расчета премии
    * @return Коэффициент для расчета премии
    **/
    @ApiModelProperty(value = "Коэффициент для расчета премии")
    
  @Valid


    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }


    /**
     * Страховая премия по договору
    * @return Страховая премия по договору
    **/
    @ApiModelProperty(value = "Страховая премия по договору")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Страховая сумма по договору
    * @return Страховая сумма по договору
    **/
    @ApiModelProperty(value = "Страховая сумма по договору")
    
  @Valid


    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
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
     * Кодировка полиса
    * @return Кодировка полиса
    **/
    @ApiModelProperty(value = "Кодировка полиса")
    


    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }


    /**
     * Вариант программы страхования
    * @return Вариант программы страхования
    **/
    @ApiModelProperty(value = "Вариант программы страхования")
    


    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }


    /**
     * Период охлаждения
    * @return Период охлаждения
    **/
    @ApiModelProperty(value = "Период охлаждения")
    


    public Integer getCoolingPeriod() {
        return coolingPeriod;
    }

    public void setCoolingPeriod(Integer coolingPeriod) {
        this.coolingPeriod = coolingPeriod;
    }


    public FindProgramSettingResult addRisksItem(FindProgramRisk risksItem) {
        if (this.risks == null) {
            this.risks = new ArrayList<FindProgramRisk>();
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


    public List<FindProgramRisk> getRisks() {
        return risks;
    }

    public void setRisks(List<FindProgramRisk> risks) {
        this.risks = risks;
    }


    public FindProgramSettingResult addOptionalRisksItem(FindProgramRisk optionalRisksItem) {
        if (this.optionalRisks == null) {
            this.optionalRisks = new ArrayList<FindProgramRisk>();
        }
        this.optionalRisks.add(optionalRisksItem);
        return this;
    }

    /**
     * Список обязательных рисков
    * @return Список обязательных рисков
    **/
    @ApiModelProperty(value = "Список обязательных рисков")
    
  @Valid


    public List<FindProgramRisk> getOptionalRisks() {
        return optionalRisks;
    }

    public void setOptionalRisks(List<FindProgramRisk> optionalRisks) {
        this.optionalRisks = optionalRisks;
    }


    public FindProgramSettingResult addRequiredFieldsItem(RequiredField requiredFieldsItem) {
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


    public FindProgramSettingResult addDocumentsItem(RiskDocument documentsItem) {
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


    public FindProgramSettingResult addContractTemplateItem(ContractTemplate contractTemplateItem) {
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


    public FindProgramSettingResult addStrategiesItem(StrategyData strategiesItem) {
        if (this.strategies == null) {
            this.strategies = new ArrayList<StrategyData>();
        }
        this.strategies.add(strategiesItem);
        return this;
    }

    /**
     * Список стратегий программы
    * @return Список стратегий программы
    **/
    @ApiModelProperty(value = "Список стратегий программы")
    
  @Valid


    public List<StrategyData> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<StrategyData> strategies) {
        this.strategies = strategies;
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
     * Признак является ли Страхователь и Застрахованный одним физическим лицом
    * @return Признак является ли Страхователь и Застрахованный одним физическим лицом
    **/
    @ApiModelProperty(value = "Признак является ли Страхователь и Застрахованный одним физическим лицом")
    


    public Boolean isPolicyholderInsured() {
        return policyholderInsured;
    }

    public void setPolicyholderInsured(Boolean policyholderInsured) {
        this.policyholderInsured = policyholderInsured;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FindProgramSettingResult findProgramSettingResult = (FindProgramSettingResult) o;
        return Objects.equals(this.programId, findProgramSettingResult.programId) &&
            Objects.equals(this.programName, findProgramSettingResult.programName) &&
            Objects.equals(this.programKind, findProgramSettingResult.programKind) &&
            Objects.equals(this.programNumber, findProgramSettingResult.programNumber) &&
            Objects.equals(this.programSettingId, findProgramSettingResult.programSettingId) &&
            Objects.equals(this.premiumMethod, findProgramSettingResult.premiumMethod) &&
            Objects.equals(this.coefficient, findProgramSettingResult.coefficient) &&
            Objects.equals(this.premium, findProgramSettingResult.premium) &&
            Objects.equals(this.sum, findProgramSettingResult.sum) &&
            Objects.equals(this.minimumTerm, findProgramSettingResult.minimumTerm) &&
            Objects.equals(this.maximumTerm, findProgramSettingResult.maximumTerm) &&
            Objects.equals(this.calendarUnit, findProgramSettingResult.calendarUnit) &&
            Objects.equals(this.currency, findProgramSettingResult.currency) &&
            Objects.equals(this.periodicity, findProgramSettingResult.periodicity) &&
            Objects.equals(this.policyCode, findProgramSettingResult.policyCode) &&
            Objects.equals(this.option, findProgramSettingResult.option) &&
            Objects.equals(this.coolingPeriod, findProgramSettingResult.coolingPeriod) &&
            Objects.equals(this.risks, findProgramSettingResult.risks) &&
            Objects.equals(this.optionalRisks, findProgramSettingResult.optionalRisks) &&
            Objects.equals(this.requiredFields, findProgramSettingResult.requiredFields) &&
            Objects.equals(this.documents, findProgramSettingResult.documents) &&
            Objects.equals(this.contractTemplate, findProgramSettingResult.contractTemplate) &&
            Objects.equals(this.strategies, findProgramSettingResult.strategies) &&
            Objects.equals(this.guaranteeLevel, findProgramSettingResult.guaranteeLevel) &&
            Objects.equals(this.paymentTerm, findProgramSettingResult.paymentTerm) &&
            Objects.equals(this.policyholderInsured, findProgramSettingResult.policyholderInsured) &&
            Objects.equals(this.specialRate, findProgramSettingResult.specialRate) &&
            Objects.equals(this.specialRateValue, findProgramSettingResult.specialRateValue) &&
            Objects.equals(this.individualRate, findProgramSettingResult.individualRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programId, programName, programKind, programNumber, programSettingId, premiumMethod, coefficient, premium, sum, minimumTerm, maximumTerm, calendarUnit, currency, periodicity, policyCode, option, coolingPeriod, risks, optionalRisks, requiredFields, documents, contractTemplate, strategies, guaranteeLevel, paymentTerm, policyholderInsured, specialRate, specialRateValue, individualRate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FindProgramSettingResult {\n");
        
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    programNumber: ").append(toIndentedString(programNumber)).append("\n");
        sb.append("    programSettingId: ").append(toIndentedString(programSettingId)).append("\n");
        sb.append("    premiumMethod: ").append(toIndentedString(premiumMethod)).append("\n");
        sb.append("    coefficient: ").append(toIndentedString(coefficient)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    sum: ").append(toIndentedString(sum)).append("\n");
        sb.append("    minimumTerm: ").append(toIndentedString(minimumTerm)).append("\n");
        sb.append("    maximumTerm: ").append(toIndentedString(maximumTerm)).append("\n");
        sb.append("    calendarUnit: ").append(toIndentedString(calendarUnit)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    periodicity: ").append(toIndentedString(periodicity)).append("\n");
        sb.append("    policyCode: ").append(toIndentedString(policyCode)).append("\n");
        sb.append("    option: ").append(toIndentedString(option)).append("\n");
        sb.append("    coolingPeriod: ").append(toIndentedString(coolingPeriod)).append("\n");
        sb.append("    risks: ").append(toIndentedString(risks)).append("\n");
        sb.append("    optionalRisks: ").append(toIndentedString(optionalRisks)).append("\n");
        sb.append("    requiredFields: ").append(toIndentedString(requiredFields)).append("\n");
        sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
        sb.append("    contractTemplate: ").append(toIndentedString(contractTemplate)).append("\n");
        sb.append("    strategies: ").append(toIndentedString(strategies)).append("\n");
        sb.append("    guaranteeLevel: ").append(toIndentedString(guaranteeLevel)).append("\n");
        sb.append("    paymentTerm: ").append(toIndentedString(paymentTerm)).append("\n");
        sb.append("    policyholderInsured: ").append(toIndentedString(policyholderInsured)).append("\n");
        sb.append("    specialRate: ").append(toIndentedString(specialRate)).append("\n");
        sb.append("    specialRateValue: ").append(toIndentedString(specialRateValue)).append("\n");
        sb.append("    individualRate: ").append(toIndentedString(individualRate)).append("\n");
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

