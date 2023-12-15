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
 * Структура данных справочника шаблонов заявлений и инструкций для отображения в списке
 */
@ApiModel(description = "Структура данных справочника шаблонов заявлений и инструкций для отображения в списке")
@Validated
public class ClientTemplateDataForList   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("isTemplate")
    private Boolean isTemplate = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("sortPriority")
    private Long sortPriority = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientTemplateDataForList() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи в справочнике
     * @param kind Вид программы страхования
     * @param programName Наименование программы страхования
     * @param isTemplate Признак шаблона
     * @param name Наименование документа
     * @param startDate Дата начала отображения документа
     * @param endDate Дата окончания отображения документа
     * @param sortPriority Идентификатор записи в справочнике
     */
    public ClientTemplateDataForList(Long id, ProgramKind kind, String programName, Boolean isTemplate, String name, LocalDate startDate, LocalDate endDate, Long sortPriority) {
        this.id = id;
        this.kind = kind;
        this.programName = programName;
        this.isTemplate = isTemplate;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
    @ApiModelProperty(value = "Наименование документа")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Дата начала отображения документа
    * @return Дата начала отображения документа
    **/
    @ApiModelProperty(value = "Дата начала отображения документа")
    
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
        ClientTemplateDataForList clientTemplateDataForList = (ClientTemplateDataForList) o;
        return Objects.equals(this.id, clientTemplateDataForList.id) &&
            Objects.equals(this.kind, clientTemplateDataForList.kind) &&
            Objects.equals(this.programName, clientTemplateDataForList.programName) &&
            Objects.equals(this.isTemplate, clientTemplateDataForList.isTemplate) &&
            Objects.equals(this.name, clientTemplateDataForList.name) &&
            Objects.equals(this.startDate, clientTemplateDataForList.startDate) &&
            Objects.equals(this.endDate, clientTemplateDataForList.endDate) &&
            Objects.equals(this.sortPriority, clientTemplateDataForList.sortPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kind, programName, isTemplate, name, startDate, endDate, sortPriority);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTemplateDataForList {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    isTemplate: ").append(toIndentedString(isTemplate)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

