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
 * Поле, обязательное для заполнения
 */
@ApiModel(description = "Поле, обязательное для заполнения")
@Validated
public class RequiredField   {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("strId")
    private String strId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RequiredField() {}

    /**
     * Создает экземпляр класса
     * @param name Отображаемое наименование поля
     * @param strId Строковый идентификатор поля
     */
    public RequiredField(String name, String strId) {
        this.name = name;
        this.strId = strId;
    }

    /**
     * Отображаемое наименование поля
    * @return Отображаемое наименование поля
    **/
    @ApiModelProperty(required = true, value = "Отображаемое наименование поля")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Строковый идентификатор поля
    * @return Строковый идентификатор поля
    **/
    @ApiModelProperty(required = true, value = "Строковый идентификатор поля")
      @NotNull



    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredField requiredField = (RequiredField) o;
        return Objects.equals(this.name, requiredField.name) &&
            Objects.equals(this.strId, requiredField.strId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequiredField {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    strId: ").append(toIndentedString(strId)).append("\n");
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

