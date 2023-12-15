package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.Phone;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об измененном номере телефона
 */
@ApiModel(description = "Информация об измененном номере телефона")
@Validated
public class PhonesDataHistory   {
    @JsonProperty("phone")
    private Phone phone = null;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate = null;

    @JsonProperty("userFullName")
    private String userFullName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public PhonesDataHistory() {}

    /**
     * Создает экземпляр класса
     * @param phone Информация о номере телефона
     * @param lastModifiedDate Дата изменения
     * @param userFullName ФИО пользователя изменившего данные
     */
    public PhonesDataHistory(Phone phone, String lastModifiedDate, String userFullName) {
        this.phone = phone;
        this.lastModifiedDate = lastModifiedDate;
        this.userFullName = userFullName;
    }

    /**
     * Информация о номере телефона
    * @return Информация о номере телефона
    **/
    @ApiModelProperty(value = "Информация о номере телефона")
    
  @Valid


    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
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
        PhonesDataHistory phonesDataHistory = (PhonesDataHistory) o;
        return Objects.equals(this.phone, phonesDataHistory.phone) &&
            Objects.equals(this.lastModifiedDate, phonesDataHistory.lastModifiedDate) &&
            Objects.equals(this.userFullName, phonesDataHistory.userFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, lastModifiedDate, userFullName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PhonesDataHistory {\n");
        
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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

