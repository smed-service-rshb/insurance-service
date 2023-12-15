package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.DictType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные для запуска проверки клиентов
 */
@ApiModel(description = "Данные для запуска проверки клиентов")
@Validated
public class CheckClientRq   {
    @JsonProperty("dictTypes")
    @Valid
    private List<DictType> dictTypes = null;

    @JsonProperty("allClientCheck")
    private Boolean allClientCheck = null;

    @JsonProperty("clientIds")
    @Valid
    private List<Long> clientIds = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CheckClientRq() {}

    /**
     * Создает экземпляр класса
     * @param dictTypes 
     * @param allClientCheck Провести проверку всех клиентов
     * @param clientIds Перечень идентификаторов клиентов, для которых требуется запустить проверку
     */
    public CheckClientRq(List<DictType> dictTypes, Boolean allClientCheck, List<Long> clientIds) {
        this.dictTypes = dictTypes;
        this.allClientCheck = allClientCheck;
        this.clientIds = clientIds;
    }

    public CheckClientRq addDictTypesItem(DictType dictTypesItem) {
        if (this.dictTypes == null) {
            this.dictTypes = new ArrayList<DictType>();
        }
        this.dictTypes.add(dictTypesItem);
        return this;
    }

    /**
    * Get dictTypes
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<DictType> getDictTypes() {
        return dictTypes;
    }

    public void setDictTypes(List<DictType> dictTypes) {
        this.dictTypes = dictTypes;
    }


    /**
     * Провести проверку всех клиентов
    * @return Провести проверку всех клиентов
    **/
    @ApiModelProperty(value = "Провести проверку всех клиентов")
    


    public Boolean isAllClientCheck() {
        return allClientCheck;
    }

    public void setAllClientCheck(Boolean allClientCheck) {
        this.allClientCheck = allClientCheck;
    }


    public CheckClientRq addClientIdsItem(Long clientIdsItem) {
        if (this.clientIds == null) {
            this.clientIds = new ArrayList<Long>();
        }
        this.clientIds.add(clientIdsItem);
        return this;
    }

    /**
     * Перечень идентификаторов клиентов, для которых требуется запустить проверку
    * @return Перечень идентификаторов клиентов, для которых требуется запустить проверку
    **/
    @ApiModelProperty(value = "Перечень идентификаторов клиентов, для которых требуется запустить проверку")
    


    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CheckClientRq checkClientRq = (CheckClientRq) o;
        return Objects.equals(this.dictTypes, checkClientRq.dictTypes) &&
            Objects.equals(this.allClientCheck, checkClientRq.allClientCheck) &&
            Objects.equals(this.clientIds, checkClientRq.clientIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dictTypes, allClientCheck, clientIds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CheckClientRq {\n");
        
        sb.append("    dictTypes: ").append(toIndentedString(dictTypes)).append("\n");
        sb.append("    allClientCheck: ").append(toIndentedString(allClientCheck)).append("\n");
        sb.append("    clientIds: ").append(toIndentedString(clientIds)).append("\n");
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

