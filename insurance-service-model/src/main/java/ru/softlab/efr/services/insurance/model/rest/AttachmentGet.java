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
 * Файлы в обращении для загрузки на сервер в формате base64 и выгрузки 
 */
@ApiModel(description = "Файлы в обращении для загрузки на сервер в формате base64 и выгрузки ")
@Validated
public class AttachmentGet   {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("size")
    private Long size = null;

    @JsonProperty("link")
    private String link = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AttachmentGet() {}

    /**
     * Создает экземпляр класса
     * @param name Название и расширение файла 
     * @param size Размер вложения 
     * @param link Ссылка на файл 
     */
    public AttachmentGet(String name, Long size, String link) {
        this.name = name;
        this.size = size;
        this.link = link;
    }

    /**
     * Название и расширение файла 
    * @return Название и расширение файла 
    **/
    @ApiModelProperty(value = "Название и расширение файла ")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Размер вложения 
    * @return Размер вложения 
    **/
    @ApiModelProperty(value = "Размер вложения ")
    


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


    /**
     * Ссылка на файл 
    * @return Ссылка на файл 
    **/
    @ApiModelProperty(value = "Ссылка на файл ")
    


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachmentGet attachmentGet = (AttachmentGet) o;
        return Objects.equals(this.name, attachmentGet.name) &&
            Objects.equals(this.size, attachmentGet.size) &&
            Objects.equals(this.link, attachmentGet.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, link);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttachmentGet {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    size: ").append(toIndentedString(size)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

