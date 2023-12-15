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
 * Результат выполнения запроса создания заказа оплаты договора страхования
 */
@ApiModel(description = "Результат выполнения запроса создания заказа оплаты договора страхования")
@Validated
public class CreateOrderResponse   {
    @JsonProperty("errorMessage")
    private String errorMessage = null;

    @JsonProperty("redirectUrl")
    private String redirectUrl = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateOrderResponse() {}

    /**
     * Создает экземпляр класса
     * @param errorMessage Текст ошибки, которая произошла в платёжном шлюзе
     * @param redirectUrl Адрес, на который нужно перенаправить браузер клиента в случае, когда заказ успешно создан
     */
    public CreateOrderResponse(String errorMessage, String redirectUrl) {
        this.errorMessage = errorMessage;
        this.redirectUrl = redirectUrl;
    }

    /**
     * Текст ошибки, которая произошла в платёжном шлюзе
    * @return Текст ошибки, которая произошла в платёжном шлюзе
    **/
    @ApiModelProperty(value = "Текст ошибки, которая произошла в платёжном шлюзе")
    


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Адрес, на который нужно перенаправить браузер клиента в случае, когда заказ успешно создан
    * @return Адрес, на который нужно перенаправить браузер клиента в случае, когда заказ успешно создан
    **/
    @ApiModelProperty(value = "Адрес, на который нужно перенаправить браузер клиента в случае, когда заказ успешно создан")
    


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateOrderResponse createOrderResponse = (CreateOrderResponse) o;
        return Objects.equals(this.errorMessage, createOrderResponse.errorMessage) &&
            Objects.equals(this.redirectUrl, createOrderResponse.redirectUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, redirectUrl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateOrderResponse {\n");
        
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    redirectUrl: ").append(toIndentedString(redirectUrl)).append("\n");
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

