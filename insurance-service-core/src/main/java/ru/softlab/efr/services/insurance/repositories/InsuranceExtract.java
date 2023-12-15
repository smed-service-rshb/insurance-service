package ru.softlab.efr.services.insurance.repositories;

import ru.softlab.efr.services.insurance.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class InsuranceExtract {

    private Long id;
    private String contractNumber;
    private Long branchId;
    private String branchName;
    private Long subdivisionId;
    private String subdivisionName;
    private LocalDate startDate;
    private LocalDate closeDate;
    private LocalDateTime creationDate;
    private Integer duration;
    private Integer paymentTerm;
    private String employeeName;
    private Long employeeId;
    private ProgramKind type;
    private BigDecimal premium;
    private BigDecimal rurPremium;
    private String status;
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
    private String callCenterBranchName;
    private String callCenterSubdivisionName;
    private String callCenterEmployeeName;
    private String callCenterEmployeeNumber;
    private SourceEnum source;
    private String strategyName;
    private LocalDate conclusionDate;
    private LocalDate endDate;
    private PeriodicityEnum periodicity;
    private Long currency;
    private BigDecimal amount;
    private BigDecimal rurAmount;
    private BigDecimal strategyRate;
    private String programCode;
    private String holderResident;
    private TaxResidenceTypeEnum holderTaxResidence; // налоговое резидентство (РФ, иностранное гос-во, иное)
    private String holderTaxPayerNumber;
    private String holderSnils; // СНИЛС
    private String holderBirthCountry;
    private String holderBirthPlace;
    private String holderRegistrationIndex;
    private String holderRegistrationRegion;
    private String holderRegistrationArea;
    private String holderRegistrationCity;
    private String holderRegistrationLocality;
    private String holderRegistrationStreet;
    private String holderRegistrationHouse;
    private String holderRegistrationConstruction;
    private String holderRegistrationHousing;
    private String holderRegistrationApartment;
    private String holderResidenceIndex;
    private String holderResidenceRegion;
    private String holderResidenceArea;
    private String holderResidenceCity;
    private String holderResidenceLocality;
    private String holderResidenceStreet;
    private String holderResidenceHouse;
    private String holderResidenceConstruction;
    private String holderResidenceHousing;
    private String holderResidenceApartment;
    private LocalDate holderDocIssuedDate;
    private String holderDocIssuedBy;
    private String holderDocDivisionCode;
    private String holderMigrationCardSeriesy;
    private String holderMigrationCardNumber;
    private LocalDate holderMigrationCardStartDate;
    private LocalDate holderMigrationCardEndDate;
    private String holderForeignRegDocName;
    private LocalDate holderForeignRegDocStartDate;
    private LocalDate holderForeignRegDocEndDate;
    private PublicOfficialTypeEnum holderPublicOfficial;
    private String holderPublicOfficialPosition;
    private String holderBeneficialOwner;
    private String holderBusinessRelations;
    private String holderActivitiesGoal;
    private String holderBusinessRelationsGoal;
    private String holderRiskLevel;
    private String holderBusinessReputation;
    private String holderFinancialStability;
    private String holderFinancesSource;
    private Boolean holderEqualsInsured;
    private Long insuredId;
    private String insuredFirstName;
    private String insuredSurName;
    private String insuredMiddleName;
    private String insuredResident;
    private TaxResidenceTypeEnum insuredTaxResidence; // налоговое резидентство (РФ, иностранное гос-во, иное)
    private LocalDate insuredBirthDate;
    private GenderTypeEnum insuredGender;
    private String insuredTaxPayerNumber;
    private String insuredSnils; // СНИЛС
    private String insuredBirthCountry;
    private String insuredBirthPlace;
    private String insuredPhoneNumber;
    private String insuredEmail;
    private String insuredRegistrationIndex;
    private String insuredRegistrationRegion;
    private String insuredRegistrationArea;
    private String insuredRegistrationCity;
    private String insuredRegistrationLocality;
    private String insuredRegistrationStreet;
    private String insuredRegistrationHouse;
    private String insuredRegistrationConstruction;
    private String insuredRegistrationHousing;
    private String insuredRegistrationApartment;
    private String insuredResidenceIndex;
    private String insuredResidenceRegion;
    private String insuredResidenceArea;
    private String insuredResidenceCity;
    private String insuredResidenceLocality;
    private String insuredResidenceStreet;
    private String insuredResidenceHouse;
    private String insuredResidenceConstruction;
    private String insuredResidenceHousing;
    private String insuredResidenceApartment;
    private IdentityDocTypeEnum insuredDocType;
    private String insuredDocSeria;
    private String insuredDocNumber;
    private String insuredDocDivisionCode;
    private LocalDate insuredDocIssuedDate;
    private String insuredDocIssuedBy;
    private String insuredMigrationCardSeriesy;
    private String insuredMigrationCardNumber;
    private LocalDate insuredMigrationCardStartDate;
    private LocalDate insuredMigrationCardEndDate;
    private String insuredForeignRegDocName;
    private LocalDate insuredForeignRegDocStartDate;
    private LocalDate insuredForeignRegDocEndDate;
    private PublicOfficialTypeEnum insuredPublicOfficial;
    private String insuredPublicOfficialPosition;
    private String insuredBeneficialOwner;
    private String insuredBusinessRelations;
    private String insuredActivitiesGoal;
    private String insuredBusinessRelationsGoal;
    private String insuredRiskLevel;
    private String insuredBusinessReputation;
    private String insuredFinancialStability;
    private String insuredFinancesSource;
    private Recipient recipient1;
    private Recipient recipient2;
    private Recipient recipient3;
    private Recipient recipient4;
    private Recipient recipient5;
    private BigDecimal risk2Amount;
    private BigDecimal risk3Amount;
    private BigDecimal risk4Amount;
    private Boolean deathRiskHasRecipient;
    private String segment;
    private BigDecimal calculationCoefficient;
    private RiskCalculationSumTypeEnum calculationSumTypeEnum;
    private RiskCalculationTypeEnum calculationType;
    private Integer coolingPeriod;
    private Boolean survivalRiskHasRecipient;
    private Boolean hasSurvivalRisk;
    private Boolean hasDeathRisk;
    private BigDecimal exchangeRate;
    private Boolean fullSetDocument;
    private LocalDate nzbaDate;
    private LocalDate nzbaDate2;
    private LocalDate expirationDate2;
    private LocalDate expirationDate;
    private BigDecimal rate2;
    private String commentForNotFullSetDocument;
    private BigDecimal risk2RurAmount;
    private BigDecimal risk3RurAmount;
    private BigDecimal risk4RurAmount;
    private BigDecimal accidentRiskAmount;

    public InsuranceExtract(Long id, String contractNumber, Long branchId, String branchName, Long subdivisionId, String subdivisionName,
                            LocalDate startDate, LocalDate closeDate, LocalDateTime creationDate, Integer duration, Integer paymentTerm, String employeeName,
                            Long employeeId, ProgramKind type, BigDecimal premium, BigDecimal rurPremium, String status, CalendarUnitEnum calendarUnit,
                            Long holderId, String holderFirstName, String holderSurName, String holderMiddleName, LocalDate holderBirthDate, GenderTypeEnum holderGender,
                            IdentityDocTypeEnum holderDocType, String holderDocSeria, String holderDocNumber, String holderPhoneNumber, String holderEmail, String programName,
                            String callCenterBranchName, String callCenterSubdivisionName, String callCenterEmployeeName, String callCenterEmployeeNumber,
                            SourceEnum source, String strategyName, LocalDate conclusionDate, LocalDate endDate, PeriodicityEnum periodicity, Long currency,
                            BigDecimal amount, BigDecimal rurAmount, BigDecimal strategyRate) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.branchId = branchId;
        this.branchName = branchName;
        this.subdivisionId = subdivisionId;
        this.subdivisionName = subdivisionName;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.creationDate = creationDate;
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
        this.callCenterBranchName = callCenterBranchName;
        this.callCenterSubdivisionName = callCenterSubdivisionName;
        this.callCenterEmployeeName = callCenterEmployeeName;
        this.callCenterEmployeeNumber = callCenterEmployeeNumber;
        this.source = source;
        this.strategyName = strategyName;
        this.conclusionDate = conclusionDate;
        this.endDate = endDate;
        this.periodicity = periodicity;
        this.currency = currency;
        this.amount = amount;
        this.rurAmount = rurAmount;
        this.strategyRate = strategyRate;
    }

    public InsuranceExtract(Long id, String contractNumber, Long branchId, String branchName, Long subdivisionId,
                            String subdivisionName, LocalDate startDate, LocalDate closeDate, LocalDateTime creationDate, Integer duration, Integer paymentTerm, String employeeName, Long employeeId,
                            ProgramKind type, BigDecimal premium, BigDecimal rurPremium, String status, CalendarUnitEnum calendarUnit, Long holderId, String holderFirstName, String holderSurName,
                            String holderMiddleName, LocalDate holderBirthDate, GenderTypeEnum holderGender, IdentityDocTypeEnum holderDocType, String holderDocSeria, String holderDocNumber, String holderPhoneNumber,
                            String holderEmail, String programName, String callCenterBranchName, String callCenterSubdivisionName, String callCenterEmployeeName, String callCenterEmployeeNumber,
                            SourceEnum source, String strategyName, LocalDate conclusionDate, LocalDate endDate, PeriodicityEnum periodicity, Long currency, BigDecimal amount, BigDecimal rurAmount,
                            BigDecimal strategyRate, String programCode, String holderResident, TaxResidenceTypeEnum holderTaxResidence, String holderTaxPayerNumber, String holderSnils,
                            String holderBirthCountry, String holderBirthPlace, String holderRegistrationIndex, String holderRegistrationRegion, String holderRegistrationArea,
                            String holderRegistrationCity, String holderRegistrationLocality, String holderRegistrationStreet, String holderRegistrationHouse, String holderRegistrationConstruction,
                            String holderRegistrationHousing, String holderRegistrationApartment, String holderResidenceIndex, String holderResidenceRegion, String holderResidenceArea,
                            String holderResidenceCity, String holderResidenceLocality, String holderResidenceStreet, String holderResidenceHouse, String holderResidenceConstruction,
                            String holderResidenceHousing, String holderResidenceApartment, LocalDate holderDocIssuedDate, String holderDocIssuedBy, String holderMigrationCardSeriesy,
                            String holderMigrationCardNumber, LocalDate holderMigrationCardStartDate, LocalDate holderMigrationCardEndDate, String holderForeignRegDocName,
                            LocalDate holderForeignRegDocStartDate, LocalDate holderForeignRegDocEndDate, PublicOfficialTypeEnum holderPublicOfficial, String holderPublicOfficialPosition,
                            String holderBeneficialOwner, String holderBusinessRelations, String holderActivitiesGoal, String holderBusinessRelationsGoal, String holderRiskLevel,
                            String holderBusinessReputation, String holderFinancialStability, String holderFinancesSource, Boolean holderEqualsInsured, Long insuredId, String insuredFirstName,
                            String insuredSurName, String insuredMiddleName, String insuredResident, TaxResidenceTypeEnum insuredTaxResidence, LocalDate insuredBirthDate, GenderTypeEnum insuredGender,
                            String insuredTaxPayerNumber, String insuredSnils, String insuredBirthCountry, String insuredBirthPlace, String insuredPhoneNumber, String insuredEmail,
                            String insuredRegistrationIndex, String insuredRegistrationRegion, String insuredRegistrationArea, String insuredRegistrationCity, String insuredRegistrationLocality,
                            String insuredRegistrationStreet, String insuredRegistrationHouse, String insuredRegistrationConstruction, String insuredRegistrationHousing, String insuredRegistrationApartment,
                            String insuredResidenceIndex, String insuredResidenceRegion, String insuredResidenceArea, String insuredResidenceCity, String insuredResidenceLocality, String insuredResidenceStreet,
                            String insuredResidenceHouse, String insuredResidenceConstruction, String insuredResidenceHousing, String insuredResidenceApartment, IdentityDocTypeEnum insuredDocType, String insuredDocSeria,
                            String insuredDocNumber, LocalDate insuredDocIssuedDate, String insuredDocIssuedBy, String insuredMigrationCardSeriesy, String insuredMigrationCardNumber,
                            LocalDate insuredMigrationCardStartDate, LocalDate insuredMigrationCardEndDate, String insuredForeignRegDocName, LocalDate insuredForeignRegDocStartDate,
                            LocalDate insuredForeignRegDocEndDate, PublicOfficialTypeEnum insuredPublicOfficial, String insuredPublicOfficialPosition, String insuredBeneficialOwner,
                            String insuredBusinessRelations, String insuredActivitiesGoal, String insuredBusinessRelationsGoal, String insuredRiskLevel, String insuredBusinessReputation,
                            String insuredFinancialStability, String insuredFinancesSource, String holderDocDivisionCode, String insuredDocDivisionCode,
                            String recipient1Name, LocalDate recipient1birthDate, String recipient1birthPlace, TaxResidenceEnum recipient1taxResidence, BigDecimal recipient1share,
                            String recipient2Name, LocalDate recipient2birthDate, String recipient2birthPlace, TaxResidenceEnum recipient2taxResidence, BigDecimal recipient2share,
                            String recipient3Name, LocalDate recipient3birthDate, String recipient3birthPlace, TaxResidenceEnum recipient3taxResidence, BigDecimal recipient3share,
                            String recipient4Name, LocalDate recipient4birthDate, String recipient4birthPlace, TaxResidenceEnum recipient4taxResidence, BigDecimal recipient4share,
                            String recipient5Name, LocalDate recipient5birthDate, String recipient5birthPlace, TaxResidenceEnum recipient5taxResidence, BigDecimal recipient5share,
                            String segment, BigDecimal risk2Amount, BigDecimal risk3Amount, BigDecimal risk4Amount, Boolean risk1HasRecipient,
                            Boolean risk2HasRecipient,
                            BigDecimal calculationCoefficient, RiskCalculationSumTypeEnum typec, RiskCalculationTypeEnum calculationType, Integer coolingPeriod,
                            Boolean survivalRiskHasRecipient, String survivalRiskName, String deathRiskName,
                            BigDecimal exchangeRate, Boolean fullSetDocument, Date nzbaDate, Date expirationDate, BigDecimal rate2, Date nzbaDate2, Date expirationDate2,
                            String commentForNotFullSetDocument, BigDecimal risk2RurAmount, BigDecimal risk3RurAmount, BigDecimal risk4RurAmount, BigDecimal accidentRiskAmount) {

        this.accidentRiskAmount = accidentRiskAmount;
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
        this.nzbaDate = nzbaDate != null ? nzbaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        this.expirationDate = expirationDate != null ? expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        this.rate2 = rate2;
        this.nzbaDate2 = nzbaDate2 != null ? nzbaDate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        this.expirationDate2 = expirationDate2 != null ? expirationDate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        this.fullSetDocument = fullSetDocument;
        this.exchangeRate = exchangeRate;
        this.coolingPeriod = coolingPeriod;
        this.calculationCoefficient = calculationCoefficient;
        this.calculationSumTypeEnum = typec;
        this.calculationType = calculationType;
        this.recipient1 = new Recipient(recipient1Name, recipient1birthDate, recipient1birthPlace, recipient1taxResidence, recipient1share);
        this.recipient2 = new Recipient(recipient2Name, recipient2birthDate, recipient2birthPlace, recipient2taxResidence, recipient2share);
        this.recipient3 = new Recipient(recipient3Name, recipient3birthDate, recipient3birthPlace, recipient3taxResidence, recipient3share);
        this.recipient4 = new Recipient(recipient4Name, recipient4birthDate, recipient4birthPlace, recipient4taxResidence, recipient4share);
        this.recipient5 = new Recipient(recipient5Name, recipient5birthDate, recipient5birthPlace, recipient5taxResidence, recipient5share);
        this.risk2Amount = risk2Amount;
        this.risk3Amount = risk3Amount;
        this.risk4Amount = risk4Amount;
        this.deathRiskHasRecipient = Boolean.TRUE.equals(risk2HasRecipient);
        this.segment = segment;
        this.id = id;
        this.contractNumber = contractNumber;
        this.holderDocDivisionCode = holderDocDivisionCode;
        this.insuredDocDivisionCode = insuredDocDivisionCode;
        this.branchId = branchId;
        this.branchName = branchName;
        this.subdivisionId = subdivisionId;
        this.subdivisionName = subdivisionName;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.creationDate = creationDate;
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
        this.callCenterBranchName = callCenterBranchName;
        this.callCenterSubdivisionName = callCenterSubdivisionName;
        this.callCenterEmployeeName = callCenterEmployeeName;
        this.callCenterEmployeeNumber = callCenterEmployeeNumber;
        this.source = source;
        this.strategyName = strategyName;
        this.conclusionDate = conclusionDate;
        this.endDate = endDate;
        this.periodicity = periodicity;
        this.currency = currency;
        this.amount = amount;
        this.rurAmount = rurAmount;
        this.strategyRate = strategyRate;
        this.programCode = programCode;
        this.holderResident = holderResident;
        this.holderTaxResidence = holderTaxResidence;
        this.holderTaxPayerNumber = holderTaxPayerNumber;
        this.holderSnils = holderSnils;
        this.holderBirthCountry = holderBirthCountry;
        this.holderBirthPlace = holderBirthPlace;
        this.holderRegistrationIndex = holderRegistrationIndex;
        this.holderRegistrationRegion = holderRegistrationRegion;
        this.holderRegistrationArea = holderRegistrationArea;
        this.holderRegistrationCity = holderRegistrationCity;
        this.holderRegistrationLocality = holderRegistrationLocality;
        this.holderRegistrationStreet = holderRegistrationStreet;
        this.holderRegistrationHouse = holderRegistrationHouse;
        this.holderRegistrationConstruction = holderRegistrationConstruction;
        this.holderRegistrationHousing = holderRegistrationHousing;
        this.holderRegistrationApartment = holderRegistrationApartment;
        this.holderResidenceIndex = holderResidenceIndex;
        this.holderResidenceRegion = holderResidenceRegion;
        this.holderResidenceArea = holderResidenceArea;
        this.holderResidenceCity = holderResidenceCity;
        this.holderResidenceLocality = holderResidenceLocality;
        this.holderResidenceStreet = holderResidenceStreet;
        this.holderResidenceHouse = holderResidenceHouse;
        this.holderResidenceConstruction = holderResidenceConstruction;
        this.holderResidenceHousing = holderResidenceHousing;
        this.holderResidenceApartment = holderResidenceApartment;
        this.holderDocIssuedDate = holderDocIssuedDate;
        this.holderDocIssuedBy = holderDocIssuedBy;
        this.holderMigrationCardSeriesy = holderMigrationCardSeriesy;
        this.holderMigrationCardNumber = holderMigrationCardNumber;
        this.holderMigrationCardStartDate = holderMigrationCardStartDate;
        this.holderMigrationCardEndDate = holderMigrationCardEndDate;
        this.holderForeignRegDocName = holderForeignRegDocName;
        this.holderForeignRegDocStartDate = holderForeignRegDocStartDate;
        this.holderForeignRegDocEndDate = holderForeignRegDocEndDate;
        this.holderPublicOfficial = holderPublicOfficial;
        this.holderPublicOfficialPosition = holderPublicOfficialPosition;
        this.holderBeneficialOwner = holderBeneficialOwner;
        this.holderBusinessRelations = holderBusinessRelations;
        this.holderActivitiesGoal = holderActivitiesGoal;
        this.holderBusinessRelationsGoal = holderBusinessRelationsGoal;
        this.holderRiskLevel = holderRiskLevel;
        this.holderBusinessReputation = holderBusinessReputation;
        this.holderFinancialStability = holderFinancialStability;
        this.holderFinancesSource = holderFinancesSource;
        this.holderEqualsInsured = holderEqualsInsured;
        this.insuredId = insuredId;
        this.insuredFirstName = insuredFirstName;
        this.insuredSurName = insuredSurName;
        this.insuredMiddleName = insuredMiddleName;
        this.insuredResident = insuredResident;
        this.insuredTaxResidence = insuredTaxResidence;
        this.insuredBirthDate = insuredBirthDate;
        this.insuredGender = insuredGender;
        this.insuredTaxPayerNumber = insuredTaxPayerNumber;
        this.insuredSnils = insuredSnils;
        this.insuredBirthCountry = insuredBirthCountry;
        this.insuredBirthPlace = insuredBirthPlace;
        this.insuredPhoneNumber = insuredPhoneNumber;
        this.insuredEmail = insuredEmail;
        this.insuredRegistrationIndex = insuredRegistrationIndex;
        this.insuredRegistrationRegion = insuredRegistrationRegion;
        this.insuredRegistrationArea = insuredRegistrationArea;
        this.insuredRegistrationCity = insuredRegistrationCity;
        this.insuredRegistrationLocality = insuredRegistrationLocality;
        this.insuredRegistrationStreet = insuredRegistrationStreet;
        this.insuredRegistrationHouse = insuredRegistrationHouse;
        this.insuredRegistrationConstruction = insuredRegistrationConstruction;
        this.insuredRegistrationHousing = insuredRegistrationHousing;
        this.insuredRegistrationApartment = insuredRegistrationApartment;
        this.insuredResidenceIndex = insuredResidenceIndex;
        this.insuredResidenceRegion = insuredResidenceRegion;
        this.insuredResidenceArea = insuredResidenceArea;
        this.insuredResidenceCity = insuredResidenceCity;
        this.insuredResidenceLocality = insuredResidenceLocality;
        this.insuredResidenceStreet = insuredResidenceStreet;
        this.insuredResidenceHouse = insuredResidenceHouse;
        this.insuredResidenceConstruction = insuredResidenceConstruction;
        this.insuredResidenceHousing = insuredResidenceHousing;
        this.insuredResidenceApartment = insuredResidenceApartment;
        this.insuredDocType = insuredDocType;
        this.insuredDocSeria = insuredDocSeria;
        this.insuredDocNumber = insuredDocNumber;
        this.insuredDocIssuedDate = insuredDocIssuedDate;
        this.insuredDocIssuedBy = insuredDocIssuedBy;
        this.insuredMigrationCardSeriesy = insuredMigrationCardSeriesy;
        this.insuredMigrationCardNumber = insuredMigrationCardNumber;
        this.insuredMigrationCardStartDate = insuredMigrationCardStartDate;
        this.insuredMigrationCardEndDate = insuredMigrationCardEndDate;
        this.insuredForeignRegDocName = insuredForeignRegDocName;
        this.insuredForeignRegDocStartDate = insuredForeignRegDocStartDate;
        this.insuredForeignRegDocEndDate = insuredForeignRegDocEndDate;
        this.insuredPublicOfficial = insuredPublicOfficial;
        this.insuredPublicOfficialPosition = insuredPublicOfficialPosition;
        this.insuredBeneficialOwner = insuredBeneficialOwner;
        this.insuredBusinessRelations = insuredBusinessRelations;
        this.insuredActivitiesGoal = insuredActivitiesGoal;
        this.insuredBusinessRelationsGoal = insuredBusinessRelationsGoal;
        this.insuredRiskLevel = insuredRiskLevel;
        this.insuredBusinessReputation = insuredBusinessReputation;
        this.insuredFinancialStability = insuredFinancialStability;
        this.insuredFinancesSource = insuredFinancesSource;
        this.survivalRiskHasRecipient = Boolean.TRUE.equals(survivalRiskHasRecipient);
        this.hasSurvivalRisk = survivalRiskName != null;
        this.hasDeathRisk = deathRiskName != null;
    }

    public BigDecimal getRisk2RurAmount() {
        return risk2RurAmount;
    }

    public BigDecimal getRisk3RurAmount() {
        return risk3RurAmount;
    }

    public BigDecimal getRisk4RurAmount() {
        return risk4RurAmount;
    }

    public String getCommentForNotFullSetDocument() {
        return commentForNotFullSetDocument;
    }

    public LocalDate getNzbaDate() {
        return nzbaDate;
    }

    public LocalDate getNzbaDate2() {
        return nzbaDate2;
    }

    public LocalDate getExpirationDate2() {
        return expirationDate2;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public BigDecimal getRate2() {
        return rate2;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public Boolean getFullSetDocument() {
        return fullSetDocument;
    }

    public class Recipient {
        private String name;
        LocalDate birthDate;
        private String birthPlace;
        private TaxResidenceTypeEnum taxResidence;
        private BigDecimal share;

        Recipient(String name, LocalDate birthDate, String birthPlace, TaxResidenceEnum taxResidence, BigDecimal share) {
            this.name = name;
            this.birthDate = birthDate;
            this.birthPlace = birthPlace;
            if (taxResidence != null) {
                this.taxResidence = TaxResidenceTypeEnum.valueOf(taxResidence.name());
            }
            this.share = share;
        }

        public String getName() {
            return name;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public String getBirthPlace() {
            return birthPlace;
        }

        public TaxResidenceTypeEnum getTaxResidence() {
            return taxResidence;
        }

        public BigDecimal getShare() {
            return share;
        }
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

    public String getStatus() {
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

    public String getHolderDocType() {
        if (holderDocType != null) {
            return holderDocType.toString();
        }
        return "";
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

    public String getCallCenterBranchName() {
        return callCenterBranchName;
    }

    public String getCallCenterSubdivisionName() {
        return callCenterSubdivisionName;
    }

    public String getCallCenterEmployeeName() {
        return callCenterEmployeeName;
    }

    public String getCallCenterEmployeeNumber() {
        return callCenterEmployeeNumber;
    }

    public SourceEnum getSource() {
        return source;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PeriodicityEnum getPeriodicity() {
        return periodicity;
    }

    public Long getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRurAmount() {
        return rurAmount;
    }

    public BigDecimal getStrategyRate() {
        return strategyRate;
    }

    public String getProgramCode() {
        return programCode;
    }

    public String getHolderResident() {
        if (this.holderResident != null) {
            return CitizenshipTypeEnum.valueOf(this.holderResident.toUpperCase()).toString();
        }
        return "";
    }

    public TaxResidenceTypeEnum getHolderTaxResidence() {
        return holderTaxResidence;
    }

    public String getHolderTaxPayerNumber() {
        return holderTaxPayerNumber;
    }

    public String getHolderSnils() {
        return holderSnils;
    }

    public String getHolderBirthCountry() {
        return holderBirthCountry;
    }

    public String getHolderBirthPlace() {
        return holderBirthPlace;
    }

    public String getHolderRegistrationIndex() {
        return holderRegistrationIndex;
    }

    public String getHolderRegistrationRegion() {
        return holderRegistrationRegion;
    }

    public String getHolderRegistrationArea() {
        return holderRegistrationArea;
    }

    public String getHolderRegistrationCity() {
        return holderRegistrationCity;
    }

    public String getHolderRegistrationLocality() {
        return holderRegistrationLocality;
    }

    public String getHolderRegistrationStreet() {
        return holderRegistrationStreet;
    }

    public String getHolderRegistrationHouse() {
        return holderRegistrationHouse;
    }

    public String getHolderRegistrationConstruction() {
        return holderRegistrationConstruction;
    }

    public String getHolderRegistrationHousing() {
        return holderRegistrationHousing;
    }

    public String getHolderRegistrationApartment() {
        return holderRegistrationApartment;
    }

    public String getHolderResidenceIndex() {
        return holderResidenceIndex;
    }

    public String getHolderResidenceRegion() {
        return holderResidenceRegion;
    }

    public String getHolderResidenceArea() {
        return holderResidenceArea;
    }

    public String getHolderResidenceCity() {
        return holderResidenceCity;
    }

    public String getHolderResidenceLocality() {
        return holderResidenceLocality;
    }

    public String getHolderResidenceStreet() {
        return holderResidenceStreet;
    }

    public String getHolderResidenceHouse() {
        return holderResidenceHouse;
    }

    public String getHolderResidenceConstruction() {
        return holderResidenceConstruction;
    }

    public String getHolderResidenceHousing() {
        return holderResidenceHousing;
    }

    public String getHolderResidenceApartment() {
        return holderResidenceApartment;
    }

    public LocalDate getHolderDocIssuedDate() {
        return holderDocIssuedDate;
    }

    public String getHolderDocIssuedBy() {
        return holderDocIssuedBy;
    }

    public String getHolderMigrationCardSeriesy() {
        return holderMigrationCardSeriesy;
    }

    public String getHolderMigrationCardNumber() {
        return holderMigrationCardNumber;
    }

    public LocalDate getHolderMigrationCardStartDate() {
        return holderMigrationCardStartDate;
    }

    public LocalDate getHolderMigrationCardEndDate() {
        return holderMigrationCardEndDate;
    }

    public String getHolderForeignRegDocName() {
        return holderForeignRegDocName;
    }

    public LocalDate getHolderForeignRegDocStartDate() {
        return holderForeignRegDocStartDate;
    }

    public LocalDate getHolderForeignRegDocEndDate() {
        return holderForeignRegDocEndDate;
    }

    public String getHolderPublicOfficial() {
        if (holderPublicOfficial != null) {
            return (holderPublicOfficial == PublicOfficialTypeEnum.NONE) ? "Нет" : "Да";
        }
        return "Нет";
    }

    public String getHolderPublicOfficialPosition() {
        return holderPublicOfficialPosition;
    }

    public String getHolderBeneficialOwner() {
        return holderBeneficialOwner;
    }

    public String getHolderBusinessRelations() {
        return holderBusinessRelations;
    }

    public String getHolderActivitiesGoal() {
        return holderActivitiesGoal;
    }

    public String getHolderBusinessRelationsGoal() {
        return holderBusinessRelationsGoal;
    }

    public String getHolderRiskLevel() {
        return holderRiskLevel;
    }

    public String getHolderBusinessReputation() {
        return holderBusinessReputation;
    }

    public String getHolderFinancialStability() {
        return holderFinancialStability;
    }

    public String getHolderFinancesSource() {
        return holderFinancesSource;
    }

    public Long getInsuredId() {
        return holderEqualsInsured ? holderId : insuredId;
    }

    public String getInsuredFirstName() {
        return holderEqualsInsured ? holderFirstName : insuredFirstName;
    }

    public String getInsuredSurName() {
        return holderEqualsInsured ? holderSurName : insuredSurName;
    }

    public String getInsuredMiddleName() {
        return holderEqualsInsured ? holderMiddleName : insuredMiddleName;
    }

    public String getInsuredResident() {
        return holderEqualsInsured ? getHolderResident() : (this.insuredResident != null ?
                CitizenshipTypeEnum.valueOf(this.insuredResident.toUpperCase()).toString() : "");
    }

    public TaxResidenceTypeEnum getInsuredTaxResidence() {
        return holderEqualsInsured ? holderTaxResidence : insuredTaxResidence;
    }

    public LocalDate getInsuredBirthDate() {
        return holderEqualsInsured ? holderBirthDate : insuredBirthDate;
    }

    public String getInsuredTaxPayerNumber() {
        return holderEqualsInsured ? holderTaxPayerNumber : insuredTaxPayerNumber;
    }

    public String getInsuredSnils() {
        return holderEqualsInsured ? holderSnils : insuredSnils;
    }

    public String getInsuredBirthCountry() {
        return holderEqualsInsured ? holderBirthCountry : insuredBirthCountry;
    }

    public String getInsuredBirthPlace() {
        return holderEqualsInsured ? holderBirthPlace : insuredBirthPlace;
    }

    public String getInsuredPhoneNumber() {
        return holderEqualsInsured ? holderPhoneNumber : insuredPhoneNumber;
    }

    public String getInsuredEmail() {
        return holderEqualsInsured ? holderEmail : insuredEmail;
    }

    public String getInsuredRegistrationIndex() {
        return holderEqualsInsured ? holderRegistrationIndex : insuredRegistrationIndex;
    }

    public String getInsuredRegistrationRegion() {
        return holderEqualsInsured ? holderRegistrationRegion : insuredRegistrationRegion;
    }

    public String getInsuredRegistrationArea() {
        return holderEqualsInsured ? holderRegistrationArea : insuredRegistrationArea;
    }

    public String getInsuredRegistrationCity() {
        return holderEqualsInsured ? holderRegistrationCity : insuredRegistrationCity;
    }

    public String getInsuredRegistrationLocality() {
        return holderEqualsInsured ? holderRegistrationLocality : insuredRegistrationLocality;
    }

    public String getInsuredRegistrationStreet() {
        return holderEqualsInsured ? holderRegistrationStreet : insuredRegistrationStreet;
    }

    public String getInsuredRegistrationHouse() {
        return holderEqualsInsured ? holderRegistrationHouse : insuredRegistrationHouse;
    }

    public String getInsuredRegistrationConstruction() {
        return holderEqualsInsured ? holderRegistrationConstruction : insuredRegistrationConstruction;
    }

    public String getInsuredRegistrationHousing() {
        return holderEqualsInsured ? holderRegistrationHousing : insuredRegistrationHousing;
    }

    public String getInsuredRegistrationApartment() {
        return holderEqualsInsured ? holderRegistrationApartment : insuredRegistrationApartment;
    }

    public String getInsuredResidenceIndex() {
        return holderEqualsInsured ? holderResidenceIndex : insuredResidenceIndex;
    }

    public String getInsuredResidenceRegion() {
        return holderEqualsInsured ? holderResidenceRegion : insuredResidenceRegion;
    }

    public String getInsuredResidenceArea() {
        return holderEqualsInsured ? holderResidenceArea : insuredResidenceArea;
    }

    public String getInsuredResidenceCity() {
        return holderEqualsInsured ? holderResidenceCity : insuredResidenceCity;
    }

    public String getInsuredResidenceLocality() {
        return holderEqualsInsured ? holderResidenceLocality : insuredResidenceLocality;
    }

    public String getInsuredResidenceStreet() {
        return holderEqualsInsured ? holderResidenceStreet : insuredResidenceStreet;
    }

    public String getInsuredResidenceHouse() {
        return holderEqualsInsured ? holderResidenceHouse : insuredResidenceHouse;
    }

    public String getInsuredResidenceConstruction() {
        return holderEqualsInsured ? holderResidenceConstruction : insuredResidenceConstruction;
    }

    public String getInsuredResidenceHousing() {
        return holderEqualsInsured ? holderResidenceHousing : insuredResidenceHousing;
    }

    public String getInsuredResidenceApartment() {
        return holderEqualsInsured ? holderResidenceApartment : insuredResidenceApartment;
    }

    public GenderTypeEnum getInsuredGender() {
        return holderEqualsInsured ? holderGender : insuredGender;
    }


    public String getInsuredDocType() {
        return holderEqualsInsured ? getHolderDocType() : (insuredDocType != null ? insuredDocType.toString() : "");
    }

    public String getInsuredDocSeria() {
        return holderEqualsInsured ? holderDocSeria : insuredDocSeria;
    }

    public String getInsuredDocNumber() {
        return holderEqualsInsured ? holderDocNumber : insuredDocNumber;
    }

    public LocalDate getInsuredDocIssuedDate() {
        return holderEqualsInsured ? holderDocIssuedDate : insuredDocIssuedDate;
    }

    public String getInsuredDocIssuedBy() {
        return holderEqualsInsured ? holderDocIssuedBy : insuredDocIssuedBy;
    }

    public String getInsuredMigrationCardSeriesy() {
        return holderEqualsInsured ? holderMigrationCardSeriesy : insuredMigrationCardSeriesy;
    }

    public String getInsuredMigrationCardNumber() {
        return holderEqualsInsured ? holderMigrationCardNumber : insuredMigrationCardNumber;
    }

    public LocalDate getInsuredMigrationCardStartDate() {
        return holderEqualsInsured ? holderMigrationCardStartDate : insuredMigrationCardStartDate;
    }

    public LocalDate getInsuredMigrationCardEndDate() {
        return holderEqualsInsured ? holderMigrationCardEndDate : insuredMigrationCardEndDate;
    }

    public String getInsuredForeignRegDocName() {
        return holderEqualsInsured ? holderForeignRegDocName : insuredForeignRegDocName;
    }

    public LocalDate getInsuredForeignRegDocStartDate() {
        return holderEqualsInsured ? holderForeignRegDocStartDate : insuredForeignRegDocStartDate;
    }

    public LocalDate getInsuredForeignRegDocEndDate() {
        return holderEqualsInsured ? holderForeignRegDocEndDate : insuredForeignRegDocEndDate;
    }

    public String getInsuredPublicOfficial() {
        return holderEqualsInsured ? getHolderPublicOfficial() : (insuredPublicOfficial == null ||
                insuredPublicOfficial == PublicOfficialTypeEnum.NONE ? "Нет" : "Да");
    }

    public String getInsuredPublicOfficialPosition() {
        return holderEqualsInsured ? holderPublicOfficialPosition : insuredPublicOfficialPosition;
    }

    public String getInsuredBeneficialOwner() {
        return holderEqualsInsured ? holderBeneficialOwner : insuredBeneficialOwner;
    }

    public String getInsuredBusinessRelations() {
        return holderEqualsInsured ? holderBusinessRelations : insuredBusinessRelations;
    }

    public String getInsuredActivitiesGoal() {
        return holderEqualsInsured ? holderActivitiesGoal : insuredActivitiesGoal;
    }

    public String getInsuredBusinessRelationsGoal() {
        return holderEqualsInsured ? holderBusinessRelationsGoal : insuredBusinessRelationsGoal;
    }

    public String getInsuredRiskLevel() {
        return holderEqualsInsured ? holderRiskLevel : insuredRiskLevel;
    }

    public String getInsuredBusinessReputation() {
        return holderEqualsInsured ? holderBusinessReputation : insuredBusinessReputation;
    }

    public String getInsuredFinancialStability() {
        return holderEqualsInsured ? holderFinancialStability : insuredFinancialStability;
    }

    public String getInsuredFinancesSource() {
        return holderEqualsInsured ? holderFinancesSource : insuredFinancesSource;
    }

    public String getHolderDocDivisionCode() {
        return holderDocDivisionCode;
    }

    public String getInsuredDocDivisionCode() {
        return insuredDocDivisionCode;
    }

    public String getHolderName() {
        return this.getHolderSurName().concat(" ".concat(this.getHolderFirstName())).concat(
                this.getHolderMiddleName() == null || this.getHolderMiddleName().trim().isEmpty() ? "" :
                        " ".concat(this.getHolderMiddleName())
        );

    }

    public String getInsuredName() {
        return holderEqualsInsured ? getHolderName() : this.getInsuredSurName().concat(" ".concat(this.getInsuredFirstName())).concat(
                this.getInsuredMiddleName() == null || this.getInsuredMiddleName().trim().isEmpty() ? "" :
                        " ".concat(this.getInsuredMiddleName())
        );

    }

    public Recipient getRecipient1() {
        return recipient1;
    }

    public Recipient getRecipient2() {
        return recipient2;
    }

    public Recipient getRecipient3() {
        return recipient3;
    }

    public Recipient getRecipient4() {
        return recipient4;
    }

    public Recipient getRecipient5() {
        return recipient5;
    }

    public String getSegment() {
        return segment;
    }

    public BigDecimal getRisk2Amount() {
        return risk2Amount;
    }

    public BigDecimal getRisk3Amount() {
        return risk3Amount;
    }

    public BigDecimal getRisk4Amount() {
        return risk4Amount;
    }

    public boolean getDeathRiskHasRecipient() {
        return deathRiskHasRecipient;
    }

    public BigDecimal getCalculationCoefficient() {
        return calculationCoefficient;
    }

    public RiskCalculationSumTypeEnum getCalculationSumTypeEnum() {
        return calculationSumTypeEnum;
    }

    public RiskCalculationTypeEnum getCalculationType() {
        return calculationType;
    }

    public Integer getCoolingPeriod() {
        return coolingPeriod;
    }

    public Boolean getSurvivalRiskHasRecipient() {
        return survivalRiskHasRecipient;
    }

    public Boolean hasSurvivalRisk() {
        return hasSurvivalRisk;
    }

    public Boolean hasDeathRisk() {
        return hasDeathRisk;
    }

    public BigDecimal getAccidentRiskAmount() {
        return accidentRiskAmount;
    }
}
