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
 * Файл, прикреплённый к объектам системы
 */
@ApiModel(description = "Файл, прикреплённый к объектам системы")
@Validated
public class Attachment   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("attachedDate")
    private LocalDate attachedDate = null;

    @JsonProperty("comment")
    private String comment = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Attachment() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор файла
     * @param name Наименование файла
     * @param attachedDate Дата загрузки в систему
     * @param comment Комментарий, описание файла
     */
    public Attachment(String id, String name, LocalDate attachedDate, String comment) {
        this.id = id;
        this.name = name;
        this.attachedDate = attachedDate;
        this.comment = comment;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attachment attachment = (Attachment) o;
        return Objects.equals(this.id, attachment.id) &&
            Objects.equals(this.name, attachment.name) &&
            Objects.equals(this.attachedDate, attachment.attachedDate) &&
            Objects.equals(this.comment, attachment.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, attachedDate, comment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Attachment {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    attachedDate: ").append(toIndentedString(attachedDate)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

