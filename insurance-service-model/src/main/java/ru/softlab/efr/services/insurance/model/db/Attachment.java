package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Сущность для хранения данных вложений, сканов документов
 *
 * @author olshansky
 * @since 25.10.2018
 */

@Entity
@Table(name = "attachments")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachment {

    /**
     * Идентификатор вложения.
     */
    @Id
    private String id;

    /**
     * Вид вложения
     * Текстовое поле длиной 100 символов.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private AttachmentKindEnum kind;

    /**
     * Ссылка на справочник обязательных документов, если вид = INSURANCE_CONTRACT
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "documentType", nullable = true)
    private RequiredDocument documentType;

    /**
     * Дата загрузки вложения
     */
    @Column
    private Timestamp createDate;

    /**
     * Договор к которому относится данное вложение
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "contract", nullable = true)
    private Insurance contract;

    /**
     * Временный идентификатор договора
     */
    @Column(name = "contract_uuid")
    private String contractUuid;

    /**
     * Наименование вложения
     */
    @Column
    private String fileName;

    /**
     * Id пользователя или клиента, загрузившего вложение
     */
    @Column
    private Long owner;

    /**
     * ФИО пользователя или клиента, загрузившего вложение
     */
    @Column
    private String ownerName;

    /**
     * Признак проверенного вложения
     */
    @Column
    private Boolean verified;

    /**
     * Описание файла
     */
    @Column
    private String comment;

    /**
     * Признак удалённого вложения
     */
    @Column
    private Boolean deleted;

    public Attachment(){}

    public Attachment(AttachmentKindEnum kind, RequiredDocument documentType, Timestamp createDate, Insurance contract, String contractUuid, String fileName, Long owner, String ownerName, Boolean verified, String comment, Boolean deleted) {
        this.kind = kind;
        this.documentType = documentType;
        this.createDate = createDate;
        this.contract = contract;
        this.contractUuid = contractUuid;
        this.fileName = fileName;
        this.owner = owner;
        this.ownerName = ownerName;
        this.verified = verified;
        this.comment = comment;
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttachmentKindEnum getKind() {
        return kind;
    }

    public void setKind(AttachmentKindEnum kind) {
        this.kind = kind;
    }

    public RequiredDocument getDocumentType() {
        return documentType;
    }

    public void setDocumentType(RequiredDocument documentType) {
        this.documentType = documentType;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Insurance getContract() {
        return contract;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public void setContract(Insurance contract) {
        this.contract = contract;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
