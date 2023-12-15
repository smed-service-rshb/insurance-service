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
 * Описание программы страхования
 */
@ApiModel(description = "Описание программы страхования")
@Validated
public class Segment   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Segment() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор сегмента
     * @param name Наименование сегмента
     */
    public Segment(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Идентификатор сегмента
    * @return Идентификатор сегмента
    **/
    @ApiModelProperty(required = true, value = "Идентификатор сегмента")
      @NotNull



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование сегмента
    * @return Наименование сегмента
    **/
    @ApiModelProperty(required = true, value = "Наименование сегмента")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Segment segment = (Segment) o;
        return Objects.equals(this.id, segment.id) &&
            Objects.equals(this.name, segment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Segment {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

