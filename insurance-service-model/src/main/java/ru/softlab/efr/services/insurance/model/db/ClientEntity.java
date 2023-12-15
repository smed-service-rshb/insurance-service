package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.converters.AgreementConverter;
import ru.softlab.efr.services.insurance.model.converters.ListStringConverter;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.model.utils.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.softlab.efr.clients.model.Constants.isCodeRF;
import static ru.softlab.efr.services.insurance.model.utils.ListUtils.convertList;

/**
 * Клиенты
 *
 * @author basharin
 * @since 02.02.2018
 */

@Entity
@Table(name = "clients")
@Audited
public class ClientEntity {

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

    @Column
    @Enumerated(EnumType.STRING)
    private GenderTypeEnum gender;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "birth_country")
    private String birthCountry;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "birth_region")
    private String birthRegion;

    @Column(name = "birth_region_okato")
    private String birthRegionOkato;

    @Column(name = "birth_area")
    private String birthArea;

    // страна гражданства (если гражданство - иное)
    @Column(name = "citizenship_country")
    private String citizenshipCountry;

    @Column(name = "inn")
    private String inn; // ИНН

    @Column(name = "snils")
    private String snils; // СНИЛС

    @Column(name = "license_driver")
    private String licenseDriver; // лицензия водителя

    @Column
    private String resident;

    @Column(name = "resident_rf")
    private Boolean residentRF; // признак резиденства (резидент, нерезидент)

    @Column(name = "tax_residence")
    @Enumerated(EnumType.STRING)
    private TaxResidenceTypeEnum taxResidence; // налоговое резидентство (РФ, иностранное гос-во, иное)

    @Column(name = "tax_payer_number")
    private String taxPayerNumber;  // ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ

    @Column(name = "public_official_status")
    @Enumerated(EnumType.STRING)
    private PublicOfficialTypeEnum publicOfficialStatus; // признак принадлежности к публичным лицам

    @Column(name = "foreign_public_official_type")
    @Enumerated(EnumType.STRING)
    private ForeignPublicOfficialTypeEnum foreignPublicOfficialType;  // признак ИПДЛ

    @Column(name = "russian_public_official_type")
    @Enumerated(EnumType.STRING)
    private RussianPublicOfficialTypeEnum russianPublicOfficialType;  // признак РПДЛ

    @Column(name = "relations")
    @Enumerated(EnumType.STRING)
    private RelationTypeEnum relations; // степень родства

    @Column(name = "public_official_position")
    private String publicOfficialPosition; // должность

    @Column(name = "public_official_name_and_position")
    private String publicOfficialNameAndPosition; // ФИО, должность

    @Column(name = "beneficial_owner")
    private String beneficialOwner; // сведения о бенефициарном владельце

    @Column(name = "business_relations")
    private String businessRelations; // сведения о характере деловых отношений

    @Column(name = "activities_goal")
    private String activitiesGoal; // сведения о целях ФХД

    @Column(name = "business_relations_goal")
    private String businessRelationsGoal; // сведения о целях деловых отношений

    @Column(name = "risk_level_desc")
    private String riskLevelDesc; // уровень риска

    @Column(name = "business_reputation")
    private String businessReputation; // сведения о деловой репутации

    @Column(name = "financial_stability")
    private String financialStability; // сведения о финансовом положении

    @Column(name = "finances_source")
    private String financesSource; // сведения об источниках происхождения средств и/или имущества

    @Column(name = "personal_data_consent")
    private Boolean personalDataConsent; // согласие на обработку ПД

    @Column(name = "sign_ipdl")
    private Boolean signIPDL;

    @Column(name = "sign_pdl")
    private Boolean signPDL;

    @Column(name = "adoption_pdl_written_decision")
    private Boolean adoptionPDLWrittenDecision;

    @Column(name = "has_fatca")
    private Boolean hasFATCA;

    @Column(name = "has_beneficial_owner")
    private Boolean hasBeneficialOwner;

    @Column(name = "has_beneficiary")
    private Boolean hasBeneficiary;

    @Column(name = "bankruptcy_info")
    @Enumerated(EnumType.STRING)
    private BankruptcyInfoEnum bankruptcyInfo;

    @Column(name = "bankruptcy_stage")
    private String bankruptcyStage;

    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevelEnum riskLevel;

    @Column(name = "latin_name")
    private String latinName;

    @Column(name = "code_word")
    private String codeWord;

    @Column
    private String status;

    @Column
    private String email;

    @Column
    private Boolean workflowAgreements;

    @Column(name = "equals_addresses")
    private Boolean equalsAddresses;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "cache_save_time")
    private LocalDateTime cacheSaveTime;

    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @Convert(converter = AgreementConverter.class)
    @Column
    @NotAudited
    private List<Agreement> agreements;

    @Convert(converter = ListStringConverter.class)
    @Column
    @NotAudited
    private List<String> nationalities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PhoneForClaimEntity> phones = new ArrayList<>(); // телефоны

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<AddressForClientEntity> addresses = new ArrayList<>(); // адреса

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<DocumentForClientEntity> documents = new ArrayList<>(); // документы

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotAudited
    private List<PersonDecisionEntity> personDecisions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @NotAudited
    private EmployeeEntity initiator;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotAudited
    private List<ClientCheck> clientChecks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "holder", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Insurance> insurancesHolder = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "insured", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Insurance> insurancesInsured = new ArrayList<>();

    /**
     * Дефолтный конструктор
     */
    public ClientEntity() {
    }

    /**
     * конструктор
     *
     * @param client    клиент
     * @param initiator даннные инициатора заявки
     */
    public ClientEntity(Client client, PrincipalData initiator) {
        this(client);
        if (initiator != null) {
            this.setInitiator(initiator);
        }
    }

    /**
     * Конструктор
     *
     * @param client - Клиент
     */
    public ClientEntity(Client client) {
        this.update(client);
        this.cacheSaveTime = LocalDateTime.now();
        this.registrationDate = LocalDate.now();
        if (!CollectionUtils.isEmpty(client.getPhones())) {
            this.phones = client.getPhones().stream().map(x -> new PhoneForClaimEntity(x, this)).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(client.getDocuments())) {
            this.documents = client.getDocuments().stream().map(x -> new DocumentForClientEntity(x, this)).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(client.getAddresses())) {
            this.addresses = client.getAddresses().stream().map(x -> new AddressForClientEntity(x, this)).collect(Collectors.toList());
        }
    }

    /**
     * конструктор
     *
     * @param clientData данные поиска
     * @param initiator  даннные сотрудника
     */
    public ClientEntity(SearchClientData clientData, PrincipalData initiator) {
        this.surName = clientData.getSurName();
        this.firstName = clientData.getFirstName();
        this.middleName = clientData.getMiddleName();
        this.birthDate = clientData.getBirthDate();
        this.cacheSaveTime = LocalDateTime.now();
        this.registrationDate = LocalDate.now();
        this.setInitiator(initiator);
    }

    /**
     * Конветрация в объекта класса {@link Client}
     *
     * @param clientEntity заявка
     * @return клиент
     */
    public static Client toClient(ClientEntity clientEntity) {
        Client client = new Client();
        client.setId(String.valueOf(clientEntity.getId()));
        client.setSurName(clientEntity.getSurName());
        client.setFirstName(clientEntity.getFirstName());
        client.setMiddleName(clientEntity.getMiddleName());
        client.setBirthDate(clientEntity.getBirthDate());
        if (clientEntity.getGender() != null) {
            client.setGender(Gender.valueOf(clientEntity.getGender().name()));
        }
        client.setBirthPlace(clientEntity.getBirthPlace());
        client.setBirthCountry(clientEntity.getBirthCountry());
        client.setCountryCode(clientEntity.getCountryCode());
        client.setBirthRegion(clientEntity.getBirthRegion());
        client.setBirthRegionOkato(clientEntity.getBirthRegionOkato());
        client.setBirthArea(clientEntity.getBirthArea());
        client.setCitizenshipCountry(clientEntity.getCitizenshipCountry());
        client.setCitizenship(CitizenshipType.fromValue(clientEntity.getResident()));
        client.setResident(clientEntity.getResident());
        client.setResidentRF(isCodeRF(client.getResident()));
        client.setSignIPDL(clientEntity.isSignIPDL());
        client.setSignPDL(clientEntity.isSignPDL());
        client.setHasFATCA(clientEntity.isHasFATCA());
        client.setHasBeneficialOwner(clientEntity.isHasBeneficialOwner());
        client.setHasBeneficiary(clientEntity.isHasBeneficiary());
        client.setAdoptionPDLWrittenDecision(clientEntity.getAdoptionPDLWrittenDecision());
        if (clientEntity.getBankruptcyInfo() != null) {
            client.setBankruptcyInfo(BankruptcyInfo.valueOf(clientEntity.getBankruptcyInfo().name()));
        }
        client.setBankruptcyStage(clientEntity.getBankruptcyStage());
        if (clientEntity.getRiskLevel() != null) {
            client.setRiskLevel(RiskLevel.valueOf(clientEntity.getRiskLevel().name()));
        }
        client.setLatinName(clientEntity.getLatinName());
        client.setCodeWord(clientEntity.getCodeWord());
        client.setStatus(clientEntity.getStatus());
        client.setLastUpdateDate(clientEntity.getLastUpdateDate());
        if (clientEntity.getAgreements() != null) {
            client.setAgreements(clientEntity.getAgreements().stream().map(m -> new ru.softlab.efr.services.insurance.model.rest.Agreement(m.getType(),
                    m.getScanId(),
                    m.getScanFileName(),
                    m.getRecallScanId(),
                    m.getRecallScanFileName(),
                    m.getStartDate(),
                    m.getEndDate(),
                    m.getRecall())).collect(Collectors.toList()));
        }
        client.setEmail(clientEntity.getEmail());
        client.setInn(clientEntity.getInn());
        client.setSnils(clientEntity.getSnils());
        client.setLicenseDriver(clientEntity.getLicenseDriver());
        List<DocumentForClientEntity> documents = clientEntity.getDocuments();
        if (documents == null) documents = Collections.emptyList();
        List<DocumentForClientEntity> identityDocs = documents.stream().filter(DocumentForClientEntity::isIdentity).collect(Collectors.toList());
        client.setDocuments(convertList(identityDocs, DocumentForClientEntity::toDocument));
        client.setPhones(convertList(clientEntity.getPhones(), PhoneForClaimEntity::toPhone));
        client.setAddresses(convertList(clientEntity.getAddresses(), AddressForClientEntity::toAddress));
        client.setEqualsAddresses(clientEntity.getEqualsAddresses());
        client.setResidentRF(clientEntity.getResidentRF());
        if (clientEntity.getTaxResidence() != null) {
            client.setTaxResidence(TaxResidenceType.valueOf(clientEntity.getTaxResidence().name()));
        }
        client.setTaxPayerNumber(clientEntity.getTaxPayerNumber());
        if (clientEntity.getPublicOfficialStatus() != null) {
            client.setPublicOfficialStatus(PublicOfficialType.valueOf(clientEntity.getPublicOfficialStatus().name()));
        }
        if (clientEntity.getForeignPublicOfficialType() != null) {
            client.setForeignPublicOfficialType(ForeignPublicOfficialType.valueOf(clientEntity.getForeignPublicOfficialType().name()));
        }
        if (clientEntity.getRussianPublicOfficialType() != null) {
            client.setRussianPublicOfficialType(RussianPublicOfficialType.valueOf(clientEntity.getRussianPublicOfficialType().name()));
        }
        if (clientEntity.getRelations() != null) {
            client.setRelations(RelationType.valueOf(clientEntity.getRelations().name()));
        }
        client.setPublicOfficialPosition(clientEntity.getPublicOfficialPosition());
        client.setPublicOfficialNameAndPosition(clientEntity.getPublicOfficialNameAndPosition());
        client.setBeneficialOwner(clientEntity.getBeneficialOwner());
        client.setBusinessRelations(clientEntity.getBusinessRelations());
        client.setActivitiesGoal(clientEntity.getActivitiesGoal());
        client.setBusinessRelationsGoal(clientEntity.getBusinessRelationsGoal());
        client.setRiskLevelDesc(clientEntity.getRiskLevelDesc());
        client.setBusinessReputation(clientEntity.getBusinessReputation());
        client.setFinancialStability(clientEntity.getFinancialStability());
        client.setFinancesSource(clientEntity.getFinancesSource());
        client.setPersonalDataConsent(clientEntity.getPersonalDataConsent());
        client.setWorkflowAgreements(clientEntity.getWorkflowAgreements());
        client.setMigrationCard(clientEntity.getAsForeignerDoc(IdentityDocTypeEnum.MIGRATION_CARD));
        client.setForeignerDoc(clientEntity.getAsForeignerDoc(IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT));
        return client;
    }

    public static String getMobilePhone(Client client) {
        if (CollectionUtils.isEmpty(client.getPhones())) {
            return null;
        }
        return client.getPhones().stream()
                .filter(p -> PhoneType.MOBILE.equals(p.getType()) && p.isMain()).findFirst()
                .map(Phone::getNumber).orElse(null);
    }

    public static Document getActualMainDocument(Client client) {
        if (CollectionUtils.isEmpty(client.getDocuments())) {
            return null;
        }
        return client.getDocuments().stream()
                .filter(d -> d.isIsMain() && DocumentForClientEntity.isActualDocument(d))
                .findFirst().orElse(null);
    }

    /**
     * получить id клиента
     *
     * @return id клиента
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * получить фамилию клиента
     *
     * @return фамилию клиента
     */
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * получить имя клиента
     *
     * @return имя клиента
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * получить отчество клиента
     *
     * @return отчество клиента
     */
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * получить дату рождения клиента
     *
     * @return дату рождения клиента
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * получить пол клиента
     *
     * @return пол клиента
     */
    public GenderTypeEnum getGender() {
        return gender;
    }

    public void setGender(GenderTypeEnum gender) {
        this.gender = gender;
    }

    /**
     * получить место рождения клиента
     *
     * @return место рождения клиента
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    /**
     * получить гражданство клиента
     *
     * @return гражданство клиента
     */
    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    /**
     * получить ИПДЛ клиента
     *
     * @return ИПДЛ клиента
     */
    public Boolean isSignIPDL() {
        return signIPDL;
    }

    public void setSignIPDL(Boolean signIPDL) {
        this.signIPDL = signIPDL;
    }

    /**
     * получить ПДЛ клиента
     *
     * @return ПДЛ клиента
     */
    public Boolean isSignPDL() {
        return signPDL;
    }

    public void setSignPDL(Boolean signPDL) {
        this.signPDL = signPDL;
    }

    /**
     * получить наличие письменного решения о принятии ИПДЛ/ПДЛ на обслуживание
     *
     * @return наличие письменного решения о принятии ИПДЛ/ПДЛ на обслуживание
     */
    public Boolean getAdoptionPDLWrittenDecision() {
        return adoptionPDLWrittenDecision;
    }

    public void setAdoptionPDLWrittenDecision(Boolean adoptionPDLWrittenDecision) {
        this.adoptionPDLWrittenDecision = adoptionPDLWrittenDecision;
    }

    /**
     * получить есть ли fatca у клиента
     *
     * @return есть ли fatca у клиента
     */
    public Boolean isHasFATCA() {
        return hasFATCA;
    }

    public void setHasFATCA(Boolean hasFATCA) {
        this.hasFATCA = hasFATCA;
    }

    /**
     * получить есть ли фактический владелец
     *
     * @return есть ли фактический владелец
     */
    public Boolean isHasBeneficialOwner() {
        return hasBeneficialOwner;
    }

    public void setHasBeneficialOwner(Boolean hasBeneficialOwner) {
        this.hasBeneficialOwner = hasBeneficialOwner;
    }

    /**
     * получить есть ли бенифициар
     *
     * @return есть ли бенифициар
     */
    public Boolean isHasBeneficiary() {
        return hasBeneficiary;
    }

    public void setHasBeneficiary(Boolean hasBeneficiary) {
        this.hasBeneficiary = hasBeneficiary;
    }

    /**
     * получить информацию о банкротстве
     *
     * @return информация о банкротстве
     */
    public BankruptcyInfoEnum getBankruptcyInfo() {
        return bankruptcyInfo;
    }

    public void setBankruptcyInfo(BankruptcyInfoEnum bankruptcyInfo) {
        this.bankruptcyInfo = bankruptcyInfo;
    }

    /**
     * получить информацию о стадии банкротства
     *
     * @return стадия банкротства
     */
    public String getBankruptcyStage() {
        return bankruptcyStage;
    }

    public void setBankruptcyStage(String bankruptcyStage) {
        this.bankruptcyStage = bankruptcyStage;
    }

    /**
     * получить уровень риска
     *
     * @return уровень риска
     */
    public RiskLevelEnum getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevelEnum riskLevel) {
        this.riskLevel = riskLevel;
    }

    /**
     * получить имя в латинице
     *
     * @return имя в латинице
     */
    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    /**
     * получить кодовое слово
     *
     * @return кодовое слово
     */
    public String getCodeWord() {
        return codeWord;
    }

    public void setCodeWord(String codeWord) {
        this.codeWord = codeWord;
    }

    /**
     * получить статус
     *
     * @return статус
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * получить email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * получить согласие на обработку персональных данных
     *
     * @return согласие на обработку персональных данных
     */
    public List<ru.softlab.efr.services.insurance.model.db.Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    /**
     * Получить национальности клиента
     *
     * @return национальности клиента
     */
    public List<String> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<String> nationalities) {
        this.nationalities = nationalities;
    }

    /**
     * получить ИНН документа
     *
     * @return ИНН документа
     */
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * получить СНИЛС документа
     *
     * @return СНИЛС документа
     */
    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    /**
     * получить лицензию водителя документа
     *
     * @return лицензию водителя документа
     */
    public String getLicenseDriver() {
        return licenseDriver;
    }

    public void setLicenseDriver(String licenseDriver) {
        this.licenseDriver = licenseDriver;
    }

    public List<PersonDecisionEntity> getPersonDecisions() {
        return personDecisions;
    }

    public List<PhoneForClaimEntity> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneForClaimEntity> phones) {
        this.phones = phones;
    }

    public List<AddressForClientEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressForClientEntity> addresses) {
        this.addresses = addresses;
    }

    public List<DocumentForClientEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentForClientEntity> documents) {
        this.documents = documents;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getCacheSaveTime() {
        return cacheSaveTime;
    }

    public void setCacheSaveTime(LocalDateTime cacheSaveTime) {
        this.cacheSaveTime = cacheSaveTime;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Boolean getResidentRF() {
        return residentRF;
    }

    public void setResidentRF(Boolean residentRF) {
        this.residentRF = residentRF;
    }

    public TaxResidenceTypeEnum getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(TaxResidenceTypeEnum taxResidence) {
        this.taxResidence = taxResidence;
    }

    public String getTaxPayerNumber() {
        return taxPayerNumber;
    }

    public void setTaxPayerNumber(String taxPayerNumber) {
        this.taxPayerNumber = taxPayerNumber;
    }

    public PublicOfficialTypeEnum getPublicOfficialStatus() {
        return publicOfficialStatus;
    }

    public void setPublicOfficialStatus(PublicOfficialTypeEnum publicOfficialStatus) {
        this.publicOfficialStatus = publicOfficialStatus;
    }

    public ForeignPublicOfficialTypeEnum getForeignPublicOfficialType() {
        return foreignPublicOfficialType;
    }

    public void setForeignPublicOfficialType(ForeignPublicOfficialTypeEnum foreignPublicOfficialType) {
        this.foreignPublicOfficialType = foreignPublicOfficialType;
    }

    public RussianPublicOfficialTypeEnum getRussianPublicOfficialType() {
        return russianPublicOfficialType;
    }

    public void setRussianPublicOfficialType(RussianPublicOfficialTypeEnum russianPublicOfficialType) {
        this.russianPublicOfficialType = russianPublicOfficialType;
    }

    public RelationTypeEnum getRelations() {
        return relations;
    }

    public void setRelations(RelationTypeEnum relations) {
        this.relations = relations;
    }

    public String getPublicOfficialPosition() {
        return publicOfficialPosition;
    }

    public void setPublicOfficialPosition(String publicOfficialPosition) {
        this.publicOfficialPosition = publicOfficialPosition;
    }

    public String getPublicOfficialNameAndPosition() {
        return publicOfficialNameAndPosition;
    }

    public void setPublicOfficialNameAndPosition(String publicOfficialNameAndPosition) {
        this.publicOfficialNameAndPosition = publicOfficialNameAndPosition;
    }

    public String getBeneficialOwner() {
        return beneficialOwner;
    }

    public void setBeneficialOwner(String beneficialOwner) {
        this.beneficialOwner = beneficialOwner;
    }

    public String getBusinessRelations() {
        return businessRelations;
    }

    public void setBusinessRelations(String businessRelations) {
        this.businessRelations = businessRelations;
    }

    public String getActivitiesGoal() {
        return activitiesGoal;
    }

    public void setActivitiesGoal(String activitiesGoal) {
        this.activitiesGoal = activitiesGoal;
    }

    public String getBusinessRelationsGoal() {
        return businessRelationsGoal;
    }

    public void setBusinessRelationsGoal(String businessRelationsGoal) {
        this.businessRelationsGoal = businessRelationsGoal;
    }

    public String getBusinessReputation() {
        return businessReputation;
    }

    public void setBusinessReputation(String businessReputation) {
        this.businessReputation = businessReputation;
    }

    public String getFinancialStability() {
        return financialStability;
    }

    public void setFinancialStability(String financialStability) {
        this.financialStability = financialStability;
    }

    public String getFinancesSource() {
        return financesSource;
    }

    public void setFinancesSource(String financesSource) {
        this.financesSource = financesSource;
    }

    public Boolean getPersonalDataConsent() {
        return personalDataConsent;
    }

    public void setPersonalDataConsent(Boolean personalDataConsent) {
        this.personalDataConsent = personalDataConsent;
    }

    public String getRiskLevelDesc() {
        return riskLevelDesc;
    }

    public void setRiskLevelDesc(String riskLevelDesc) {
        this.riskLevelDesc = riskLevelDesc;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public EmployeeEntity getInitiator() {
        return initiator;
    }

    public void setInitiator(EmployeeEntity initiator) {
        this.initiator = initiator;
        this.initiator.setClient(this);
    }

    private void setInitiator(PrincipalData initiator) {
        this.initiator = new EmployeeEntity(initiator.getSecondName(),
                initiator.getFirstName(),
                initiator.getMiddleName(),
                initiator.getPersonnelNumber(),
                initiator.getOffice(),
                "admin",
                this);
    }

    public String getCitizenshipCountry() {
        return citizenshipCountry;
    }

    public void setCitizenshipCountry(String citizenshipCountry) {
        this.citizenshipCountry = citizenshipCountry;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBirthRegion() {
        return birthRegion;
    }

    public void setBirthRegion(String birthRegion) {
        this.birthRegion = birthRegion;
    }

    public String getBirthRegionOkato() {
        return birthRegionOkato;
    }

    public void setBirthRegionOkato(String birthRegionOkato) {
        this.birthRegionOkato = birthRegionOkato;
    }

    public String getBirthArea() {
        return birthArea;
    }

    public void setBirthArea(String birthArea) {
        this.birthArea = birthArea;
    }

    public List<ClientCheck> getClientChecks() {
        return clientChecks;
    }

    public void setClientChecks(List<ClientCheck> clientChecks) {
        this.clientChecks = clientChecks;
    }

    public List<Insurance> getInsurancesHolder() {
        return insurancesHolder;
    }

    public void setInsurancesHolder(List<Insurance> insurancesHolder) {
        this.insurancesHolder = insurancesHolder;
    }

    public List<Insurance> getInsurancesInsured() {
        return insurancesInsured;
    }

    public void setInsurancesInsured(List<Insurance> insurancesInsured) {
        this.insurancesInsured = insurancesInsured;
    }

    /**
     * Добавить решение сотрудника
     *
     * @param decision решение
     */
    public void addPersonDecision(PersonDecisionEntity decision) {
        this.personDecisions.add(decision);
        decision.setClient(this);
    }

    /**
     * Обновить информацию в заявке
     *
     * @param client данные о клиенте
     */
    public final void update(Client client) {
        updateData(client);
        updateAddresses(client.getAddresses());
        updateDocuments(client.getDocuments());
        updateForeignDocuments(client.getForeignerDoc(), IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT);
        updateForeignDocuments(client.getMigrationCard(), IdentityDocTypeEnum.MIGRATION_CARD);
        updatePhones(client.getPhones());
    }

    private void updateForeignDocuments(ForeignerDoc foreignerDoc, IdentityDocTypeEnum foreignerDocType) {
        if (foreignerDoc == null) {
            // Документа нет, поэтому мы должны удалить из списка все документы с типом foreignerDocType
            List<DocumentForClientEntity> updatedList = this.getDocuments().stream()
                    .filter(d -> !foreignerDocType.equals(d.getDocType()))
                    .collect(Collectors.toList());
            this.getDocuments().clear();
            this.getDocuments().addAll(updatedList);
        } else {
            DocumentForClientEntity foreignerDocEntity;
            Optional<DocumentForClientEntity> document = this.getDocuments().stream()
                    .filter(d -> foreignerDocType.equals(d.getDocType()))
                    .findFirst();
            if (document.isPresent()) {
                foreignerDocEntity = document.get();
            } else {
                foreignerDocEntity = new DocumentForClientEntity();
                foreignerDocEntity.setClient(this);
                this.getDocuments().add(foreignerDocEntity);
            }
            foreignerDocEntity.setDocSeries(foreignerDoc.getDocSeries());
            foreignerDocEntity.setDocNumber(foreignerDoc.getDocNumber());
            foreignerDocEntity.setStayStartDate(foreignerDoc.getStayStartDate());
            // Если не указано даты окончания действия документа, назначить документ активным
            if (Objects.isNull(foreignerDoc.getStayEndDate())) {
                foreignerDocEntity.setStayEndDate(null);
                foreignerDocEntity.setActive(true);
            } else {
                foreignerDocEntity.setStayEndDate(foreignerDoc.getStayEndDate());
            }
            foreignerDocEntity.setDocName(foreignerDoc.getDocName() != null ? foreignerDoc.getDocName() : foreignerDocType.toString());
            foreignerDocEntity.setDocType(foreignerDocType);
        }
    }

    private void updateData(Client client) {
        this.surName = client.getSurName().trim();
        this.firstName = client.getFirstName().trim();
        this.middleName = client.getMiddleName() != null ? client.getMiddleName().trim() : null;
        this.lastUpdateDate = client.getLastUpdateDate();
        this.birthDate = client.getBirthDate();
        if (client.getGender() != null) {
            this.gender = GenderTypeEnum.valueOf(client.getGender().name());
        }
        this.birthPlace = client.getBirthPlace();
        this.birthCountry = client.getBirthCountry();
        this.resident = client.getCitizenship() != null ? client.getCitizenship().toString() : null;
        this.citizenshipCountry = client.getCitizenshipCountry();
        this.countryCode = client.getCountryCode();
        this.birthRegion = client.getBirthRegion();
        this.birthRegionOkato = client.getBirthRegionOkato();
        this.birthArea = client.getBirthArea();
        this.signIPDL = client.isSignIPDL();
        this.signPDL = client.isSignPDL();
        this.hasFATCA = client.isHasFATCA();
        this.hasBeneficialOwner = client.isHasBeneficialOwner();
        this.hasBeneficiary = client.isHasBeneficiary();

        if (client.getBankruptcyInfo() != null) {
            this.bankruptcyInfo = BankruptcyInfoEnum.valueOf(client.getBankruptcyInfo().name());
        }
        this.bankruptcyStage = client.getBankruptcyStage();
        if (client.getRiskLevel() != null) {
            this.riskLevel = RiskLevelEnum.valueOf(client.getRiskLevel().name());
        }
        this.latinName = client.getLatinName();
        this.codeWord = client.getCodeWord();
        this.status = client.getStatus();

        if (!CollectionUtils.isEmpty(client.getAgreements())) {
            this.agreements = client.getAgreements().stream().map(m -> new Agreement(m.getType(),
                    m.getScanId(),
                    m.getScanFileName(),
                    m.getRecallScanId(),
                    m.getRecallScanFileName(),
                    m.getStartDate(),
                    m.getEndDate(),
                    m.isIsRecall())).collect(Collectors.toList());
        }
        this.email = client.getEmail() != null ? client.getEmail().toLowerCase() : null;
        this.inn = client.getInn();
        this.snils = client.getSnils();
        this.licenseDriver = client.getLicenseDriver();
        this.adoptionPDLWrittenDecision = client.isAdoptionPDLWrittenDecision();
        this.residentRF = client.isResidentRF();
        if (client.getTaxResidence() != null) {
            this.taxResidence = TaxResidenceTypeEnum.valueOf(client.getTaxResidence().name());
        }
        this.taxPayerNumber = client.getTaxPayerNumber();
        if (client.getPublicOfficialStatus() != null) {
            this.publicOfficialStatus = PublicOfficialTypeEnum.valueOf(client.getPublicOfficialStatus().name());
        }
        if (client.getForeignPublicOfficialType() != null) {
            this.foreignPublicOfficialType = ForeignPublicOfficialTypeEnum.valueOf(client.getForeignPublicOfficialType().name());
        }
        if (client.getRussianPublicOfficialType() != null) {
            this.russianPublicOfficialType = RussianPublicOfficialTypeEnum.valueOf(client.getRussianPublicOfficialType().name());
        }
        if (client.getRelations() != null) {
            this.relations = RelationTypeEnum.valueOf(client.getRelations().name());
        }
        this.publicOfficialPosition = client.getPublicOfficialPosition();
        this.publicOfficialNameAndPosition = client.getPublicOfficialNameAndPosition();
        this.beneficialOwner = client.getBeneficialOwner();
        this.businessRelations = client.getBusinessRelations();
        this.activitiesGoal = client.getActivitiesGoal();
        this.businessRelationsGoal = client.getBusinessRelationsGoal();
        this.riskLevelDesc = client.getRiskLevelDesc();
        this.businessReputation = client.getBusinessReputation();
        this.financialStability = client.getFinancialStability();
        this.financesSource = client.getFinancesSource();
        this.personalDataConsent = client.isPersonalDataConsent();
        this.equalsAddresses = client.isEqualsAddresses();
        if (client.isWorkflowAgreements() != null) {
            this.workflowAgreements = client.isWorkflowAgreements();
        }
    }

    /**
     * Обновить адреса
     *
     * @param addresses адреса
     */
    private void updateAddresses(List<Address> addresses) {
        if (!CollectionUtils.isEmpty(addresses)) {
            List<AddressForClientEntity> updatedList = addresses
                    .stream()
                    .map(x -> {
                        Optional<AddressForClientEntity> orig = this.getAddresses().stream()
                                .filter(el -> el.getAddressType().equals(
                                        AddressTypeEnum.valueOf(x.getAddressType().name()))
                                ).findFirst();
                        if (orig.isPresent()) {
                            AddressForClientEntity addressForClaimEntity = orig.get();
                            addressForClaimEntity.update(x);
                            return addressForClaimEntity;
                        } else {
                            return new AddressForClientEntity(x, this);
                        }
                    })
                    .collect(Collectors.toList());
            this.getAddresses().clear();
            this.getAddresses().addAll(updatedList);
        } else {
            this.getAddresses().clear();
        }
    }

    /**
     * Обновить телефоны
     *
     * @param phones телефоны
     */
    private void updatePhones(List<Phone> phones) {
        if (!CollectionUtils.isEmpty(phones)) {
            List<PhoneForClaimEntity> updatedList = phones
                    .stream()
                    .map(x -> {
                        Optional<PhoneForClaimEntity> orig = this.getPhones().stream()
                                .filter(el -> el.getNumber().equals(x.getNumber())).findFirst();
                        if (orig.isPresent()) {
                            PhoneForClaimEntity phoneForClaimEntity = orig.get();
                            phoneForClaimEntity.update(x);
                            return phoneForClaimEntity;
                        } else {
                            return new PhoneForClaimEntity(x, this);
                        }
                    })
                    .collect(Collectors.toList());
            this.getPhones().clear();
            this.getPhones().addAll(updatedList);
        } else {
            this.getPhones().clear();
        }
    }

    /**
     * Обновить документы
     *
     * @param documents документы
     */
    private void updateDocuments(List<Document> documents) {
        List<DocumentForClientEntity> foreignDocList = new ArrayList<>();
        Optional<DocumentForClientEntity> residentCard = this.getDocuments().stream()
                .filter(foreignDoc -> IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT.equals(foreignDoc.getDocType()))
                .findFirst();
        Optional<DocumentForClientEntity> migrationCard = this.getDocuments().stream()
                .filter(foreignDoc -> IdentityDocTypeEnum.MIGRATION_CARD.equals(foreignDoc.getDocType()))
                .findFirst();
        if (!CollectionUtils.isEmpty(documents)) {
            List<DocumentForClientEntity> updatedList = documents
                    .stream()
                    .map(x -> {
                        IdentityDocTypeEnum docType = x.getDocType() != null ? IdentityDocTypeEnum.valueOf(x.getDocType().name()) : null;
                        Optional<DocumentForClientEntity> orig = this.getDocuments().stream()
                                .filter(el -> Objects.equals(el.getDocType(), docType) &&
                                        StringUtils.compare(el.getDocSeries(), x.getDocSeries()) &&
                                        StringUtils.compare(el.getDocNumber(), x.getDocNumber())).findFirst();
                        if (orig.isPresent()) {
                            DocumentForClientEntity documentForClaimEntity = orig.get();
                            documentForClaimEntity.update(x);
                            return documentForClaimEntity;
                        } else {
                            DocumentForClientEntity documentForClientEntity = new DocumentForClientEntity(x, this);
                            documentForClientEntity.setIdentity(true);
                            return documentForClientEntity;
                        }
                    })
                    .collect(Collectors.toList());
            this.getDocuments().clear();
            residentCard.ifPresent(updatedList::add);
            migrationCard.ifPresent(updatedList::add);
            this.getDocuments().addAll(updatedList);
        } else {
            this.getDocuments().clear();
            residentCard.ifPresent(foreignDocList::add);
            migrationCard.ifPresent(foreignDocList::add);
            this.getDocuments().addAll(foreignDocList);
        }
    }

    public ForeignerDoc getAsForeignerDoc(IdentityDocTypeEnum foreignerDocType) {
        Optional<DocumentForClientEntity> card = Optional.ofNullable(this.getDocuments()).orElseGet(Collections::emptyList).stream().filter(d -> foreignerDocType.equals(d.getDocType())).findFirst();
        if (card.isPresent()) {
            DocumentForClientEntity doc = card.get();
            return new ForeignerDoc(doc.getDocName(), doc.getDocSeries(), doc.getDocNumber(), doc.getStayStartDate(), doc.getStayEndDate());
        } else {
            return new ForeignerDoc();
        }
    }

    public DocumentForClientEntity getMainDocument() {
        if (CollectionUtils.isEmpty(documents)) {
            return null;
        }
        return documents.stream()
                .filter(d -> d.isMain() && d.isIdentity())
                .findFirst()
                .orElse(null);
    }

    public FoundClient toFoundClient(final IdentityDocTypeEnum docType, final String docSeries, final String docNumber) {
        FoundClient foundClient = new FoundClient();
        foundClient.setFirstName(this.getFirstName());
        foundClient.setSurName(this.getSurName());
        foundClient.setMiddleName(this.getMiddleName());
        foundClient.setBirthDate(this.getBirthDate());
        if (Objects.nonNull(this.getGender())) {
            foundClient.setGender(Gender.valueOf(this.getGender().name()));
        }

        // TODO: возможно, необходимо выполнять также и вызов foundClient.setStatus

        foundClient.setId(this.getId() != null ? this.getId().toString() : null);
        DocumentForClientEntity mainDoc;
        if (Objects.nonNull(documents)) {
            //Основной документ
            if (Objects.nonNull(docType)) {
                mainDoc = documents.stream().filter(d -> Objects.equals(d.getDocType(), docType) &&
                        StringUtils.compare(d.getDocSeries(), docSeries) && StringUtils.compare(d.getDocNumber(), docNumber))
                        .findFirst().orElse(new DocumentForClientEntity());
            } else {
                mainDoc = documents.stream().filter(d -> d.isMain() && d.isIdentity()).findFirst().orElse(new DocumentForClientEntity());
            }
        } else {
            mainDoc = new DocumentForClientEntity();
        }
        foundClient.setDocNumber(mainDoc.getDocNumber());
        foundClient.setDocType(mainDoc.getDocType() != null ? DocumentType.valueOf(mainDoc.getDocType().name()) : null);
        foundClient.setDocSeries(mainDoc.getDocSeries());
        if (Objects.nonNull(this.getAddresses())) {
            AddressForClientEntity address = this.getAddresses().stream().findFirst().orElse(new AddressForClientEntity());
            foundClient.setRegAddress(address.asString());
        }
        foundClient.setPhoneNumber(this.getPhones().stream()
                .filter(PhoneForClaimEntity::isMain)
                .map(PhoneForClaimEntity::getNumber)
                .findFirst().orElse(null));
        return foundClient;
    }

    public Boolean getEqualsAddresses() {
        return equalsAddresses;
    }

    public void setEqualsAddresses(Boolean equalsAddresses) {
        this.equalsAddresses = equalsAddresses;
    }

    @Transient
    public String getName() {
        return this.getSurName().concat(" ".concat(this.getFirstName())).concat(
                this.getMiddleName() == null || this.getMiddleName().trim().isEmpty() ? "" :
                        " ".concat(this.getMiddleName())
        );

    }

    @Transient
    public String getMobilePhone() {
        if (CollectionUtils.isEmpty(this.getPhones())) {
            return null;
        }
        return this.getPhones().stream()
                .filter(p -> PhoneType.MOBILE.equals(p.getPhoneType())).findFirst()
                .map(PhoneForClaimEntity::getNumber).orElse(null);
    }

    public Boolean getWorkflowAgreements() {
        return workflowAgreements;
    }

    public void setWorkflowAgreements(Boolean workflowAgreements) {
        this.workflowAgreements = workflowAgreements;
    }
}
