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
 * FilterProgramsRq
 */
@Validated
public class FilterProgramsRq   {
    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("policyCode")
    private String policyCode = null;

    @JsonProperty("programVariant")
    private String programVariant = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterProgramsRq() {}

    /**
     * Создает экземпляр класса
     * @param kind Вид программы страхования
     * @param programName Наименование программы страхования
     * @param policyCode Кодировка программы
     * @param programVariant Вариант программы страхования
     */
    public FilterProgramsRq(ProgramKind kind, String programName, String policyCode, String programVariant) {
        this.kind = kind;
        this.programName = programName;
        this.policyCode = policyCode;
        this.programVariant = programVariant;
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
     * Кодировка программы
    * @return Кодировка программы
    **/
    @ApiModelProperty(value = "Кодировка программы")
    


    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }


    /**
     * Вариант программы страхования
    * @return Вариант программы страхования
    **/
    @ApiModelProperty(value = "Вариант программы страхования")
    


    public String getProgramVariant() {
        return programVariant;
    }

    public void setProgramVariant(String programVariant) {
        this.programVariant = programVariant;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterProgramsRq filterProgramsRq = (FilterProgramsRq) o;
        return Objects.equals(this.kind, filterProgramsRq.kind) &&
            Objects.equals(this.programName, filterProgramsRq.programName) &&
            Objects.equals(this.policyCode, filterProgramsRq.policyCode) &&
            Objects.equals(this.programVariant, filterProgramsRq.programVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, programName, policyCode, programVariant);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterProgramsRq {\n");
        
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    policyCode: ").append(toIndentedString(policyCode)).append("\n");
        sb.append("    programVariant: ").append(toIndentedString(programVariant)).append("\n");
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

