package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.IncomeData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InvestmentIncomeDataRsIncomesSet
 */
@Validated
public class InvestmentIncomeDataRsIncomesSet   {
    @JsonProperty("strategyId")
    private Long strategyId = null;

    @JsonProperty("strategy")
    private String strategy = null;

    @JsonProperty("ticker")
    private String ticker = null;

    @JsonProperty("currentIncome")
    private String currentIncome = null;

    @JsonProperty("incomes")
    @Valid
    private List<IncomeData> incomes = null;


    /**
     * Создает пустой экземпляр класса
     */
    public InvestmentIncomeDataRsIncomesSet() {}

    /**
     * Создает экземпляр класса
     * @param strategyId Идентификатор стратегии
     * @param strategy Наименование стратегии
     * @param ticker Тикер, задается в настройках стратегии
     * @param currentIncome Значение текущей динамики инвестиционного дохода (для стратегии с видом Локомотивы Европы)
     * @param incomes 
     */
    public InvestmentIncomeDataRsIncomesSet(Long strategyId, String strategy, String ticker, String currentIncome, List<IncomeData> incomes) {
        this.strategyId = strategyId;
        this.strategy = strategy;
        this.ticker = ticker;
        this.currentIncome = currentIncome;
        this.incomes = incomes;
    }

    /**
     * Идентификатор стратегии
    * @return Идентификатор стратегии
    **/
    @ApiModelProperty(value = "Идентификатор стратегии")
    


    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }


    /**
     * Наименование стратегии
    * @return Наименование стратегии
    **/
    @ApiModelProperty(value = "Наименование стратегии")
    


    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }


    /**
     * Тикер, задается в настройках стратегии
    * @return Тикер, задается в настройках стратегии
    **/
    @ApiModelProperty(value = "Тикер, задается в настройках стратегии")
    


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }


    /**
     * Значение текущей динамики инвестиционного дохода (для стратегии с видом Локомотивы Европы)
    * @return Значение текущей динамики инвестиционного дохода (для стратегии с видом Локомотивы Европы)
    **/
    @ApiModelProperty(value = "Значение текущей динамики инвестиционного дохода (для стратегии с видом Локомотивы Европы)")
    


    public String getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(String currentIncome) {
        this.currentIncome = currentIncome;
    }


    public InvestmentIncomeDataRsIncomesSet addIncomesItem(IncomeData incomesItem) {
        if (this.incomes == null) {
            this.incomes = new ArrayList<IncomeData>();
        }
        this.incomes.add(incomesItem);
        return this;
    }

    /**
    * Get incomes
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<IncomeData> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<IncomeData> incomes) {
        this.incomes = incomes;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvestmentIncomeDataRsIncomesSet investmentIncomeDataRsIncomesSet = (InvestmentIncomeDataRsIncomesSet) o;
        return Objects.equals(this.strategyId, investmentIncomeDataRsIncomesSet.strategyId) &&
            Objects.equals(this.strategy, investmentIncomeDataRsIncomesSet.strategy) &&
            Objects.equals(this.ticker, investmentIncomeDataRsIncomesSet.ticker) &&
            Objects.equals(this.currentIncome, investmentIncomeDataRsIncomesSet.currentIncome) &&
            Objects.equals(this.incomes, investmentIncomeDataRsIncomesSet.incomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategyId, strategy, ticker, currentIncome, incomes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InvestmentIncomeDataRsIncomesSet {\n");
        
        sb.append("    strategyId: ").append(toIndentedString(strategyId)).append("\n");
        sb.append("    strategy: ").append(toIndentedString(strategy)).append("\n");
        sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
        sb.append("    currentIncome: ").append(toIndentedString(currentIncome)).append("\n");
        sb.append("    incomes: ").append(toIndentedString(incomes)).append("\n");
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

