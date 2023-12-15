package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.ListClientsResponse;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация по найденным клиентам
 */
@ApiModel(description = "Информация по найденным клиентам")
@Validated
public class FoundMultipleClients   {
    @JsonProperty("insured")
    private ListClientsResponse insured = null;

    @JsonProperty("holdered")
    private ListClientsResponse holdered = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FoundMultipleClients() {}

    /**
     * Создает экземпляр класса
     * @param insured Список найденных застрахованных клиентов
     * @param holdered Список найденных страхователей клиентов
     */
    public FoundMultipleClients(ListClientsResponse insured, ListClientsResponse holdered) {
        this.insured = insured;
        this.holdered = holdered;
    }

    /**
     * Список найденных застрахованных клиентов
    * @return Список найденных застрахованных клиентов
    **/
    @ApiModelProperty(value = "Список найденных застрахованных клиентов")
    
  @Valid


    public ListClientsResponse getInsured() {
        return insured;
    }

    public void setInsured(ListClientsResponse insured) {
        this.insured = insured;
    }


    /**
     * Список найденных страхователей клиентов
    * @return Список найденных страхователей клиентов
    **/
    @ApiModelProperty(value = "Список найденных страхователей клиентов")
    
  @Valid


    public ListClientsResponse getHoldered() {
        return holdered;
    }

    public void setHoldered(ListClientsResponse holdered) {
        this.holdered = holdered;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoundMultipleClients foundMultipleClients = (FoundMultipleClients) o;
        return Objects.equals(this.insured, foundMultipleClients.insured) &&
            Objects.equals(this.holdered, foundMultipleClients.holdered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insured, holdered);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FoundMultipleClients {\n");
        
        sb.append("    insured: ").append(toIndentedString(insured)).append("\n");
        sb.append("    holdered: ").append(toIndentedString(holdered)).append("\n");
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

