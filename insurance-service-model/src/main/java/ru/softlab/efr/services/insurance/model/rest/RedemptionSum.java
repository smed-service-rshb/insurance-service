package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Значение выкупной суммы для конкретного периода
 */
@ApiModel(description = "Значение выкупной суммы для конкретного периода")
@Validated
public class RedemptionSum   {
    @JsonProperty("periodNumber")
    private String periodNumber = null;

    @JsonProperty("startPeriod")
    private String startPeriod = null;

    @JsonProperty("endPeriod")
    private String endPeriod = null;

    @JsonProperty("redemptionAmount")
    private String redemptionAmount = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RedemptionSum() {}

    /**
     * Создает экземпляр класса
     * @param periodNumber Номер выкупного периода
     * @param startPeriod Начало выкупного периода
     * @param endPeriod Окончание выкупного периода
     * @param redemptionAmount Размер выкупной суммы
     */
    public RedemptionSum(String periodNumber, String startPeriod, String endPeriod, String redemptionAmount) {
        this.periodNumber = periodNumber;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.redemptionAmount = redemptionAmount;
    }

    /**
     * Номер выкупного периода
    * @return Номер выкупного периода
    **/
    @ApiModelProperty(value = "Номер выкупного периода")
    


    public String getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }


    /**
     * Начало выкупного периода
    * @return Начало выкупного периода
    **/
    @ApiModelProperty(value = "Начало выкупного периода")
    


    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }


    /**
     * Окончание выкупного периода
    * @return Окончание выкупного периода
    **/
    @ApiModelProperty(value = "Окончание выкупного периода")
    


    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }


    /**
     * Размер выкупной суммы
    * @return Размер выкупной суммы
    **/
    @ApiModelProperty(value = "Размер выкупной суммы")
    


    public String getRedemptionAmount() {
        return redemptionAmount;
    }

    public void setRedemptionAmount(String redemptionAmount) {
        this.redemptionAmount = redemptionAmount;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedemptionSum redemptionSum = (RedemptionSum) o;
        return Objects.equals(this.periodNumber, redemptionSum.periodNumber) &&
            Objects.equals(this.startPeriod, redemptionSum.startPeriod) &&
            Objects.equals(this.endPeriod, redemptionSum.endPeriod) &&
            Objects.equals(this.redemptionAmount, redemptionSum.redemptionAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodNumber, startPeriod, endPeriod, redemptionAmount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RedemptionSum {\n");
        
        sb.append("    periodNumber: ").append(toIndentedString(periodNumber)).append("\n");
        sb.append("    startPeriod: ").append(toIndentedString(startPeriod)).append("\n");
        sb.append("    endPeriod: ").append(toIndentedString(endPeriod)).append("\n");
        sb.append("    redemptionAmount: ").append(toIndentedString(redemptionAmount)).append("\n");
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

