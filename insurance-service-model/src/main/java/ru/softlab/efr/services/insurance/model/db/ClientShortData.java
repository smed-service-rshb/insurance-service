package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Маппинг для получения сокращённой информации по клиенту.
 * Создан для оптимизации запросов к БД.
 *
 * @author Andrey Grigorov
 */
@Entity
@Table(name = "clients")
public class ClientShortData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "surname")
    private String surName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<DocumentForClientShortData> documents = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<AddressForClientEntity> addresses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<DocumentForClientShortData> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentForClientShortData> documents) {
        this.documents = documents;
    }

    public List<AddressForClientEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressForClientEntity> addresses) {
        this.addresses = addresses;
    }
}
