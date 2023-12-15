package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.*;
import ru.softlab.efr.services.insurance.model.enums.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Сущность для хранения данных справочника параметров программ страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "program_setting")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProgramSetting implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Идентификатор программы страхования, число
     * Выбор из справочника программ страхования (п.2.6.2)
     * по умолчанию не выбрано.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "program", nullable = true)
    private Program program;

    /**
     * Дата начала действия программы страхования, дата
     * Значение по умолчанию следующий день
     */
    @Column
    private Timestamp startDate;

    /**
     * Дата окончания действия программы страхования, дата
     * Значение по умолчанию: «31.12.2999».
     */
    @Column
    private Timestamp endDate;

    /**
     * Статичная дата начала действия договора.
     */
    @Column
    private Timestamp staticDate;

    /**
     * Стратегия
     * Справочник стратегий, по умолчанию не выбрано.
     * выбор одного или нескольких значений
     */
    @JoinTable(
            name = "strategy_2_program_setting",
            joinColumns = @JoinColumn(name = "programsettingid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "strategyid", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Strategy> strategyList;

    /**
     * Страхователь является Застрахованным, Признак
     * по умолчанию установлен
     */
    @Column
    private Boolean policyHolderInsured;

    /**
     * Минимальный срок страхования, число
     * Максимум 4 символа, по умолчанию не установлен.
     * Если значение поля «Минимальный срок страхования» равен «Максимальный срок страхования»,
     * то это означает, что срок страхования при подборе программы для клиента задан жестко и недоступен для выбора
     */
    @Column
    private Integer minimumTerm;

    /**
     * Максимальный срок страхования, число
     * Максимум 4 символа, по умолчанию не установлен.
     * Если значение поля «Минимальный срок страхования» равен «Максимальный срок страхования»,
     * то это означает, что срок страхования при подборе программы для клиента задан жестко и недоступен для выбора
     */
    @Column
    private Integer maximumTerm;

    /**
     * Срок выплаты в годах, число
     * Максимум 4 символа, по умолчанию не установлен.
     * Указывается только для ренты
     */
    @Column
    private Integer paymentTerm;

    /**
     * Календарная единица срока страхования, выбор одного значения
     * лет, месяцев, дней
     * по умолчанию лет
     */
    @Column
    @Enumerated(EnumType.STRING)
    private CalendarUnitEnum calendarUnit;

    /**
     * Валюта программы страхования, выбор одного значения
     * Отображается  в формате RUB/USD/EUR,
     * по умолчанию выбрано RUB.
     */
    @Column
    private Long currency;

    /**
     * Минимальная страховая сумма по договору
     * Формат ввода «ХХХ XХХ XXX.XX», где Х – цифра от нуля до 9. Не может быть равна нулю.
     * Если минимальная сумма заполнена и равна максимальной сумме страхования, то страховая сумма фиксированная и не рассчитывается. Описание п.2.6.6.2.
     * По умолчанию не установлено.
     */
    @Column
    private BigDecimal minSum;

    /**
     * Максимальная страховая сумма по договору
     * Формат ввода «ХХХ XХХ XXX.XX», где Х – цифра от нуля до 9. Не может быть равна нулю.
     * Если минимальная сумма заполнена и равна максимальной сумме страхования, то страховая сумма фиксированная и не рассчитывается. Описание п.2.6.6.2.
     * По умолчанию не установлено.
     */
    @Column
    private BigDecimal maxSum;

    /**
     * Минимальная страховая премия по договору
     * Формат ввода «ХХХ XХХ XXX.XX», где Х – цифра от нуля до 9. Не может быть равна нулю.
     * Если минимальная премия заполнена и равна максимальной премии, то страховая премия фиксированная и не рассчитывается. Описание п. 2.6.6.3.
     * По умолчанию не установлено.
     */
    @Column
    private BigDecimal minPremium;

    /**
     * Максимальная страховая премия по договору
     * Формат ввода «ХХХ XХХ XXX.XX», где Х – цифра от нуля до 9. Не может быть равна нулю.
     * Если минимальная премия заполнена и равна максимальной премии, то страховая премия фиксированная и не рассчитывается. Описание п. 2.6.6.3.
     * По умолчанию не установлено.
     */
    @Column
    private BigDecimal maxPremium;

    /**
     * Тип расчета страховой премии, возможен выбор одного из значений:
     * •	не рассчитывается (ввод в ручном режиме).
     * •	СС, умноженная на коэффициент;
     * •	фиксированное значение.
     * •	По формуле. Значение возможно только для риска
     * Описание (п.2.6.6.4).
     */
    @Column
    @Enumerated(EnumType.STRING)
    private PremiumMethodEnum premiumMethod;

    /**
     * Коэффициент для расчета премии, число
     * Необязательно для установки
     */
    @Column
    private BigDecimal coefficient;

    /**
     * Страховая премия по договору, число
     * Необязательно для установки
     */
    @Column
    private BigDecimal bonusAmount;


    /**
     * Страховая сумма по договору, число
     * Необязательно для установки
     */
    @Column
    private BigDecimal insuranceAmount;

    /**
     * Тариф для расчета, число
     * Указывается, если «Тип расчета СС и СП» = «по формуле», иначе
     * Необязательно для установки
     */
    @Column
    private BigDecimal tariff;

    /**
     * Периодичность уплаты взносов, выбор одного значения:
     * единовременно, ежемесячно, ежеквартально, «раз в полгода», ежегодно
     * По умолчанию единовременно
     */
    @Column
    @Enumerated(EnumType.STRING)
    private PeriodicityEnum periodicity;

    /**
     * Вид андеррайтинга, выбор одного значения:
     * 1. Без заявления
     * 2. Декларация
     * 3. Заполнение заявления (анкета МЕДО)
     * 4. Проведение МЕДО,
     * По умолчанию «Без заявления».
     */
    @Column
    @Enumerated(EnumType.STRING)
    private UnderwritingEnum underwriting;

    /**
     * Шаблоны документов для печати
     * Ссылка на справочник шаблонов документов (п.2.6.7).
     * Если шаблоны не заданы, то печать документов для данной программы страхования не осуществляется.
     */

    @ElementCollection
    @CollectionTable(name = "document_template_2_program_setting", joinColumns = @JoinColumn(name = "programSettingId"))
    @Column(name = "documentTemplateId")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> documentTemplateList;

    /**
     * Данные по обязательным рискам.
     * Список обязательных рисков.
     * Описание в п.2.6.6.10
     */
    @JoinTable(
            name = "risk_2_program_setting",
            joinColumns = @JoinColumn(name = "programSettingId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "riskId", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<RiskSetting> requiredRiskSettingList;

    /**
     * Поля, обязательные для заполнения
     * Список полей из карточки клиента, которые необходимо заполнить для оформления текущей программы страхования.
     */
    @JoinTable(
            name = "required_field_2_program_setting",
            joinColumns = @JoinColumn(name = "programSettingId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "requiredFieldId", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<RequiredField> requiredFieldList;

    /**
     * Данные по дополнительным рискам.
     * Список дополнительных рисков.
     * Описание в п.2.6.6.10
     */
    @JoinTable(
            name = "optional_risk_2_program_setting",
            joinColumns = @JoinColumn(name = "programSettingId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "riskId", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<RiskSetting> additionalRiskSettingList;


    /**
     * Данные по обязательным документам.
     * Список обязательных документов.
     * Описание в п.2.6.6.9.
     */
    @JoinTable(
            name = "required_document_setting_2_program_setting",
            joinColumns = @JoinColumn(name = "programSettingId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "requiredDocumentSettingId", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<RequiredDocumentSetting> requiredDocumentList;

    /**
     * Страхователь, возраст минимум
     * полных лет,
     * по умолчанию не установлен
     */
    @Column
    private Integer minAgeHolder;

    /**
     * Страхователь, возраст максимум
     * полных лет,
     * по умолчанию не установлен
     */
    @Column
    private Integer maxAgeHolder;

    /**
     * Застрахованный, возраст минимум
     * полных лет,
     * по умолчанию не установлен
     */
    @Column
    private Integer minAgeInsured;

    /**
     * Застрахованный, возраст максимум
     * полных лет,
     * по умолчанию не установлен
     */
    @Column
    private Integer maxAgeInsured;

    /**
     * Минимальный рост застрахованного в сантиметрах
     * Значение больше нуля. Если значение не установлено, то рост «снизу» не ограничивается. По умолчанию не установлен
     * Необязательно для установки.
     */
    @Column
    private Integer minGrowth;

    /**
     * Максимальный рост застрахованного в сантиметрах
     * Значение больше нуля. Если значение не установлено, то рост «сверху» не ограничивается. По умолчанию не установлен
     * Необязательно для установки.
     */
    @Column
    private Integer maxGrowth;

    /**
     * Минимальный вес застрахованного в килограммах
     * Значение больше нуля. Если значение не установлено, то минимальный вес не ограничен. По умолчанию не установлен
     */
    @Column
    private Integer minWeight;

    /**
     * Максимальный вес застрахованного в килограммах
     * Значение больше нуля. Если значение не установлено, то рост «сверху» не ограничивается. По умолчанию не установлен
     */
    @Column
    private Integer maxWeight;

    /**
     * Пол застрахованного, выбор значения
     * мужской / женский. По умолчанию не выбрано
     */
    @Column
    @Enumerated(EnumType.STRING)
    private GenderTypeEnum gender;

    /**
     * Максимальное значение. Давление верхнее Застрахованного, число
     * Значение больше нуля. Если значение не установлено, то максимальное значение верхнего давления не установлено. По умолчанию не установлено.
     */
    @Column
    private Integer maxUpperPressure;

    /**
     * Минимальное значение. Давление верхнее Застрахованного, число
     * Значение больше нуля. Если значение не установлено, то минимальное значение верхнего давления не установлено. По умолчанию не установлено.
     */
    @Column
    private Integer minUpperPressure;

    /**
     * Максимальное значение. Давление верхнее Застрахованного, число
     * Значение больше нуля. Если значение не установлено, то максимальное значение верхнего давления не установлено. По умолчанию не установлено.
     */
    @Column
    private Integer maxLowerPressure;

    /**
     * Минимальное значение. Давление верхнее Застрахованного, число
     * Значение больше нуля. Если значение не установлено, то минимальное значение верхнего давления не установлено. По умолчанию не установлено.
     */
    @Column
    private Integer minLowerPressure;

    /**
     * Уровень гарантии (%)
     */
    @Column(name = "guarantee_level")
    private BigDecimal guaranteeLevel;

    /**
     * Признак специального курса валюты для программы
     */
    @Column(name = "special_rate")
    private Boolean specialRate;

    /**
     * Добавочный процент специального курса валют
     */
    @Column(name = "special_rate_value")
    private BigDecimal specialRateValue;

    /**
     * Признак возможности использования индивидуального курса валют
     */
    @Column(name = "individual_rate")
    private Boolean individualRate;

    /**
     * Размер скидки
     */
    @Column
    private BigDecimal discount;

    /**
     * Признак удалённой программы страхования
     */
    @Column
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public List<Strategy> getStrategyList() {
        return strategyList;
    }

    public void setStrategyList(List<Strategy> strategyList) {
        this.strategyList = strategyList;
    }

    public Boolean getPolicyHolderInsured() {
        return policyHolderInsured;
    }

    public void setPolicyHolderInsured(Boolean policyHolderInsured) {
        this.policyHolderInsured = policyHolderInsured;
    }

    public Integer getMinimumTerm() {
        return minimumTerm;
    }

    public void setMinimumTerm(Integer minimumTerm) {
        this.minimumTerm = minimumTerm;
    }

    public Integer getMaximumTerm() {
        return maximumTerm;
    }

    public void setMaximumTerm(Integer maximumTerm) {
        this.maximumTerm = maximumTerm;
    }

    public CalendarUnitEnum getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnitEnum calendarUnit) {
        this.calendarUnit = calendarUnit;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public BigDecimal getMinSum() {
        return minSum;
    }

    public void setMinSum(BigDecimal minSum) {
        this.minSum = minSum;
    }

    public BigDecimal getMaxSum() {
        return maxSum;
    }

    public void setMaxSum(BigDecimal maxSum) {
        this.maxSum = maxSum;
    }

    public BigDecimal getMinPremium() {
        return minPremium;
    }

    public void setMinPremium(BigDecimal minPremium) {
        this.minPremium = minPremium;
    }

    public BigDecimal getMaxPremium() {
        return maxPremium;
    }

    public void setMaxPremium(BigDecimal maxPremium) {
        this.maxPremium = maxPremium;
    }

    public PremiumMethodEnum getPremiumMethod() {
        return premiumMethod;
    }

    public void setPremiumMethod(PremiumMethodEnum premiumMethod) {
        this.premiumMethod = premiumMethod;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public PeriodicityEnum getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PeriodicityEnum periodicity) {
        this.periodicity = periodicity;
    }

    public UnderwritingEnum getUnderwriting() {
        return underwriting;
    }

    public void setUnderwriting(UnderwritingEnum underwriting) {
        this.underwriting = underwriting;
    }

    public List<String> getDocumentTemplateList() {
        return documentTemplateList;
    }

    public void setDocumentTemplateList(List<String> documentTemplateList) {
        this.documentTemplateList = documentTemplateList;
    }

    public List<RiskSetting> getRequiredRiskSettingList() {
        return requiredRiskSettingList;
    }

    public void setRequiredRiskSettingList(List<RiskSetting> requiredRiskSettingList) {
        this.requiredRiskSettingList = requiredRiskSettingList;
    }

    public List<RiskSetting> getAdditionalRiskSettingList() {
        return additionalRiskSettingList;
    }

    public void setAdditionalRiskSettingList(List<RiskSetting> additionalRiskSettingList) {
        this.additionalRiskSettingList = additionalRiskSettingList;
    }

    public List<RequiredDocumentSetting> getRequiredDocumentList() {
        return requiredDocumentList;
    }

    public void setRequiredDocumentList(List<RequiredDocumentSetting> requiredDocumentList) {
        this.requiredDocumentList = requiredDocumentList;
    }

    public Integer getMinAgeHolder() {
        return minAgeHolder;
    }

    public void setMinAgeHolder(Integer minAgeHolder) {
        this.minAgeHolder = minAgeHolder;
    }

    public Integer getMaxAgeHolder() {
        return maxAgeHolder;
    }

    public void setMaxAgeHolder(Integer maxAgeHolder) {
        this.maxAgeHolder = maxAgeHolder;
    }

    public Integer getMinAgeInsured() {
        return minAgeInsured;
    }

    public void setMinAgeInsured(Integer minAgeInsured) {
        this.minAgeInsured = minAgeInsured;
    }

    public Integer getMaxAgeInsured() {
        return maxAgeInsured;
    }

    public void setMaxAgeInsured(Integer maxAgeInsured) {
        this.maxAgeInsured = maxAgeInsured;
    }

    public Integer getMinGrowth() {
        return minGrowth;
    }

    public void setMinGrowth(Integer minGrowth) {
        this.minGrowth = minGrowth;
    }

    public Integer getMaxGrowth() {
        return maxGrowth;
    }

    public void setMaxGrowth(Integer maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public GenderTypeEnum getGender() {
        return gender;
    }

    public void setGender(GenderTypeEnum gender) {
        this.gender = gender;
    }

    public Integer getMaxUpperPressure() {
        return maxUpperPressure;
    }

    public void setMaxUpperPressure(Integer maxUpperPressure) {
        this.maxUpperPressure = maxUpperPressure;
    }

    public Integer getMinUpperPressure() {
        return minUpperPressure;
    }

    public void setMinUpperPressure(Integer minUpperPressure) {
        this.minUpperPressure = minUpperPressure;
    }

    public Integer getMaxLowerPressure() {
        return maxLowerPressure;
    }

    public void setMaxLowerPressure(Integer maxLowerPressure) {
        this.maxLowerPressure = maxLowerPressure;
    }

    public Integer getMinLowerPressure() {
        return minLowerPressure;
    }

    public void setMinLowerPressure(Integer minLowerPressure) {
        this.minLowerPressure = minLowerPressure;
    }

    public List<RequiredField> getRequiredFieldList() {
        return requiredFieldList;
    }

    public void setRequiredFieldList(List<RequiredField> requiredFieldList) {
        this.requiredFieldList = requiredFieldList;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getTariff() {
        return tariff;
    }

    public void setTariff(BigDecimal tariff) {
        this.tariff = tariff;
    }

    public BigDecimal getGuaranteeLevel() {
        return guaranteeLevel;
    }

    public void setGuaranteeLevel(BigDecimal guaranteeLevel) {
        this.guaranteeLevel = guaranteeLevel;
    }

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public Boolean getSpecialRate() {
        return specialRate;
    }

    public void setSpecialRate(Boolean specialRate) {
        this.specialRate = specialRate;
    }

    public BigDecimal getSpecialRateValue() {
        return specialRateValue;
    }

    public void setSpecialRateValue(BigDecimal specialRateValue) {
        this.specialRateValue = specialRateValue;
    }

    public Boolean getIndividualRate() {
        return individualRate;
    }

    public void setIndividualRate(Boolean individualRate) {
        this.individualRate = individualRate;
    }

    public Timestamp getStaticDate() {
        return staticDate;
    }

    public void setStaticDate(Timestamp staticData) {
        this.staticDate = staticData;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
