package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Файл, прикреплённый к заявлениям
 */
@ApiModel(description = "Файл, прикреплённый к заявлениям")
@Validated
public class StatementAttachment   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("attachedDate")
    private LocalDate attachedDate = null;

    @JsonProperty("comment")
    private String comment = null;

    @JsonProperty("attachmentTypeId")
    private Long attachmentTypeId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public StatementAttachment() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор файла
     * @param name Наименование файла
     * @param attachedDate Дата загрузки в систему
     * @param comment Комментарий, описание файла
     * @param attachmentTypeId Идентификатор типа приложения
     */
    public StatementAttachment(String id, String name, LocalDate attachedDate, String comment, Long attachmentTypeId) {
        this.id = id;
        this.name = name;
        this.attachedDate = attachedDate;
        this.comment = comment;
        this.attachmentTypeId = attachmentTypeId;
    }

    /**
     * Идентификатор файла
    * @return Идентификатор файла
    **/
    @ApiModelProperty(value = "Идентификатор файла")
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Наименование файла
    * @return Наименование файла
    **/
    @ApiModelProperty(value = "Наименование файла")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Дата загрузки в систему
    * @return Дата загрузки в систему
    **/
    @ApiModelProperty(value = "Дата загрузки в систему")
    
  @Valid


    public LocalDate getAttachedDate() {
        return attachedDate;
    }

    public void setAttachedDate(LocalDate attachedDate) {
        this.attachedDate = attachedDate;
    }


    /**
     * Комментарий, описание файла
    * @return Комментарий, описание файла
    **/
    @ApiModelProperty(value = "Комментарий, описание файла")
    


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * Идентификатор типа приложения
    * @return Идентификатор типа приложения
    **/
    @ApiModelProperty(value = "Идентификатор типа приложения")
    


    public Long getAttachmentTypeId() {
        return attachmentTypeId;
    }

    public void setAttachmentTypeId(Long attachmentTypeId) {
        this.attachmentTypeId = attachmentTypeId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatementAttachment statementAttachment = (StatementAttachment) o;
        return Objects.equals(this.id, statementAttachment.id) &&
            Objects.equals(this.name, statementAttachment.name) &&
            Objects.equals(this.attachedDate, statementAttachment.attachedDate) &&
            Objects.equals(this.comment, statementAttachment.comment) &&
            Objects.equals(this.attachmentTypeId, statementAttachment.attachmentTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, attachedDate, comment, attachmentTypeId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StatementAttachment {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    attachedDate: ").append(toIndentedString(attachedDate)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    attachmentTypeId: ").append(toIndentedString(attachmentTypeId)).append("\n");
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

