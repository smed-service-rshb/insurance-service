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
 * Данные котировки акции
 */
@ApiModel(description = "Данные котировки акции")
@Validated
public class QuoteData   {
    @JsonProperty("date")
    private LocalDate date = null;

    @JsonProperty("value")
    private BigDecimal value = null;

    @JsonProperty("relativeValue")
    private BigDecimal relativeValue = null;


    /**
     * Создает пустой экземпляр класса
     */
    public QuoteData() {}

    /**
     * Создает экземпляр класса
     * @param date Дата действия котировки
     * @param value Значение котировки
     * @param relativeValue Изменение значения относительно исходного
     */
    public QuoteData(LocalDate date, BigDecimal value, BigDecimal relativeValue) {
        this.date = date;
        this.value = value;
        this.relativeValue = relativeValue;
    }

    /**
     * Дата действия котировки
    * @return Дата действия котировки
    **/
    @ApiModelProperty(required = true, value = "Дата действия котировки")
      @NotNull

  @Valid


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    /**
     * Значение котировки
    * @return Значение котировки
    **/
    @ApiModelProperty(required = true, value = "Значение котировки")
      @NotNull

  @Valid


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    /**
     * Изменение значения относительно исходного
    * @return Изменение значения относительно исходного
    **/
    @ApiModelProperty(required = true, value = "Изменение значения относительно исходного")
      @NotNull

  @Valid


    public BigDecimal getRelativeValue() {
        return relativeValue;
    }

    public void setRelativeValue(BigDecimal relativeValue) {
        this.relativeValue = relativeValue;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuoteData quoteData = (QuoteData) o;
        return Objects.equals(this.date, quoteData.date) &&
            Objects.equals(this.value, quoteData.value) &&
            Objects.equals(this.relativeValue, quoteData.relativeValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, value, relativeValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class QuoteData {\n");
        
        sb.append("    date: ").append(toIndentedString(date)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    relativeValue: ").append(toIndentedString(relativeValue)).append("\n");
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

