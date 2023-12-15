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
 * Данные обязательного документа
 */
@ApiModel(description = "Данные обязательного документа")
@Validated
public class RequiredDocumentData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("activeFlag")
    private Boolean activeFlag = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RequiredDocumentData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param type Тип документа
     * @param activeFlag Признак действующего документа
     */
    public RequiredDocumentData(Long id, String type, Boolean activeFlag) {
        this.id = id;
        this.type = type;
        this.activeFlag = activeFlag;
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
     * Тип документа
    * @return Тип документа
    **/
    @ApiModelProperty(required = true, value = "Тип документа")
      @NotNull



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * Признак действующего документа
    * @return Признак действующего документа
    **/
    @ApiModelProperty(value = "Признак действующего документа")
    


    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredDocumentData requiredDocumentData = (RequiredDocumentData) o;
        return Objects.equals(this.id, requiredDocumentData.id) &&
            Objects.equals(this.type, requiredDocumentData.type) &&
            Objects.equals(this.activeFlag, requiredDocumentData.activeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, activeFlag);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequiredDocumentData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    activeFlag: ").append(toIndentedString(activeFlag)).append("\n");
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

