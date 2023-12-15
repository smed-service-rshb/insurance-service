package ru.softlab.efr.services.insurance.repositories;

import ru.softlab.efr.services.insurance.model.enums.TaxResidenceEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NonResidentDTO {

    private String firstName;
    private String surName;
    private String middleName;
    private String contractNumber;
    private LocalDate startDate;
    private LocalDate closeDate;
    private BigDecimal amount;
    private String citizenshipCountry;
    private String inn;
    private TaxResidenceEnum taxResidenceEnum;
    private Long insuranceId;

    public NonResidentDTO(NonResidentDTO nonResidentDTO) {
        this.firstName = nonResidentDTO.getFirstName();
        this.surName = nonResidentDTO.getSurName();
        this.middleName = nonResidentDTO.getMiddleName();
        this.contractNumber = nonResidentDTO.getContractNumber();
        this.startDate = nonResidentDTO.getStartDate();
        this.closeDate = nonResidentDTO.getCloseDate();
        this.amount = nonResidentDTO.getAmount();
        this.citizenshipCountry = nonResidentDTO.getCitizenshipCountry();
        this.inn = nonResidentDTO.getInn();
        this.taxResidenceEnum = nonResidentDTO.getTaxResidenceEnum();
        this.insuranceId = nonResidentDTO.getInsuranceId();
    }

    public NonResidentDTO(String firstName, String surName, String middleName, String contractNumber, LocalDate startDate, LocalDate closeDate, BigDecimal amount, String citizenshipCountry, String inn, Long insuranceId) {
        this.firstName = firstName;
        this.surName = surName;
        this.middleName = middleName;
        this.contractNumber = contractNumber;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.amount = amount;
        this.citizenshipCountry = citizenshipCountry;
        this.inn = inn;
        this.insuranceId = insuranceId;
    }

    public NonResidentDTO(String firstName, String surName, String middleName, String contractNumber, LocalDate startDate, LocalDate closeDate, BigDecimal amount, TaxResidenceEnum taxResidenceEnum, Long insuranceId) {
        this.firstName = firstName;
        this.surName = surName;
        this.middleName = middleName;
        this.contractNumber = contractNumber;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.amount = amount;
        this.taxResidenceEnum = taxResidenceEnum;
        this.insuranceId = insuranceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public TaxResidenceEnum getTaxResidenceEnum() {
        return taxResidenceEnum;
    }

    public void setTaxResidenceEnum(TaxResidenceEnum taxResidenceEnum) {
        this.taxResidenceEnum = taxResidenceEnum;
    }

    public String getCitizenshipCountry() {
        return citizenshipCountry;
    }

    public void setCitizenshipCountry(String citizenshipCountry) {
        this.citizenshipCountry = citizenshipCountry;
    }

    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public void setByNonResidentDTO(NonResidentDTO nonResidentDTO) {
        this.firstName = nonResidentDTO.getFirstName();
        this.surName = nonResidentDTO.getSurName();
        this.middleName = nonResidentDTO.getMiddleName();
        this.contractNumber = nonResidentDTO.getContractNumber();
        this.startDate = nonResidentDTO.getStartDate();
        this.closeDate = nonResidentDTO.getCloseDate();
        this.amount = nonResidentDTO.getAmount();
        this.citizenshipCountry = nonResidentDTO.getCitizenshipCountry();
        this.inn = nonResidentDTO.getInn();
        this.taxResidenceEnum = nonResidentDTO.getTaxResidenceEnum();
        this.insuranceId = nonResidentDTO.getInsuranceId();
    }
}
