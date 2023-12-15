package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Контейнер со всеми результатами проверки договора страхования
 */
@ApiModel(description = "Контейнер со всеми результатами проверки договора страхования")
@Validated
public class ContractValidationResult   {
    @JsonProperty("contractId")
    private Long contractId = null;

    @JsonProperty("validationErrors")
    @Valid
    private List<CheckModel> validationErrors = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ContractValidationResult() {}

    /**
     * Создает экземпляр класса
     * @param contractId Идентификатор договора страхования, если был сохранён
     * @param validationErrors 
     */
    public ContractValidationResult(Long contractId, List<CheckModel> validationErrors) {
        this.contractId = contractId;
        this.validationErrors = validationErrors;
    }

    /**
     * Идентификатор договора страхования, если был сохранён
    * @return Идентификатор договора страхования, если был сохранён
    **/
    @ApiModelProperty(value = "Идентификатор договора страхования, если был сохранён")
    


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }


    public ContractValidationResult addValidationErrorsItem(CheckModel validationErrorsItem) {
        if (this.validationErrors == null) {
            this.validationErrors = new ArrayList<CheckModel>();
        }
        this.validationErrors.add(validationErrorsItem);
        return this;
    }

    /**
    * Get validationErrors
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<CheckModel> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<CheckModel> validationErrors) {
        this.validationErrors = validationErrors;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractValidationResult contractValidationResult = (ContractValidationResult) o;
        return Objects.equals(this.contractId, contractValidationResult.contractId) &&
            Objects.equals(this.validationErrors, contractValidationResult.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractId, validationErrors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContractValidationResult {\n");
        
        sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
        sb.append("    validationErrors: ").append(toIndentedString(validationErrors)).append("\n");
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

