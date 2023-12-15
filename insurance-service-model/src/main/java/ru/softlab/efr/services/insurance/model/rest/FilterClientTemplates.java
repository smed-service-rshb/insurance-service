package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * структура для фильтрации справочника шаблонов заявлений и инструкций
 */
@ApiModel(description = "структура для фильтрации справочника шаблонов заявлений и инструкций")
@Validated
public class FilterClientTemplates   {
    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("program")
    private Long program = null;

    @JsonProperty("isTemplate")
    private Boolean isTemplate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterClientTemplates() {}

    /**
     * Создает экземпляр класса
     * @param kind Вид программы страхования
     * @param program Идентификатор программы страхования
     * @param isTemplate Признак шаблона
     */
    public FilterClientTemplates(ProgramKind kind, Long program, Boolean isTemplate) {
        this.kind = kind;
        this.program = program;
        this.isTemplate = isTemplate;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterClientTemplates filterClientTemplates = (FilterClientTemplates) o;
        return Objects.equals(this.kind, filterClientTemplates.kind) &&
            Objects.equals(this.program, filterClientTemplates.program) &&
            Objects.equals(this.isTemplate, filterClientTemplates.isTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, program, isTemplate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterClientTemplates {\n");
        
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    program: ").append(toIndentedString(program)).append("\n");
        sb.append("    isTemplate: ").append(toIndentedString(isTemplate)).append("\n");
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

