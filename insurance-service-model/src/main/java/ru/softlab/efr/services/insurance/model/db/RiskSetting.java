package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.softlab.efr.services.insurance.model.enums.RiskCalculationSumTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.RiskCalculationTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.RiskRecordAmountTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.RiskTypeEnum;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Сущность для хранения данных параметров риска
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "risk_setting")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RiskSetting implements BaseEntity {

    /**
     * Идентификатор параметра риска
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Вид риска, возможные значения:
     * Обязательный
     * Дополнительный
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RiskTypeEnum riskType;

    /**
     * Краткое наименование риска, Ссылка
     * Ссылка на справочник рисков (п.2.6.3).
     * Обязательно для заполнения
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "risk", nullable = true)
    private Risk risk;

    /**
     * Признак СС по договору
     * Если для риска установлен данный признак, то страховая сумма по данному риску
     * используется в качестве страховой суммы по договору.
     * Признак должен быть установлен только для одного риска из списка рисков.
     */
    @Column
    private Boolean signAmount;

    /**
     * Минимальная страховая сумма по риску
     * Поле «Минимальная страховая сумма по риску» и «Максимальная страховая сумма по риску» необходимы для ограничения страховой суммы по риску.
     * •	Если оба значения не заданы, то ограничения на сумму страхового риска не накладывается.
     * •	Если задано одно из полей или оба поля, то величина суммы должна устанавливаться в заданных рамках.
     * Описание п.2.6.6.5
     */
    @Column
    private BigDecimal minRiskAmount;

    /**
     * Максимальная страховая сумма по риску
     * Поле «Минимальная страховая сумма по риску» и «Максимальная страховая сумма по риску» необходимы для ограничения страховой суммы по риску.
     * •	Если оба значения не заданы, то ограничения на сумму страхового риска не накладывается.
     * •	Если задано одно из полей или оба поля, то величина суммы должна устанавливаться в заданных рамках.
     * Описание п.2.6.6.5
     */
    @Column
    private BigDecimal maxRiskAmount;

    /**
     * Тип расчета страховой суммы и страховой премии для риска
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RiskCalculationTypeEnum calculationType;

    /**
     * Страховая сумма по риску
     * Заполняется, если «Тип расчета страховой суммы» = «Фиксированная страховая сумма»
     */
    @Column
    private BigDecimal riskAmount;

    /**
     * Зависимость от риска
     * Заполняется, если «Тип расчета страховой суммы» = «Зависит от СС другого риска».
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "riskDependence", nullable = true)
    private RiskSetting riskDependence;

    /**
     * Коэффициент для расчета СС
     * 2 варианта заполнения:
     * <p>
     * 1. Заполняется, если «Тип расчета страховой суммы» = «Зависит от СС другого риска».
     * При этом, указывается коэффициент, на который необходимо умножить страховую сумму для расчета СС по текущему риску
     * <p>
     * 2. Заполняется, если «Тип расчета страховой суммы» = «Зависит от страховой премии»
     * При этом, указывается коэффициент, на который необходимо умножить страховую премию.
     */
    @Column
    private BigDecimal calculationCoefficient;

    /**
     * Страховая премия по риску
     * Заполняется, если «Тип расчета страховой премии для риска» = «Фиксированная страховая премия»
     * Необязательно для установки
     */
    @Column
    private BigDecimal riskPremium;

    /**
     * Коэффициент для расчета премии
     * Заполняется, если «Тип расчета страховой премии для риска» = «СС, умноженная на коэффициент»
     * Необязательно для установки
     */
    @Column
    private BigDecimal calculationCoefficientPremium;

    /**
     * Родительский риск
     * Риск, при включении которого в договор, возможно включение в договор текущего риска
     * Заполняется, если «Тип риска» = «допонительный риск»
     * Необязательно для установки
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "parentRisk", nullable = true)
    private RiskSetting parentRisk;

    /**
     * Указывается в процентах. Число должно быть больше нуля.
     * Обязательно для заполнения
     */
    @Column
    private BigDecimal riskReturnRate;

    /**
     * Другие параметры риска
     * Не более 200 символов.
     * Не обязательно для заполнения.
     */
    @Column
    private String otherRiskParam;

    /**
     * Наименование Правил страхования
     * Не более 200 символов.
     * Не обязательно для заполнения.
     */
    @Column
    private String insuranceRule;

    /**
     * Реквизиты правил страхования
     * Не более 200 символов.
     * Не обязательно для заполнения.
     */
    @Column
    private String rulesDetails;

    /**
     * Формула, по которой производится расчёт
     * Заполняется, если «Тип расчета страховой премии для риска» = «По формуле»
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "formula", nullable = true)
    private Formula formula;

    /**
     * Учет суммы премии. Должно быть задано значения: «от полной премии». Значение не может изменяться.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RiskRecordAmountTypeEnum recordAmount;

    /**
     * «Тип расчета». Выбор из значений: "постоянная", "убывающая", "возрастающая".
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RiskCalculationSumTypeEnum type;

    /**
     * Вид программы страхования
     */
    @Column
    private String insuranceKind;

    /**
     * Признак удалённого риска программы страхования
     */
    @Column
    private Boolean deleted;

    /**
     * Порядок сортировки риска в печатной форме
     */
    @Column
    private BigDecimal sortPriority;

    public RiskSetting() {
    }

    public RiskSetting(Long version, RiskTypeEnum riskType, Risk risk, Boolean signAmount, BigDecimal minRiskAmount, BigDecimal maxRiskAmount, RiskCalculationTypeEnum calculationType, BigDecimal riskAmount, RiskSetting riskDependence, BigDecimal calculationCoefficient, BigDecimal riskPremium, BigDecimal calculationCoefficientPremium, RiskSetting parentRisk, BigDecimal riskReturnRate, String otherRiskParam, String insuranceRule, String rulesDetails, Formula formula, RiskRecordAmountTypeEnum recordAmount, RiskCalculationSumTypeEnum type, String insuranceKind, Boolean deleted, BigDecimal sortPriority) {
        this.version = version;
        this.riskType = riskType;
        this.risk = risk;
        this.signAmount = signAmount;
        this.minRiskAmount = minRiskAmount;
        this.maxRiskAmount = maxRiskAmount;
        this.calculationType = calculationType;
        this.riskAmount = riskAmount;
        this.riskDependence = riskDependence;
        this.calculationCoefficient = calculationCoefficient;
        this.riskPremium = riskPremium;
        this.calculationCoefficientPremium = calculationCoefficientPremium;
        this.parentRisk = parentRisk;
        this.riskReturnRate = riskReturnRate;
        this.otherRiskParam = otherRiskParam;
        this.insuranceRule = insuranceRule;
        this.rulesDetails = rulesDetails;
        this.formula = formula;
        this.recordAmount = recordAmount;
        this.type = type;
        this.insuranceKind = insuranceKind;
        this.deleted = deleted;
        this.sortPriority = sortPriority;
    }

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

    public RiskTypeEnum getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskTypeEnum riskType) {
        this.riskType = riskType;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public Boolean getSignAmount() {
        return signAmount;
    }

    public void setSignAmount(Boolean signAmount) {
        this.signAmount = signAmount;
    }

    public BigDecimal getMinRiskAmount() {
        return minRiskAmount;
    }

    public void setMinRiskAmount(BigDecimal minRiskAmount) {
        this.minRiskAmount = minRiskAmount;
    }

    public BigDecimal getMaxRiskAmount() {
        return maxRiskAmount;
    }

    public void setMaxRiskAmount(BigDecimal maxRiskAmount) {
        this.maxRiskAmount = maxRiskAmount;
    }

    public RiskCalculationTypeEnum getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(RiskCalculationTypeEnum calculationType) {
        this.calculationType = calculationType;
    }

    public BigDecimal getRiskAmount() {
        return riskAmount;
    }

    public void setRiskAmount(BigDecimal riskAmount) {
        this.riskAmount = riskAmount;
    }

    public RiskSetting getRiskDependence() {
        return riskDependence;
    }

    public void setRiskDependence(RiskSetting riskDependence) {
        this.riskDependence = riskDependence;
    }

    public BigDecimal getCalculationCoefficient() {
        return calculationCoefficient;
    }

    public void setCalculationCoefficient(BigDecimal calculationCoefficient) {
        this.calculationCoefficient = calculationCoefficient;
    }

    public BigDecimal getCalculationCoefficientPremium() {
        return calculationCoefficientPremium;
    }

    public void setCalculationCoefficientPremium(BigDecimal calculationCoefficientPremium) {
        this.calculationCoefficientPremium = calculationCoefficientPremium;
    }

    public RiskSetting getParentRisk() {
        return parentRisk;
    }

    public void setParentRisk(RiskSetting parentRisk) {
        this.parentRisk = parentRisk;
    }

    public BigDecimal getRiskReturnRate() {
        return riskReturnRate;
    }

    public void setRiskReturnRate(BigDecimal riskReturnRate) {
        this.riskReturnRate = riskReturnRate;
    }

    public String getOtherRiskParam() {
        return otherRiskParam;
    }

    public void setOtherRiskParam(String otherRiskParam) {
        this.otherRiskParam = otherRiskParam;
    }

    public String getInsuranceRule() {
        return insuranceRule;
    }

    public void setInsuranceRule(String insuranceRule) {
        this.insuranceRule = insuranceRule;
    }

    public String getRulesDetails() {
        return rulesDetails;
    }

    public void setRulesDetails(String rulesDetails) {
        this.rulesDetails = rulesDetails;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public BigDecimal getRiskPremium() {
        return riskPremium;
    }

    public void setRiskPremium(BigDecimal riskPremium) {
        this.riskPremium = riskPremium;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }

    public RiskRecordAmountTypeEnum getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(RiskRecordAmountTypeEnum recordAmount) {
        this.recordAmount = recordAmount;
    }

    public RiskCalculationSumTypeEnum getType() {
        return type;
    }

    public void setType(RiskCalculationSumTypeEnum type) {
        this.type = type;
    }

    public String getInsuranceKind() {
        return insuranceKind;
    }

    public void setInsuranceKind(String insuranceKind) {
        this.insuranceKind = insuranceKind;
    }

    public BigDecimal getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(BigDecimal sortPriority) {
        this.sortPriority = sortPriority;
    }

}
