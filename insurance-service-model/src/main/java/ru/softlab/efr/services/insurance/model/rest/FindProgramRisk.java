package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.MethodCalcRisk;
import ru.softlab.efr.services.insurance.model.rest.RiskRecordAmountType;
import ru.softlab.efr.services.insurance.model.rest.RiskSum;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Риск
 */
@ApiModel(description = "Риск")
@Validated
public class FindProgramRisk   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("signAmount")
    private Boolean signAmount = null;

    @JsonProperty("riskAmount")
    private BigDecimal riskAmount = null;

    @JsonProperty("riskPremium")
    private BigDecimal riskPremium = null;

    @JsonProperty("recordAmount")
    private RiskRecordAmountType recordAmount = null;

    @JsonProperty("type")
    private MethodCalcRisk type = null;

    @JsonProperty("underwritingRate")
    private BigDecimal underwritingRate = null;

    @JsonProperty("riskAmounts")
    @Valid
    private List<RiskSum> riskAmounts = null;

    @JsonProperty("benefitsInsured")
    private Boolean benefitsInsured = null;

    @JsonProperty("otherRiskParam")
    private String otherRiskParam = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FindProgramRisk() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор риска
     * @param name Наименование риска
     * @param signAmount Признак страховой суммы по договору
     * @param riskAmount Страховая сумма по риску. Заполняется, если calculationType == FIXED.
     * @param riskPremium Страховая премия по риску. Заполняется, если calculationType == FIXED.
     * @param recordAmount Учёт суммы премии, на данный момент одно значение TOTAL_PREMIUM
     * @param type Тип расчета, возможные значения, CONSTANT, DECREASING, INCREASING
     * @param underwritingRate Андеррайтинский коэффициент
     * @param riskAmounts 
     * @param benefitsInsured Флаг, определяющий можно указать выгодоприобретателя
     * @param otherRiskParam Другие параметры риска
     */
    public FindProgramRisk(Long id, String name, Boolean signAmount, BigDecimal riskAmount, BigDecimal riskPremium, RiskRecordAmountType recordAmount, MethodCalcRisk type, BigDecimal underwritingRate, List<RiskSum> riskAmounts, Boolean benefitsInsured, String otherRiskParam) {
        this.id = id;
        this.name = name;
        this.signAmount = signAmount;
        this.riskAmount = riskAmount;
        this.riskPremium = riskPremium;
        this.recordAmount = recordAmount;
        this.type = type;
        this.underwritingRate = underwritingRate;
        this.riskAmounts = riskAmounts;
        this.benefitsInsured = benefitsInsured;
        this.otherRiskParam = otherRiskParam;
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


    /**
     * Андеррайтинский коэффициент
    * @return Андеррайтинский коэффициент
    **/
    @ApiModelProperty(value = "Андеррайтинский коэффициент")
    
  @Valid


    public BigDecimal getUnderwritingRate() {
        return underwritingRate;
    }

    public void setUnderwritingRate(BigDecimal underwritingRate) {
        this.underwritingRate = underwritingRate;
    }


    public FindProgramRisk addRiskAmountsItem(RiskSum riskAmountsItem) {
        if (this.riskAmounts == null) {
            this.riskAmounts = new ArrayList<RiskSum>();
        }
        this.riskAmounts.add(riskAmountsItem);
        return this;
    }

    /**
    * Get riskAmounts
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<RiskSum> getRiskAmounts() {
        return riskAmounts;
    }

    public void setRiskAmounts(List<RiskSum> riskAmounts) {
        this.riskAmounts = riskAmounts;
    }


    /**
     * Флаг, определяющий можно указать выгодоприобретателя
    * @return Флаг, определяющий можно указать выгодоприобретателя
    **/
    @ApiModelProperty(value = "Флаг, определяющий можно указать выгодоприобретателя")
    


    public Boolean isBenefitsInsured() {
        return benefitsInsured;
    }

    public void setBenefitsInsured(Boolean benefitsInsured) {
        this.benefitsInsured = benefitsInsured;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FindProgramRisk findProgramRisk = (FindProgramRisk) o;
        return Objects.equals(this.id, findProgramRisk.id) &&
            Objects.equals(this.name, findProgramRisk.name) &&
            Objects.equals(this.signAmount, findProgramRisk.signAmount) &&
            Objects.equals(this.riskAmount, findProgramRisk.riskAmount) &&
            Objects.equals(this.riskPremium, findProgramRisk.riskPremium) &&
            Objects.equals(this.recordAmount, findProgramRisk.recordAmount) &&
            Objects.equals(this.type, findProgramRisk.type) &&
            Objects.equals(this.underwritingRate, findProgramRisk.underwritingRate) &&
            Objects.equals(this.riskAmounts, findProgramRisk.riskAmounts) &&
            Objects.equals(this.benefitsInsured, findProgramRisk.benefitsInsured) &&
            Objects.equals(this.otherRiskParam, findProgramRisk.otherRiskParam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, signAmount, riskAmount, riskPremium, recordAmount, type, underwritingRate, riskAmounts, benefitsInsured, otherRiskParam);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FindProgramRisk {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    signAmount: ").append(toIndentedString(signAmount)).append("\n");
        sb.append("    riskAmount: ").append(toIndentedString(riskAmount)).append("\n");
        sb.append("    riskPremium: ").append(toIndentedString(riskPremium)).append("\n");
        sb.append("    recordAmount: ").append(toIndentedString(recordAmount)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    underwritingRate: ").append(toIndentedString(underwritingRate)).append("\n");
        sb.append("    riskAmounts: ").append(toIndentedString(riskAmounts)).append("\n");
        sb.append("    benefitsInsured: ").append(toIndentedString(benefitsInsured)).append("\n");
        sb.append("    otherRiskParam: ").append(toIndentedString(otherRiskParam)).append("\n");
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

