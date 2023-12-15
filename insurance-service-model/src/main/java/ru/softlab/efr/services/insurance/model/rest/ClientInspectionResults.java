package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.InspectionResult;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Результаты проверок
 */
@ApiModel(description = "Результаты проверок")
@Validated
public class ClientInspectionResults   {
    @JsonProperty("results")
    @Valid
    private List<InspectionResult> results = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientInspectionResults() {}

    /**
     * Создает экземпляр класса
     * @param results Список результатов проверок клиента по справочникам
     */
    public ClientInspectionResults(List<InspectionResult> results) {
        this.results = results;
    }

    public ClientInspectionResults addResultsItem(InspectionResult resultsItem) {
        if (this.results == null) {
            this.results = new ArrayList<InspectionResult>();
        }
        this.results.add(resultsItem);
        return this;
    }

    /**
     * Список результатов проверок клиента по справочникам
    * @return Список результатов проверок клиента по справочникам
    **/
    @ApiModelProperty(value = "Список результатов проверок клиента по справочникам")
    
  @Valid


    public List<InspectionResult> getResults() {
        return results;
    }

    public void setResults(List<InspectionResult> results) {
        this.results = results;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientInspectionResults clientInspectionResults = (ClientInspectionResults) o;
        return Objects.equals(this.results, clientInspectionResults.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientInspectionResults {\n");
        
        sb.append("    results: ").append(toIndentedString(results)).append("\n");
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

