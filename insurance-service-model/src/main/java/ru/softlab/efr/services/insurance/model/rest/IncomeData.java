package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные дохода за определенную дату
 */
@ApiModel(description = "Данные дохода за определенную дату")
@Validated
public class IncomeData   {
    @JsonProperty("date")
    private LocalDate date = null;

    @JsonProperty("value")
    private BigDecimal value = null;

    @JsonProperty("relativeValue")
    private BigDecimal relativeValue = null;

    @JsonProperty("baseIndex")
    private BigDecimal baseIndex = null;


    /**
     * Создает пустой экземпляр класса
     */
    public IncomeData() {}

    /**
     * Создает экземпляр класса
     * @param date Дата, на которую расчитан доход
     * @param value Зачение инвестиционного дохода
     * @param relativeValue Значение базового актива
     * @param baseIndex Значение базового индекса
     */
    public IncomeData(LocalDate date, BigDecimal value, BigDecimal relativeValue, BigDecimal baseIndex) {
        this.date = date;
        this.value = value;
        this.relativeValue = relativeValue;
        this.baseIndex = baseIndex;
    }

    /**
     * Дата, на которую расчитан доход
    * @return Дата, на которую расчитан доход
    **/
    @ApiModelProperty(required = true, value = "Дата, на которую расчитан доход")
      @NotNull

  @Valid


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    /**
     * Зачение инвестиционного дохода
    * @return Зачение инвестиционного дохода
    **/
    @ApiModelProperty(required = true, value = "Зачение инвестиционного дохода")
      @NotNull

  @Valid


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    /**
     * Значение базового актива
    * @return Значение базового актива
    **/
    @ApiModelProperty(value = "Значение базового актива")
    
  @Valid


    public BigDecimal getRelativeValue() {
        return relativeValue;
    }

    public void setRelativeValue(BigDecimal relativeValue) {
        this.relativeValue = relativeValue;
    }


    /**
     * Значение базового индекса
    * @return Значение базового индекса
    **/
    @ApiModelProperty(value = "Значение базового индекса")
    
  @Valid


    public BigDecimal getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(BigDecimal baseIndex) {
        this.baseIndex = baseIndex;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IncomeData incomeData = (IncomeData) o;
        return Objects.equals(this.date, incomeData.date) &&
            Objects.equals(this.value, incomeData.value) &&
            Objects.equals(this.relativeValue, incomeData.relativeValue) &&
            Objects.equals(this.baseIndex, incomeData.baseIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, value, relativeValue, baseIndex);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IncomeData {\n");
        
        sb.append("    date: ").append(toIndentedString(date)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    relativeValue: ").append(toIndentedString(relativeValue)).append("\n");
        sb.append("    baseIndex: ").append(toIndentedString(baseIndex)).append("\n");
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

