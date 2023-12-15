package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.PaymentMethod;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные записи справочника рисков
 */
@ApiModel(description = "Данные записи справочника рисков")
@Validated
public class RiskData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("program")
    private ProgramKind program = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("fullName")
    private String fullName = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("benefitsInsured")
    private Boolean benefitsInsured = null;

    @JsonProperty("paymentMethod")
    private PaymentMethod paymentMethod = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RiskData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param program Вид программы страхования
     * @param name Краткое наименование риска
     * @param fullName Полное наименование риска
     * @param startDate Дата начала действия риска
     * @param endDate Дата окончания действия риска
     * @param benefitsInsured Флаг, определяющий можно указать выгодоприобретателя
     * @param paymentMethod Порядок выплаты по страховому риску
     */
    public RiskData(Long id, ProgramKind program, String name, String fullName, LocalDate startDate, LocalDate endDate, Boolean benefitsInsured, PaymentMethod paymentMethod) {
        this.id = id;
        this.program = program;
        this.name = name;
        this.fullName = fullName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.benefitsInsured = benefitsInsured;
        this.paymentMethod = paymentMethod;
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
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(required = true, value = "Вид программы страхования")
      @NotNull

  @Valid


    public ProgramKind getProgram() {
        return program;
    }

    public void setProgram(ProgramKind program) {
        this.program = program;
    }


    /**
     * Краткое наименование риска
    * @return Краткое наименование риска
    **/
    @ApiModelProperty(required = true, value = "Краткое наименование риска")
      @NotNull

 @Size(max=100)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Полное наименование риска
    * @return Полное наименование риска
    **/
    @ApiModelProperty(required = true, value = "Полное наименование риска")
      @NotNull

 @Size(max=150)

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    /**
     * Дата начала действия риска
    * @return Дата начала действия риска
    **/
    @ApiModelProperty(value = "Дата начала действия риска")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания действия риска
    * @return Дата окончания действия риска
    **/
    @ApiModelProperty(value = "Дата окончания действия риска")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Флаг, определяющий можно указать выгодоприобретателя
    * @return Флаг, определяющий можно указать выгодоприобретателя
    **/
    @ApiModelProperty(value = "Флаг, определяющий можно указать выгодоприобретателя")
    


    public Boolean isBenefitsInsured() {
        return benefitsInsured;
    }

    public void setBenefitsInsured(Boolean benefitsInsured) {
        this.benefitsInsured = benefitsInsured;
    }


    /**
     * Порядок выплаты по страховому риску
    * @return Порядок выплаты по страховому риску
    **/
    @ApiModelProperty(value = "Порядок выплаты по страховому риску")
    
  @Valid


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RiskData riskData = (RiskData) o;
        return Objects.equals(this.id, riskData.id) &&
            Objects.equals(this.program, riskData.program) &&
            Objects.equals(this.name, riskData.name) &&
            Objects.equals(this.fullName, riskData.fullName) &&
            Objects.equals(this.startDate, riskData.startDate) &&
            Objects.equals(this.endDate, riskData.endDate) &&
            Objects.equals(this.benefitsInsured, riskData.benefitsInsured) &&
            Objects.equals(this.paymentMethod, riskData.paymentMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, program, name, fullName, startDate, endDate, benefitsInsured, paymentMethod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RiskData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    program: ").append(toIndentedString(program)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    benefitsInsured: ").append(toIndentedString(benefitsInsured)).append("\n");
        sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
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

