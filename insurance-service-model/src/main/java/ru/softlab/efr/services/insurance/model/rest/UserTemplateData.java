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
 * Пользовательский шаблон документа (ТЗ п.1.2)
 */
@ApiModel(description = "Пользовательский шаблон документа (ТЗ п.1.2)")
@Validated
public class UserTemplateData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("templateId")
    private String templateId = null;

    @JsonProperty("fileName")
    private String fileName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public UserTemplateData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор вида программы страхования
     * @param name Отображаемое имя пользовательского шаблона документа
     * @param priority Приоритет, задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет
     * @param templateId Идентификатор шаблона
     * @param fileName Наименование файла
     */
    public UserTemplateData(Long id, String name, Integer priority, String templateId, String fileName) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.templateId = templateId;
        this.fileName = fileName;
    }

    /**
     * Идентификатор вида программы страхования
    * @return Идентификатор вида программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор вида программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Отображаемое имя пользовательского шаблона документа
    * @return Отображаемое имя пользовательского шаблона документа
    **/
    @ApiModelProperty(value = "Отображаемое имя пользовательского шаблона документа")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Приоритет, задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет
    * @return Приоритет, задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет
    **/
    @ApiModelProperty(value = "Приоритет, задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет")
    


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    /**
     * Идентификатор шаблона
    * @return Идентификатор шаблона
    **/
    @ApiModelProperty(value = "Идентификатор шаблона")
    


    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    /**
     * Наименование файла
    * @return Наименование файла
    **/
    @ApiModelProperty(value = "Наименование файла")
    


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserTemplateData userTemplateData = (UserTemplateData) o;
        return Objects.equals(this.id, userTemplateData.id) &&
            Objects.equals(this.name, userTemplateData.name) &&
            Objects.equals(this.priority, userTemplateData.priority) &&
            Objects.equals(this.templateId, userTemplateData.templateId) &&
            Objects.equals(this.fileName, userTemplateData.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, priority, templateId, fileName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserTemplateData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
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

