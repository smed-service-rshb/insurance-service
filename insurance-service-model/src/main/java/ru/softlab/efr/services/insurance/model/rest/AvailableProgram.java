package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Программа страхования для покупки в ЛК
 */
@ApiModel(description = "Программа страхования для покупки в ЛК")
@Validated
public class AvailableProgram   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("programKind")
    private ProgramKind programKind = null;

    @JsonProperty("image")
    private Long image = null;

    @JsonProperty("infoImage")
    private Long infoImage = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("link")
    private String link = null;

    @JsonProperty("maxAge")
    private Integer maxAge = null;

    @JsonProperty("minAge")
    private Integer minAge = null;

    @JsonProperty("risks")
    @Valid
    private List<String> risks = null;

    @JsonProperty("canBay")
    private Boolean canBay = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailableProgram() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы для покупки в ЛК
     * @param title Заголовок
     * @param name Наименование программы страхования
     * @param programKind Вид программы страхования
     * @param image id изображения продукта
     * @param infoImage id изображения информации о страховом продукте
     * @param description Описание
     * @param link Ссылка на внешний ресурс
     * @param maxAge Максимальный возраст застрахованного для оформления программы
     * @param minAge Минимальный возраст застрахованного для оформления программы
     * @param risks Список рисков
     * @param canBay признак Оформление в виде заявления
     */
    public AvailableProgram(Long id, String title, String name, ProgramKind programKind, Long image, Long infoImage, String description, String link, Integer maxAge, Integer minAge, List<String> risks, Boolean canBay) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.programKind = programKind;
        this.image = image;
        this.infoImage = infoImage;
        this.description = description;
        this.link = link;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.risks = risks;
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
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getProgramKind() {
        return programKind;
    }

    public void setProgramKind(ProgramKind programKind) {
        this.programKind = programKind;
    }


    /**
     * id изображения продукта
    * @return id изображения продукта
    **/
    @ApiModelProperty(value = "id изображения продукта")
    


    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }


    /**
     * id изображения информации о страховом продукте
    * @return id изображения информации о страховом продукте
    **/
    @ApiModelProperty(value = "id изображения информации о страховом продукте")
    


    public Long getInfoImage() {
        return infoImage;
    }

    public void setInfoImage(Long infoImage) {
        this.infoImage = infoImage;
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


    public AvailableProgram addRisksItem(String risksItem) {
        if (this.risks == null) {
            this.risks = new ArrayList<String>();
        }
        this.risks.add(risksItem);
        return this;
    }

    /**
     * Список рисков
    * @return Список рисков
    **/
    @ApiModelProperty(value = "Список рисков")
    


    public List<String> getRisks() {
        return risks;
    }

    public void setRisks(List<String> risks) {
        this.risks = risks;
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
        AvailableProgram availableProgram = (AvailableProgram) o;
        return Objects.equals(this.id, availableProgram.id) &&
            Objects.equals(this.title, availableProgram.title) &&
            Objects.equals(this.name, availableProgram.name) &&
            Objects.equals(this.programKind, availableProgram.programKind) &&
            Objects.equals(this.image, availableProgram.image) &&
            Objects.equals(this.infoImage, availableProgram.infoImage) &&
            Objects.equals(this.description, availableProgram.description) &&
            Objects.equals(this.link, availableProgram.link) &&
            Objects.equals(this.maxAge, availableProgram.maxAge) &&
            Objects.equals(this.minAge, availableProgram.minAge) &&
            Objects.equals(this.risks, availableProgram.risks) &&
            Objects.equals(this.canBay, availableProgram.canBay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, name, programKind, image, infoImage, description, link, maxAge, minAge, risks, canBay);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailableProgram {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    programKind: ").append(toIndentedString(programKind)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    infoImage: ").append(toIndentedString(infoImage)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
        sb.append("    minAge: ").append(toIndentedString(minAge)).append("\n");
        sb.append("    risks: ").append(toIndentedString(risks)).append("\n");
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

