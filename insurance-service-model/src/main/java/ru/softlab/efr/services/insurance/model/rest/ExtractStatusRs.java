package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.ExtractStatus;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ответ запроса получения статуса формирования отчета
 */
@ApiModel(description = "Ответ запроса получения статуса формирования отчета")
@Validated
public class ExtractStatusRs   {
    @JsonProperty("status")
    private ExtractStatus status = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ExtractStatusRs() {}

    /**
     * Создает экземпляр класса
     * @param status Статус операции
     */
    public ExtractStatusRs(ExtractStatus status) {
        this.status = status;
    }

    /**
     * Статус операции
    * @return Статус операции
    **/
    @ApiModelProperty(value = "Статус операции")
    
  @Valid


    public ExtractStatus getStatus() {
        return status;
    }

    public void setStatus(ExtractStatus status) {
        this.status = status;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtractStatusRs extractStatusRs = (ExtractStatusRs) o;
        return Objects.equals(this.status, extractStatusRs.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ExtractStatusRs {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

