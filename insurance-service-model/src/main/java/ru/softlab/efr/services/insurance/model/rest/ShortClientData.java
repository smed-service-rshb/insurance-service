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
public class ShortClientData   {
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


    /**
     * Создает пустой экземпляр класса
     */
    public ShortClientData() {}

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
     */
    public ShortClientData(Long id, String surName, String firstName, String middleName, LocalDate birthDate, Gender gender, DocumentType docType, String docSeries, String docNumber, String phoneNumber, String email) {
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShortClientData shortClientData = (ShortClientData) o;
        return Objects.equals(this.id, shortClientData.id) &&
            Objects.equals(this.surName, shortClientData.surName) &&
            Objects.equals(this.firstName, shortClientData.firstName) &&
            Objects.equals(this.middleName, shortClientData.middleName) &&
            Objects.equals(this.birthDate, shortClientData.birthDate) &&
            Objects.equals(this.gender, shortClientData.gender) &&
            Objects.equals(this.docType, shortClientData.docType) &&
            Objects.equals(this.docSeries, shortClientData.docSeries) &&
            Objects.equals(this.docNumber, shortClientData.docNumber) &&
            Objects.equals(this.phoneNumber, shortClientData.phoneNumber) &&
            Objects.equals(this.email, shortClientData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, firstName, middleName, birthDate, gender, docType, docSeries, docNumber, phoneNumber, email);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShortClientData {\n");
        
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

