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
 * RiskSum
 */
@Validated
public class RiskSum   {
    @JsonProperty("periodNumber")
    private Integer periodNumber = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RiskSum() {}

    /**
     * Создает экземпляр класса
     * @param periodNumber Порядковый номер периода рассчёта
     * @param amount Сумма
     */
    public RiskSum(Integer periodNumber, BigDecimal amount) {
        this.periodNumber = periodNumber;
        this.amount = amount;
    }

    /**
     * Порядковый номер периода рассчёта
    * @return Порядковый номер периода рассчёта
    **/
    @ApiModelProperty(required = true, value = "Порядковый номер периода рассчёта")
      @NotNull



    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }


    /**
     * Сумма
    * @return Сумма
    **/
    @ApiModelProperty(required = true, value = "Сумма")
      @NotNull

  @Valid


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RiskSum riskSum = (RiskSum) o;
        return Objects.equals(this.periodNumber, riskSum.periodNumber) &&
            Objects.equals(this.amount, riskSum.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodNumber, amount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RiskSum {\n");
        
        sb.append("    periodNumber: ").append(toIndentedString(periodNumber)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

