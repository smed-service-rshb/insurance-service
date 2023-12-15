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
 * Обязятельный документ в описании риска
 */
@ApiModel(description = "Обязятельный документ в описании риска")
@Validated
public class RiskDocument   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("state")
    private String state = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RiskDocument() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор документа в справочнике обязательных документов
     * @param name Наименование документа
     * @param state Статус договора, в котором должен быть приложен документ
     */
    public RiskDocument(Long id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    /**
     * Идентификатор документа в справочнике обязательных документов
    * @return Идентификатор документа в справочнике обязательных документов
    **/
    @ApiModelProperty(required = true, value = "Идентификатор документа в справочнике обязательных документов")
      @NotNull



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование документа
    * @return Наименование документа
    **/
    @ApiModelProperty(required = true, value = "Наименование документа")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Статус договора, в котором должен быть приложен документ
    * @return Статус договора, в котором должен быть приложен документ
    **/
    @ApiModelProperty(required = true, value = "Статус договора, в котором должен быть приложен документ")
      @NotNull



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RiskDocument riskDocument = (RiskDocument) o;
        return Objects.equals(this.id, riskDocument.id) &&
            Objects.equals(this.name, riskDocument.name) &&
            Objects.equals(this.state, riskDocument.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RiskDocument {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

