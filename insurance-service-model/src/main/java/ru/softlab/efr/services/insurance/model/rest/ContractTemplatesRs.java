package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ответ на запрос получения печатных форм договора
 */
@ApiModel(description = "Ответ на запрос получения печатных форм договора")
@Validated
public class ContractTemplatesRs   {
    @JsonProperty("templates")
    @Valid
    private List<ContractTemplate> templates = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ContractTemplatesRs() {}

    /**
     * Создает экземпляр класса
     * @param templates Список шаблонов документов для печати
     */
    public ContractTemplatesRs(List<ContractTemplate> templates) {
        this.templates = templates;
    }

    public ContractTemplatesRs addTemplatesItem(ContractTemplate templatesItem) {
        if (this.templates == null) {
            this.templates = new ArrayList<ContractTemplate>();
        }
        this.templates.add(templatesItem);
        return this;
    }

    /**
     * Список шаблонов документов для печати
    * @return Список шаблонов документов для печати
    **/
    @ApiModelProperty(value = "Список шаблонов документов для печати")
    
  @Valid


    public List<ContractTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ContractTemplate> templates) {
        this.templates = templates;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractTemplatesRs contractTemplatesRs = (ContractTemplatesRs) o;
        return Objects.equals(this.templates, contractTemplatesRs.templates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templates);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContractTemplatesRs {\n");
        
        sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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

