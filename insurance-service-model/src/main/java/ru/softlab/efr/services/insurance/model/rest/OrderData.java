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
 * Заказ клиента на оплату договора страхования
 */
@ApiModel(description = "Заказ клиента на оплату договора страхования")
@Validated
public class OrderData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("creationDateTime")
    private String creationDateTime = null;

    @JsonProperty("contract")
    private Long contract = null;

    @JsonProperty("client")
    private Long client = null;

    @JsonProperty("amount")
    private Long amount = null;

    @JsonProperty("currency")
    private Integer currency = null;

    @JsonProperty("extId")
    private String extId = null;

    @JsonProperty("redirectUrl")
    private String redirectUrl = null;

    @JsonProperty("orderCode")
    private Integer orderCode = null;

    @JsonProperty("errorCode")
    private Integer errorCode = null;

    @JsonProperty("errorMessage")
    private String errorMessage = null;

    @JsonProperty("pan")
    private String pan = null;

    @JsonProperty("expiration")
    private Integer expiration = null;

    @JsonProperty("cardHolderName")
    private String cardHolderName = null;

    @JsonProperty("ip")
    private String ip = null;

    @JsonProperty("url")
    private String url = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OrderData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор заказа
     * @param creationDateTime Дата и время создания заказа.
     * @param contract Идентификатор договора
     * @param client Идентификатор клиента
     * @param amount Сумма платежа в копейках (или центах)
     * @param currency Код валюты платежа ISO 4217. Если не указан, считается равным коду валюты по умолчанию.
     * @param extId Номер заказа в платежной системе. Уникален в пределах системы. Отсутствует если регистрация заказа на удалась по причине ошибки, детализированной в ErrorCode.
     * @param redirectUrl URL платежной формы, на который надо перенаправить броузер клиента. Не возвращается если регистрация заказа не удалась по причине ошибки, детализированной в ErrorCode
     * @param orderCode Код состояния заказа.
     * @param errorCode Код ошибки.
     * @param errorMessage Описание ошибки на языке, переданном в параметре Language в запросе.
     * @param pan Маскированный номер карты, которая использовалась для оплаты. Указан только после оплаты заказа
     * @param expiration Срок истечения действия карты в формате YYYYMM. Указан только после оплаты заказа.
     * @param cardHolderName Имя держателя карты. Указан только после оплаты заказа.
     * @param ip IP адрес пользователя, который оплачивал заказ
     * @param url URL-адрес (document.location.href), с которого осуществлялся вызов сервиса и на который потребуется вернуть браузер из платёжного шлюза
     */
    public OrderData(Long id, String creationDateTime, Long contract, Long client, Long amount, Integer currency, String extId, String redirectUrl, Integer orderCode, Integer errorCode, String errorMessage, String pan, Integer expiration, String cardHolderName, String ip, String url) {
        this.id = id;
        this.creationDateTime = creationDateTime;
        this.contract = contract;
        this.client = client;
        this.amount = amount;
        this.currency = currency;
        this.extId = extId;
        this.redirectUrl = redirectUrl;
        this.orderCode = orderCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.pan = pan;
        this.expiration = expiration;
        this.cardHolderName = cardHolderName;
        this.ip = ip;
        this.url = url;
    }

    /**
     * Идентификатор заказа
    * @return Идентификатор заказа
    **/
    @ApiModelProperty(value = "Идентификатор заказа")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Дата и время создания заказа.
    * @return Дата и время создания заказа.
    **/
    @ApiModelProperty(value = "Дата и время создания заказа.")
    


    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }


    /**
     * Идентификатор договора
    * @return Идентификатор договора
    **/
    @ApiModelProperty(value = "Идентификатор договора")
    


    public Long getContract() {
        return contract;
    }

    public void setContract(Long contract) {
        this.contract = contract;
    }


    /**
     * Идентификатор клиента
    * @return Идентификатор клиента
    **/
    @ApiModelProperty(value = "Идентификатор клиента")
    


    public Long getClient() {
        return client;
    }

    public void setClient(Long client) {
        this.client = client;
    }


    /**
     * Сумма платежа в копейках (или центах)
    * @return Сумма платежа в копейках (или центах)
    **/
    @ApiModelProperty(value = "Сумма платежа в копейках (или центах)")
    


    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }


    /**
     * Код валюты платежа ISO 4217. Если не указан, считается равным коду валюты по умолчанию.
    * @return Код валюты платежа ISO 4217. Если не указан, считается равным коду валюты по умолчанию.
    **/
    @ApiModelProperty(value = "Код валюты платежа ISO 4217. Если не указан, считается равным коду валюты по умолчанию.")
    


    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }


    /**
     * Номер заказа в платежной системе. Уникален в пределах системы. Отсутствует если регистрация заказа на удалась по причине ошибки, детализированной в ErrorCode.
    * @return Номер заказа в платежной системе. Уникален в пределах системы. Отсутствует если регистрация заказа на удалась по причине ошибки, детализированной в ErrorCode.
    **/
    @ApiModelProperty(value = "Номер заказа в платежной системе. Уникален в пределах системы. Отсутствует если регистрация заказа на удалась по причине ошибки, детализированной в ErrorCode.")
    


    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }


    /**
     * URL платежной формы, на который надо перенаправить броузер клиента. Не возвращается если регистрация заказа не удалась по причине ошибки, детализированной в ErrorCode
    * @return URL платежной формы, на который надо перенаправить броузер клиента. Не возвращается если регистрация заказа не удалась по причине ошибки, детализированной в ErrorCode
    **/
    @ApiModelProperty(value = "URL платежной формы, на который надо перенаправить броузер клиента. Не возвращается если регистрация заказа не удалась по причине ошибки, детализированной в ErrorCode")
    


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


    /**
     * Код состояния заказа.
    * @return Код состояния заказа.
    **/
    @ApiModelProperty(value = "Код состояния заказа.")
    


    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }


    /**
     * Код ошибки.
    * @return Код ошибки.
    **/
    @ApiModelProperty(value = "Код ошибки.")
    


    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Описание ошибки на языке, переданном в параметре Language в запросе.
    * @return Описание ошибки на языке, переданном в параметре Language в запросе.
    **/
    @ApiModelProperty(value = "Описание ошибки на языке, переданном в параметре Language в запросе.")
    


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Маскированный номер карты, которая использовалась для оплаты. Указан только после оплаты заказа
    * @return Маскированный номер карты, которая использовалась для оплаты. Указан только после оплаты заказа
    **/
    @ApiModelProperty(value = "Маскированный номер карты, которая использовалась для оплаты. Указан только после оплаты заказа")
    


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }


    /**
     * Срок истечения действия карты в формате YYYYMM. Указан только после оплаты заказа.
    * @return Срок истечения действия карты в формате YYYYMM. Указан только после оплаты заказа.
    **/
    @ApiModelProperty(value = "Срок истечения действия карты в формате YYYYMM. Указан только после оплаты заказа.")
    


    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }


    /**
     * Имя держателя карты. Указан только после оплаты заказа.
    * @return Имя держателя карты. Указан только после оплаты заказа.
    **/
    @ApiModelProperty(value = "Имя держателя карты. Указан только после оплаты заказа.")
    


    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }


    /**
     * IP адрес пользователя, который оплачивал заказ
    * @return IP адрес пользователя, который оплачивал заказ
    **/
    @ApiModelProperty(value = "IP адрес пользователя, который оплачивал заказ")
    


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * URL-адрес (document.location.href), с которого осуществлялся вызов сервиса и на который потребуется вернуть браузер из платёжного шлюза
    * @return URL-адрес (document.location.href), с которого осуществлялся вызов сервиса и на который потребуется вернуть браузер из платёжного шлюза
    **/
    @ApiModelProperty(value = "URL-адрес (document.location.href), с которого осуществлялся вызов сервиса и на который потребуется вернуть браузер из платёжного шлюза")
    


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderData orderData = (OrderData) o;
        return Objects.equals(this.id, orderData.id) &&
            Objects.equals(this.creationDateTime, orderData.creationDateTime) &&
            Objects.equals(this.contract, orderData.contract) &&
            Objects.equals(this.client, orderData.client) &&
            Objects.equals(this.amount, orderData.amount) &&
            Objects.equals(this.currency, orderData.currency) &&
            Objects.equals(this.extId, orderData.extId) &&
            Objects.equals(this.redirectUrl, orderData.redirectUrl) &&
            Objects.equals(this.orderCode, orderData.orderCode) &&
            Objects.equals(this.errorCode, orderData.errorCode) &&
            Objects.equals(this.errorMessage, orderData.errorMessage) &&
            Objects.equals(this.pan, orderData.pan) &&
            Objects.equals(this.expiration, orderData.expiration) &&
            Objects.equals(this.cardHolderName, orderData.cardHolderName) &&
            Objects.equals(this.ip, orderData.ip) &&
            Objects.equals(this.url, orderData.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateTime, contract, client, amount, currency, extId, redirectUrl, orderCode, errorCode, errorMessage, pan, expiration, cardHolderName, ip, url);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    creationDateTime: ").append(toIndentedString(creationDateTime)).append("\n");
        sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
        sb.append("    client: ").append(toIndentedString(client)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    extId: ").append(toIndentedString(extId)).append("\n");
        sb.append("    redirectUrl: ").append(toIndentedString(redirectUrl)).append("\n");
        sb.append("    orderCode: ").append(toIndentedString(orderCode)).append("\n");
        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    pan: ").append(toIndentedString(pan)).append("\n");
        sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
        sb.append("    cardHolderName: ").append(toIndentedString(cardHolderName)).append("\n");
        sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

