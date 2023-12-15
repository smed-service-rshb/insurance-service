package ru.softlab.efr.services.insurance.repositories;

import ru.softlab.efr.services.insurance.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для представления договоров страхования в списках.
 *
 * @author Andrey Grigorov
 */
public class InsuranceSummary {

    private Long id;
    private String contractNumber;
    private String initialContractNumber;
    private Long branchId;
    private String branchName;
    private Long subdivisionId;
    private String subdivisionName;
    private LocalDate startDate;
    private LocalDate closeDate;
    private LocalDateTime creationDate;
    private LocalDate conclusionDate;
    private Integer duration;
    private Integer paymentTerm;
    private String employeeName;
    private Long employeeId;
    private ProgramKind type;
    private BigDecimal premium;
    private BigDecimal rurPremium;
    private InsuranceStatusCode status;
    private CalendarUnitEnum calendarUnit;
    private Long holderId;
    private String holderFirstName;
    private String holderSurName;
    private String holderMiddleName;
    private LocalDate holderBirthDate;
    private GenderTypeEnum holderGender;
    private IdentityDocTypeEnum holderDocType;
    private String holderDocSeria;
    private String holderDocNumber;
    private String holderPhoneNumber;
    private String holderEmail;
    private String programName;
    private Long currency;

    public InsuranceSummary(Long id, String contractNumber, Long branchId, String branchName, Long subdivisionId, String subdivisionName, LocalDate startDate, LocalDate closeDate, LocalDateTime creationDate, LocalDate conclusionDate, Integer duration, Integer paymentTerm, String employeeName, Long employeeId, ProgramKind type, BigDecimal premium, BigDecimal rurPremium, InsuranceStatusCode status, CalendarUnitEnum calendarUnit, Long holderId, String holderFirstName, String holderSurName, String holderMiddleName, LocalDate holderBirthDate, GenderTypeEnum holderGender, IdentityDocTypeEnum holderDocType, String holderDocSeria, String holderDocNumber, String holderPhoneNumber, String holderEmail, String programName, String initialContractNumber, Long currency) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.branchId = branchId;
        this.branchName = branchName;
        this.subdivisionId = subdivisionId;
        this.subdivisionName = subdivisionName;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.creationDate = creationDate;
        this.conclusionDate = conclusionDate;
        this.duration = duration;
        this.paymentTerm = paymentTerm;
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.type = type;
        this.premium = premium;
        this.rurPremium = rurPremium;
        this.status = status;
        this.calendarUnit = calendarUnit;
        this.holderId = holderId;
        this.holderFirstName = holderFirstName;
        this.holderSurName = holderSurName;
        this.holderMiddleName = holderMiddleName;
        this.holderBirthDate = holderBirthDate;
        this.holderGender = holderGender;
        this.holderDocType = holderDocType;
        this.holderDocSeria = holderDocSeria;
        this.holderDocNumber = holderDocNumber;
        this.holderPhoneNumber = holderPhoneNumber;
        this.holderEmail = holderEmail;
        this.programName = programName;
        this.initialContractNumber = initialContractNumber;
        this.currency = currency;
    }

    public InsuranceSummary(String contractNumber, LocalDateTime creationDate) {
        this.contractNumber = contractNumber;
        this.creationDate = creationDate;
    }

    public InsuranceSummary() {
    }

    public Long getId() {
        return id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Long getSubdivisionId() {
        return subdivisionId;
    }

    public String getSubdivisionName() {
        return subdivisionName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public ProgramKind getType() {
        return type;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public BigDecimal getRurPremium() {
        return rurPremium;
    }

    public InsuranceStatusCode getStatus() {
        return status;
    }

    public CalendarUnitEnum getCalendarUnit() {
        return calendarUnit;
    }

    public Long getHolderId() {
        return holderId;
    }

    public String getHolderFirstName() {
        return holderFirstName;
    }

    public String getHolderSurName() {
        return holderSurName;
    }

    public String getHolderMiddleName() {
        return holderMiddleName;
    }

    public LocalDate getHolderBirthDate() {
        return holderBirthDate;
    }

    public GenderTypeEnum getHolderGender() {
        return holderGender;
    }

    public IdentityDocTypeEnum getHolderDocType() {
        return holderDocType;
    }

    public String getHolderDocSeria() {
        return holderDocSeria;
    }

    public String getHolderDocNumber() {
        return holderDocNumber;
    }

    public String getHolderPhoneNumber() {
        return holderPhoneNumber;
    }

    public String getHolderEmail() {
        return holderEmail;
    }

    public String getProgramName() {
        return programName;
    }

    public String getInitialContractNumber() {
        return initialContractNumber;
    }

    public Long getCurrency() {
        return currency;
    }
}
