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
 * Тип документа
 */
@ApiModel(description = "Тип документа")
@Validated
public class AttachedDocumentType   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("documents")
    @Valid
    private List<Attachment> documents = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachedDocumentType() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор типа документа
     * @param name Наименование типа документа
     * @param documents Список загруженных файлов
     */
    public AttachedDocumentType(Long id, String name, List<Attachment> documents) {
        this.id = id;
        this.name = name;
        this.documents = documents;
    }

    /**
     * Идентификатор типа документа
    * @return Идентификатор типа документа
    **/
    @ApiModelProperty(value = "Идентификатор типа документа")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование типа документа
    * @return Наименование типа документа
    **/
    @ApiModelProperty(value = "Наименование типа документа")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public AttachedDocumentType addDocumentsItem(Attachment documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<Attachment>();
        }
        this.documents.add(documentsItem);
        return this;
    }

    /**
     * Список загруженных файлов
    * @return Список загруженных файлов
    **/
    @ApiModelProperty(value = "Список загруженных файлов")
    
  @Valid


    public List<Attachment> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Attachment> documents) {
        this.documents = documents;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachedDocumentType attachedDocumentType = (AttachedDocumentType) o;
        return Objects.equals(this.id, attachedDocumentType.id) &&
            Objects.equals(this.name, attachedDocumentType.name) &&
            Objects.equals(this.documents, attachedDocumentType.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, documents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachedDocumentType {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

