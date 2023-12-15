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
 * Данные элемента списка шаблонов и инструкций для отображения в ЛК
 */
@ApiModel(description = "Данные элемента списка шаблонов и инструкций для отображения в ЛК")
@Validated
public class ClientTemplate   {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("link")
    private String link = null;

    @JsonProperty("attachId")
    private String attachId = null;

    @JsonProperty("attachName")
    private String attachName = null;

    @JsonProperty("sortPriority")
    private Long sortPriority = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientTemplate() {}

    /**
     * Создает экземпляр класса
     * @param name Наименование документа
     * @param description Описание документа
     * @param link Ссылка на документ
     * @param attachId Идентификатор приложенного документа
     * @param attachName Наименование приложенного документа
     * @param sortPriority Идентификатор записи в справочнике
     */
    public ClientTemplate(String name, String description, String link, String attachId, String attachName, Long sortPriority) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.attachId = attachId;
        this.attachName = attachName;
        this.sortPriority = sortPriority;
    }

    /**
     * Наименование документа
    * @return Наименование документа
    **/
    @ApiModelProperty(value = "Наименование документа")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Описание документа
    * @return Описание документа
    **/
    @ApiModelProperty(value = "Описание документа")
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Ссылка на документ
    * @return Ссылка на документ
    **/
    @ApiModelProperty(value = "Ссылка на документ")
    


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    /**
     * Идентификатор приложенного документа
    * @return Идентификатор приложенного документа
    **/
    @ApiModelProperty(value = "Идентификатор приложенного документа")
    


    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }


    /**
     * Наименование приложенного документа
    * @return Наименование приложенного документа
    **/
    @ApiModelProperty(value = "Наименование приложенного документа")
    


    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }


    /**
     * Идентификатор записи в справочнике
    * @return Идентификатор записи в справочнике
    **/
    @ApiModelProperty(value = "Идентификатор записи в справочнике")
    


    public Long getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Long sortPriority) {
        this.sortPriority = sortPriority;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTemplate clientTemplate = (ClientTemplate) o;
        return Objects.equals(this.name, clientTemplate.name) &&
            Objects.equals(this.description, clientTemplate.description) &&
            Objects.equals(this.link, clientTemplate.link) &&
            Objects.equals(this.attachId, clientTemplate.attachId) &&
            Objects.equals(this.attachName, clientTemplate.attachName) &&
            Objects.equals(this.sortPriority, clientTemplate.sortPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, link, attachId, attachName, sortPriority);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTemplate {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    attachId: ").append(toIndentedString(attachId)).append("\n");
        sb.append("    attachName: ").append(toIndentedString(attachName)).append("\n");
        sb.append("    sortPriority: ").append(toIndentedString(sortPriority)).append("\n");
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

