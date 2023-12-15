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
 * Результат создания записи в справочнике параметров программ страхования
 */
@ApiModel(description = "Результат создания записи в справочнике параметров программ страхования")
@Validated
public class CreateProgramSettingRs   {
    @JsonProperty("id")
    private Long id = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateProgramSettingRs() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор набора параметров программы страхования
     */
    public CreateProgramSettingRs(Long id) {
        this.id = id;
    }

    /**
     * Идентификатор набора параметров программы страхования
    * @return Идентификатор набора параметров программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор набора параметров программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateProgramSettingRs createProgramSettingRs = (CreateProgramSettingRs) o;
        return Objects.equals(this.id, createProgramSettingRs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateProgramSettingRs {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

