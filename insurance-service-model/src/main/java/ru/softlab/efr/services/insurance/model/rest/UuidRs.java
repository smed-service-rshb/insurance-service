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
 * Ответ, содержащий uuid объекта или процесса
 */
@ApiModel(description = "Ответ, содержащий uuid объекта или процесса")
@Validated
public class UuidRs   {
    @JsonProperty("uuid")
    private String uuid = null;


    /**
     * Создает пустой экземпляр класса
     */
    public UuidRs() {}

    /**
     * Создает экземпляр класса
     * @param uuid uuid сущности или процесса
     */
    public UuidRs(String uuid) {
        this.uuid = uuid;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UuidRs uuidRs = (UuidRs) o;
        return Objects.equals(this.uuid, uuidRs.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UuidRs {\n");
        
        sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
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

