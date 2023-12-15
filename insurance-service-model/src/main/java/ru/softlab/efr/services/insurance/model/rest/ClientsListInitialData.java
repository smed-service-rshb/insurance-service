package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.SearchClientData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные для подпроцесса поиска клиента
 */
@ApiModel(description = "Данные для подпроцесса поиска клиента")
@Validated
public class ClientsListInitialData   {
    @JsonProperty("searchClientData")
    private SearchClientData searchClientData = null;

    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("cancel")
    private Boolean cancel = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientsListInitialData() {}

    /**
     * Создает экземпляр класса
     * @param searchClientData Данные формы поиска клиента
     * @param clientId id клиента
     * @param cancel Нажатали ли кнопка отмена
     */
    public ClientsListInitialData(SearchClientData searchClientData, String clientId, Boolean cancel) {
        this.searchClientData = searchClientData;
        this.clientId = clientId;
        this.cancel = cancel;
    }

    /**
     * Данные формы поиска клиента
    * @return Данные формы поиска клиента
    **/
    @ApiModelProperty(value = "Данные формы поиска клиента")
    
  @Valid


    public SearchClientData getSearchClientData() {
        return searchClientData;
    }

    public void setSearchClientData(SearchClientData searchClientData) {
        this.searchClientData = searchClientData;
    }


    /**
     * id клиента
    * @return id клиента
    **/
    @ApiModelProperty(value = "id клиента")
    


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    /**
     * Нажатали ли кнопка отмена
    * @return Нажатали ли кнопка отмена
    **/
    @ApiModelProperty(value = "Нажатали ли кнопка отмена")
    


    public Boolean isCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientsListInitialData clientsListInitialData = (ClientsListInitialData) o;
        return Objects.equals(this.searchClientData, clientsListInitialData.searchClientData) &&
            Objects.equals(this.clientId, clientsListInitialData.clientId) &&
            Objects.equals(this.cancel, clientsListInitialData.cancel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchClientData, clientId, cancel);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientsListInitialData {\n");
        
        sb.append("    searchClientData: ").append(toIndentedString(searchClientData)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    cancel: ").append(toIndentedString(cancel)).append("\n");
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

