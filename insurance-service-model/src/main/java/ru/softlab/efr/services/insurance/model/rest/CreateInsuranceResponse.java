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
 * Результат создания договора страхования
 */
@ApiModel(description = "Результат создания договора страхования")
@Validated
public class CreateInsuranceResponse   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("contractNumber")
    private String contractNumber = null;

    @JsonProperty("holderId")
    private Long holderId = null;

    @JsonProperty("insuredId")
    private Long insuredId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateInsuranceResponse() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор договора страхования
     * @param contractNumber Номер договора
     * @param holderId ID анкеты страхователя
     * @param insuredId ID анкеты застрахованного
     */
    public CreateInsuranceResponse(Long id, String contractNumber, Long holderId, Long insuredId) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.holderId = holderId;
        this.insuredId = insuredId;
    }

    /**
     * Идентификатор договора страхования
    * @return Идентификатор договора страхования
    **/
    @ApiModelProperty(value = "Идентификатор договора страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Номер договора
    * @return Номер договора
    **/
    @ApiModelProperty(value = "Номер договора")
    


    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }


    /**
     * ID анкеты страхователя
    * @return ID анкеты страхователя
    **/
    @ApiModelProperty(value = "ID анкеты страхователя")
    


    public Long getHolderId() {
        return holderId;
    }

    public void setHolderId(Long holderId) {
        this.holderId = holderId;
    }


    /**
     * ID анкеты застрахованного
    * @return ID анкеты застрахованного
    **/
    @ApiModelProperty(value = "ID анкеты застрахованного")
    


    public Long getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(Long insuredId) {
        this.insuredId = insuredId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateInsuranceResponse createInsuranceResponse = (CreateInsuranceResponse) o;
        return Objects.equals(this.id, createInsuranceResponse.id) &&
            Objects.equals(this.contractNumber, createInsuranceResponse.contractNumber) &&
            Objects.equals(this.holderId, createInsuranceResponse.holderId) &&
            Objects.equals(this.insuredId, createInsuranceResponse.insuredId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contractNumber, holderId, insuredId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateInsuranceResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    contractNumber: ").append(toIndentedString(contractNumber)).append("\n");
        sb.append("    holderId: ").append(toIndentedString(holderId)).append("\n");
        sb.append("    insuredId: ").append(toIndentedString(insuredId)).append("\n");
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

