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
 * Элемент с объектом для темы обращения, отправляемая пользователю для добавления в список
 */
@ApiModel(description = "Элемент с объектом для темы обращения, отправляемая пользователю для добавления в список")
@Validated
public class AvailableTopicFormat   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("about")
    private String about = null;

    @JsonProperty("active")
    private Boolean active = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailableTopicFormat() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор элемента темы обращения
     * @param name Имя для элемента темы обращений
     * @param about Описание темы обращения
     * @param active Признак активности темы для обращения
     */
    public AvailableTopicFormat(Long id, String name, String about, Boolean active) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.active = active;
    }

    /**
     * Идентификатор элемента темы обращения
    * @return Идентификатор элемента темы обращения
    **/
    @ApiModelProperty(value = "Идентификатор элемента темы обращения")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Имя для элемента темы обращений
    * @return Имя для элемента темы обращений
    **/
    @ApiModelProperty(value = "Имя для элемента темы обращений")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Описание темы обращения
    * @return Описание темы обращения
    **/
    @ApiModelProperty(value = "Описание темы обращения")
    


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    /**
     * Признак активности темы для обращения
    * @return Признак активности темы для обращения
    **/
    @ApiModelProperty(value = "Признак активности темы для обращения")
    


    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableTopicFormat availableTopicFormat = (AvailableTopicFormat) o;
        return Objects.equals(this.id, availableTopicFormat.id) &&
            Objects.equals(this.name, availableTopicFormat.name) &&
            Objects.equals(this.about, availableTopicFormat.about) &&
            Objects.equals(this.active, availableTopicFormat.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, about, active);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailableTopicFormat {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    about: ").append(toIndentedString(about)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

