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
 * AttachmentData
 */
@Validated
public class AttachmentData   {
    @JsonProperty("fileName")
    private String fileName = null;

    @JsonProperty("content")
    private String content = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachmentData() {}

    /**
     * Создает экземпляр класса
     * @param fileName Имя файла
     * @param content Содержимое файла в формате Base64
     */
    public AttachmentData(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    /**
     * Имя файла
    * @return Имя файла
    **/
    @ApiModelProperty(required = true, value = "Имя файла")
      @NotNull



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * Содержимое файла в формате Base64
    * @return Содержимое файла в формате Base64
    **/
    @ApiModelProperty(required = true, value = "Содержимое файла в формате Base64")
      @NotNull



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachmentData attachmentData = (AttachmentData) o;
        return Objects.equals(this.fileName, attachmentData.fileName) &&
            Objects.equals(this.content, attachmentData.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachmentData {\n");
        
        sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
        sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

