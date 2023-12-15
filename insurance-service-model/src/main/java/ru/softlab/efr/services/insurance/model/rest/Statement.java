package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.StatementAttachment;
import ru.softlab.efr.services.insurance.model.rest.StatementCompleteStatus;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация о загруженных файлах (заявлениях)
 */
@ApiModel(description = "Информация о загруженных файлах (заявлениях)")
@Validated
public class Statement   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("info")
    private String info = null;

    @JsonProperty("statementCompleteStatus")
    private StatementCompleteStatus statementCompleteStatus = null;

    @JsonProperty("documents")
    @Valid
    private List<StatementAttachment> documents = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Statement() {}

    /**
     * Создает экземпляр класса
     * @param id Id блока заявлений
     * @param code Код статуса программы страхования
     * @param info Комментарий к блоку
     * @param statementCompleteStatus 
     * @param documents Отсортированный, в соответствии с хронологией добавления, список прикрепленных файлов
     */
    public Statement(Long id, String code, String info, StatementCompleteStatus statementCompleteStatus, List<StatementAttachment> documents) {
        this.id = id;
        this.code = code;
        this.info = info;
        this.statementCompleteStatus = statementCompleteStatus;
        this.documents = documents;
    }

    /**
     * Id блока заявлений
    * @return Id блока заявлений
    **/
    @ApiModelProperty(value = "Id блока заявлений")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Код статуса программы страхования
    * @return Код статуса программы страхования
    **/
    @ApiModelProperty(value = "Код статуса программы страхования")
    


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Комментарий к блоку
    * @return Комментарий к блоку
    **/
    @ApiModelProperty(value = "Комментарий к блоку")
    


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    /**
    * Get statementCompleteStatus
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public StatementCompleteStatus getStatementCompleteStatus() {
        return statementCompleteStatus;
    }

    public void setStatementCompleteStatus(StatementCompleteStatus statementCompleteStatus) {
        this.statementCompleteStatus = statementCompleteStatus;
    }


    public Statement addDocumentsItem(StatementAttachment documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<StatementAttachment>();
        }
        this.documents.add(documentsItem);
        return this;
    }

    /**
     * Отсортированный, в соответствии с хронологией добавления, список прикрепленных файлов
    * @return Отсортированный, в соответствии с хронологией добавления, список прикрепленных файлов
    **/
    @ApiModelProperty(value = "Отсортированный, в соответствии с хронологией добавления, список прикрепленных файлов")
    
  @Valid


    public List<StatementAttachment> getDocuments() {
        return documents;
    }

    public void setDocuments(List<StatementAttachment> documents) {
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
        Statement statement = (Statement) o;
        return Objects.equals(this.id, statement.id) &&
            Objects.equals(this.code, statement.code) &&
            Objects.equals(this.info, statement.info) &&
            Objects.equals(this.statementCompleteStatus, statement.statementCompleteStatus) &&
            Objects.equals(this.documents, statement.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, info, statementCompleteStatus, documents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Statement {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    info: ").append(toIndentedString(info)).append("\n");
        sb.append("    statementCompleteStatus: ").append(toIndentedString(statementCompleteStatus)).append("\n");
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

