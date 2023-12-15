package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация по обязательному риску
 */
@ApiModel(description = "Информация по обязательному риску")
@Validated
public class RiskInfo   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("riskId")
    private Long riskId = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("underwritingRate")
    private BigDecimal underwritingRate = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("benefitsInsured")
    private Boolean benefitsInsured = null;

    @JsonProperty("otherRiskParam")
    private String otherRiskParam = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RiskInfo() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор документа в справочнике обязательных документов
     * @param riskId ID риска
     * @param amount Страховая сумма в валюте договора
     * @param premium Страховая премия в валюте договора
     * @param underwritingRate Андеррайтерский коэффициент
     * @param name Наименование риска
     * @param benefitsInsured Флаг, определяющий можно указать выгодоприобретателя
     * @param otherRiskParam Другие параметры риска
     */
    public RiskInfo(Long id, Long riskId, BigDecimal amount, BigDecimal premium, BigDecimal underwritingRate, String name, Boolean benefitsInsured, String otherRiskParam) {
        this.id = id;
        this.riskId = riskId;
        this.amount = amount;
        this.premium = premium;
        this.underwritingRate = underwritingRate;
        this.name = name;
        this.benefitsInsured = benefitsInsured;
        this.otherRiskParam = otherRiskParam;
    }

    /**
     * Идентификатор документа в справочнике обязательных документов
    * @return Идентификатор документа в справочнике обязательных документов
    **/
    @ApiModelProperty(value = "Идентификатор документа в справочнике обязательных документов")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * ID риска
    * @return ID риска
    **/
    @ApiModelProperty(required = true, value = "ID риска")
      @NotNull



    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }


    /**
     * Страховая сумма в валюте договора
    * @return Страховая сумма в валюте договора
    **/
    @ApiModelProperty(value = "Страховая сумма в валюте договора")
    
  @Valid


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    /**
     * Страховая премия в валюте договора
    * @return Страховая премия в валюте договора
    **/
    @ApiModelProperty(value = "Страховая премия в валюте договора")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Андеррайтерский коэффициент
    * @return Андеррайтерский коэффициент
    **/
    @ApiModelProperty(value = "Андеррайтерский коэффициент")
    
  @Valid


    public BigDecimal getUnderwritingRate() {
        return underwritingRate;
    }

    public void setUnderwritingRate(BigDecimal underwritingRate) {
        this.underwritingRate = underwritingRate;
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
        RiskInfo riskInfo = (RiskInfo) o;
        return Objects.equals(this.id, riskInfo.id) &&
            Objects.equals(this.riskId, riskInfo.riskId) &&
            Objects.equals(this.amount, riskInfo.amount) &&
            Objects.equals(this.premium, riskInfo.premium) &&
            Objects.equals(this.underwritingRate, riskInfo.underwritingRate) &&
            Objects.equals(this.name, riskInfo.name) &&
            Objects.equals(this.benefitsInsured, riskInfo.benefitsInsured) &&
            Objects.equals(this.otherRiskParam, riskInfo.otherRiskParam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, riskId, amount, premium, underwritingRate, name, benefitsInsured, otherRiskParam);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RiskInfo {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    riskId: ").append(toIndentedString(riskId)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    underwritingRate: ").append(toIndentedString(underwritingRate)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

