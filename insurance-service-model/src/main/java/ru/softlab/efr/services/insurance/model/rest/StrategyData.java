package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.StrategyProperty;
import ru.softlab.efr.services.insurance.model.rest.StrategyType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные записи справочника стратегий
 */
@ApiModel(description = "Данные записи справочника стратегий")
@Validated
public class StrategyData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("policyCode")
    private Integer policyCode = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("strategyProperties")
    @Valid
    private List<StrategyProperty> strategyProperties = null;

    @JsonProperty("type")
    private StrategyType type = null;


    /**
     * Создает пустой экземпляр класса
     */
    public StrategyData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param name Вид программы страхования
     * @param description Описание
     * @param policyCode Кодировка полиса стратегия. Числовое поле формата 99, где 9 – цифра от нуля до 9. Данное поле участвует в формировании номера договора
     * @param startDate Дата начала действия стратегии. По умолчанию, текущая дата.
     * @param endDate Дата окончания действия стратегии. По умолчанию, 31.12.2999
     * @param strategyProperties 
     * @param type Тип ИСЖ стратегии
     */
    public StrategyData(Long id, String name, String description, Integer policyCode, LocalDate startDate, LocalDate endDate, List<StrategyProperty> strategyProperties, StrategyType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.policyCode = policyCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strategyProperties = strategyProperties;
        this.type = type;
    }

    /**
     * Идентификатор записи
    * @return Идентификатор записи
    **/
    @ApiModelProperty(value = "Идентификатор записи")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(required = true, value = "Вид программы страхования")
      @NotNull

 @Size(max=50)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Описание
    * @return Описание
    **/
    @ApiModelProperty(value = "Описание")
    
 @Size(max=500)

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Кодировка полиса стратегия. Числовое поле формата 99, где 9 – цифра от нуля до 9. Данное поле участвует в формировании номера договора
    * @return Кодировка полиса стратегия. Числовое поле формата 99, где 9 – цифра от нуля до 9. Данное поле участвует в формировании номера договора
    **/
    @ApiModelProperty(required = true, value = "Кодировка полиса стратегия. Числовое поле формата 99, где 9 – цифра от нуля до 9. Данное поле участвует в формировании номера договора")
      @NotNull



    public Integer getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(Integer policyCode) {
        this.policyCode = policyCode;
    }


    /**
     * Дата начала действия стратегии. По умолчанию, текущая дата.
    * @return Дата начала действия стратегии. По умолчанию, текущая дата.
    **/
    @ApiModelProperty(required = true, value = "Дата начала действия стратегии. По умолчанию, текущая дата.")
      @NotNull

  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания действия стратегии. По умолчанию, 31.12.2999
    * @return Дата окончания действия стратегии. По умолчанию, 31.12.2999
    **/
    @ApiModelProperty(required = true, value = "Дата окончания действия стратегии. По умолчанию, 31.12.2999")
      @NotNull

  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public StrategyData addStrategyPropertiesItem(StrategyProperty strategyPropertiesItem) {
        if (this.strategyProperties == null) {
            this.strategyProperties = new ArrayList<StrategyProperty>();
        }
        this.strategyProperties.add(strategyPropertiesItem);
        return this;
    }

    /**
    * Get strategyProperties
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<StrategyProperty> getStrategyProperties() {
        return strategyProperties;
    }

    public void setStrategyProperties(List<StrategyProperty> strategyProperties) {
        this.strategyProperties = strategyProperties;
    }


    /**
     * Тип ИСЖ стратегии
    * @return Тип ИСЖ стратегии
    **/
    @ApiModelProperty(value = "Тип ИСЖ стратегии")
    
  @Valid


    public StrategyType getType() {
        return type;
    }

    public void setType(StrategyType type) {
        this.type = type;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StrategyData strategyData = (StrategyData) o;
        return Objects.equals(this.id, strategyData.id) &&
            Objects.equals(this.name, strategyData.name) &&
            Objects.equals(this.description, strategyData.description) &&
            Objects.equals(this.policyCode, strategyData.policyCode) &&
            Objects.equals(this.startDate, strategyData.startDate) &&
            Objects.equals(this.endDate, strategyData.endDate) &&
            Objects.equals(this.strategyProperties, strategyData.strategyProperties) &&
            Objects.equals(this.type, strategyData.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, policyCode, startDate, endDate, strategyProperties, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StrategyData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    policyCode: ").append(toIndentedString(policyCode)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    strategyProperties: ").append(toIndentedString(strategyProperties)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

