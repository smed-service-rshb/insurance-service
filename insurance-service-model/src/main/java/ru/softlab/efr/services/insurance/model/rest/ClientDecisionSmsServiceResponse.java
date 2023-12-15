package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.ClientDecisionSmsServiceEnum;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ClientDecisionSmsServiceResponse
 */
@Validated
public class ClientDecisionSmsServiceResponse   {
    @JsonProperty("value")
    private ClientDecisionSmsServiceEnum value = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientDecisionSmsServiceResponse() {}

    /**
     * Создает экземпляр класса
     * @param value признак различия адресов
     */
    public ClientDecisionSmsServiceResponse(ClientDecisionSmsServiceEnum value) {
        this.value = value;
    }

    /**
     * признак различия адресов
    * @return признак различия адресов
    **/
    @ApiModelProperty(value = "признак различия адресов")
    
  @Valid


    public ClientDecisionSmsServiceEnum getValue() {
        return value;
    }

    public void setValue(ClientDecisionSmsServiceEnum value) {
        this.value = value;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientDecisionSmsServiceResponse clientDecisionSmsServiceResponse = (ClientDecisionSmsServiceResponse) o;
        return Objects.equals(this.value, clientDecisionSmsServiceResponse.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientDecisionSmsServiceResponse {\n");
        
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

