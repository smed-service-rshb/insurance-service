package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Структура данных записи справочника шаблонов заявлений и инструкций
 */
@ApiModel(description = "Структура данных записи справочника шаблонов заявлений и инструкций")
@Validated
public class ClientTemplateData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("program")
    private Long program = null;

    @JsonProperty("isTemplate")
    private Boolean isTemplate = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("link")
    private String link = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("attachId")
    private String attachId = null;

    @JsonProperty("attachName")
    private String attachName = null;

    @JsonProperty("sortPriority")
    private Long sortPriority = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientTemplateData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи в справочнике
     * @param kind Вид программы страхования
     * @param program Идентификатор программы страхования
     * @param isTemplate Признак шаблона
     * @param name Наименование документа
     * @param description Описание документа
     * @param link Ссылка на документ
     * @param startDate Дата начала отображения документа
     * @param endDate Дата окончания отображения документа
     * @param attachId Идентификатор приложенного документа
     * @param attachName Наименование приложенного документа
     * @param sortPriority Идентификатор записи в справочнике
     */
    public ClientTemplateData(Long id, ProgramKind kind, Long program, Boolean isTemplate, String name, String description, String link, LocalDate startDate, LocalDate endDate, String attachId, String attachName, Long sortPriority) {
        this.id = id;
        this.kind = kind;
        this.program = program;
        this.isTemplate = isTemplate;
        this.name = name;
        this.description = description;
        this.link = link;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attachId = attachId;
        this.attachName = attachName;
        this.sortPriority = sortPriority;
    }

    /**
     * Идентификатор записи в справочнике
    * @return Идентификатор записи в справочнике
    **/
    @ApiModelProperty(value = "Идентификатор записи в справочнике")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }


    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }


    /**
     * Признак шаблона
    * @return Признак шаблона
    **/
    @ApiModelProperty(value = "Признак шаблона")
    


    public Boolean isIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }


    /**
     * Наименование документа
    * @return Наименование документа
    **/
    @ApiModelProperty(required = true, value = "Наименование документа")
      @NotNull



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
    @ApiModelProperty(required = true, value = "Описание документа")
      @NotNull



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
     * Дата начала отображения документа
    * @return Дата начала отображения документа
    **/
    @ApiModelProperty(required = true, value = "Дата начала отображения документа")
      @NotNull

  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания отображения документа
    * @return Дата окончания отображения документа
    **/
    @ApiModelProperty(value = "Дата окончания отображения документа")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        ClientTemplateData clientTemplateData = (ClientTemplateData) o;
        return Objects.equals(this.id, clientTemplateData.id) &&
            Objects.equals(this.kind, clientTemplateData.kind) &&
            Objects.equals(this.program, clientTemplateData.program) &&
            Objects.equals(this.isTemplate, clientTemplateData.isTemplate) &&
            Objects.equals(this.name, clientTemplateData.name) &&
            Objects.equals(this.description, clientTemplateData.description) &&
            Objects.equals(this.link, clientTemplateData.link) &&
            Objects.equals(this.startDate, clientTemplateData.startDate) &&
            Objects.equals(this.endDate, clientTemplateData.endDate) &&
            Objects.equals(this.attachId, clientTemplateData.attachId) &&
            Objects.equals(this.attachName, clientTemplateData.attachName) &&
            Objects.equals(this.sortPriority, clientTemplateData.sortPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kind, program, isTemplate, name, description, link, startDate, endDate, attachId, attachName, sortPriority);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTemplateData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    program: ").append(toIndentedString(program)).append("\n");
        sb.append("    isTemplate: ").append(toIndentedString(isTemplate)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

