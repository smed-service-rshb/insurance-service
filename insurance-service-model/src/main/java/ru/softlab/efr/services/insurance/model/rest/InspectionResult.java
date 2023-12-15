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
 * Результат проверки определенного справочника
 */
@ApiModel(description = "Результат проверки определенного справочника")
@Validated
public class InspectionResult   {
    @JsonProperty("lastDateCheck")
    private String lastDateCheck = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("result")
    private String result = null;


    /**
     * Создает пустой экземпляр класса
     */
    public InspectionResult() {}

    /**
     * Создает экземпляр класса
     * @param lastDateCheck Последняя дата проверки
     * @param type Тип проверяемого справочника
     * @param result Результат проверки
     */
    public InspectionResult(String lastDateCheck, String type, String result) {
        this.lastDateCheck = lastDateCheck;
        this.type = type;
        this.result = result;
    }

    /**
     * Последняя дата проверки
    * @return Последняя дата проверки
    **/
    @ApiModelProperty(value = "Последняя дата проверки")
    


    public String getLastDateCheck() {
        return lastDateCheck;
    }

    public void setLastDateCheck(String lastDateCheck) {
        this.lastDateCheck = lastDateCheck;
    }


    /**
     * Тип проверяемого справочника
    * @return Тип проверяемого справочника
    **/
    @ApiModelProperty(value = "Тип проверяемого справочника")
    


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * Результат проверки
    * @return Результат проверки
    **/
    @ApiModelProperty(value = "Результат проверки")
    


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InspectionResult inspectionResult = (InspectionResult) o;
        return Objects.equals(this.lastDateCheck, inspectionResult.lastDateCheck) &&
            Objects.equals(this.type, inspectionResult.type) &&
            Objects.equals(this.result, inspectionResult.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastDateCheck, type, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InspectionResult {\n");
        
        sb.append("    lastDateCheck: ").append(toIndentedString(lastDateCheck)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    result: ").append(toIndentedString(result)).append("\n");
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

