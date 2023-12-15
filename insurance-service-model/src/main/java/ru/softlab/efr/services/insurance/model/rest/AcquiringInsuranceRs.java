package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.ShortClientData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ответ на запрос создания договора с информацией по договору
 */
@ApiModel(description = "ответ на запрос создания договора с информацией по договору")
@Validated
public class AcquiringInsuranceRs   {
    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("needShowExist")
    private Boolean needShowExist = null;

    @JsonProperty("insuranceId")
    private Long insuranceId = null;

    @JsonProperty("insuranceNumber")
    private String insuranceNumber = null;

    @JsonProperty("insuranceStatus")
    private InsuranceStatusType insuranceStatus = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("currencyIso")
    private String currencyIso = null;

    @JsonProperty("paymentAmount")
    private BigDecimal paymentAmount = null;

    @JsonProperty("insuranceAmount")
    private BigDecimal insuranceAmount = null;

    @JsonProperty("insurancePremium")
    private BigDecimal insurancePremium = null;

    @JsonProperty("dateCreate")
    private LocalDate dateCreate = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("risks")
    @Valid
    private List<String> risks = null;

    @JsonProperty("address")
    private String address = null;

    @JsonProperty("image")
    private Long image = null;

    @JsonProperty("insured")
    private ShortClientData insured = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AcquiringInsuranceRs() {}

    /**
     * Создает экземпляр класса
     * @param uuid uuid процесса
     * @param needShowExist Признак необходимости отобразить уведомление о существующих договорах
     * @param insuranceId id договора, устанавливается на шаге оформления
     * @param insuranceNumber Номер договора, устанавливается на шаге оформления
     * @param insuranceStatus Статус договора страхования
     * @param programKind Вид программы страхования
     * @param programId идентификатор программы страхования
     * @param programName Наименование программы страхования
     * @param currencyIso Валюта программы страхования
     * @param paymentAmount Сумма к оплате
     * @param insuranceAmount Страховая сумма
     * @param insurancePremium Страховая премия
     * @param dateCreate Дата оформления
     * @param startDate Дата начала действия
     * @param endDate Дата окончания
     * @param risks Список рисков
     * @param address почтовый адрес клиента
     * @param image id изображения
     * @param insured Данные застрахованного
     */
    public AcquiringInsuranceRs(String uuid, Boolean needShowExist, Long insuranceId, String insuranceNumber, InsuranceStatusType insuranceStatus, ProgramKind programKind, Long programId, String programName, String currencyIso, BigDecimal paymentAmount, BigDecimal insuranceAmount, BigDecimal insurancePremium, LocalDate dateCreate, LocalDate startDate, LocalDate endDate, List<String> risks, String address, Long image, ShortClientData insured) {
        this.uuid = uuid;
        this.needShowExist = needShowExist;
        this.insuranceId = insuranceId;
        this.insuranceNumber = insuranceNumber;
        this.insuranceStatus = insuranceStatus;
        this.programKind = programKind;
        this.programId = programId;
        this.programName = programName;
        this.currencyIso = currencyIso;
        this.paymentAmount = paymentAmount;
        this.insuranceAmount = insuranceAmount;
        this.insurancePremium = insurancePremium;
        this.dateCreate = dateCreate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.risks = risks;
        this.address = address;
        this.image = image;
        this.insured = insured;
    }

    /**
     * uuid процесса
    * @return uuid процесса
    **/
    @ApiModelProperty(value = "uuid процесса")
    


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    /**
     * Признак необходимости отобразить уведомление о существующих договорах
    * @return Признак необходимости отобразить уведомление о существующих договорах
    **/
    @ApiModelProperty(value = "Признак необходимости отобразить уведомление о существующих договорах")
    


    public Boolean isNeedShowExist() {
        return needShowExist;
    }

    public void setNeedShowExist(Boolean needShowExist) {
        this.needShowExist = needShowExist;
    }


    /**
     * id договора, устанавливается на шаге оформления
    * @return id договора, устанавливается на шаге оформления
    **/
    @ApiModelProperty(value = "id договора, устанавливается на шаге оформления")
    


    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }


    /**
     * Номер договора, устанавливается на шаге оформления
    * @return Номер договора, устанавливается на шаге оформления
    **/
    @ApiModelProperty(value = "Номер договора, устанавливается на шаге оформления")
    


    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }


    /**
     * Статус договора страхования
    * @return Статус договора страхования
    **/
    @ApiModelProperty(value = "Статус договора страхования")
    
  @Valid


    public InsuranceStatusType getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(InsuranceStatusType insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
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
     * идентификатор программы страхования
    * @return идентификатор программы страхования
    **/
    @ApiModelProperty(value = "идентификатор программы страхования")
    


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


    /**
     * Валюта программы страхования
    * @return Валюта программы страхования
    **/
    @ApiModelProperty(value = "Валюта программы страхования")
    


    public String getCurrencyIso() {
        return currencyIso;
    }

    public void setCurrencyIso(String currencyIso) {
        this.currencyIso = currencyIso;
    }


    /**
     * Сумма к оплате
    * @return Сумма к оплате
    **/
    @ApiModelProperty(value = "Сумма к оплате")
    
  @Valid


    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }


    /**
     * Страховая сумма
    * @return Страховая сумма
    **/
    @ApiModelProperty(value = "Страховая сумма")
    
  @Valid


    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }


    /**
     * Страховая премия
    * @return Страховая премия
    **/
    @ApiModelProperty(value = "Страховая премия")
    
  @Valid


    public BigDecimal getInsurancePremium() {
        return insurancePremium;
    }

    public void setInsurancePremium(BigDecimal insurancePremium) {
        this.insurancePremium = insurancePremium;
    }


    /**
     * Дата оформления
    * @return Дата оформления
    **/
    @ApiModelProperty(value = "Дата оформления")
    
  @Valid


    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }


    /**
     * Дата начала действия
    * @return Дата начала действия
    **/
    @ApiModelProperty(value = "Дата начала действия")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания
    * @return Дата окончания
    **/
    @ApiModelProperty(value = "Дата окончания")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public AcquiringInsuranceRs addRisksItem(String risksItem) {
        if (this.risks == null) {
            this.risks = new ArrayList<String>();
        }
        this.risks.add(risksItem);
        return this;
    }

    /**
     * Список рисков
    * @return Список рисков
    **/
    @ApiModelProperty(value = "Список рисков")
    


    public List<String> getRisks() {
        return risks;
    }

    public void setRisks(List<String> risks) {
        this.risks = risks;
    }


    /**
     * почтовый адрес клиента
    * @return почтовый адрес клиента
    **/
    @ApiModelProperty(value = "почтовый адрес клиента")
    


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * id изображения
    * @return id изображения
    **/
    @ApiModelProperty(value = "id изображения")
    


    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }


    /**
     * Данные застрахованного
    * @return Данные застрахованного
    **/
    @ApiModelProperty(value = "Данные застрахованного")
    
  @Valid


    public ShortClientData getInsured() {
        return insured;
    }

    public void setInsured(ShortClientData insured) {
        this.insured = insured;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquiringInsuranceRs acquiringInsuranceRs = (AcquiringInsuranceRs) o;
        return Objects.equals(this.uuid, acquiringInsuranceRs.uuid) &&
            Objects.equals(this.needShowExist, acquiringInsuranceRs.needShowExist) &&
            Objects.equals(this.insuranceId, acquiringInsuranceRs.insuranceId) &&
            Objects.equals(this.insuranceNumber, acquiringInsuranceRs.insuranceNumber) &&
            Objects.equals(this.insuranceStatus, acquiringInsuranceRs.insuranceStatus) &&
            Objects.equals(this.programKind, acquiringInsuranceRs.programKind) &&
            Objects.equals(this.programId, acquiringInsuranceRs.programId) &&
            Objects.equals(this.programName, acquiringInsuranceRs.programName) &&
            Objects.equals(this.currencyIso, acquiringInsuranceRs.currencyIso) &&
            Objects.equals(this.paymentAmount, acquiringInsuranceRs.paymentAmount) &&
            Objects.equals(this.insuranceAmount, acquiringInsuranceRs.insuranceAmount) &&
            Objects.equals(this.insurancePremium, acquiringInsuranceRs.insurancePremium) &&
            Objects.equals(this.dateCreate, acquiringInsuranceRs.dateCreate) &&
            Objects.equals(this.startDate, acquiringInsuranceRs.startDate) &&
            Objects.equals(this.endDate, acquiringInsuranceRs.endDate) &&
            Objects.equals(this.risks, acquiringInsuranceRs.risks) &&
            Objects.equals(this.address, acquiringInsuranceRs.address) &&
            Objects.equals(this.image, acquiringInsuranceRs.image) &&
            Objects.equals(this.insured, acquiringInsuranceRs.insured);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, needShowExist, insuranceId, insuranceNumber, insuranceStatus, programKind, programId, programName, currencyIso, paymentAmount, insuranceAmount, insurancePremium, dateCreate, startDate, endDate, risks, address, image, insured);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquiringInsuranceRs {\n");
        
        sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
        sb.append("    needShowExist: ").append(toIndentedString(needShowExist)).append("\n");
        sb.append("    insuranceId: ").append(toIndentedString(insuranceId)).append("\n");
        sb.append("    insuranceNumber: ").append(toIndentedString(insuranceNumber)).append("\n");
        sb.append("    insuranceStatus: ").append(toIndentedString(insuranceStatus)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    currencyIso: ").append(toIndentedString(currencyIso)).append("\n");
        sb.append("    paymentAmount: ").append(toIndentedString(paymentAmount)).append("\n");
        sb.append("    insuranceAmount: ").append(toIndentedString(insuranceAmount)).append("\n");
        sb.append("    insurancePremium: ").append(toIndentedString(insurancePremium)).append("\n");
        sb.append("    dateCreate: ").append(toIndentedString(dateCreate)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    risks: ").append(toIndentedString(risks)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    insured: ").append(toIndentedString(insured)).append("\n");
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

