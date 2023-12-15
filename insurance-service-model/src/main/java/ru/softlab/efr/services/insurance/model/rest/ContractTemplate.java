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
 * Шаблон документа для печати
 */
@ApiModel(description = "Шаблон документа для печати")
@Validated
public class ContractTemplate   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ContractTemplate() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор шаблона
     * @param name Наименование шаблона
     */
    public ContractTemplate(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Идентификатор шаблона
    * @return Идентификатор шаблона
    **/
    @ApiModelProperty(required = true, value = "Идентификатор шаблона")
      @NotNull



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Наименование шаблона
    * @return Наименование шаблона
    **/
    @ApiModelProperty(required = true, value = "Наименование шаблона")
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
        ContractTemplate contractTemplate = (ContractTemplate) o;
        return Objects.equals(this.id, contractTemplate.id) &&
            Objects.equals(this.name, contractTemplate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContractTemplate {\n");
        
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

