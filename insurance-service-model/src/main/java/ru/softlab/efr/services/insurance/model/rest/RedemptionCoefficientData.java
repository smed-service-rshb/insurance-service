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
 * Данные выкупного коэффициента за определенный период
 */
@ApiModel(description = "Данные выкупного коэффициента за определенный период")
@Validated
public class RedemptionCoefficientData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("period")
    private Integer period = null;

    @JsonProperty("coefficient")
    private BigDecimal coefficient = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RedemptionCoefficientData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор коэффициента
     * @param period Период расчета
     * @param coefficient Выкупной коэффициент
     */
    public RedemptionCoefficientData(Long id, Integer period, BigDecimal coefficient) {
        this.id = id;
        this.period = period;
        this.coefficient = coefficient;
    }

    /**
     * Идентификатор коэффициента
    * @return Идентификатор коэффициента
    **/
    @ApiModelProperty(value = "Идентификатор коэффициента")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Период расчета
    * @return Период расчета
    **/
    @ApiModelProperty(required = true, value = "Период расчета")
      @NotNull



    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }


    /**
     * Выкупной коэффициент
    * @return Выкупной коэффициент
    **/
    @ApiModelProperty(required = true, value = "Выкупной коэффициент")
      @NotNull

  @Valid


    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedemptionCoefficientData redemptionCoefficientData = (RedemptionCoefficientData) o;
        return Objects.equals(this.id, redemptionCoefficientData.id) &&
            Objects.equals(this.period, redemptionCoefficientData.period) &&
            Objects.equals(this.coefficient, redemptionCoefficientData.coefficient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, coefficient);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RedemptionCoefficientData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    period: ").append(toIndentedString(period)).append("\n");
        sb.append("    coefficient: ").append(toIndentedString(coefficient)).append("\n");
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

