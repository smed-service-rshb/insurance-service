package ru.softlab.efr.services.insurance.repositories;

import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import java.time.LocalDate;

public class ShortClientTemplates {

    private Long id;

    /**
     * Вид программы страхования
     */
    private ProgramKind kind;

    /**
     * Наименование программы страхования
     */
    private String program;

    /**
     * Признак шаблона
     */
    private Boolean isTemplate;

    /**
     * Наименование документа
     */
    private String name;
    /**
     * Дата начала отображения документа
     */

    private LocalDate startDate;

    /**
     * Дата окончания отображения документа
     */
    private LocalDate endDate;

    /**
     * Приоритет отображения
     */
    private Long sortPriority;

    public ShortClientTemplates(Long id, ProgramKind kind, String program, Boolean isTemplate, String name, LocalDate startDate, LocalDate endDate, Long sortPriority) {
        this.id = id;
        this.kind = kind;
        this.program = program;
        this.isTemplate = isTemplate;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortPriority = sortPriority;
    }

    public Long getId() {
        return id;
    }

    public ProgramKind getKind() {
        return kind;
    }

    public String getProgram() {
        return program;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Long sortPriority) {
        this.sortPriority = sortPriority;
    }
}
