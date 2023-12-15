package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Сущность программ страхования для покупки в ЛК
 */
@Entity
@Table(name = "acquiring_program")
public class AcquiringProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id записи

    /**
     * Вид программы страхования.
     * Возможные типы: ИСЖ, НСЖ, КСП.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ProgramKind kind;

    /**
     * Идентификатор настройки программы страхования
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "program_setting_id", nullable = false)
    private ProgramSetting program;

    /**
     * Дата начала продаж
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Дата окончания продаж
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Заголов
     */
    @Column
    private String title;

    /**
     * Доступность в авторизованной зоне
     */
    @Column(name = "authorized_enable")
    private Boolean authorizedZoneEnable;

    /**
     * Доступно в неавторизованной зоне
     */
    @Column(name = "not_authorized_enable")
    private Boolean notAuthorizedZoneEnable;

    /**
     * Признак оформления по заявлению без оплаты
     */
    @Column
    private Boolean application;

    /**
     * Описание программы страхования
     */
    @Column
    private String description;

    /**
     * Ссылка на внешний ресурс
     */
    @Column
    private String link;

    /**
     * Изображение продукта
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "image_id", nullable = false)
    private DbImage image;

    /**
     * Изображение информации о страховом продукте
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "info_image_id")
    private DbImage infoImage;

    @Column
    private Integer priority;

    /**
     * Шаблоны документов для печати
     * Ссылка на справочник шаблонов документов.
     * Если шаблоны не заданы, то печать документов для данной программы страхования не осуществляется.
     */

    @ElementCollection
    @CollectionTable(name = "document_template_2_acquiring_program", joinColumns = @JoinColumn(name = "programId"))
    @Column(name = "documentTemplateId")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> documentTemplateList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }

    public ProgramSetting getProgram() {
        return program;
    }

    public void setProgram(ProgramSetting program) {
        this.program = program;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAuthorizedZoneEnable() {
        return authorizedZoneEnable;
    }

    public void setAuthorizedZoneEnable(Boolean authorizedZoneEnable) {
        this.authorizedZoneEnable = authorizedZoneEnable;
    }

    public Boolean getNotAuthorizedZoneEnable() {
        return notAuthorizedZoneEnable;
    }

    public void setNotAuthorizedZoneEnable(Boolean notAuthorizedZoneEnable) {
        this.notAuthorizedZoneEnable = notAuthorizedZoneEnable;
    }

    public Boolean getApplication() {
        return application;
    }

    public void setApplication(Boolean application) {
        this.application = application;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public DbImage getImage() {
        return image;
    }

    public void setImage(DbImage image) {
        this.image = image;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<String> getDocumentTemplateList() {
        return documentTemplateList;
    }

    public void setDocumentTemplateList(List<String> documentTemplateList) {
        this.documentTemplateList = documentTemplateList;
    }

    public DbImage getInfoImage() {
        return infoImage;
    }

    public void setInfoImage(DbImage infoImage) {
        this.infoImage = infoImage;
    }
}
