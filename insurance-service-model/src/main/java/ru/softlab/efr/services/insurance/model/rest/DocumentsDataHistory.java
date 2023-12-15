package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.Document;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об измененном документе
 */
@ApiModel(description = "Информация об измененном документе")
@Validated
public class DocumentsDataHistory   {
    @JsonProperty("document")
    private Document document = null;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate = null;

    @JsonProperty("userFullName")
    private String userFullName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public DocumentsDataHistory() {}

    /**
     * Создает экземпляр класса
     * @param document Информация о документе
     * @param lastModifiedDate Дата изменения
     * @param userFullName ФИО пользователя изменившего данные
     */
    public DocumentsDataHistory(Document document, String lastModifiedDate, String userFullName) {
        this.document = document;
        this.lastModifiedDate = lastModifiedDate;
        this.userFullName = userFullName;
    }

    /**
     * Информация о документе
    * @return Информация о документе
    **/
    @ApiModelProperty(value = "Информация о документе")
    
  @Valid


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }


    /**
     * Дата изменения
    * @return Дата изменения
    **/
    @ApiModelProperty(value = "Дата изменения")
    


    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    /**
     * ФИО пользователя изменившего данные
    * @return ФИО пользователя изменившего данные
    **/
    @ApiModelProperty(value = "ФИО пользователя изменившего данные")
    


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentsDataHistory documentsDataHistory = (DocumentsDataHistory) o;
        return Objects.equals(this.document, documentsDataHistory.document) &&
            Objects.equals(this.lastModifiedDate, documentsDataHistory.lastModifiedDate) &&
            Objects.equals(this.userFullName, documentsDataHistory.userFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, lastModifiedDate, userFullName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DocumentsDataHistory {\n");
        
        sb.append("    document: ").append(toIndentedString(document)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    userFullName: ").append(toIndentedString(userFullName)).append("\n");
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

