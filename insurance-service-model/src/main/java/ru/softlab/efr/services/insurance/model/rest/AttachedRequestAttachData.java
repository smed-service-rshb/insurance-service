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
 * AttachedRequestAttachData
 */
@Validated
public class AttachedRequestAttachData   {
    @JsonProperty("requestAttachId")
    private Long requestAttachId = null;

    @JsonProperty("fileName")
    private String fileName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachedRequestAttachData() {}

    /**
     * Создает экземпляр класса
     * @param requestAttachId Идентификатор файла, прикреплённого к обращению.
     * @param fileName Имя прикрепленного файла
     */
    public AttachedRequestAttachData(Long requestAttachId, String fileName) {
        this.requestAttachId = requestAttachId;
        this.fileName = fileName;
    }

    /**
     * Идентификатор файла, прикреплённого к обращению.
    * @return Идентификатор файла, прикреплённого к обращению.
    **/
    @ApiModelProperty(required = true, value = "Идентификатор файла, прикреплённого к обращению.")
      @NotNull



    public Long getRequestAttachId() {
        return requestAttachId;
    }

    public void setRequestAttachId(Long requestAttachId) {
        this.requestAttachId = requestAttachId;
    }


    /**
     * Имя прикрепленного файла
    * @return Имя прикрепленного файла
    **/
    @ApiModelProperty(value = "Имя прикрепленного файла")
    


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachedRequestAttachData attachedRequestAttachData = (AttachedRequestAttachData) o;
        return Objects.equals(this.requestAttachId, attachedRequestAttachData.requestAttachId) &&
            Objects.equals(this.fileName, attachedRequestAttachData.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestAttachId, fileName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachedRequestAttachData {\n");
        
        sb.append("    requestAttachId: ").append(toIndentedString(requestAttachId)).append("\n");
        sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
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

