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
 * Ответ на запрос выбора клиента из списка
 */
@ApiModel(description = "Ответ на запрос выбора клиента из списка")
@Validated
public class ClientDecisionResponse   {
    @JsonProperty("clientAnswered")
    private Boolean clientAnswered = null;

    @JsonProperty("clientAnswerId")
    private Long clientAnswerId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientDecisionResponse() {}

    /**
     * Создает экземпляр класса
     * @param clientAnswered ответ получен (клиент выбран из списка)
     * @param clientAnswerId ID выбранного клиента
     */
    public ClientDecisionResponse(Boolean clientAnswered, Long clientAnswerId) {
        this.clientAnswered = clientAnswered;
        this.clientAnswerId = clientAnswerId;
    }

    /**
     * ответ получен (клиент выбран из списка)
    * @return ответ получен (клиент выбран из списка)
    **/
    @ApiModelProperty(value = "ответ получен (клиент выбран из списка)")
    


    public Boolean isClientAnswered() {
        return clientAnswered;
    }

    public void setClientAnswered(Boolean clientAnswered) {
        this.clientAnswered = clientAnswered;
    }


    /**
     * ID выбранного клиента
    * @return ID выбранного клиента
    **/
    @ApiModelProperty(value = "ID выбранного клиента")
    


    public Long getClientAnswerId() {
        return clientAnswerId;
    }

    public void setClientAnswerId(Long clientAnswerId) {
        this.clientAnswerId = clientAnswerId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientDecisionResponse clientDecisionResponse = (ClientDecisionResponse) o;
        return Objects.equals(this.clientAnswered, clientDecisionResponse.clientAnswered) &&
            Objects.equals(this.clientAnswerId, clientDecisionResponse.clientAnswerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientAnswered, clientAnswerId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientDecisionResponse {\n");
        
        sb.append("    clientAnswered: ").append(toIndentedString(clientAnswered)).append("\n");
        sb.append("    clientAnswerId: ").append(toIndentedString(clientAnswerId)).append("\n");
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

