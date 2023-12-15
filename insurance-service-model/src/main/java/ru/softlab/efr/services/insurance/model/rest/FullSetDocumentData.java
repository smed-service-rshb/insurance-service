package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Запрос на установку признака полного комплекта документов
 */
@ApiModel(description = "Запрос на установку признака полного комплекта документов")
@Validated
public class FullSetDocumentData   {
    @JsonProperty("fullSetDocument")
    private Boolean fullSetDocument = null;

    @JsonProperty("commentForNotFullSetDocument")
    private String commentForNotFullSetDocument = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FullSetDocumentData() {}

    /**
     * Создает экземпляр класса
     * @param fullSetDocument Признак полного комплекта документов
     * @param commentForNotFullSetDocument Комментарий при неполном комплекте документов
     */
    public FullSetDocumentData(Boolean fullSetDocument, String commentForNotFullSetDocument) {
        this.fullSetDocument = fullSetDocument;
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
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


    /**
     * Комментарий при неполном комплекте документов
    * @return Комментарий при неполном комплекте документов
    **/
    @ApiModelProperty(value = "Комментарий при неполном комплекте документов")
    


    public String getCommentForNotFullSetDocument() {
        return commentForNotFullSetDocument;
    }

    public void setCommentForNotFullSetDocument(String commentForNotFullSetDocument) {
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FullSetDocumentData fullSetDocumentData = (FullSetDocumentData) o;
        return Objects.equals(this.fullSetDocument, fullSetDocumentData.fullSetDocument) &&
            Objects.equals(this.commentForNotFullSetDocument, fullSetDocumentData.commentForNotFullSetDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullSetDocument, commentForNotFullSetDocument);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FullSetDocumentData {\n");
        
        sb.append("    fullSetDocument: ").append(toIndentedString(fullSetDocument)).append("\n");
        sb.append("    commentForNotFullSetDocument: ").append(toIndentedString(commentForNotFullSetDocument)).append("\n");
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

