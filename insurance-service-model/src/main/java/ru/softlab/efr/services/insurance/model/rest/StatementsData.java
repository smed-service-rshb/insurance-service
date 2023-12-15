package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.Statement;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация о загруженных файлах (заявлениях)
 */
@ApiModel(description = "Информация о загруженных файлах (заявлениях)")
@Validated
public class StatementsData   {
    @JsonProperty("statementTypes")
    @Valid
    private List<Statement> statementTypes = null;


    /**
     * Создает пустой экземпляр класса
     */
    public StatementsData() {}

    /**
     * Создает экземпляр класса
     * @param statementTypes 
     */
    public StatementsData(List<Statement> statementTypes) {
        this.statementTypes = statementTypes;
    }

    public StatementsData addStatementTypesItem(Statement statementTypesItem) {
        if (this.statementTypes == null) {
            this.statementTypes = new ArrayList<Statement>();
        }
        this.statementTypes.add(statementTypesItem);
        return this;
    }

    /**
    * Get statementTypes
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<Statement> getStatementTypes() {
        return statementTypes;
    }

    public void setStatementTypes(List<Statement> statementTypes) {
        this.statementTypes = statementTypes;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatementsData statementsData = (StatementsData) o;
        return Objects.equals(this.statementTypes, statementsData.statementTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statementTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StatementsData {\n");
        
        sb.append("    statementTypes: ").append(toIndentedString(statementTypes)).append("\n");
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

