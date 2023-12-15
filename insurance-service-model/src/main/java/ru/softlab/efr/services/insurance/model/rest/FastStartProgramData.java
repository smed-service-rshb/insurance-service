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
 * Описание программы страхования (\&quot;быстрый старт\&quot;)
 */
@ApiModel(description = "Описание программы страхования (\"быстрый старт\")")
@Validated
public class FastStartProgramData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("minAge")
    private Integer minAge = null;

    @JsonProperty("maxAge")
    private Integer maxAge = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FastStartProgramData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы страхования
     * @param name Наименование программы страхования
     * @param minAge Минимальный возраст доступный для программы страхования
     * @param maxAge Максимальный возраст доступный для программы страхования
     */
    public FastStartProgramData(Long id, String name, Integer minAge, Integer maxAge) {
        this.id = id;
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Минимальный возраст доступный для программы страхования
    * @return Минимальный возраст доступный для программы страхования
    **/
    @ApiModelProperty(value = "Минимальный возраст доступный для программы страхования")
    


    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }


    /**
     * Максимальный возраст доступный для программы страхования
    * @return Максимальный возраст доступный для программы страхования
    **/
    @ApiModelProperty(value = "Максимальный возраст доступный для программы страхования")
    


    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FastStartProgramData fastStartProgramData = (FastStartProgramData) o;
        return Objects.equals(this.id, fastStartProgramData.id) &&
            Objects.equals(this.name, fastStartProgramData.name) &&
            Objects.equals(this.minAge, fastStartProgramData.minAge) &&
            Objects.equals(this.maxAge, fastStartProgramData.maxAge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minAge, maxAge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FastStartProgramData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    minAge: ").append(toIndentedString(minAge)).append("\n");
        sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
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

