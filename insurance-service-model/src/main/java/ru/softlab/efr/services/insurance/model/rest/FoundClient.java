package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные клиента
 */
@ApiModel(description = "Данные клиента")
@Validated
public class FoundClient   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("docType")
    private DocumentType docType = null;

    @JsonProperty("docSeries")
    private String docSeries = null;

    @JsonProperty("docNumber")
    private String docNumber = null;

    @JsonProperty("issuedDate")
    private LocalDate issuedDate = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("regAddress")
    private String regAddress = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("phoneNumber")
    private String phoneNumber = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FoundClient() {}

    /**
     * Создает экземпляр класса
     * @param id Id клиента
     * @param surName Фамилия
     * @param firstName Имя
     * @param middleName Отчество
     * @param birthDate Дата рождения
     * @param docType Тип документа
     * @param docSeries Серия документа
     * @param docNumber Номер документа
     * @param issuedDate Дата выдачи документа
     * @param gender Пол
     * @param regAddress Адрес регистрации
     * @param status Статус значимости клиента
     * @param phoneNumber Номер телефона
     */
    public FoundClient(String id, String surName, String firstName, String middleName, LocalDate birthDate, DocumentType docType, String docSeries, String docNumber, LocalDate issuedDate, Gender gender, String regAddress, String status, String phoneNumber) {
        this.id = id;
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.docType = docType;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
        this.issuedDate = issuedDate;
        this.gender = gender;
        this.regAddress = regAddress;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Id клиента
    * @return Id клиента
    **/
    @ApiModelProperty(value = "Id клиента")
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
     * Дата выдачи документа
    * @return Дата выдачи документа
    **/
    @ApiModelProperty(value = "Дата выдачи документа")
    
  @Valid


    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
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
     * Адрес регистрации
    * @return Адрес регистрации
    **/
    @ApiModelProperty(value = "Адрес регистрации")
    


    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }


    /**
     * Статус значимости клиента
    * @return Статус значимости клиента
    **/
    @ApiModelProperty(value = "Статус значимости клиента")
    


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoundClient foundClient = (FoundClient) o;
        return Objects.equals(this.id, foundClient.id) &&
            Objects.equals(this.surName, foundClient.surName) &&
            Objects.equals(this.firstName, foundClient.firstName) &&
            Objects.equals(this.middleName, foundClient.middleName) &&
            Objects.equals(this.birthDate, foundClient.birthDate) &&
            Objects.equals(this.docType, foundClient.docType) &&
            Objects.equals(this.docSeries, foundClient.docSeries) &&
            Objects.equals(this.docNumber, foundClient.docNumber) &&
            Objects.equals(this.issuedDate, foundClient.issuedDate) &&
            Objects.equals(this.gender, foundClient.gender) &&
            Objects.equals(this.regAddress, foundClient.regAddress) &&
            Objects.equals(this.status, foundClient.status) &&
            Objects.equals(this.phoneNumber, foundClient.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, issuedDate, gender, regAddress, status, phoneNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FoundClient {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    docType: ").append(toIndentedString(docType)).append("\n");
        sb.append("    docSeries: ").append(toIndentedString(docSeries)).append("\n");
        sb.append("    docNumber: ").append(toIndentedString(docNumber)).append("\n");
        sb.append("    issuedDate: ").append(toIndentedString(issuedDate)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    regAddress: ").append(toIndentedString(regAddress)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
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

