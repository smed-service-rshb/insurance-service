package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.Address;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об измененном адресе клиента
 */
@ApiModel(description = "Информация об измененном адресе клиента")
@Validated
public class AddressesDataHistory   {
    @JsonProperty("address")
    private Address address = null;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate = null;

    @JsonProperty("userFullName")
    private String userFullName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AddressesDataHistory() {}

    /**
     * Создает экземпляр класса
     * @param address Информация об адресе клиента
     * @param lastModifiedDate Дата изменения
     * @param userFullName ФИО пользователя изменившего данные
     */
    public AddressesDataHistory(Address address, String lastModifiedDate, String userFullName) {
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
        this.userFullName = userFullName;
    }

    /**
     * Информация об адресе клиента
    * @return Информация об адресе клиента
    **/
    @ApiModelProperty(value = "Информация об адресе клиента")
    
  @Valid


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
        AddressesDataHistory addressesDataHistory = (AddressesDataHistory) o;
        return Objects.equals(this.address, addressesDataHistory.address) &&
            Objects.equals(this.lastModifiedDate, addressesDataHistory.lastModifiedDate) &&
            Objects.equals(this.userFullName, addressesDataHistory.userFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, lastModifiedDate, userFullName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AddressesDataHistory {\n");
        
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
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

