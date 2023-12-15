package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.softlab.efr.services.insurance.model.enums.AcquiringStatus;
import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "acquiring_info")
public class AcquiringInfo {

    /**
     * Идентификатор процесса.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String uuid;

    /**
     * Дата создания
     */
    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp createDate;

    /**
     * Идентификатор программы страхования для оформления в ЛК
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acquiring_program_id")
    private AcquiringProgram acquiringProgram;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "surname")
    private String surName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column
    @Enumerated(EnumType.STRING)
    private GenderTypeEnum gender;

    @Column(name = "doc_number")
    private String docNumber;

    @Column(name = "doc_series")
    private String docSeries;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    /**
     * Идентификатор оформленного договора
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @Column
    @Enumerated(EnumType.STRING)
    private AcquiringStatus status;

    @Column
    private String description;

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

    public AcquiringProgram getAcquiringProgram() {
        return acquiringProgram;
    }

    public void setAcquiringProgram(AcquiringProgram acquiringProgram) {
        this.acquiringProgram = acquiringProgram;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public GenderTypeEnum getGender() {
        return gender;
    }

    public void setGender(GenderTypeEnum gender) {
        this.gender = gender;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public AcquiringStatus getStatus() {
        return status;
    }

    public void setStatus(AcquiringStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
