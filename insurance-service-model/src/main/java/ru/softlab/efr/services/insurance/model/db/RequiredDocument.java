package ru.softlab.efr.services.insurance.model.db;


import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника обязательных документов.
 *
 * @author Kalantaev
 * @since 14.09.2018
 */
@Entity
@Table(name = "required_document")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RequiredDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Тип документа.
     */
    @Column(name = "type")
    private String type;

    /**
     * Признак действующего документа.
     */
    @Column(name = "active")
    private Boolean activeFlag;

    /**
     * Признак логического удаления договора.
     */
    @Column(name = "deleted")
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
