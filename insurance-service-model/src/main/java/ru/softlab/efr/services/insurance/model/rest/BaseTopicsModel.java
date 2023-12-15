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
 * Модеь для модификации темы для создания обращения отправляется админом при добавлении, отображается в списке тем обращений
 */
@ApiModel(description = "Модеь для модификации темы для создания обращения отправляется админом при добавлении, отображается в списке тем обращений")
@Validated
public class BaseTopicsModel   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("topicDescription")
    private String topicDescription = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;


    /**
     * Создает пустой экземпляр класса
     */
    public BaseTopicsModel() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор темы обращения
     * @param name Наименование темы обращения
     * @param topicDescription Описание темы обращения
     * @param email Назначение адреса электронной почты для темы обращения
     * @param isActive Назначение статуса активности темы обращения
     */
    public BaseTopicsModel(Long id, String name, String topicDescription, String email, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.topicDescription = topicDescription;
        this.email = email;
        this.isActive = isActive;
    }

    /**
     * Идентификатор темы обращения
    * @return Идентификатор темы обращения
    **/
    @ApiModelProperty(value = "Идентификатор темы обращения")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование темы обращения
    * @return Наименование темы обращения
    **/
    @ApiModelProperty(required = true, value = "Наименование темы обращения")
      @NotNull



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
    


    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }


    /**
     * Назначение адреса электронной почты для темы обращения
    * @return Назначение адреса электронной почты для темы обращения
    **/
    @ApiModelProperty(required = true, value = "Назначение адреса электронной почты для темы обращения")
      @NotNull

 @Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(((\\w+)|(\\w+-\\w+))\\.)+[a-zA-Z]{2,6})$)")

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Назначение статуса активности темы обращения
    * @return Назначение статуса активности темы обращения
    **/
    @ApiModelProperty(required = true, value = "Назначение статуса активности темы обращения")
      @NotNull



    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseTopicsModel baseTopicsModel = (BaseTopicsModel) o;
        return Objects.equals(this.id, baseTopicsModel.id) &&
            Objects.equals(this.name, baseTopicsModel.name) &&
            Objects.equals(this.topicDescription, baseTopicsModel.topicDescription) &&
            Objects.equals(this.email, baseTopicsModel.email) &&
            Objects.equals(this.isActive, baseTopicsModel.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, topicDescription, email, isActive);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BaseTopicsModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    topicDescription: ").append(toIndentedString(topicDescription)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

