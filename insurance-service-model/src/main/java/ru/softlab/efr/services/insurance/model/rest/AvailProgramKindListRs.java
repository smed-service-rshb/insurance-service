package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AvailProgramKindData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Результат запроса поиска доступных видов программ страхования
 */
@ApiModel(description = "Результат запроса поиска доступных видов программ страхования")
@Validated
public class AvailProgramKindListRs   {
    @JsonProperty("availProgramKinds")
    @Valid
    private List<AvailProgramKindData> availProgramKinds = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailProgramKindListRs() {}

    /**
     * Создает экземпляр класса
     * @param availProgramKinds 
     */
    public AvailProgramKindListRs(List<AvailProgramKindData> availProgramKinds) {
        this.availProgramKinds = availProgramKinds;
    }

    public AvailProgramKindListRs addAvailProgramKindsItem(AvailProgramKindData availProgramKindsItem) {
        if (this.availProgramKinds == null) {
            this.availProgramKinds = new ArrayList<AvailProgramKindData>();
        }
        this.availProgramKinds.add(availProgramKindsItem);
        return this;
    }

    /**
    * Get availProgramKinds
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<AvailProgramKindData> getAvailProgramKinds() {
        return availProgramKinds;
    }

    public void setAvailProgramKinds(List<AvailProgramKindData> availProgramKinds) {
        this.availProgramKinds = availProgramKinds;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailProgramKindListRs availProgramKindListRs = (AvailProgramKindListRs) o;
        return Objects.equals(this.availProgramKinds, availProgramKindListRs.availProgramKinds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availProgramKinds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailProgramKindListRs {\n");
        
        sb.append("    availProgramKinds: ").append(toIndentedString(availProgramKinds)).append("\n");
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

