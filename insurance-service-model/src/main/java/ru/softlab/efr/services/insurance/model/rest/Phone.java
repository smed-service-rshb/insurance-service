package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.PhoneType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Телефон
 */
@ApiModel(description = "Телефон")
@Validated
public class Phone   {
    @JsonProperty("number")
    private String number = null;

    @JsonProperty("type")
    private PhoneType type = null;

    @JsonProperty("main")
    private Boolean main = null;

    @JsonProperty("notification")
    private Boolean notification = null;

    @JsonProperty("verified")
    private Boolean verified = null;

    @JsonProperty("token")
    private String token = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Phone() {}

    /**
     * Создает экземпляр класса
     * @param number Номер телефона
     * @param type Тип телефона
     * @param main Признак \"Основной\"
     * @param notification Для оповещения
     * @param verified Признак \"Проверенный\"
     * @param token Токен подтверждающий верификацию
     */
    public Phone(String number, PhoneType type, Boolean main, Boolean notification, Boolean verified, String token) {
        this.number = number;
        this.type = type;
        this.main = main;
        this.notification = notification;
        this.verified = verified;
        this.token = token;
    }

    /**
     * Номер телефона
    * @return Номер телефона
    **/
    @ApiModelProperty(required = true, value = "Номер телефона")
      @NotNull



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    /**
     * Тип телефона
    * @return Тип телефона
    **/
    @ApiModelProperty(required = true, value = "Тип телефона")
      @NotNull

  @Valid


    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }


    /**
     * Признак \"Основной\"
    * @return Признак \"Основной\"
    **/
    @ApiModelProperty(value = "Признак \"Основной\"")
    


    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }


    /**
     * Для оповещения
    * @return Для оповещения
    **/
    @ApiModelProperty(value = "Для оповещения")
    


    public Boolean isNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }


    /**
     * Признак \"Проверенный\"
    * @return Признак \"Проверенный\"
    **/
    @ApiModelProperty(value = "Признак \"Проверенный\"")
    


    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }


    /**
     * Токен подтверждающий верификацию
    * @return Токен подтверждающий верификацию
    **/
    @ApiModelProperty(value = "Токен подтверждающий верификацию")
    


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        return Objects.equals(this.number, phone.number) &&
            Objects.equals(this.type, phone.type) &&
            Objects.equals(this.main, phone.main) &&
            Objects.equals(this.notification, phone.notification) &&
            Objects.equals(this.verified, phone.verified) &&
            Objects.equals(this.token, phone.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type, main, notification, verified, token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Phone {\n");
        
        sb.append("    number: ").append(toIndentedString(number)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    main: ").append(toIndentedString(main)).append("\n");
        sb.append("    notification: ").append(toIndentedString(notification)).append("\n");
        sb.append("    verified: ").append(toIndentedString(verified)).append("\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

