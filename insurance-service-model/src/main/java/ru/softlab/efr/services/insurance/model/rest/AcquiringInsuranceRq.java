package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import ru.softlab.efr.services.insurance.model.rest.ShortClientData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Запрос на оформление договора клиентом
 */
@ApiModel(description = "Запрос на оформление договора клиентом")
@Validated
public class AcquiringInsuranceRq   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("docType")
    private DocumentType docType = null;

    @JsonProperty("docSeries")
    private String docSeries = null;

    @JsonProperty("docNumber")
    private String docNumber = null;

    @JsonProperty("phoneNumber")
    private String phoneNumber = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("address")
    private String address = null;

    @JsonProperty("isMobile")
    private Boolean isMobile = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AcquiringInsuranceRq() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор
     * @param surName Фамилия
     * @param firstName Имя
     * @param middleName Отчество
     * @param birthDate Дата рождения
     * @param gender Пол застрахованного
     * @param docType Тип документа
     * @param docSeries Серия документа
     * @param docNumber Номер документа
     * @param phoneNumber Номер телефона
     * @param email Адрес электронной почты страхователя
     * @param uuid uuid процесса
     * @param programId идентификатор выбранной программы страхования
     * @param address почтовый адрес клиента
     * @param isMobile признак оформления договора в мобильном приложении
     */
    public AcquiringInsuranceRq(Long id, String surName, String firstName, String middleName, LocalDate birthDate, Gender gender, DocumentType docType, String docSeries, String docNumber, String phoneNumber, String email, String uuid, Long programId, String address, Boolean isMobile) {
        this.id = id;
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.docType = docType;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.uuid = uuid;
        this.programId = programId;
        this.address = address;
        this.isMobile = isMobile;
    }

    /**
     * Идентификатор
    * @return Идентификатор
    **/
    @ApiModelProperty(value = "Идентификатор")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Фамилия
    * @return Фамилия
    **/
    @ApiModelProperty(value = "Фамилия")
    


    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    /**
     * Имя
    * @return Имя
    **/
    @ApiModelProperty(value = "Имя")
    


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество
    * @return Отчество
    **/
    @ApiModelProperty(value = "Отчество")
    


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Дата рождения
    * @return Дата рождения
    **/
    @ApiModelProperty(value = "Дата рождения")
    
  @Valid


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Пол застрахованного
    * @return Пол застрахованного
    **/
    @ApiModelProperty(value = "Пол застрахованного")
    
  @Valid


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    /**
     * Тип документа
    * @return Тип документа
    **/
    @ApiModelProperty(value = "Тип документа")
    
  @Valid


    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }


    /**
     * Серия документа
    * @return Серия документа
    **/
    @ApiModelProperty(value = "Серия документа")
    


    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }


    /**
     * Номер документа
    * @return Номер документа
    **/
    @ApiModelProperty(value = "Номер документа")
    


    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }


    /**
     * Номер телефона
    * @return Номер телефона
    **/
    @ApiModelProperty(value = "Номер телефона")
    


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Адрес электронной почты страхователя
    * @return Адрес электронной почты страхователя
    **/
    @ApiModelProperty(value = "Адрес электронной почты страхователя")
    
 @Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(((\\w+)|(\\w+-\\w+))\\.)+[a-zA-Z]{2,6})$)") @Size(max=150)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
     * идентификатор выбранной программы страхования
    * @return идентификатор выбранной программы страхования
    **/
    @ApiModelProperty(value = "идентификатор выбранной программы страхования")
    


    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
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
     * признак оформления договора в мобильном приложении
    * @return признак оформления договора в мобильном приложении
    **/
    @ApiModelProperty(value = "признак оформления договора в мобильном приложении")
    


    public Boolean isIsMobile() {
        return isMobile;
    }

    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquiringInsuranceRq acquiringInsuranceRq = (AcquiringInsuranceRq) o;
        return Objects.equals(this.id, acquiringInsuranceRq.id) &&
            Objects.equals(this.surName, acquiringInsuranceRq.surName) &&
            Objects.equals(this.firstName, acquiringInsuranceRq.firstName) &&
            Objects.equals(this.middleName, acquiringInsuranceRq.middleName) &&
            Objects.equals(this.birthDate, acquiringInsuranceRq.birthDate) &&
            Objects.equals(this.gender, acquiringInsuranceRq.gender) &&
            Objects.equals(this.docType, acquiringInsuranceRq.docType) &&
            Objects.equals(this.docSeries, acquiringInsuranceRq.docSeries) &&
            Objects.equals(this.docNumber, acquiringInsuranceRq.docNumber) &&
            Objects.equals(this.phoneNumber, acquiringInsuranceRq.phoneNumber) &&
            Objects.equals(this.email, acquiringInsuranceRq.email) &&
            Objects.equals(this.uuid, acquiringInsuranceRq.uuid) &&
            Objects.equals(this.programId, acquiringInsuranceRq.programId) &&
            Objects.equals(this.address, acquiringInsuranceRq.address) &&
            Objects.equals(this.isMobile, acquiringInsuranceRq.isMobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, firstName, middleName, birthDate, gender, docType, docSeries, docNumber, phoneNumber, email, uuid, programId, address, isMobile);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquiringInsuranceRq {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    docType: ").append(toIndentedString(docType)).append("\n");
        sb.append("    docSeries: ").append(toIndentedString(docSeries)).append("\n");
        sb.append("    docNumber: ").append(toIndentedString(docNumber)).append("\n");
        sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    isMobile: ").append(toIndentedString(isMobile)).append("\n");
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

