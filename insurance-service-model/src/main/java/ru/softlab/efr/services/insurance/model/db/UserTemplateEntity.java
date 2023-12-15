package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника пользовательских шаблонов документов
 *
 * @author olshansky
 * @since 10.04.2019
 */
@Entity
@Table(name = "user_template")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserTemplateEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование, которое будет отображаться пользователю. Текстовое поле длиной 150 символов
     * Обязательно для заполнения
     */
    @Column
    private String name;

    /**
     * Приоритет. Задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет
     */
    @Column
    private Integer priority;

    /**
     * ID записи шаблона документа в таблице print-templates (микросервисе common-dict).
     */
    @Column
    private String templateId;

    /**
     * Наименование файла, которое увидит пользователь при скачивании
     */
    @Column
    private String fileName;

    public UserTemplateEntity() {
    }

    public UserTemplateEntity(String name, Integer priority, String templateId, String fileName) {
        this.name = name;
        this.priority = priority;
        this.templateId = templateId;
        this.fileName = fileName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean getDeleted() {
        return false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
