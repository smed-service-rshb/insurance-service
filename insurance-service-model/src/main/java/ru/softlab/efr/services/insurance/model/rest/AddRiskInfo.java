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
 * Информация по дополнтельному риску
 */
@ApiModel(description = "Информация по дополнтельному риску")
@Validated
public class AddRiskInfo   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("riskId")
    private Long riskId = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("name")
    private String name = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AddRiskInfo() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор
     * @param riskId ID риска
     * @param amount Страховая сумма в валюте договора
     * @param premium Страховая премия в валюте договора
     * @param name Наименование риска
     */
    public AddRiskInfo(Long id, Long riskId, BigDecimal amount, BigDecimal premium, String name) {
        this.id = id;
        this.riskId = riskId;
        this.amount = amount;
        this.premium = premium;
        this.name = name;
    }

    /**
     * Идентификатор
    * @return Идентификатор
    **/
    @ApiModelProperty(value = "Идентификатор")
    


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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddRiskInfo addRiskInfo = (AddRiskInfo) o;
        return Objects.equals(this.id, addRiskInfo.id) &&
            Objects.equals(this.riskId, addRiskInfo.riskId) &&
            Objects.equals(this.amount, addRiskInfo.amount) &&
            Objects.equals(this.premium, addRiskInfo.premium) &&
            Objects.equals(this.name, addRiskInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, riskId, amount, premium, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AddRiskInfo {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    riskId: ").append(toIndentedString(riskId)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

