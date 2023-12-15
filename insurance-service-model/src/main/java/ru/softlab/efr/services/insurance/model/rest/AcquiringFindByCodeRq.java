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
 * Запрос поиска договора по коду для оплаты в ЛК
 */
@ApiModel(description = "Запрос поиска договора по коду для оплаты в ЛК")
@Validated
public class AcquiringFindByCodeRq   {
    @JsonProperty("code")
    private String code = null;

    @JsonProperty("surName")
    private String surName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AcquiringFindByCodeRq() {}

    /**
     * Создает экземпляр класса
     * @param code Код договора
     * @param surName Фамилия клиента
     */
    public AcquiringFindByCodeRq(String code, String surName) {
        this.code = code;
        this.surName = surName;
    }

    /**
     * Код договора
    * @return Код договора
    **/
    @ApiModelProperty(required = true, value = "Код договора")
      @NotNull



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Фамилия клиента
    * @return Фамилия клиента
    **/
    @ApiModelProperty(value = "Фамилия клиента")
    


    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquiringFindByCodeRq acquiringFindByCodeRq = (AcquiringFindByCodeRq) o;
        return Objects.equals(this.code, acquiringFindByCodeRq.code) &&
            Objects.equals(this.surName, acquiringFindByCodeRq.surName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, surName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquiringFindByCodeRq {\n");
        
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
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

