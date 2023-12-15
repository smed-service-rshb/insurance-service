package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.Client;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об измененном клиенте
 */
@ApiModel(description = "Информация об измененном клиенте")
@Validated
public class ClientDataHistory   {
    @JsonProperty("client")
    private Client client = null;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate = null;

    @JsonProperty("userFullName")
    private String userFullName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientDataHistory() {}

    /**
     * Создает экземпляр класса
     * @param client Вся информация о клиенте
     * @param lastModifiedDate Дата изменения
     * @param userFullName ФИО пользователя изменившего данные
     */
    public ClientDataHistory(Client client, String lastModifiedDate, String userFullName) {
        this.client = client;
        this.lastModifiedDate = lastModifiedDate;
        this.userFullName = userFullName;
    }

    /**
     * Вся информация о клиенте
    * @return Вся информация о клиенте
    **/
    @ApiModelProperty(value = "Вся информация о клиенте")
    
  @Valid


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Дата изменения
    * @return Дата изменения
    **/
    @ApiModelProperty(value = "Дата изменения")
    


    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    /**
     * ФИО пользователя изменившего данные
    * @return ФИО пользователя изменившего данные
    **/
    @ApiModelProperty(value = "ФИО пользователя изменившего данные")
    


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientDataHistory clientDataHistory = (ClientDataHistory) o;
        return Objects.equals(this.client, clientDataHistory.client) &&
            Objects.equals(this.lastModifiedDate, clientDataHistory.lastModifiedDate) &&
            Objects.equals(this.userFullName, clientDataHistory.userFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, lastModifiedDate, userFullName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientDataHistory {\n");
        
        sb.append("    client: ").append(toIndentedString(client)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    userFullName: ").append(toIndentedString(userFullName)).append("\n");
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

