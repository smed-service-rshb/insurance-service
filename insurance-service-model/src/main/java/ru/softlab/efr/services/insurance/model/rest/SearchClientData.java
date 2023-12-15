package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные клиента
 */
@ApiModel(description = "Данные клиента")
@Validated
public class SearchClientData   {
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

    @JsonProperty("phoneNumber")
    private String phoneNumber = null;


    /**
     * Создает пустой экземпляр класса
     */
    public SearchClientData() {}

    /**
     * Создает экземпляр класса
     * @param surName Фамилия
     * @param firstName Имя
     * @param middleName Отчество
     * @param birthDate Дата рождения
     * @param docType Тип документа
     * @param docSeries Серия документа
     * @param docNumber Номер документа
     * @param phoneNumber Номер телефона
     */
    public SearchClientData(String surName, String firstName, String middleName, LocalDate birthDate, DocumentType docType, String docSeries, String docNumber, String phoneNumber) {
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.docType = docType;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
        this.phoneNumber = phoneNumber;
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
        SearchClientData searchClientData = (SearchClientData) o;
        return Objects.equals(this.surName, searchClientData.surName) &&
            Objects.equals(this.firstName, searchClientData.firstName) &&
            Objects.equals(this.middleName, searchClientData.middleName) &&
            Objects.equals(this.birthDate, searchClientData.birthDate) &&
            Objects.equals(this.docType, searchClientData.docType) &&
            Objects.equals(this.docSeries, searchClientData.docSeries) &&
            Objects.equals(this.docNumber, searchClientData.docNumber) &&
            Objects.equals(this.phoneNumber, searchClientData.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SearchClientData {\n");
        
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    docType: ").append(toIndentedString(docType)).append("\n");
        sb.append("    docSeries: ").append(toIndentedString(docSeries)).append("\n");
        sb.append("    docNumber: ").append(toIndentedString(docNumber)).append("\n");
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

