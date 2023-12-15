package ru.softlab.efr.services.insurance.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Информация по найденным клиентам
 */
@ApiModel(description = "Информация по найденным клиентам")
@Validated
public class ListClientsResponse   {
    @JsonProperty("clients")
    @Valid
    private List<FoundClient> clients = null;

    @JsonProperty("totalFounds")
    private Integer totalFounds = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ListClientsResponse() {}

    /**
     * Создает экземпляр класса
     * @param clients Список найденных клиентов
     * @param totalFounds Количество найденных клиентов
     */
    public ListClientsResponse(List<FoundClient> clients, Integer totalFounds) {
        this.clients = clients;
        this.totalFounds = totalFounds;
    }

    public ListClientsResponse addClientsItem(FoundClient clientsItem) {
        if (this.clients == null) {
            this.clients = new ArrayList<FoundClient>();
        }
        this.clients.add(clientsItem);
        return this;
    }

    /**
     * Список найденных клиентов
    * @return Список найденных клиентов
    **/
    @ApiModelProperty(value = "Список найденных клиентов")
    
  @Valid


    public List<FoundClient> getClients() {
        return clients;
    }

    public void setClients(List<FoundClient> clients) {
        this.clients = clients;
    }


    /**
     * Количество найденных клиентов
    * @return Количество найденных клиентов
    **/
    @ApiModelProperty(value = "Количество найденных клиентов")
    


    public Integer getTotalFounds() {
        return totalFounds;
    }

    public void setTotalFounds(Integer totalFounds) {
        this.totalFounds = totalFounds;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListClientsResponse listClientsResponse = (ListClientsResponse) o;
        return Objects.equals(this.clients, listClientsResponse.clients) &&
            Objects.equals(this.totalFounds, listClientsResponse.totalFounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clients, totalFounds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListClientsResponse {\n");
        
        sb.append("    clients: ").append(toIndentedString(clients)).append("\n");
        sb.append("    totalFounds: ").append(toIndentedString(totalFounds)).append("\n");
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

