package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AddressesDataHistory;
import ru.softlab.efr.services.insurance.model.rest.ClientDataHistory;
import ru.softlab.efr.services.insurance.model.rest.DocumentsDataHistory;
import ru.softlab.efr.services.insurance.model.rest.PhonesDataHistory;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные клиента об истории изменений
 */
@ApiModel(description = "Данные клиента об истории изменений")
@Validated
public class DataChangeHistoryClient   {
    @JsonProperty("clientData")
    @Valid
    private List<ClientDataHistory> clientData = null;

    @JsonProperty("clientDocuments")
    @Valid
    private List<DocumentsDataHistory> clientDocuments = null;

    @JsonProperty("clientPhones")
    @Valid
    private List<PhonesDataHistory> clientPhones = null;

    @JsonProperty("clientAddresses")
    @Valid
    private List<AddressesDataHistory> clientAddresses = null;


    /**
     * Создает пустой экземпляр класса
     */
    public DataChangeHistoryClient() {}

    /**
     * Создает экземпляр класса
     * @param clientData Список изменений основных данных
     * @param clientDocuments Список изменений документов
     * @param clientPhones Список изменений телефонов
     * @param clientAddresses Список изменений адресов
     */
    public DataChangeHistoryClient(List<ClientDataHistory> clientData, List<DocumentsDataHistory> clientDocuments, List<PhonesDataHistory> clientPhones, List<AddressesDataHistory> clientAddresses) {
        this.clientData = clientData;
        this.clientDocuments = clientDocuments;
        this.clientPhones = clientPhones;
        this.clientAddresses = clientAddresses;
    }

    public DataChangeHistoryClient addClientDataItem(ClientDataHistory clientDataItem) {
        if (this.clientData == null) {
            this.clientData = new ArrayList<ClientDataHistory>();
        }
        this.clientData.add(clientDataItem);
        return this;
    }

    /**
     * Список изменений основных данных
    * @return Список изменений основных данных
    **/
    @ApiModelProperty(value = "Список изменений основных данных")
    
  @Valid


    public List<ClientDataHistory> getClientData() {
        return clientData;
    }

    public void setClientData(List<ClientDataHistory> clientData) {
        this.clientData = clientData;
    }


    public DataChangeHistoryClient addClientDocumentsItem(DocumentsDataHistory clientDocumentsItem) {
        if (this.clientDocuments == null) {
            this.clientDocuments = new ArrayList<DocumentsDataHistory>();
        }
        this.clientDocuments.add(clientDocumentsItem);
        return this;
    }

    /**
     * Список изменений документов
    * @return Список изменений документов
    **/
    @ApiModelProperty(value = "Список изменений документов")
    
  @Valid


    public List<DocumentsDataHistory> getClientDocuments() {
        return clientDocuments;
    }

    public void setClientDocuments(List<DocumentsDataHistory> clientDocuments) {
        this.clientDocuments = clientDocuments;
    }


    public DataChangeHistoryClient addClientPhonesItem(PhonesDataHistory clientPhonesItem) {
        if (this.clientPhones == null) {
            this.clientPhones = new ArrayList<PhonesDataHistory>();
        }
        this.clientPhones.add(clientPhonesItem);
        return this;
    }

    /**
     * Список изменений телефонов
    * @return Список изменений телефонов
    **/
    @ApiModelProperty(value = "Список изменений телефонов")
    
  @Valid


    public List<PhonesDataHistory> getClientPhones() {
        return clientPhones;
    }

    public void setClientPhones(List<PhonesDataHistory> clientPhones) {
        this.clientPhones = clientPhones;
    }


    public DataChangeHistoryClient addClientAddressesItem(AddressesDataHistory clientAddressesItem) {
        if (this.clientAddresses == null) {
            this.clientAddresses = new ArrayList<AddressesDataHistory>();
        }
        this.clientAddresses.add(clientAddressesItem);
        return this;
    }

    /**
     * Список изменений адресов
    * @return Список изменений адресов
    **/
    @ApiModelProperty(value = "Список изменений адресов")
    
  @Valid


    public List<AddressesDataHistory> getClientAddresses() {
        return clientAddresses;
    }

    public void setClientAddresses(List<AddressesDataHistory> clientAddresses) {
        this.clientAddresses = clientAddresses;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataChangeHistoryClient dataChangeHistoryClient = (DataChangeHistoryClient) o;
        return Objects.equals(this.clientData, dataChangeHistoryClient.clientData) &&
            Objects.equals(this.clientDocuments, dataChangeHistoryClient.clientDocuments) &&
            Objects.equals(this.clientPhones, dataChangeHistoryClient.clientPhones) &&
            Objects.equals(this.clientAddresses, dataChangeHistoryClient.clientAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientData, clientDocuments, clientPhones, clientAddresses);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DataChangeHistoryClient {\n");
        
        sb.append("    clientData: ").append(toIndentedString(clientData)).append("\n");
        sb.append("    clientDocuments: ").append(toIndentedString(clientDocuments)).append("\n");
        sb.append("    clientPhones: ").append(toIndentedString(clientPhones)).append("\n");
        sb.append("    clientAddresses: ").append(toIndentedString(clientAddresses)).append("\n");
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

