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
 * Элемент с объектом для обращения отправляемый пользователем для добавления
 */
@ApiModel(description = "Элемент с объектом для обращения отправляемый пользователем для добавления")
@Validated
public class ClientRequestRq   {
    @JsonProperty("requestsTopicId")
    private Long requestsTopicId = null;

    @JsonProperty("insuranceId")
    private Long insuranceId = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("requestText")
    private String requestText = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientRequestRq() {}

    /**
     * Создает экземпляр класса
     * @param requestsTopicId Id обращения
     * @param insuranceId Id страхования
     * @param phone Номер телефона
     * @param email Адрес электронной почты
     * @param requestText текст сообщения
     */
    public ClientRequestRq(Long requestsTopicId, Long insuranceId, String phone, String email, String requestText) {
        this.requestsTopicId = requestsTopicId;
        this.insuranceId = insuranceId;
        this.phone = phone;
        this.email = email;
        this.requestText = requestText;
    }

    /**
     * Id обращения
    * @return Id обращения
    **/
    @ApiModelProperty(required = true, value = "Id обращения")
      @NotNull



    public Long getRequestsTopicId() {
        return requestsTopicId;
    }

    public void setRequestsTopicId(Long requestsTopicId) {
        this.requestsTopicId = requestsTopicId;
    }


    /**
     * Id страхования
    * @return Id страхования
    **/
    @ApiModelProperty(value = "Id страхования")
    


    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }


    /**
     * Номер телефона
    * @return Номер телефона
    **/
    @ApiModelProperty(value = "Номер телефона")
    


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * Адрес электронной почты
    * @return Адрес электронной почты
    **/
    @ApiModelProperty(required = true, value = "Адрес электронной почты")
      @NotNull

 @Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(((\\w+)|(\\w+-\\w+))\\.)+[a-zA-Z]{2,6})$)")

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * текст сообщения
    * @return текст сообщения
    **/
    @ApiModelProperty(required = true, value = "текст сообщения")
      @NotNull



    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientRequestRq clientRequestRq = (ClientRequestRq) o;
        return Objects.equals(this.requestsTopicId, clientRequestRq.requestsTopicId) &&
            Objects.equals(this.insuranceId, clientRequestRq.insuranceId) &&
            Objects.equals(this.phone, clientRequestRq.phone) &&
            Objects.equals(this.email, clientRequestRq.email) &&
            Objects.equals(this.requestText, clientRequestRq.requestText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestsTopicId, insuranceId, phone, email, requestText);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientRequestRq {\n");
        
        sb.append("    requestsTopicId: ").append(toIndentedString(requestsTopicId)).append("\n");
        sb.append("    insuranceId: ").append(toIndentedString(insuranceId)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    requestText: ").append(toIndentedString(requestText)).append("\n");
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

