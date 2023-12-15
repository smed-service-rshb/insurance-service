package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.FindProgramSettingResult;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Результат запроса поиска параметров программ страхования
 */
@ApiModel(description = "Результат запроса поиска параметров программ страхования")
@Validated
public class FindProgramSettingRs   {
    @JsonProperty("programs")
    @Valid
    private List<FindProgramSettingResult> programs = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FindProgramSettingRs() {}

    /**
     * Создает экземпляр класса
     * @param programs 
     */
    public FindProgramSettingRs(List<FindProgramSettingResult> programs) {
        this.programs = programs;
    }

    public FindProgramSettingRs addProgramsItem(FindProgramSettingResult programsItem) {
        if (this.programs == null) {
            this.programs = new ArrayList<FindProgramSettingResult>();
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


    public List<FindProgramSettingResult> getPrograms() {
        return programs;
    }

    public void setPrograms(List<FindProgramSettingResult> programs) {
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
        FindProgramSettingRs findProgramSettingRs = (FindProgramSettingRs) o;
        return Objects.equals(this.programs, findProgramSettingRs.programs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FindProgramSettingRs {\n");
        
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

