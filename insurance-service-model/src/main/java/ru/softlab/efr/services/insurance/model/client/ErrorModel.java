package ru.softlab.efr.services.insurance.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Описание ошибки
 */
@ApiModel(description = "Описание ошибки")
@Validated
public class ErrorModel {
    @JsonProperty("errors")
    @Valid
    private List<String> errors = new ArrayList<String>();


    /**
     * Создает пустой экземпляр класса
     */
    public ErrorModel() {}

    /**
     * Создает экземпляр класса
     * @param errors 
     */
    public ErrorModel(List<String> errors) {
        this.errors = errors;
    }

    public ErrorModel addErrorsItem(String errorsItem) {
        this.errors.add(errorsItem);
        return this;
    }

    /**
    * Get errors
    * @return 
    **/
    @ApiModelProperty(required = true, value = "")
      @NotNull



    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


  @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorModel errorModel = (ErrorModel) o;
        return Objects.equals(this.errors, errorModel.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorModel {\n");

        sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

