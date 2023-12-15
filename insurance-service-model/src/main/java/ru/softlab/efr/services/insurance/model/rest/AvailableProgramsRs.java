package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AvailableProgram;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ответ на получение доступных для клиента программ страхования
 */
@ApiModel(description = "Ответ на получение доступных для клиента программ страхования")
@Validated
public class AvailableProgramsRs   {
    @JsonProperty("programs")
    @Valid
    private List<AvailableProgram> programs = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailableProgramsRs() {}

    /**
     * Создает экземпляр класса
     * @param programs 
     */
    public AvailableProgramsRs(List<AvailableProgram> programs) {
        this.programs = programs;
    }

    public AvailableProgramsRs addProgramsItem(AvailableProgram programsItem) {
        if (this.programs == null) {
            this.programs = new ArrayList<AvailableProgram>();
        }
        this.programs.add(programsItem);
        return this;
    }

    /**
    * Get programs
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<AvailableProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<AvailableProgram> programs) {
        this.programs = programs;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableProgramsRs availableProgramsRs = (AvailableProgramsRs) o;
        return Objects.equals(this.programs, availableProgramsRs.programs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailableProgramsRs {\n");
        
        sb.append("    programs: ").append(toIndentedString(programs)).append("\n");
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

