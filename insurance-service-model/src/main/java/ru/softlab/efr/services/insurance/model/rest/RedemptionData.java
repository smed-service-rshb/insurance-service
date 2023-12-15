package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.RedemptionCoefficientData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные записи справочника выкупных коэффициентов
 */
@ApiModel(description = "Данные записи справочника выкупных коэффициентов")
@Validated
public class RedemptionData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("currencyId")
    private Long currencyId = null;

    @JsonProperty("duration")
    private Integer duration = null;

    @JsonProperty("periodicity")
    private PaymentPeriodicity periodicity = null;

    @JsonProperty("paymentPeriod")
    private PaymentPeriodicity paymentPeriod = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("coefficientList")
    @Valid
    private List<RedemptionCoefficientData> coefficientList = new ArrayList<RedemptionCoefficientData>();


    /**
     * Создает пустой экземпляр класса
     */
    public RedemptionData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param currencyId Идентификатор валюты
     * @param duration Срок действия договора страхования (лет)
     * @param periodicity Периодичность уплаты взносов
     * @param paymentPeriod Периодичность расчета выкупных сумм
     * @param programId Идентификатор программы страхования
     * @param programName Наименование программы страхования
     * @param coefficientList 
     */
    public RedemptionData(Long id, Long currencyId, Integer duration, PaymentPeriodicity periodicity, PaymentPeriodicity paymentPeriod, Long programId, String programName, List<RedemptionCoefficientData> coefficientList) {
        this.id = id;
        this.currencyId = currencyId;
        this.duration = duration;
        this.periodicity = periodicity;
        this.paymentPeriod = paymentPeriod;
        this.programId = programId;
        this.programName = programName;
        this.coefficientList = coefficientList;
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
     * Идентификатор валюты
    * @return Идентификатор валюты
    **/
    @ApiModelProperty(required = true, value = "Идентификатор валюты")
      @NotNull



    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }


    /**
     * Срок действия договора страхования (лет)
    * @return Срок действия договора страхования (лет)
    **/
    @ApiModelProperty(required = true, value = "Срок действия договора страхования (лет)")
      @NotNull



    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    /**
     * Периодичность уплаты взносов
    * @return Периодичность уплаты взносов
    **/
    @ApiModelProperty(required = true, value = "Периодичность уплаты взносов")
      @NotNull

  @Valid


    public PaymentPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PaymentPeriodicity periodicity) {
        this.periodicity = periodicity;
    }


    /**
     * Периодичность расчета выкупных сумм
    * @return Периодичность расчета выкупных сумм
    **/
    @ApiModelProperty(required = true, value = "Периодичность расчета выкупных сумм")
      @NotNull

  @Valid


    public PaymentPeriodicity getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriodicity paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }


    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(required = true, value = "Идентификатор программы страхования")
      @NotNull



    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }


    /**
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }


    public RedemptionData addCoefficientListItem(RedemptionCoefficientData coefficientListItem) {
        this.coefficientList.add(coefficientListItem);
        return this;
    }

    /**
    * Get coefficientList
    * @return 
    **/
    @ApiModelProperty(required = true, value = "")
      @NotNull

  @Valid


    public List<RedemptionCoefficientData> getCoefficientList() {
        return coefficientList;
    }

    public void setCoefficientList(List<RedemptionCoefficientData> coefficientList) {
        this.coefficientList = coefficientList;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedemptionData redemptionData = (RedemptionData) o;
        return Objects.equals(this.id, redemptionData.id) &&
            Objects.equals(this.currencyId, redemptionData.currencyId) &&
            Objects.equals(this.duration, redemptionData.duration) &&
            Objects.equals(this.periodicity, redemptionData.periodicity) &&
            Objects.equals(this.paymentPeriod, redemptionData.paymentPeriod) &&
            Objects.equals(this.programId, redemptionData.programId) &&
            Objects.equals(this.programName, redemptionData.programName) &&
            Objects.equals(this.coefficientList, redemptionData.coefficientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyId, duration, periodicity, paymentPeriod, programId, programName, coefficientList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RedemptionData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    periodicity: ").append(toIndentedString(periodicity)).append("\n");
        sb.append("    paymentPeriod: ").append(toIndentedString(paymentPeriod)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    coefficientList: ").append(toIndentedString(coefficientList)).append("\n");
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

