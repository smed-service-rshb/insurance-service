package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import ru.softlab.efr.services.insurance.model.rest.MethodCalcRisk;
import ru.softlab.efr.services.insurance.model.rest.RiskCalculationType;
import ru.softlab.efr.services.insurance.model.rest.RiskRecordAmountType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Риск
 */
@ApiModel(description = "Риск")
@Validated
public class Risk   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("signAmount")
    private Boolean signAmount = null;

    @JsonProperty("minRiskAmount")
    private BigDecimal minRiskAmount = null;

    @JsonProperty("maxRiskAmount")
    private BigDecimal maxRiskAmount = null;

    @JsonProperty("calculationType")
    private RiskCalculationType calculationType = null;

    @JsonProperty("riskAmount")
    private BigDecimal riskAmount = null;

    @JsonProperty("riskDependence")
    private Long riskDependence = null;

    @JsonProperty("calculationCoefficient")
    private BigDecimal calculationCoefficient = null;

    @JsonProperty("riskPremium")
    private BigDecimal riskPremium = null;

    @JsonProperty("calculationCoefficientPremium")
    private BigDecimal calculationCoefficientPremium = null;

    @JsonProperty("riskReturnRate")
    private BigDecimal riskReturnRate = null;

    @JsonProperty("otherRiskParam")
    private String otherRiskParam = null;

    @JsonProperty("rulesDetails")
    private String rulesDetails = null;

    @JsonProperty("insuranceRule")
    private String insuranceRule = null;

    @JsonProperty("insuranceKind")
    private String insuranceKind = null;

    @JsonProperty("sortPriority")
    private BigDecimal sortPriority = null;

    @JsonProperty("recordAmount")
    private RiskRecordAmountType recordAmount = null;

    @JsonProperty("type")
    private MethodCalcRisk type = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Risk() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор риска
     * @param name Наименование риска
     * @param signAmount Признак страховой суммы по договору
     * @param minRiskAmount Минимальная страховая сумма по риску
     * @param maxRiskAmount Максимальная страховая сумма по риску
     * @param calculationType Тип расчета страховой суммы и страховой премии для риска
     * @param riskAmount Страховая сумма по риску. Заполняется, если calculationType == FIXED.
     * @param riskDependence Идентификатор риска, от которого зависит данный риск. Заполняется, если calculationType == DEPENDS_ON_RISK.
     * @param calculationCoefficient Коэффициент для расчета страховой суммы. Заполняется, если calculationType == DEPENDS_ON_RISK или calculationType == DEPENDS_ON_PREMIUM.
     * @param riskPremium Страховая премия по риску. Заполняется, если calculationType == FIXED.
     * @param calculationCoefficientPremium Коэффициент для расчета премии. Заполняется, если methodCalcPremium == MULTIPLIED.
     * @param riskReturnRate Норма доходности риска
     * @param otherRiskParam Другие параметры риска
     * @param rulesDetails Реквизиты правил страхования
     * @param insuranceRule Наименование Правил страхования
     * @param insuranceKind Вид программы страхования
     * @param sortPriority Признак порядка сортировки в печатной форме
     * @param recordAmount Учёт суммы премии, на данный момент одно значение TOTAL_PREMIUM
     * @param type Тип расчета, возможные значения, CONSTANT, DECREASING, INCREASING
     */
    public Risk(Long id, String name, Boolean signAmount, BigDecimal minRiskAmount, BigDecimal maxRiskAmount, RiskCalculationType calculationType, BigDecimal riskAmount, Long riskDependence, BigDecimal calculationCoefficient, BigDecimal riskPremium, BigDecimal calculationCoefficientPremium, BigDecimal riskReturnRate, String otherRiskParam, String rulesDetails, String insuranceRule, String insuranceKind, BigDecimal sortPriority, RiskRecordAmountType recordAmount, MethodCalcRisk type) {
        this.id = id;
        this.name = name;
        this.signAmount = signAmount;
        this.minRiskAmount = minRiskAmount;
        this.maxRiskAmount = maxRiskAmount;
        this.calculationType = calculationType;
        this.riskAmount = riskAmount;
        this.riskDependence = riskDependence;
        this.calculationCoefficient = calculationCoefficient;
        this.riskPremium = riskPremium;
        this.calculationCoefficientPremium = calculationCoefficientPremium;
        this.riskReturnRate = riskReturnRate;
        this.otherRiskParam = otherRiskParam;
        this.rulesDetails = rulesDetails;
        this.insuranceRule = insuranceRule;
        this.insuranceKind = insuranceKind;
        this.sortPriority = sortPriority;
        this.recordAmount = recordAmount;
        this.type = type;
    }

    /**
     * Идентификатор риска
    * @return Идентификатор риска
    **/
    @ApiModelProperty(value = "Идентификатор риска")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование риска
    * @return Наименование риска
    **/
    @ApiModelProperty(value = "Наименование риска")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Признак страховой суммы по договору
    * @return Признак страховой суммы по договору
    **/
    @ApiModelProperty(value = "Признак страховой суммы по договору")
    


    public Boolean isSignAmount() {
        return signAmount;
    }

    public void setSignAmount(Boolean signAmount) {
        this.signAmount = signAmount;
    }


    /**
     * Минимальная страховая сумма по риску
    * @return Минимальная страховая сумма по риску
    **/
    @ApiModelProperty(value = "Минимальная страховая сумма по риску")
    
  @Valid


    public BigDecimal getMinRiskAmount() {
        return minRiskAmount;
    }

    public void setMinRiskAmount(BigDecimal minRiskAmount) {
        this.minRiskAmount = minRiskAmount;
    }


    /**
     * Максимальная страховая сумма по риску
    * @return Максимальная страховая сумма по риску
    **/
    @ApiModelProperty(value = "Максимальная страховая сумма по риску")
    
  @Valid


    public BigDecimal getMaxRiskAmount() {
        return maxRiskAmount;
    }

    public void setMaxRiskAmount(BigDecimal maxRiskAmount) {
        this.maxRiskAmount = maxRiskAmount;
    }


    /**
     * Тип расчета страховой суммы и страховой премии для риска
    * @return Тип расчета страховой суммы и страховой премии для риска
    **/
    @ApiModelProperty(value = "Тип расчета страховой суммы и страховой премии для риска")
    
  @Valid


    public RiskCalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(RiskCalculationType calculationType) {
        this.calculationType = calculationType;
    }


    /**
     * Страховая сумма по риску. Заполняется, если calculationType == FIXED.
    * @return Страховая сумма по риску. Заполняется, если calculationType == FIXED.
    **/
    @ApiModelProperty(value = "Страховая сумма по риску. Заполняется, если calculationType == FIXED.")
    
  @Valid


    public BigDecimal getRiskAmount() {
        return riskAmount;
    }

    public void setRiskAmount(BigDecimal riskAmount) {
        this.riskAmount = riskAmount;
    }


    /**
     * Идентификатор риска, от которого зависит данный риск. Заполняется, если calculationType == DEPENDS_ON_RISK.
    * @return Идентификатор риска, от которого зависит данный риск. Заполняется, если calculationType == DEPENDS_ON_RISK.
    **/
    @ApiModelProperty(value = "Идентификатор риска, от которого зависит данный риск. Заполняется, если calculationType == DEPENDS_ON_RISK.")
    


    public Long getRiskDependence() {
        return riskDependence;
    }

    public void setRiskDependence(Long riskDependence) {
        this.riskDependence = riskDependence;
    }


    /**
     * Коэффициент для расчета страховой суммы. Заполняется, если calculationType == DEPENDS_ON_RISK или calculationType == DEPENDS_ON_PREMIUM.
    * @return Коэффициент для расчета страховой суммы. Заполняется, если calculationType == DEPENDS_ON_RISK или calculationType == DEPENDS_ON_PREMIUM.
    **/
    @ApiModelProperty(value = "Коэффициент для расчета страховой суммы. Заполняется, если calculationType == DEPENDS_ON_RISK или calculationType == DEPENDS_ON_PREMIUM.")
    
  @Valid


    public BigDecimal getCalculationCoefficient() {
        return calculationCoefficient;
    }

    public void setCalculationCoefficient(BigDecimal calculationCoefficient) {
        this.calculationCoefficient = calculationCoefficient;
    }


    /**
     * Страховая премия по риску. Заполняется, если calculationType == FIXED.
    * @return Страховая премия по риску. Заполняется, если calculationType == FIXED.
    **/
    @ApiModelProperty(value = "Страховая премия по риску. Заполняется, если calculationType == FIXED.")
    
  @Valid


    public BigDecimal getRiskPremium() {
        return riskPremium;
    }

    public void setRiskPremium(BigDecimal riskPremium) {
        this.riskPremium = riskPremium;
    }


    /**
     * Коэффициент для расчета премии. Заполняется, если methodCalcPremium == MULTIPLIED.
    * @return Коэффициент для расчета премии. Заполняется, если methodCalcPremium == MULTIPLIED.
    **/
    @ApiModelProperty(value = "Коэффициент для расчета премии. Заполняется, если methodCalcPremium == MULTIPLIED.")
    
  @Valid


    public BigDecimal getCalculationCoefficientPremium() {
        return calculationCoefficientPremium;
    }

    public void setCalculationCoefficientPremium(BigDecimal calculationCoefficientPremium) {
        this.calculationCoefficientPremium = calculationCoefficientPremium;
    }


    /**
     * Норма доходности риска
    * @return Норма доходности риска
    **/
    @ApiModelProperty(value = "Норма доходности риска")
    
  @Valid


    public BigDecimal getRiskReturnRate() {
        return riskReturnRate;
    }

    public void setRiskReturnRate(BigDecimal riskReturnRate) {
        this.riskReturnRate = riskReturnRate;
    }


    /**
     * Другие параметры риска
    * @return Другие параметры риска
    **/
    @ApiModelProperty(value = "Другие параметры риска")
    


    public String getOtherRiskParam() {
        return otherRiskParam;
    }

    public void setOtherRiskParam(String otherRiskParam) {
        this.otherRiskParam = otherRiskParam;
    }


    /**
     * Реквизиты правил страхования
    * @return Реквизиты правил страхования
    **/
    @ApiModelProperty(value = "Реквизиты правил страхования")
    


    public String getRulesDetails() {
        return rulesDetails;
    }

    public void setRulesDetails(String rulesDetails) {
        this.rulesDetails = rulesDetails;
    }


    /**
     * Наименование Правил страхования
    * @return Наименование Правил страхования
    **/
    @ApiModelProperty(value = "Наименование Правил страхования")
    


    public String getInsuranceRule() {
        return insuranceRule;
    }

    public void setInsuranceRule(String insuranceRule) {
        this.insuranceRule = insuranceRule;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    


    public String getInsuranceKind() {
        return insuranceKind;
    }

    public void setInsuranceKind(String insuranceKind) {
        this.insuranceKind = insuranceKind;
    }


    /**
     * Признак порядка сортировки в печатной форме
    * @return Признак порядка сортировки в печатной форме
    **/
    @ApiModelProperty(value = "Признак порядка сортировки в печатной форме")
    
  @Valid


    public BigDecimal getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(BigDecimal sortPriority) {
        this.sortPriority = sortPriority;
    }


    /**
     * Учёт суммы премии, на данный момент одно значение TOTAL_PREMIUM
    * @return Учёт суммы премии, на данный момент одно значение TOTAL_PREMIUM
    **/
    @ApiModelProperty(value = "Учёт суммы премии, на данный момент одно значение TOTAL_PREMIUM")
    
  @Valid


    public RiskRecordAmountType getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(RiskRecordAmountType recordAmount) {
        this.recordAmount = recordAmount;
    }


    /**
     * Тип расчета, возможные значения, CONSTANT, DECREASING, INCREASING
    * @return Тип расчета, возможные значения, CONSTANT, DECREASING, INCREASING
    **/
    @ApiModelProperty(value = "Тип расчета, возможные значения, CONSTANT, DECREASING, INCREASING")
    
  @Valid


    public MethodCalcRisk getType() {
        return type;
    }

    public void setType(MethodCalcRisk type) {
        this.type = type;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Risk risk = (Risk) o;
        return Objects.equals(this.id, risk.id) &&
            Objects.equals(this.name, risk.name) &&
            Objects.equals(this.signAmount, risk.signAmount) &&
            Objects.equals(this.minRiskAmount, risk.minRiskAmount) &&
            Objects.equals(this.maxRiskAmount, risk.maxRiskAmount) &&
            Objects.equals(this.calculationType, risk.calculationType) &&
            Objects.equals(this.riskAmount, risk.riskAmount) &&
            Objects.equals(this.riskDependence, risk.riskDependence) &&
            Objects.equals(this.calculationCoefficient, risk.calculationCoefficient) &&
            Objects.equals(this.riskPremium, risk.riskPremium) &&
            Objects.equals(this.calculationCoefficientPremium, risk.calculationCoefficientPremium) &&
            Objects.equals(this.riskReturnRate, risk.riskReturnRate) &&
            Objects.equals(this.otherRiskParam, risk.otherRiskParam) &&
            Objects.equals(this.rulesDetails, risk.rulesDetails) &&
            Objects.equals(this.insuranceRule, risk.insuranceRule) &&
            Objects.equals(this.insuranceKind, risk.insuranceKind) &&
            Objects.equals(this.sortPriority, risk.sortPriority) &&
            Objects.equals(this.recordAmount, risk.recordAmount) &&
            Objects.equals(this.type, risk.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, signAmount, minRiskAmount, maxRiskAmount, calculationType, riskAmount, riskDependence, calculationCoefficient, riskPremium, calculationCoefficientPremium, riskReturnRate, otherRiskParam, rulesDetails, insuranceRule, insuranceKind, sortPriority, recordAmount, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Risk {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    signAmount: ").append(toIndentedString(signAmount)).append("\n");
        sb.append("    minRiskAmount: ").append(toIndentedString(minRiskAmount)).append("\n");
        sb.append("    maxRiskAmount: ").append(toIndentedString(maxRiskAmount)).append("\n");
        sb.append("    calculationType: ").append(toIndentedString(calculationType)).append("\n");
        sb.append("    riskAmount: ").append(toIndentedString(riskAmount)).append("\n");
        sb.append("    riskDependence: ").append(toIndentedString(riskDependence)).append("\n");
        sb.append("    calculationCoefficient: ").append(toIndentedString(calculationCoefficient)).append("\n");
        sb.append("    riskPremium: ").append(toIndentedString(riskPremium)).append("\n");
        sb.append("    calculationCoefficientPremium: ").append(toIndentedString(calculationCoefficientPremium)).append("\n");
        sb.append("    riskReturnRate: ").append(toIndentedString(riskReturnRate)).append("\n");
        sb.append("    otherRiskParam: ").append(toIndentedString(otherRiskParam)).append("\n");
        sb.append("    rulesDetails: ").append(toIndentedString(rulesDetails)).append("\n");
        sb.append("    insuranceRule: ").append(toIndentedString(insuranceRule)).append("\n");
        sb.append("    insuranceKind: ").append(toIndentedString(insuranceKind)).append("\n");
        sb.append("    sortPriority: ").append(toIndentedString(sortPriority)).append("\n");
        sb.append("    recordAmount: ").append(toIndentedString(recordAmount)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

