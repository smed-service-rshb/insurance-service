package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AttachedRequestAttachData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AttachedList
 */
@Validated
public class AttachedList   {
    @JsonProperty("elements")
    @Valid
    private List<AttachedRequestAttachData> elements = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachedList() {}

    /**
     * Создает экземпляр класса
     * @param elements 
     */
    public AttachedList(List<AttachedRequestAttachData> elements) {
        this.elements = elements;
    }

    public AttachedList addElementsItem(AttachedRequestAttachData elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<AttachedRequestAttachData>();
        }
        this.elements.add(elementsItem);
        return this;
    }

    /**
    * Get elements
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<AttachedRequestAttachData> getElements() {
        return elements;
    }

    public void setElements(List<AttachedRequestAttachData> elements) {
        this.elements = elements;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachedList attachedList = (AttachedList) o;
        return Objects.equals(this.elements, attachedList.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachedList {\n");
        
        sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
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

