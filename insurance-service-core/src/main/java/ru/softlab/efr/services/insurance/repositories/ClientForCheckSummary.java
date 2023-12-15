package ru.softlab.efr.services.insurance.repositories;

import java.time.LocalDate;

public class ClientForCheckSummary {

    private Long id;
    private String surName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String docSeries;
    private String docNumber;

    public ClientForCheckSummary(Long id, String surName, String firstName, String middleName, LocalDate birthDate, String docSeries, String docNumber) {
        this.id = id;
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
    }

    public Long getId() {
        return id;
    }

    public String getSurName() {
        return surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public String getDocNumber() {
        return docNumber;
    }
}
