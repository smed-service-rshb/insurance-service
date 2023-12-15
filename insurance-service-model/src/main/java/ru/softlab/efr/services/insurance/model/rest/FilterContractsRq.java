package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterContractsRq
 */
@Validated
public class FilterContractsRq   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("secondName")
    private String secondName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("contractNumber")
    private String contractNumber = null;

    @JsonProperty("status")
    private InsuranceStatusType status = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("startConclusionDate")
    private LocalDate startConclusionDate = null;

    @JsonProperty("endConclusionDate")
    private LocalDate endConclusionDate = null;

    @JsonProperty("fullSetDocument")
    private Boolean fullSetDocument = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterContractsRq() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор клиента
     * @param secondName Фамилия клиента
     * @param firstName Имя клиента
     * @param middleName Отчество клиента
     * @param contractNumber Фрагмент номера договора для фильтрации
     * @param status Статус договора страхования
     * @param programKind Вид программы страхования
     * @param programId Идентификатор программы страхования
     * @param startDate Начальная дата создания договора
     * @param endDate Конечная дата создания договора
     * @param startConclusionDate Начальная дата оформления договора
     * @param endConclusionDate Конечная дата оформления договора
     * @param fullSetDocument Признак полного комплекта документов
     */
    public FilterContractsRq(Long id, String secondName, String firstName, String middleName, String contractNumber, InsuranceStatusType status, ProgramKind programKind, Long programId, LocalDate startDate, LocalDate endDate, LocalDate startConclusionDate, LocalDate endConclusionDate, Boolean fullSetDocument) {
        this.id = id;
        this.secondName = secondName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.contractNumber = contractNumber;
        this.status = status;
        this.programKind = programKind;
        this.programId = programId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startConclusionDate = startConclusionDate;
        this.endConclusionDate = endConclusionDate;
        this.fullSetDocument = fullSetDocument;
    }

    /**
     * Идентификатор клиента
    * @return Идентификатор клиента
    **/
    @ApiModelProperty(value = "Идентификатор клиента")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Фамилия клиента
    * @return Фамилия клиента
    **/
    @ApiModelProperty(value = "Фамилия клиента")
    


    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }


    /**
     * Имя клиента
    * @return Имя клиента
    **/
    @ApiModelProperty(value = "Имя клиента")
    


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество клиента
    * @return Отчество клиента
    **/
    @ApiModelProperty(value = "Отчество клиента")
    


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Фрагмент номера договора для фильтрации
    * @return Фрагмент номера договора для фильтрации
    **/
    @ApiModelProperty(value = "Фрагмент номера договора для фильтрации")
    


    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }


    /**
     * Статус договора страхования
    * @return Статус договора страхования
    **/
    @ApiModelProperty(value = "Статус договора страхования")
    
  @Valid


    public InsuranceStatusType getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatusType status) {
        this.status = status;
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
    


    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }


    /**
     * Начальная дата создания договора
    * @return Начальная дата создания договора
    **/
    @ApiModelProperty(value = "Начальная дата создания договора")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Конечная дата создания договора
    * @return Конечная дата создания договора
    **/
    @ApiModelProperty(value = "Конечная дата создания договора")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Начальная дата оформления договора
    * @return Начальная дата оформления договора
    **/
    @ApiModelProperty(value = "Начальная дата оформления договора")
    
  @Valid


    public LocalDate getStartConclusionDate() {
        return startConclusionDate;
    }

    public void setStartConclusionDate(LocalDate startConclusionDate) {
        this.startConclusionDate = startConclusionDate;
    }


    /**
     * Конечная дата оформления договора
    * @return Конечная дата оформления договора
    **/
    @ApiModelProperty(value = "Конечная дата оформления договора")
    
  @Valid


    public LocalDate getEndConclusionDate() {
        return endConclusionDate;
    }

    public void setEndConclusionDate(LocalDate endConclusionDate) {
        this.endConclusionDate = endConclusionDate;
    }


    /**
     * Признак полного комплекта документов
    * @return Признак полного комплекта документов
    **/
    @ApiModelProperty(value = "Признак полного комплекта документов")
    


    public Boolean isFullSetDocument() {
        return fullSetDocument;
    }

    public void setFullSetDocument(Boolean fullSetDocument) {
        this.fullSetDocument = fullSetDocument;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterContractsRq filterContractsRq = (FilterContractsRq) o;
        return Objects.equals(this.id, filterContractsRq.id) &&
            Objects.equals(this.secondName, filterContractsRq.secondName) &&
            Objects.equals(this.firstName, filterContractsRq.firstName) &&
            Objects.equals(this.middleName, filterContractsRq.middleName) &&
            Objects.equals(this.contractNumber, filterContractsRq.contractNumber) &&
            Objects.equals(this.status, filterContractsRq.status) &&
            Objects.equals(this.programKind, filterContractsRq.programKind) &&
            Objects.equals(this.programId, filterContractsRq.programId) &&
            Objects.equals(this.startDate, filterContractsRq.startDate) &&
            Objects.equals(this.endDate, filterContractsRq.endDate) &&
            Objects.equals(this.startConclusionDate, filterContractsRq.startConclusionDate) &&
            Objects.equals(this.endConclusionDate, filterContractsRq.endConclusionDate) &&
            Objects.equals(this.fullSetDocument, filterContractsRq.fullSetDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, secondName, firstName, middleName, contractNumber, status, programKind, programId, startDate, endDate, startConclusionDate, endConclusionDate, fullSetDocument);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterContractsRq {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    contractNumber: ").append(toIndentedString(contractNumber)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    startConclusionDate: ").append(toIndentedString(startConclusionDate)).append("\n");
        sb.append("    endConclusionDate: ").append(toIndentedString(endConclusionDate)).append("\n");
        sb.append("    fullSetDocument: ").append(toIndentedString(fullSetDocument)).append("\n");
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

