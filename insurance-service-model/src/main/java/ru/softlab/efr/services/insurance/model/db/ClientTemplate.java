package ru.softlab.efr.services.insurance.model.db;

import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность справочника шаблонов заявлений и инструкций
 *
 * @author kalantaev
 */
@Entity
@Table(name = "client_template")
public class ClientTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Вид программы страхования
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ProgramKind kind;

    /**
     * Идентификатор программы страхования, число
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program program;

    /**
     * Признак шаблона
     */
    @Column(name = "is_template")
    private Boolean isTemplate;

    /**
     * Наименование документа
     */
    @Column
    private String name;

    /**
     * Описание документа
     */
    @Column
    private String description;

    /**
     * Ссылка на документ
     */
    @Column
    private String link;

    /**
     * Дата начала отображения документа
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Дата окончания отображения документа
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Приложенный документ
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attach_id")
    private Attachment attachment;

    /**
     * Признак удаленной записи
     */
    @Column
    private Boolean deleted;


    /**
     * Дата окончания отображения документа
     */
    @Column(name = "sort_priority")
    private Long sortPriority;

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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Long sortPriority) {
        this.sortPriority = sortPriority;
    }
}
