package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.CalendarUnit;
import ru.softlab.efr.services.insurance.model.rest.FindProgramType;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Запрос поиска параметров программ страхования
 */
@ApiModel(description = "Запрос поиска параметров программ страхования")
@Validated
public class FindProgramSettingRq   {
    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("program")
    private Long program = null;

    @JsonProperty("currency")
    private Long currency = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("periodicity")
    private PaymentPeriodicity periodicity = null;

    @JsonProperty("term")
    private Integer term = null;

    @JsonProperty("calendarUnit")
    private CalendarUnit calendarUnit = null;

    @JsonProperty("type")
    private FindProgramType type = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("insuredBirthDate")
    private LocalDate insuredBirthDate = null;

    @JsonProperty("policyHolderBirthDate")
    private LocalDate policyHolderBirthDate = null;

    @JsonProperty("strategy")
    private Long strategy = null;

    @JsonProperty("programDate")
    private LocalDate programDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FindProgramSettingRq() {}

    /**
     * Создает экземпляр класса
     * @param programKind Вид программы страхования
     * @param program Идентификатор программы страхования
     * @param currency Идентификатор валюты программы страхования
     * @param amount Страховая сумма
     * @param premium Сумма премии. Заполняется
     * @param periodicity Периодичность уплаты взносов
     * @param term Срок страхования
     * @param calendarUnit Календарная единица срока страхования
     * @param type Способ расчёта по договору
     * @param gender Пол
     * @param insuredBirthDate Дата рождения застрахованного
     * @param policyHolderBirthDate Дата рождения cтрахователя
     * @param strategy Идентификатор выбранной стратегии (Только для ИСЖ)
     * @param programDate Дата на которую необходимо выбрать активные программы
     */
    public FindProgramSettingRq(ProgramKind programKind, Long program, Long currency, BigDecimal amount, BigDecimal premium, PaymentPeriodicity periodicity, Integer term, CalendarUnit calendarUnit, FindProgramType type, Gender gender, LocalDate insuredBirthDate, LocalDate policyHolderBirthDate, Long strategy, LocalDate programDate) {
        this.programKind = programKind;
        this.program = program;
        this.currency = currency;
        this.amount = amount;
        this.premium = premium;
        this.periodicity = periodicity;
        this.term = term;
        this.calendarUnit = calendarUnit;
        this.type = type;
        this.gender = gender;
        this.insuredBirthDate = insuredBirthDate;
        this.policyHolderBirthDate = policyHolderBirthDate;
        this.strategy = strategy;
        this.programDate = programDate;
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
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }


    /**
     * Идентификатор валюты программы страхования
    * @return Идентификатор валюты программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор валюты программы страхования")
    


    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }


    /**
     * Страховая сумма
    * @return Страховая сумма
    **/
    @ApiModelProperty(value = "Страховая сумма")
    
  @Valid


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    /**
     * Сумма премии. Заполняется
    * @return Сумма премии. Заполняется
    **/
    @ApiModelProperty(value = "Сумма премии. Заполняется")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Периодичность уплаты взносов
    * @return Периодичность уплаты взносов
    **/
    @ApiModelProperty(value = "Периодичность уплаты взносов")
    
  @Valid


    public PaymentPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PaymentPeriodicity periodicity) {
        this.periodicity = periodicity;
    }


    /**
     * Срок страхования
    * @return Срок страхования
    **/
    @ApiModelProperty(value = "Срок страхования")
    


    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }


    /**
     * Календарная единица срока страхования
    * @return Календарная единица срока страхования
    **/
    @ApiModelProperty(required = true, value = "Календарная единица срока страхования")
      @NotNull

  @Valid


    public CalendarUnit getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnit calendarUnit) {
        this.calendarUnit = calendarUnit;
    }


    /**
     * Способ расчёта по договору
    * @return Способ расчёта по договору
    **/
    @ApiModelProperty(value = "Способ расчёта по договору")
    
  @Valid


    public FindProgramType getType() {
        return type;
    }

    public void setType(FindProgramType type) {
        this.type = type;
    }


    /**
     * Пол
    * @return Пол
    **/
    @ApiModelProperty(value = "Пол")
    
  @Valid


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    /**
     * Дата рождения застрахованного
    * @return Дата рождения застрахованного
    **/
    @ApiModelProperty(value = "Дата рождения застрахованного")
    
  @Valid


    public LocalDate getInsuredBirthDate() {
        return insuredBirthDate;
    }

    public void setInsuredBirthDate(LocalDate insuredBirthDate) {
        this.insuredBirthDate = insuredBirthDate;
    }


    /**
     * Дата рождения cтрахователя
    * @return Дата рождения cтрахователя
    **/
    @ApiModelProperty(value = "Дата рождения cтрахователя")
    
  @Valid


    public LocalDate getPolicyHolderBirthDate() {
        return policyHolderBirthDate;
    }

    public void setPolicyHolderBirthDate(LocalDate policyHolderBirthDate) {
        this.policyHolderBirthDate = policyHolderBirthDate;
    }


    /**
     * Идентификатор выбранной стратегии (Только для ИСЖ)
    * @return Идентификатор выбранной стратегии (Только для ИСЖ)
    **/
    @ApiModelProperty(value = "Идентификатор выбранной стратегии (Только для ИСЖ)")
    


    public Long getStrategy() {
        return strategy;
    }

    public void setStrategy(Long strategy) {
        this.strategy = strategy;
    }


    /**
     * Дата на которую необходимо выбрать активные программы
    * @return Дата на которую необходимо выбрать активные программы
    **/
    @ApiModelProperty(value = "Дата на которую необходимо выбрать активные программы")
    
  @Valid


    public LocalDate getProgramDate() {
        return programDate;
    }

    public void setProgramDate(LocalDate programDate) {
        this.programDate = programDate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FindProgramSettingRq findProgramSettingRq = (FindProgramSettingRq) o;
        return Objects.equals(this.programKind, findProgramSettingRq.programKind) &&
            Objects.equals(this.program, findProgramSettingRq.program) &&
            Objects.equals(this.currency, findProgramSettingRq.currency) &&
            Objects.equals(this.amount, findProgramSettingRq.amount) &&
            Objects.equals(this.premium, findProgramSettingRq.premium) &&
            Objects.equals(this.periodicity, findProgramSettingRq.periodicity) &&
            Objects.equals(this.term, findProgramSettingRq.term) &&
            Objects.equals(this.calendarUnit, findProgramSettingRq.calendarUnit) &&
            Objects.equals(this.type, findProgramSettingRq.type) &&
            Objects.equals(this.gender, findProgramSettingRq.gender) &&
            Objects.equals(this.insuredBirthDate, findProgramSettingRq.insuredBirthDate) &&
            Objects.equals(this.policyHolderBirthDate, findProgramSettingRq.policyHolderBirthDate) &&
            Objects.equals(this.strategy, findProgramSettingRq.strategy) &&
            Objects.equals(this.programDate, findProgramSettingRq.programDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programKind, program, currency, amount, premium, periodicity, term, calendarUnit, type, gender, insuredBirthDate, policyHolderBirthDate, strategy, programDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FindProgramSettingRq {\n");
        
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    program: ").append(toIndentedString(program)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    periodicity: ").append(toIndentedString(periodicity)).append("\n");
        sb.append("    term: ").append(toIndentedString(term)).append("\n");
        sb.append("    calendarUnit: ").append(toIndentedString(calendarUnit)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    insuredBirthDate: ").append(toIndentedString(insuredBirthDate)).append("\n");
        sb.append("    policyHolderBirthDate: ").append(toIndentedString(policyHolderBirthDate)).append("\n");
        sb.append("    strategy: ").append(toIndentedString(strategy)).append("\n");
        sb.append("    programDate: ").append(toIndentedString(programDate)).append("\n");
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

