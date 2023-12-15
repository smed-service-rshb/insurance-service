package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Элемент списка отчёта по изменениям
 * @author olshansky
 * @since 01.02.2019
 */

@Entity
@Immutable
@Table(name = "change_report")
public class ChangeReportItem {

    /**
     * Идентификатор.
     */
    @Id
    private Long id;

    /**
     * Номер договора
     */
    @Column(name = "contract_number")
    private String contractNumber;

    /**
     * Дата заключения договора
     */
    @Column(name = "conclusion_date")
    private LocalDate conclusionDate;

    /**
     * ФИО страхователя
     */
    @Column(name = "holder_name")
    private String holderName;

    /**
     * Дата и время внесения изменения
     */
    @Column(name = "change_date")
    private LocalDateTime changeDate;

    /**
     * Предыдущий статус договора
     */
    @Column(name = "prev_status")
    private String prevStatus;

    /**
     * Новый статус договора
     */
    @Column(name = "next_status")
    private String nextStatus;

    /**
     * Наименование вида прикрепленного документа
     */
    @Column(name = "attached_doc_type")
    private String attachedDocType;

    /**
     * Комплект обязательных документов полный
     */
    @Column(name = "full_set")
    private boolean fullSetDocument;

    /**
     * Пользователь, который внес изменение
     */
    @Column(name = "employee_name")
    private String employeeName;

    public ChangeReportItem() {
    }

    public ChangeReportItem(Long id, String contractNumber, LocalDate conclusionDate, String holderName, LocalDateTime changeDate, String prevStatus, String nextStatus, String attachedDocType, boolean fullSetDocument, String employeeName) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.conclusionDate = conclusionDate;
        this.holderName = holderName;
        this.changeDate = changeDate;
        this.prevStatus = prevStatus;
        this.nextStatus = nextStatus;
        this.attachedDocType = attachedDocType;
        this.fullSetDocument = fullSetDocument;
        this.employeeName = employeeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
    }

    public String getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(String nextStatus) {
        this.nextStatus = nextStatus;
    }

    public String getAttachedDocType() {
        return attachedDocType;
    }

    public void setAttachedDocType(String attachedDocType) {
        this.attachedDocType = attachedDocType;
    }

    public boolean isFullSetDocument() {
        return fullSetDocument;
    }

    public void setFullSetDocument(boolean fullSetDocument) {
        this.fullSetDocument = fullSetDocument;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
