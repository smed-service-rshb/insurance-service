package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.UuidRs;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ImportRs
 */
@Validated
public class ImportRs   {
    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("path")
    private String path = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ImportRs() {}

    /**
     * Создает экземпляр класса
     * @param uuid uuid сущности или процесса
     * @param path Значение настройки пути выгрузки результатов импорта
     */
    public ImportRs(String uuid, String path) {
        this.uuid = uuid;
        this.path = path;
    }

    /**
     * uuid сущности или процесса
    * @return uuid сущности или процесса
    **/
    @ApiModelProperty(value = "uuid сущности или процесса")
    


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    /**
     * Значение настройки пути выгрузки результатов импорта
    * @return Значение настройки пути выгрузки результатов импорта
    **/
    @ApiModelProperty(value = "Значение настройки пути выгрузки результатов импорта")
    


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportRs importRs = (ImportRs) o;
        return Objects.equals(this.uuid, importRs.uuid) &&
            Objects.equals(this.path, importRs.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ImportRs {\n");
        
        sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
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

