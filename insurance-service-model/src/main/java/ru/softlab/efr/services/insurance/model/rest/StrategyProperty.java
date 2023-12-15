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
 * Наборы данных стратегии - Дата экспирации, дата НЗБА и тикер -необязательные. коэффициент участия - обязательный.
 */
@ApiModel(description = "Наборы данных стратегии - Дата экспирации, дата НЗБА и тикер -необязательные. коэффициент участия - обязательный.")
@Validated
public class StrategyProperty   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("coefficient")
    private BigDecimal coefficient = null;

    @JsonProperty("ticker")
    private String ticker = null;

    @JsonProperty("expirationDate")
    private LocalDate expirationDate = null;

    @JsonProperty("nzbaDate")
    private LocalDate nzbaDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public StrategyProperty() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param coefficient Коэффициент участия или размер инвестиционного купона. Значение задается в процентах
     * @param ticker Тикер. Необязательно для заполнения. Отображается только в печатных формах
     * @param expirationDate Дата экспирации
     * @param nzbaDate Дата НЗБА (начальное значение базового актива)
     */
    public StrategyProperty(Long id, BigDecimal coefficient, String ticker, LocalDate expirationDate, LocalDate nzbaDate) {
        this.id = id;
        this.coefficient = coefficient;
        this.ticker = ticker;
        this.expirationDate = expirationDate;
        this.nzbaDate = nzbaDate;
    }

    /**
     * Идентификатор записи
    * @return Идентификатор записи
    **/
    @ApiModelProperty(value = "Идентификатор записи")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Коэффициент участия или размер инвестиционного купона. Значение задается в процентах
    * @return Коэффициент участия или размер инвестиционного купона. Значение задается в процентах
    **/
    @ApiModelProperty(required = true, value = "Коэффициент участия или размер инвестиционного купона. Значение задается в процентах")
      @NotNull

  @Valid


    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }


    /**
     * Тикер. Необязательно для заполнения. Отображается только в печатных формах
    * @return Тикер. Необязательно для заполнения. Отображается только в печатных формах
    **/
    @ApiModelProperty(value = "Тикер. Необязательно для заполнения. Отображается только в печатных формах")
    
 @Size(max=20)

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }


    /**
     * Дата экспирации
    * @return Дата экспирации
    **/
    @ApiModelProperty(value = "Дата экспирации")
    
  @Valid


    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }


    /**
     * Дата НЗБА (начальное значение базового актива)
    * @return Дата НЗБА (начальное значение базового актива)
    **/
    @ApiModelProperty(value = "Дата НЗБА (начальное значение базового актива)")
    
  @Valid


    public LocalDate getNzbaDate() {
        return nzbaDate;
    }

    public void setNzbaDate(LocalDate nzbaDate) {
        this.nzbaDate = nzbaDate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StrategyProperty strategyProperty = (StrategyProperty) o;
        return Objects.equals(this.id, strategyProperty.id) &&
            Objects.equals(this.coefficient, strategyProperty.coefficient) &&
            Objects.equals(this.ticker, strategyProperty.ticker) &&
            Objects.equals(this.expirationDate, strategyProperty.expirationDate) &&
            Objects.equals(this.nzbaDate, strategyProperty.nzbaDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coefficient, ticker, expirationDate, nzbaDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StrategyProperty {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    coefficient: ").append(toIndentedString(coefficient)).append("\n");
        sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
        sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
        sb.append("    nzbaDate: ").append(toIndentedString(nzbaDate)).append("\n");
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

