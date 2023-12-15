package ru.softlab.efr.services.insurance.model.db;


import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность для хранения данных параметров обязательных документов.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Entity
@Table(name = "required_document_setting")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RequiredDocumentSetting implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Вид документа
     * Ссылка на справочник видов обязательных документов (п.2.6.2).
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "requiredDocument", nullable = true)
    private RequiredDocument requiredDocument;

    /**
     * Статус
     */
    @Column
    private String status;

    /**
     * Признак удалённого обязательного документа программы страхования
     */
    @Column
    private Boolean deleted;

    public RequiredDocumentSetting() {
    }

    public RequiredDocumentSetting(Long version, RequiredDocument requiredDocument, String status, Boolean deleted) {
        this.version = version;
        this.requiredDocument = requiredDocument;
        this.status = status;
        this.deleted = deleted;
    }

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

    public RequiredDocument getRequiredDocument() {
        return requiredDocument;
    }

    public void setRequiredDocument(RequiredDocument requiredDocument) {
        this.requiredDocument = requiredDocument;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
