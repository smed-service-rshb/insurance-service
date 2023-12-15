package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Сущность для хранения сформированных отчетов.
 *
 * @author Kalantaev
 * @since 01.02.2019
 */
@Entity
@Table(name = "extract")
public class Extract {

    /**
     * Идентификатор вложения.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String uuid;

    /**
     * Дата создания отчета
     */
    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp createDate;

    /**
     * Наименование файла отчета
     */
    @Column
    private String name;

    /**
     * Тип отчета
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ExtractType type;

    @Column
    @Enumerated(EnumType.STRING)
    private ExtractStatus status;

    @Column(name = "request_digest", length = 32)
    private String requestDigest;

    @Transient
    private byte[] content;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtractStatus getStatus() {
        return status;
    }

    public void setStatus(ExtractStatus status) {
        this.status = status;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getRequestDigest() {
        return requestDigest;
    }

    public void setRequestDigest(String requestDigest) {
        this.requestDigest = requestDigest;
    }

    public ExtractType getType() {
        return type;
    }

    public void setType(ExtractType type) {
        this.type = type;
    }
}
