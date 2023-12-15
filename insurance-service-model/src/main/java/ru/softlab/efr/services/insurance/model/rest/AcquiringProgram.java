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
 * Программа страхования для покупки в ЛК
 */
@ApiModel(description = "Программа страхования для покупки в ЛК")
@Validated
public class AcquiringProgram   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("image")
    private String image = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("link")
    private String link = null;

    @JsonProperty("maxAge")
    private Integer maxAge = null;

    @JsonProperty("minAge")
    private Integer minAge = null;

    @JsonProperty("authorizedZoneEnable")
    private Boolean authorizedZoneEnable = null;

    @JsonProperty("notAuthorizedZoneEnable")
    private Boolean notAuthorizedZoneEnable = null;

    @JsonProperty("canBay")
    private Boolean canBay = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AcquiringProgram() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы для покупки в ЛК
     * @param title Заголовок
     * @param name Наименование программы страхования
     * @param image Ссылка на изображение
     * @param description Описание
     * @param link Ссылка на внешний ресурс
     * @param maxAge Максимальный возраст застрахованного для оформления программы
     * @param minAge Минимальный возраст застрахованного для оформления программы
     * @param authorizedZoneEnable Признак доступности программы в авторизованной зоне
     * @param notAuthorizedZoneEnable Признак доступности программы в не авторизованной зоне
     * @param canBay признак Оформление в виде заявления
     */
    public AcquiringProgram(Long id, String title, String name, String image, String description, String link, Integer maxAge, Integer minAge, Boolean authorizedZoneEnable, Boolean notAuthorizedZoneEnable, Boolean canBay) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.image = image;
        this.description = description;
        this.link = link;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.authorizedZoneEnable = authorizedZoneEnable;
        this.notAuthorizedZoneEnable = notAuthorizedZoneEnable;
        this.canBay = canBay;
    }

    /**
     * Идентификатор программы для покупки в ЛК
    * @return Идентификатор программы для покупки в ЛК
    **/
    @ApiModelProperty(value = "Идентификатор программы для покупки в ЛК")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Заголовок
    * @return Заголовок
    **/
    @ApiModelProperty(value = "Заголовок")
    


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(value = "Наименование программы страхования")
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Ссылка на изображение
    * @return Ссылка на изображение
    **/
    @ApiModelProperty(value = "Ссылка на изображение//todo - возможно будет масив, также поскольку изображение будет хранится в бд, еще не определился с реализацией, возможно будет id и отдельный сервер для получения изображения")
    


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    /**
     * Описание
    * @return Описание
    **/
    @ApiModelProperty(value = "Описание")
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Ссылка на внешний ресурс
    * @return Ссылка на внешний ресурс
    **/
    @ApiModelProperty(value = "Ссылка на внешний ресурс")
    


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    /**
     * Максимальный возраст застрахованного для оформления программы
    * @return Максимальный возраст застрахованного для оформления программы
    **/
    @ApiModelProperty(value = "Максимальный возраст застрахованного для оформления программы")
    


    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }


    /**
     * Минимальный возраст застрахованного для оформления программы
    * @return Минимальный возраст застрахованного для оформления программы
    **/
    @ApiModelProperty(value = "Минимальный возраст застрахованного для оформления программы")
    


    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }


    /**
     * Признак доступности программы в авторизованной зоне
    * @return Признак доступности программы в авторизованной зоне
    **/
    @ApiModelProperty(value = "Признак доступности программы в авторизованной зоне")
    


    public Boolean isAuthorizedZoneEnable() {
        return authorizedZoneEnable;
    }

    public void setAuthorizedZoneEnable(Boolean authorizedZoneEnable) {
        this.authorizedZoneEnable = authorizedZoneEnable;
    }


    /**
     * Признак доступности программы в не авторизованной зоне
    * @return Признак доступности программы в не авторизованной зоне
    **/
    @ApiModelProperty(value = "Признак доступности программы в не авторизованной зоне")
    


    public Boolean isNotAuthorizedZoneEnable() {
        return notAuthorizedZoneEnable;
    }

    public void setNotAuthorizedZoneEnable(Boolean notAuthorizedZoneEnable) {
        this.notAuthorizedZoneEnable = notAuthorizedZoneEnable;
    }


    /**
     * признак Оформление в виде заявления
    * @return признак Оформление в виде заявления
    **/
    @ApiModelProperty(value = "признак Оформление в виде заявления")
    


    public Boolean isCanBay() {
        return canBay;
    }

    public void setCanBay(Boolean canBay) {
        this.canBay = canBay;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquiringProgram acquiringProgram = (AcquiringProgram) o;
        return Objects.equals(this.id, acquiringProgram.id) &&
            Objects.equals(this.title, acquiringProgram.title) &&
            Objects.equals(this.name, acquiringProgram.name) &&
            Objects.equals(this.image, acquiringProgram.image) &&
            Objects.equals(this.description, acquiringProgram.description) &&
            Objects.equals(this.link, acquiringProgram.link) &&
            Objects.equals(this.maxAge, acquiringProgram.maxAge) &&
            Objects.equals(this.minAge, acquiringProgram.minAge) &&
            Objects.equals(this.authorizedZoneEnable, acquiringProgram.authorizedZoneEnable) &&
            Objects.equals(this.notAuthorizedZoneEnable, acquiringProgram.notAuthorizedZoneEnable) &&
            Objects.equals(this.canBay, acquiringProgram.canBay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, name, image, description, link, maxAge, minAge, authorizedZoneEnable, notAuthorizedZoneEnable, canBay);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquiringProgram {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
        sb.append("    minAge: ").append(toIndentedString(minAge)).append("\n");
        sb.append("    authorizedZoneEnable: ").append(toIndentedString(authorizedZoneEnable)).append("\n");
        sb.append("    notAuthorizedZoneEnable: ").append(toIndentedString(notAuthorizedZoneEnable)).append("\n");
        sb.append("    canBay: ").append(toIndentedString(canBay)).append("\n");
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

