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
 * Данные сотрудника
 */
@ApiModel(description = "Данные сотрудника")
@Validated
public class Employee   {
    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("personnelNumber")
    private String personnelNumber = null;

    @JsonProperty("office")
    private String office = null;

    @JsonProperty("position")
    private String position = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Employee() {}

    /**
     * Создает экземпляр класса
     * @param surName Фамилия сотрудника
     * @param firstName Имя сотрудника
     * @param middleName Отчество сотрудника
     * @param personnelNumber Персональный номер сотрудника
     * @param office Офис сотрудника
     * @param position Должность сотрудника
     */
    public Employee(String surName, String firstName, String middleName, String personnelNumber, String office, String position) {
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.personnelNumber = personnelNumber;
        this.office = office;
        this.position = position;
    }

    /**
     * Фамилия сотрудника
    * @return Фамилия сотрудника
    **/
    @ApiModelProperty(value = "Фамилия сотрудника")
    


    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    /**
     * Имя сотрудника
    * @return Имя сотрудника
    **/
    @ApiModelProperty(value = "Имя сотрудника")
    


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество сотрудника
    * @return Отчество сотрудника
    **/
    @ApiModelProperty(value = "Отчество сотрудника")
    


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Персональный номер сотрудника
    * @return Персональный номер сотрудника
    **/
    @ApiModelProperty(value = "Персональный номер сотрудника")
    


    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }


    /**
     * Офис сотрудника
    * @return Офис сотрудника
    **/
    @ApiModelProperty(value = "Офис сотрудника")
    


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }


    /**
     * Должность сотрудника
    * @return Должность сотрудника
    **/
    @ApiModelProperty(value = "Должность сотрудника")
    


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(this.surName, employee.surName) &&
            Objects.equals(this.firstName, employee.firstName) &&
            Objects.equals(this.middleName, employee.middleName) &&
            Objects.equals(this.personnelNumber, employee.personnelNumber) &&
            Objects.equals(this.office, employee.office) &&
            Objects.equals(this.position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surName, firstName, middleName, personnelNumber, office, position);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Employee {\n");
        
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    personnelNumber: ").append(toIndentedString(personnelNumber)).append("\n");
        sb.append("    office: ").append(toIndentedString(office)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
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

