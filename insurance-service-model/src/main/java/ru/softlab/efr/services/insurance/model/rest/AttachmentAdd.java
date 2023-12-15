package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Файлы в обращении для загрузки на сервер в формате base64 
 */
@ApiModel(description = "Файлы в обращении для загрузки на сервер в формате base64 ")
@Validated
public class AttachmentAdd   {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("file")
    private Resource file = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachmentAdd() {}

    /**
     * Создает экземпляр класса
     * @param name Название и расширение файла 
     * @param file Файл в формате base64 
     */
    public AttachmentAdd(String name, Resource file) {
        this.name = name;
        this.file = file;
    }

    /**
     * Название и расширение файла 
    * @return Название и расширение файла 
    **/
    @ApiModelProperty(required = true, value = "Название и расширение файла ")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Файл в формате base64 
    * @return Файл в формате base64 
    **/
    @ApiModelProperty(required = true, value = "Файл в формате base64 ")
      @NotNull

  @Valid


    public Resource getFile() {
        return file;
    }

    public void setFile(Resource file) {
        this.file = file;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachmentAdd attachmentAdd = (AttachmentAdd) o;
        return Objects.equals(this.name, attachmentAdd.name) &&
            Objects.equals(this.file, attachmentAdd.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, file);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachmentAdd {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    file: ").append(toIndentedString(file)).append("\n");
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

