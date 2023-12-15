package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Запрос поиска клиента при создании договора
 */
@ApiModel(description = "Запрос поиска клиента при создании договора")
@Validated
public class FindClientRq   {
    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("phoneNumber")
    private String phoneNumber = null;

    @JsonProperty("clientId")
    private Long clientId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FindClientRq() {}

    /**
     * Создает экземпляр класса
     * @param surName Фамилия клиента
     * @param firstName Имя клиента
     * @param middleName Отчество клиента
     * @param birthDate Дата рождения
     * @param phoneNumber Номер мобильного телефона клиента (71111111111)
     * @param clientId Идентификатор существующего клиента
     */
    public FindClientRq(String surName, String firstName, String middleName, LocalDate birthDate, String phoneNumber, Long clientId) {
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.clientId = clientId;
    }

    /**
     * Фамилия клиента
    * @return Фамилия клиента
    **/
    @ApiModelProperty(required = true, value = "Фамилия клиента")
      @NotNull

 @Size(min=1,max=150)

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    /**
     * Имя клиента
    * @return Имя клиента
    **/
    @ApiModelProperty(required = true, value = "Имя клиента")
      @NotNull

 @Size(min=1,max=150)

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество клиента
    * @return Отчество клиента
    **/
    @ApiModelProperty(value = "Отчество клиента")
    
 @Size(min=1,max=150)

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Дата рождения
    * @return Дата рождения
    **/
    @ApiModelProperty(required = true, value = "Дата рождения")
      @NotNull

  @Valid


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Номер мобильного телефона клиента (71111111111)
    * @return Номер мобильного телефона клиента (71111111111)
    **/
    @ApiModelProperty(required = true, value = "Номер мобильного телефона клиента (71111111111)")
      @NotNull

 @Size(min=11,max=11)

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Идентификатор существующего клиента
    * @return Идентификатор существующего клиента
    **/
    @ApiModelProperty(value = "Идентификатор существующего клиента")
    


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FindClientRq findClientRq = (FindClientRq) o;
        return Objects.equals(this.surName, findClientRq.surName) &&
            Objects.equals(this.firstName, findClientRq.firstName) &&
            Objects.equals(this.middleName, findClientRq.middleName) &&
            Objects.equals(this.birthDate, findClientRq.birthDate) &&
            Objects.equals(this.phoneNumber, findClientRq.phoneNumber) &&
            Objects.equals(this.clientId, findClientRq.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surName, firstName, middleName, birthDate, phoneNumber, clientId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FindClientRq {\n");
        
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
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

