package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.ClientTemplate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Отсортированный список шаблонов и инструкций для отображения в ЛК
 */
@ApiModel(description = "Отсортированный список шаблонов и инструкций для отображения в ЛК")
@Validated
public class ClientTemplateList   {
    @JsonProperty("templates")
    @Valid
    private List<ClientTemplate> templates = null;

    @JsonProperty("instructions")
    @Valid
    private List<ClientTemplate> instructions = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientTemplateList() {}

    /**
     * Создает экземпляр класса
     * @param templates 
     * @param instructions 
     */
    public ClientTemplateList(List<ClientTemplate> templates, List<ClientTemplate> instructions) {
        this.templates = templates;
        this.instructions = instructions;
    }

    public ClientTemplateList addTemplatesItem(ClientTemplate templatesItem) {
        if (this.templates == null) {
            this.templates = new ArrayList<ClientTemplate>();
        }
        this.templates.add(templatesItem);
        return this;
    }

    /**
    * Get templates
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<ClientTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ClientTemplate> templates) {
        this.templates = templates;
    }


    public ClientTemplateList addInstructionsItem(ClientTemplate instructionsItem) {
        if (this.instructions == null) {
            this.instructions = new ArrayList<ClientTemplate>();
        }
        this.instructions.add(instructionsItem);
        return this;
    }

    /**
    * Get instructions
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<ClientTemplate> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<ClientTemplate> instructions) {
        this.instructions = instructions;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTemplateList clientTemplateList = (ClientTemplateList) o;
        return Objects.equals(this.templates, clientTemplateList.templates) &&
            Objects.equals(this.instructions, clientTemplateList.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templates, instructions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTemplateList {\n");
        
        sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
        sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
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

