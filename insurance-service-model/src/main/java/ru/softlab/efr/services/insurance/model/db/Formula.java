package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника формул расчёта программ страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "formula")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Formula implements BaseEntity {

    /**
     * Идентификатор формулы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Формула расчёта на интерпретируемом языке
     * Обязательно для заполнения
     */
    @Column
    private String scriptFormula;

    /**
     * Язык, на котором описана формула
     * Обязательно для заполнения
     */
    @Column
    private String language;

    /**
     * Наименование формулы
     * Обязательно для заполнения
     */
    @Column
    private String name;


    /**
     * Признак удалённой формулы
     */
    @Column
    private Boolean deleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScriptFormula() {
        return scriptFormula;
    }

    public void setScriptFormula(String scriptFormula) {
        this.scriptFormula = scriptFormula;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
