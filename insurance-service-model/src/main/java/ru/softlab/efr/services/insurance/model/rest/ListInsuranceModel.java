package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.ShortClientData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация договорам для отображения в списке
 */
@ApiModel(description = "Информация договорам для отображения в списке")
@Validated
public class ListInsuranceModel   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("contractNumber")
    private String contractNumber = null;

    @JsonProperty("creationDate")
    private LocalDate creationDate = null;

    @JsonProperty("duration")
    private Integer duration = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("conclusionDate")
    private LocalDate conclusionDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("closeDate")
    private LocalDate closeDate = null;

    @JsonProperty("clientData")
    private ShortClientData clientData = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("rurPremium")
    private BigDecimal rurPremium = null;

    @JsonProperty("branchId")
    private Long branchId = null;

    @JsonProperty("branchName")
    private String branchName = null;

    @JsonProperty("subdivisionId")
    private Long subdivisionId = null;

    @JsonProperty("subdivisionName")
    private String subdivisionName = null;

    @JsonProperty("status")
    private InsuranceStatusType status = null;

    @JsonProperty("employeeId")
    private Long employeeId = null;

    @JsonProperty("employeeName")
    private String employeeName = null;

    @JsonProperty("programName")
    private String programName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ListInsuranceModel() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param contractNumber Номер договора
     * @param creationDate Дата создания договора
     * @param duration Срок договора
     * @param startDate Дата начала действия договора
     * @param conclusionDate Дата оформления договора
     * @param endDate Дата окончания договора
     * @param closeDate Дата закрытия договора
     * @param clientData Данные клиента
     * @param kind Тип договора
     * @param currency Наименование валюты договора страхования
     * @param premium Страховая премия в валюте договора
     * @param rurPremium Страховая премия в национальной валюте
     * @param branchId ID филиала
     * @param branchName Наименование филиала
     * @param subdivisionId ID ВСП оформления договора
     * @param subdivisionName Наименование ВСП оформления договора
     * @param status Актуальный статус договора
     * @param employeeId ID пользователя создавшего договор
     * @param employeeName Наименование пользователя создавшего договоро
     * @param programName Наименование программы страхования
     */
    public ListInsuranceModel(Long id, String contractNumber, LocalDate creationDate, Integer duration, LocalDate startDate, LocalDate conclusionDate, LocalDate endDate, LocalDate closeDate, ShortClientData clientData, ProgramKind kind, String currency, BigDecimal premium, BigDecimal rurPremium, Long branchId, String branchName, Long subdivisionId, String subdivisionName, InsuranceStatusType status, Long employeeId, String employeeName, String programName) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.creationDate = creationDate;
        this.duration = duration;
        this.startDate = startDate;
        this.conclusionDate = conclusionDate;
        this.endDate = endDate;
        this.closeDate = closeDate;
        this.clientData = clientData;
        this.kind = kind;
        this.currency = currency;
        this.premium = premium;
        this.rurPremium = rurPremium;
        this.branchId = branchId;
        this.branchName = branchName;
        this.subdivisionId = subdivisionId;
        this.subdivisionName = subdivisionName;
        this.status = status;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.programName = programName;
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
     * Номер договора
    * @return Номер договора
    **/
    @ApiModelProperty(value = "Номер договора")
    


    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }


    /**
     * Дата создания договора
    * @return Дата создания договора
    **/
    @ApiModelProperty(value = "Дата создания договора")
    
  @Valid


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Срок договора
    * @return Срок договора
    **/
    @ApiModelProperty(value = "Срок договора")
    


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    /**
     * Дата начала действия договора
    * @return Дата начала действия договора
    **/
    @ApiModelProperty(value = "Дата начала действия договора")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата оформления договора
    * @return Дата оформления договора
    **/
    @ApiModelProperty(value = "Дата оформления договора")
    
  @Valid


    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        this.conclusionDate = conclusionDate;
    }


    /**
     * Дата окончания договора
    * @return Дата окончания договора
    **/
    @ApiModelProperty(value = "Дата окончания договора")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Дата закрытия договора
    * @return Дата закрытия договора
    **/
    @ApiModelProperty(value = "Дата закрытия договора")
    
  @Valid


    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }


    /**
     * Данные клиента
    * @return Данные клиента
    **/
    @ApiModelProperty(value = "Данные клиента")
    
  @Valid


    public ShortClientData getClientData() {
        return clientData;
    }

    public void setClientData(ShortClientData clientData) {
        this.clientData = clientData;
    }


    /**
     * Тип договора
    * @return Тип договора
    **/
    @ApiModelProperty(value = "Тип договора")
    
  @Valid


    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }


    /**
     * Наименование валюты договора страхования
    * @return Наименование валюты договора страхования
    **/
    @ApiModelProperty(value = "Наименование валюты договора страхования")
    
 @Pattern(regexp="(^[A-Z]{3}$)")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    /**
     * Страховая премия в валюте договора
    * @return Страховая премия в валюте договора
    **/
    @ApiModelProperty(value = "Страховая премия в валюте договора")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Страховая премия в национальной валюте
    * @return Страховая премия в национальной валюте
    **/
    @ApiModelProperty(value = "Страховая премия в национальной валюте")
    
  @Valid


    public BigDecimal getRurPremium() {
        return rurPremium;
    }

    public void setRurPremium(BigDecimal rurPremium) {
        this.rurPremium = rurPremium;
    }


    /**
     * ID филиала
    * @return ID филиала
    **/
    @ApiModelProperty(value = "ID филиала")
    


    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }


    /**
     * Наименование филиала
    * @return Наименование филиала
    **/
    @ApiModelProperty(value = "Наименование филиала")
    


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    /**
     * ID ВСП оформления договора
    * @return ID ВСП оформления договора
    **/
    @ApiModelProperty(value = "ID ВСП оформления договора")
    


    public Long getSubdivisionId() {
        return subdivisionId;
    }

    public void setSubdivisionId(Long subdivisionId) {
        this.subdivisionId = subdivisionId;
    }


    /**
     * Наименование ВСП оформления договора
    * @return Наименование ВСП оформления договора
    **/
    @ApiModelProperty(value = "Наименование ВСП оформления договора")
    


    public String getSubdivisionName() {
        return subdivisionName;
    }

    public void setSubdivisionName(String subdivisionName) {
        this.subdivisionName = subdivisionName;
    }


    /**
     * Актуальный статус договора
    * @return Актуальный статус договора
    **/
    @ApiModelProperty(value = "Актуальный статус договора")
    
  @Valid


    public InsuranceStatusType getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatusType status) {
        this.status = status;
    }


    /**
     * ID пользователя создавшего договор
    * @return ID пользователя создавшего договор
    **/
    @ApiModelProperty(value = "ID пользователя создавшего договор")
    


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }


    /**
     * Наименование пользователя создавшего договоро
    * @return Наименование пользователя создавшего договоро
    **/
    @ApiModelProperty(value = "Наименование пользователя создавшего договоро")
    


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListInsuranceModel listInsuranceModel = (ListInsuranceModel) o;
        return Objects.equals(this.id, listInsuranceModel.id) &&
            Objects.equals(this.contractNumber, listInsuranceModel.contractNumber) &&
            Objects.equals(this.creationDate, listInsuranceModel.creationDate) &&
            Objects.equals(this.duration, listInsuranceModel.duration) &&
            Objects.equals(this.startDate, listInsuranceModel.startDate) &&
            Objects.equals(this.conclusionDate, listInsuranceModel.conclusionDate) &&
            Objects.equals(this.endDate, listInsuranceModel.endDate) &&
            Objects.equals(this.closeDate, listInsuranceModel.closeDate) &&
            Objects.equals(this.clientData, listInsuranceModel.clientData) &&
            Objects.equals(this.kind, listInsuranceModel.kind) &&
            Objects.equals(this.currency, listInsuranceModel.currency) &&
            Objects.equals(this.premium, listInsuranceModel.premium) &&
            Objects.equals(this.rurPremium, listInsuranceModel.rurPremium) &&
            Objects.equals(this.branchId, listInsuranceModel.branchId) &&
            Objects.equals(this.branchName, listInsuranceModel.branchName) &&
            Objects.equals(this.subdivisionId, listInsuranceModel.subdivisionId) &&
            Objects.equals(this.subdivisionName, listInsuranceModel.subdivisionName) &&
            Objects.equals(this.status, listInsuranceModel.status) &&
            Objects.equals(this.employeeId, listInsuranceModel.employeeId) &&
            Objects.equals(this.employeeName, listInsuranceModel.employeeName) &&
            Objects.equals(this.programName, listInsuranceModel.programName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contractNumber, creationDate, duration, startDate, conclusionDate, endDate, closeDate, clientData, kind, currency, premium, rurPremium, branchId, branchName, subdivisionId, subdivisionName, status, employeeId, employeeName, programName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListInsuranceModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    contractNumber: ").append(toIndentedString(contractNumber)).append("\n");
        sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    conclusionDate: ").append(toIndentedString(conclusionDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    closeDate: ").append(toIndentedString(closeDate)).append("\n");
        sb.append("    clientData: ").append(toIndentedString(clientData)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    rurPremium: ").append(toIndentedString(rurPremium)).append("\n");
        sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
        sb.append("    branchName: ").append(toIndentedString(branchName)).append("\n");
        sb.append("    subdivisionId: ").append(toIndentedString(subdivisionId)).append("\n");
        sb.append("    subdivisionName: ").append(toIndentedString(subdivisionName)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
        sb.append("    employeeName: ").append(toIndentedString(employeeName)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
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

