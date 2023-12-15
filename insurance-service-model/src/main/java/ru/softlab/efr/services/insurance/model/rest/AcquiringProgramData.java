package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplate;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Программа страхования для покупки в ЛК
 */
@ApiModel(description = "Программа страхования для покупки в ЛК")
@Validated
public class AcquiringProgramData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("programSettingId")
    private Long programSettingId = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("image")
    private Long image = null;

    @JsonProperty("imageName")
    private String imageName = null;

    @JsonProperty("infoImage")
    private Long infoImage = null;

    @JsonProperty("infoImageName")
    private String infoImageName = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("link")
    private String link = null;

    @JsonProperty("authorizedZoneEnable")
    private Boolean authorizedZoneEnable = null;

    @JsonProperty("notAuthorizedZoneEnable")
    private Boolean notAuthorizedZoneEnable = null;

    @JsonProperty("application")
    private Boolean application = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("templates")
    @Valid
    private List<ContractTemplate> templates = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AcquiringProgramData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы для покупки в ЛК
     * @param kind Вид программы страхования
     * @param programSettingId Идентификатор параметров программ страхования
     * @param startDate Дата начала продаж
     * @param endDate Дата окончания продаж
     * @param title Заголовок
     * @param image id изображения продукта
     * @param imageName имя изображения продукта
     * @param infoImage id изображения информации о страховом продукте
     * @param infoImageName имя изображения информации о страховом продукте
     * @param description Описание
     * @param link Ссылка на внешний ресурс
     * @param authorizedZoneEnable Признак доступности программы в авторизованной зоне
     * @param notAuthorizedZoneEnable Признак доступности программы в не авторизованной зоне
     * @param application признак Оформление в виде заявления
     * @param priority Приоритет отображения
     * @param templates Список шаблонов документов для печати
     */
    public AcquiringProgramData(Long id, ProgramKind kind, Long programSettingId, LocalDate startDate, LocalDate endDate, String title, Long image, String imageName, Long infoImage, String infoImageName, String description, String link, Boolean authorizedZoneEnable, Boolean notAuthorizedZoneEnable, Boolean application, Integer priority, List<ContractTemplate> templates) {
        this.id = id;
        this.kind = kind;
        this.programSettingId = programSettingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.image = image;
        this.imageName = imageName;
        this.infoImage = infoImage;
        this.infoImageName = infoImageName;
        this.description = description;
        this.link = link;
        this.authorizedZoneEnable = authorizedZoneEnable;
        this.notAuthorizedZoneEnable = notAuthorizedZoneEnable;
        this.application = application;
        this.priority = priority;
        this.templates = templates;
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
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }


    /**
     * Идентификатор параметров программ страхования
    * @return Идентификатор параметров программ страхования
    **/
    @ApiModelProperty(value = "Идентификатор параметров программ страхования")
    


    public Long getProgramSettingId() {
        return programSettingId;
    }

    public void setProgramSettingId(Long programSettingId) {
        this.programSettingId = programSettingId;
    }


    /**
     * Дата начала продаж
    * @return Дата начала продаж
    **/
    @ApiModelProperty(value = "Дата начала продаж")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания продаж
    * @return Дата окончания продаж
    **/
    @ApiModelProperty(value = "Дата окончания продаж")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
     * имя изображения продукта
    * @return имя изображения продукта
    **/
    @ApiModelProperty(value = "имя изображения продукта")
    


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
     * имя изображения информации о страховом продукте
    * @return имя изображения информации о страховом продукте
    **/
    @ApiModelProperty(value = "имя изображения информации о страховом продукте")
    


    public String getInfoImageName() {
        return infoImageName;
    }

    public void setInfoImageName(String infoImageName) {
        this.infoImageName = infoImageName;
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
    


    public Boolean isApplication() {
        return application;
    }

    public void setApplication(Boolean application) {
        this.application = application;
    }


    /**
     * Приоритет отображения
    * @return Приоритет отображения
    **/
    @ApiModelProperty(value = "Приоритет отображения")
    


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    public AcquiringProgramData addTemplatesItem(ContractTemplate templatesItem) {
        if (this.templates == null) {
            this.templates = new ArrayList<ContractTemplate>();
        }
        this.templates.add(templatesItem);
        return this;
    }

    /**
     * Список шаблонов документов для печати
    * @return Список шаблонов документов для печати
    **/
    @ApiModelProperty(value = "Список шаблонов документов для печати")
    
  @Valid


    public List<ContractTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ContractTemplate> templates) {
        this.templates = templates;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquiringProgramData acquiringProgramData = (AcquiringProgramData) o;
        return Objects.equals(this.id, acquiringProgramData.id) &&
            Objects.equals(this.kind, acquiringProgramData.kind) &&
            Objects.equals(this.programSettingId, acquiringProgramData.programSettingId) &&
            Objects.equals(this.startDate, acquiringProgramData.startDate) &&
            Objects.equals(this.endDate, acquiringProgramData.endDate) &&
            Objects.equals(this.title, acquiringProgramData.title) &&
            Objects.equals(this.image, acquiringProgramData.image) &&
            Objects.equals(this.imageName, acquiringProgramData.imageName) &&
            Objects.equals(this.infoImage, acquiringProgramData.infoImage) &&
            Objects.equals(this.infoImageName, acquiringProgramData.infoImageName) &&
            Objects.equals(this.description, acquiringProgramData.description) &&
            Objects.equals(this.link, acquiringProgramData.link) &&
            Objects.equals(this.authorizedZoneEnable, acquiringProgramData.authorizedZoneEnable) &&
            Objects.equals(this.notAuthorizedZoneEnable, acquiringProgramData.notAuthorizedZoneEnable) &&
            Objects.equals(this.application, acquiringProgramData.application) &&
            Objects.equals(this.priority, acquiringProgramData.priority) &&
            Objects.equals(this.templates, acquiringProgramData.templates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kind, programSettingId, startDate, endDate, title, image, imageName, infoImage, infoImageName, description, link, authorizedZoneEnable, notAuthorizedZoneEnable, application, priority, templates);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquiringProgramData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    programSettingId: ").append(toIndentedString(programSettingId)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    imageName: ").append(toIndentedString(imageName)).append("\n");
        sb.append("    infoImage: ").append(toIndentedString(infoImage)).append("\n");
        sb.append("    infoImageName: ").append(toIndentedString(infoImageName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    authorizedZoneEnable: ").append(toIndentedString(authorizedZoneEnable)).append("\n");
        sb.append("    notAuthorizedZoneEnable: ").append(toIndentedString(notAuthorizedZoneEnable)).append("\n");
        sb.append("    application: ").append(toIndentedString(application)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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

