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
 * Описание стратегии ИСЖ
 */
@ApiModel(description = "Описание стратегии ИСЖ")
@Validated
public class Strategy   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Strategy() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор стратегии
     * @param name Наименование стратегии
     */
    public Strategy(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Идентификатор стратегии
    * @return Идентификатор стратегии
    **/
    @ApiModelProperty(required = true, value = "Идентификатор стратегии")
      @NotNull



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование стратегии
    * @return Наименование стратегии
    **/
    @ApiModelProperty(required = true, value = "Наименование стратегии")
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
        Strategy strategy = (Strategy) o;
        return Objects.equals(this.id, strategy.id) &&
            Objects.equals(this.name, strategy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Strategy {\n");
        
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

