package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.CalendarUnit;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Описание программы страхования
 */
@ApiModel(description = "Описание программы страхования")
@Validated
public class ProgramSettingsDataForList   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("program")
    private String program = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("minimumTerm")
    private Integer minimumTerm = null;

    @JsonProperty("maximumTerm")
    private Integer maximumTerm = null;

    @JsonProperty("calendarUnit")
    private CalendarUnit calendarUnit = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("minSum")
    private BigDecimal minSum = null;

    @JsonProperty("maxSum")
    private BigDecimal maxSum = null;

    @JsonProperty("minPremium")
    private BigDecimal minPremium = null;

    @JsonProperty("maxPremium")
    private BigDecimal maxPremium = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ProgramSettingsDataForList() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы страхования
     * @param programKind Вид программы страхования
     * @param program Наименование программы страхования
     * @param startDate Дата начала действия программы страхования
     * @param endDate Дата окончания действия программы страхования
     * @param minimumTerm Минимальный срок страхования
     * @param maximumTerm Максимальный срок страхования
     * @param calendarUnit Календарная единица срока страхования
     * @param currency Наименование валюты программы страхования
     * @param minSum Минимальная страховая сумма по договору
     * @param maxSum Максимальная страховая сумма по договору
     * @param minPremium Минимальная страховая премия по договору
     * @param maxPremium Максимальная страховая премия по договору
     */
    public ProgramSettingsDataForList(Long id, ProgramKind programKind, String program, LocalDate startDate, LocalDate endDate, Integer minimumTerm, Integer maximumTerm, CalendarUnit calendarUnit, String currency, BigDecimal minSum, BigDecimal maxSum, BigDecimal minPremium, BigDecimal maxPremium) {
        this.id = id;
        this.programKind = programKind;
        this.program = program;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minimumTerm = minimumTerm;
        this.maximumTerm = maximumTerm;
        this.calendarUnit = calendarUnit;
        this.currency = currency;
        this.minSum = minSum;
        this.maxSum = maxSum;
        this.minPremium = minPremium;
        this.maxPremium = maxPremium;
    }

    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getProgramKind() {
        return programKind;
    }

    public void setProgramKind(ProgramKind programKind) {
        this.programKind = programKind;
    }


    /**
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }


    /**
     * Дата начала действия программы страхования
    * @return Дата начала действия программы страхования
    **/
    @ApiModelProperty(value = "Дата начала действия программы страхования")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания действия программы страхования
    * @return Дата окончания действия программы страхования
    **/
    @ApiModelProperty(value = "Дата окончания действия программы страхования")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Минимальный срок страхования
    * @return Минимальный срок страхования
    **/
    @ApiModelProperty(value = "Минимальный срок страхования")
    


    public Integer getMinimumTerm() {
        return minimumTerm;
    }

    public void setMinimumTerm(Integer minimumTerm) {
        this.minimumTerm = minimumTerm;
    }


    /**
     * Максимальный срок страхования
    * @return Максимальный срок страхования
    **/
    @ApiModelProperty(value = "Максимальный срок страхования")
    


    public Integer getMaximumTerm() {
        return maximumTerm;
    }

    public void setMaximumTerm(Integer maximumTerm) {
        this.maximumTerm = maximumTerm;
    }


    /**
     * Календарная единица срока страхования
    * @return Календарная единица срока страхования
    **/
    @ApiModelProperty(value = "Календарная единица срока страхования")
    
  @Valid


    public CalendarUnit getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnit calendarUnit) {
        this.calendarUnit = calendarUnit;
    }


    /**
     * Наименование валюты программы страхования
    * @return Наименование валюты программы страхования
    **/
    @ApiModelProperty(value = "Наименование валюты программы страхования")
    
 @Pattern(regexp="(^[A-Z]{3}$)")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    /**
     * Минимальная страховая сумма по договору
    * @return Минимальная страховая сумма по договору
    **/
    @ApiModelProperty(value = "Минимальная страховая сумма по договору")
    
  @Valid


    public BigDecimal getMinSum() {
        return minSum;
    }

    public void setMinSum(BigDecimal minSum) {
        this.minSum = minSum;
    }


    /**
     * Максимальная страховая сумма по договору
    * @return Максимальная страховая сумма по договору
    **/
    @ApiModelProperty(value = "Максимальная страховая сумма по договору")
    
  @Valid


    public BigDecimal getMaxSum() {
        return maxSum;
    }

    public void setMaxSum(BigDecimal maxSum) {
        this.maxSum = maxSum;
    }


    /**
     * Минимальная страховая премия по договору
    * @return Минимальная страховая премия по договору
    **/
    @ApiModelProperty(value = "Минимальная страховая премия по договору")
    
  @Valid


    public BigDecimal getMinPremium() {
        return minPremium;
    }

    public void setMinPremium(BigDecimal minPremium) {
        this.minPremium = minPremium;
    }


    /**
     * Максимальная страховая премия по договору
    * @return Максимальная страховая премия по договору
    **/
    @ApiModelProperty(value = "Максимальная страховая премия по договору")
    
  @Valid


    public BigDecimal getMaxPremium() {
        return maxPremium;
    }

    public void setMaxPremium(BigDecimal maxPremium) {
        this.maxPremium = maxPremium;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramSettingsDataForList programSettingsDataForList = (ProgramSettingsDataForList) o;
        return Objects.equals(this.id, programSettingsDataForList.id) &&
            Objects.equals(this.programKind, programSettingsDataForList.programKind) &&
            Objects.equals(this.program, programSettingsDataForList.program) &&
            Objects.equals(this.startDate, programSettingsDataForList.startDate) &&
            Objects.equals(this.endDate, programSettingsDataForList.endDate) &&
            Objects.equals(this.minimumTerm, programSettingsDataForList.minimumTerm) &&
            Objects.equals(this.maximumTerm, programSettingsDataForList.maximumTerm) &&
            Objects.equals(this.calendarUnit, programSettingsDataForList.calendarUnit) &&
            Objects.equals(this.currency, programSettingsDataForList.currency) &&
            Objects.equals(this.minSum, programSettingsDataForList.minSum) &&
            Objects.equals(this.maxSum, programSettingsDataForList.maxSum) &&
            Objects.equals(this.minPremium, programSettingsDataForList.minPremium) &&
            Objects.equals(this.maxPremium, programSettingsDataForList.maxPremium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, programKind, program, startDate, endDate, minimumTerm, maximumTerm, calendarUnit, currency, minSum, maxSum, minPremium, maxPremium);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProgramSettingsDataForList {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    program: ").append(toIndentedString(program)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    minimumTerm: ").append(toIndentedString(minimumTerm)).append("\n");
        sb.append("    maximumTerm: ").append(toIndentedString(maximumTerm)).append("\n");
        sb.append("    calendarUnit: ").append(toIndentedString(calendarUnit)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    minSum: ").append(toIndentedString(minSum)).append("\n");
        sb.append("    maxSum: ").append(toIndentedString(maxSum)).append("\n");
        sb.append("    minPremium: ").append(toIndentedString(minPremium)).append("\n");
        sb.append("    maxPremium: ").append(toIndentedString(maxPremium)).append("\n");
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

