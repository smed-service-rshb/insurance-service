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
 * FilterProgramSettingsRq
 */
@Validated
public class FilterProgramSettingsRq   {
    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("strategyId")
    private Long strategyId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterProgramSettingsRq() {}

    /**
     * Создает экземпляр класса
     * @param kind Вид программы страхования
     * @param programName Наименование программы страхования
     * @param strategyId Id стратегии страхования
     */
    public FilterProgramSettingsRq(ProgramKind kind, String programName, Long strategyId) {
        this.kind = kind;
        this.programName = programName;
        this.strategyId = strategyId;
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
     * Id стратегии страхования
    * @return Id стратегии страхования
    **/
    @ApiModelProperty(value = "Id стратегии страхования")
    


    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterProgramSettingsRq filterProgramSettingsRq = (FilterProgramSettingsRq) o;
        return Objects.equals(this.kind, filterProgramSettingsRq.kind) &&
            Objects.equals(this.programName, filterProgramSettingsRq.programName) &&
            Objects.equals(this.strategyId, filterProgramSettingsRq.strategyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, programName, strategyId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterProgramSettingsRq {\n");
        
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    strategyId: ").append(toIndentedString(strategyId)).append("\n");
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

