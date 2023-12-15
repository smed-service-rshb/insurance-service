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
 * Описание вида программы страхования
 */
@ApiModel(description = "Описание вида программы страхования")
@Validated
public class AvailProgramKindData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailProgramKindData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор вида программы страхования
     * @param code Код вида программы страхования
     * @param name Наименование вида программы страхования
     * @param isActive Признак активности вида программы страхования
     */
    public AvailProgramKindData(Long id, String code, String name, Boolean isActive) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.isActive = isActive;
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
     * Код вида программы страхования
    * @return Код вида программы страхования
    **/
    @ApiModelProperty(required = true, value = "Код вида программы страхования")
      @NotNull



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Наименование вида программы страхования
    * @return Наименование вида программы страхования
    **/
    @ApiModelProperty(value = "Наименование вида программы страхования")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Признак активности вида программы страхования
    * @return Признак активности вида программы страхования
    **/
    @ApiModelProperty(required = true, value = "Признак активности вида программы страхования")
      @NotNull



    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailProgramKindData availProgramKindData = (AvailProgramKindData) o;
        return Objects.equals(this.id, availProgramKindData.id) &&
            Objects.equals(this.code, availProgramKindData.code) &&
            Objects.equals(this.name, availProgramKindData.name) &&
            Objects.equals(this.isActive, availProgramKindData.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, isActive);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailProgramKindData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

