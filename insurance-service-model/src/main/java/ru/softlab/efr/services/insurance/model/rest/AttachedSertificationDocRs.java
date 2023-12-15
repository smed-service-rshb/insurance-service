package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.Attachment;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация о загруженных файлах
 */
@ApiModel(description = "Информация о загруженных файлах")
@Validated
public class AttachedSertificationDocRs   {
    @JsonProperty("attachedDocs")
    @Valid
    private List<Attachment> attachedDocs = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachedSertificationDocRs() {}

    /**
     * Создает экземпляр класса
     * @param attachedDocs 
     */
    public AttachedSertificationDocRs(List<Attachment> attachedDocs) {
        this.attachedDocs = attachedDocs;
    }

    public AttachedSertificationDocRs addAttachedDocsItem(Attachment attachedDocsItem) {
        if (this.attachedDocs == null) {
            this.attachedDocs = new ArrayList<Attachment>();
        }
        this.attachedDocs.add(attachedDocsItem);
        return this;
    }

    /**
    * Get attachedDocs
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<Attachment> getAttachedDocs() {
        return attachedDocs;
    }

    public void setAttachedDocs(List<Attachment> attachedDocs) {
        this.attachedDocs = attachedDocs;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachedSertificationDocRs attachedSertificationDocRs = (AttachedSertificationDocRs) o;
        return Objects.equals(this.attachedDocs, attachedSertificationDocRs.attachedDocs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachedDocs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachedSertificationDocRs {\n");
        
        sb.append("    attachedDocs: ").append(toIndentedString(attachedDocs)).append("\n");
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

