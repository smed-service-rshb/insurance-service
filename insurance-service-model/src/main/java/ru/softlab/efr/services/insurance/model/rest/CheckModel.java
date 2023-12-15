package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.CheckModelErrorType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Контейнер с результатами одной проверки договора страхования
 */
@ApiModel(description = "Контейнер с результатами одной проверки договора страхования")
@Validated
public class CheckModel   {
    @JsonProperty("key")
    private String key = null;

    @JsonProperty("value")
    private String value = null;

    @JsonProperty("errorType")
    private CheckModelErrorType errorType = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CheckModel() {}

    /**
     * Создает экземпляр класса
     * @param key Поле или иной объект, подверженный проверке
     * @param value Результат проверки, описывающий выявленные отклонения от ТЗ
     * @param errorType Тип ошибки, определяющий доступность операций с договором на стороне клиента
     */
    public CheckModel(String key, String value, CheckModelErrorType errorType) {
        this.key = key;
        this.value = value;
        this.errorType = errorType;
    }

    /**
     * Поле или иной объект, подверженный проверке
    * @return Поле или иной объект, подверженный проверке
    **/
    @ApiModelProperty(value = "Поле или иной объект, подверженный проверке")
    


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    /**
     * Результат проверки, описывающий выявленные отклонения от ТЗ
    * @return Результат проверки, описывающий выявленные отклонения от ТЗ
    **/
    @ApiModelProperty(value = "Результат проверки, описывающий выявленные отклонения от ТЗ")
    


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * Тип ошибки, определяющий доступность операций с договором на стороне клиента
    * @return Тип ошибки, определяющий доступность операций с договором на стороне клиента
    **/
    @ApiModelProperty(value = "Тип ошибки, определяющий доступность операций с договором на стороне клиента")
    
  @Valid


    public CheckModelErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(CheckModelErrorType errorType) {
        this.errorType = errorType;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CheckModel checkModel = (CheckModel) o;
        return Objects.equals(this.key, checkModel.key) &&
            Objects.equals(this.value, checkModel.value) &&
            Objects.equals(this.errorType, checkModel.errorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, errorType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CheckModel {\n");
        
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    errorType: ").append(toIndentedString(errorType)).append("\n");
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

