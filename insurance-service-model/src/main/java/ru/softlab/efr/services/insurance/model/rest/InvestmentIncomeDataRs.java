package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.InvestmentIncomeDataRsIncomesSet;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ответ на запрос получения списка с данными инвестиционного дохода
 */
@ApiModel(description = "Ответ на запрос получения списка с данными инвестиционного дохода")
@Validated
public class InvestmentIncomeDataRs   {
    @JsonProperty("incomesSet")
    @Valid
    private List<InvestmentIncomeDataRsIncomesSet> incomesSet = null;


    /**
     * Создает пустой экземпляр класса
     */
    public InvestmentIncomeDataRs() {}

    /**
     * Создает экземпляр класса
     * @param incomesSet 
     */
    public InvestmentIncomeDataRs(List<InvestmentIncomeDataRsIncomesSet> incomesSet) {
        this.incomesSet = incomesSet;
    }

    public InvestmentIncomeDataRs addIncomesSetItem(InvestmentIncomeDataRsIncomesSet incomesSetItem) {
        if (this.incomesSet == null) {
            this.incomesSet = new ArrayList<InvestmentIncomeDataRsIncomesSet>();
        }
        this.incomesSet.add(incomesSetItem);
        return this;
    }

    /**
    * Get incomesSet
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<InvestmentIncomeDataRsIncomesSet> getIncomesSet() {
        return incomesSet;
    }

    public void setIncomesSet(List<InvestmentIncomeDataRsIncomesSet> incomesSet) {
        this.incomesSet = incomesSet;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvestmentIncomeDataRs investmentIncomeDataRs = (InvestmentIncomeDataRs) o;
        return Objects.equals(this.incomesSet, investmentIncomeDataRs.incomesSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(incomesSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InvestmentIncomeDataRs {\n");
        
        sb.append("    incomesSet: ").append(toIndentedString(incomesSet)).append("\n");
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

