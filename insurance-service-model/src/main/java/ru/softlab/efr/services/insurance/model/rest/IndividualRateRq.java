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
 * Запрос устанвки индивидуального курса валюты для договора
 */
@ApiModel(description = "Запрос устанвки индивидуального курса валюты для договора")
@Validated
public class IndividualRateRq   {
    @JsonProperty("rate")
    private BigDecimal rate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public IndividualRateRq() {}

    /**
     * Создает экземпляр класса
     * @param rate Индивидуальный курс для договора
     */
    public IndividualRateRq(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * Индивидуальный курс для договора
    * @return Индивидуальный курс для договора
    **/
    @ApiModelProperty(value = "Индивидуальный курс для договора")
    
  @Valid


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndividualRateRq individualRateRq = (IndividualRateRq) o;
        return Objects.equals(this.rate, individualRateRq.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IndividualRateRq {\n");
        
        sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
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

