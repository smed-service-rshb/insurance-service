package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import ru.softlab.efr.services.insurance.model.enums.CheckStateEnum;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Результат проверки клиента по разного рода спискам (террористы/недействительные паспорта/замороженные)
 * @author krivenko
 * @since 10.01.2018
 */

@Entity
@Table(name = "client_check")
public class ClientCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип списка по которому производится проверка
     */
    @Column(name="check_type")
    @Enumerated(EnumType.STRING)
    private CheckUnitTypeEnum checkType;

    /**
     * Результат проверки
     */
    @Column(name="check_result")
    @Enumerated(EnumType.STRING)
    private CheckStateEnum checkResult;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    protected ClientShortData client;

    @Column(name="comment")
    private String comment;

    /**
     * Идентификатор записи с информацией о процессе обновления и файле-справочнике
     */
    @Column(name="update_id")
    private Long updateId;

    /**
     * Дата и время создания результата проверки
     */
    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    public ClientCheck() {
    }

    public ClientCheck(CheckUnitTypeEnum checkType, CheckStateEnum checkResult, ClientShortData client, String comment, Long updateId, LocalDateTime creationDate) {
        this.checkType = checkType;
        this.checkResult = checkResult;
        this.client = client;
        this.comment = comment;
        this.updateId = updateId;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientShortData getClient() {
        return client;
    }

    public void setClient(ClientShortData client) {
        this.client = client;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public CheckUnitTypeEnum getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckUnitTypeEnum checkType) {
        this.checkType = checkType;
    }

    public CheckStateEnum getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(CheckStateEnum checkResult) {
        this.checkResult = checkResult;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }
}
