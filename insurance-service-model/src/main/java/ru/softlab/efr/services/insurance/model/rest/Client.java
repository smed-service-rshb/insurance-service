package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.Address;
import ru.softlab.efr.services.insurance.model.rest.Agreement;
import ru.softlab.efr.services.insurance.model.rest.BankruptcyInfo;
import ru.softlab.efr.services.insurance.model.rest.CitizenshipType;
import ru.softlab.efr.services.insurance.model.rest.Document;
import ru.softlab.efr.services.insurance.model.rest.ForeignPublicOfficialType;
import ru.softlab.efr.services.insurance.model.rest.ForeignerDoc;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import ru.softlab.efr.services.insurance.model.rest.Phone;
import ru.softlab.efr.services.insurance.model.rest.PublicOfficialType;
import ru.softlab.efr.services.insurance.model.rest.RelationType;
import ru.softlab.efr.services.insurance.model.rest.RiskLevel;
import ru.softlab.efr.services.insurance.model.rest.RussianPublicOfficialType;
import ru.softlab.efr.services.insurance.model.rest.TaxResidenceType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные клиента
 */
@ApiModel(description = "Данные клиента")
@Validated
public class Client   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("birthPlace")
    private String birthPlace = null;

    @JsonProperty("birthCountry")
    private String birthCountry = null;

    @JsonProperty("countryCode")
    private String countryCode = null;

    @JsonProperty("birthRegion")
    private String birthRegion = null;

    @JsonProperty("birthRegionOkato")
    private String birthRegionOkato = null;

    @JsonProperty("birthArea")
    private String birthArea = null;

    @JsonProperty("resident")
    private String resident = null;

    @JsonProperty("citizenship")
    private CitizenshipType citizenship = null;

    @JsonProperty("citizenshipCountry")
    private String citizenshipCountry = null;

    @JsonProperty("taxResidence")
    private TaxResidenceType taxResidence = null;

    @JsonProperty("residentRF")
    private Boolean residentRF = null;

    @JsonProperty("inn")
    private String inn = null;

    @JsonProperty("snils")
    private String snils = null;

    @JsonProperty("licenseDriver")
    private String licenseDriver = null;

    @JsonProperty("agreements")
    @Valid
    private List<Agreement> agreements = null;

    @JsonProperty("taxPayerNumber")
    private String taxPayerNumber = null;

    @JsonProperty("migrationCard")
    private ForeignerDoc migrationCard = null;

    @JsonProperty("foreignerDoc")
    private ForeignerDoc foreignerDoc = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("phones")
    @Valid
    private List<Phone> phones = null;

    @JsonProperty("addresses")
    @Valid
    private List<Address> addresses = null;

    @JsonProperty("equalsAddresses")
    private Boolean equalsAddresses = null;

    @JsonProperty("signIPDL")
    private Boolean signIPDL = null;

    @JsonProperty("signPDL")
    private Boolean signPDL = null;

    @JsonProperty("adoptionPDLWrittenDecision")
    private Boolean adoptionPDLWrittenDecision = null;

    @JsonProperty("hasFATCA")
    private Boolean hasFATCA = null;

    @JsonProperty("hasBeneficialOwner")
    private Boolean hasBeneficialOwner = null;

    @JsonProperty("hasBeneficiary")
    private Boolean hasBeneficiary = null;

    @JsonProperty("bankruptcyInfo")
    private BankruptcyInfo bankruptcyInfo = null;

    @JsonProperty("bankruptcyStage")
    private String bankruptcyStage = null;

    @JsonProperty("latinName")
    private String latinName = null;

    @JsonProperty("codeWord")
    private String codeWord = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("lastUpdateDate")
    private LocalDate lastUpdateDate = null;

    @JsonProperty("publicOfficialStatus")
    private PublicOfficialType publicOfficialStatus = null;

    @JsonProperty("foreignPublicOfficialType")
    private ForeignPublicOfficialType foreignPublicOfficialType = null;

    @JsonProperty("russianPublicOfficialType")
    private RussianPublicOfficialType russianPublicOfficialType = null;

    @JsonProperty("relations")
    private RelationType relations = null;

    @JsonProperty("publicOfficialPosition")
    private String publicOfficialPosition = null;

    @JsonProperty("publicOfficialNameAndPosition")
    private String publicOfficialNameAndPosition = null;

    @JsonProperty("beneficialOwner")
    private String beneficialOwner = null;

    @JsonProperty("businessRelations")
    private String businessRelations = null;

    @JsonProperty("activitiesGoal")
    private String activitiesGoal = null;

    @JsonProperty("businessRelationsGoal")
    private String businessRelationsGoal = null;

    @JsonProperty("riskLevel")
    private RiskLevel riskLevel = null;

    @JsonProperty("riskLevelDesc")
    private String riskLevelDesc = null;

    @JsonProperty("businessReputation")
    private String businessReputation = null;

    @JsonProperty("financialStability")
    private String financialStability = null;

    @JsonProperty("financesSource")
    private String financesSource = null;

    @JsonProperty("personalDataConsent")
    private Boolean personalDataConsent = null;

    @JsonProperty("workflowAgreements")
    private Boolean workflowAgreements = null;

    @JsonProperty("authId")
    private Long authId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Client() {}

    /**
     * Создает экземпляр класса
     * @param id Id клиента
     * @param surName Фамилия клиента
     * @param firstName Имя клиента
     * @param middleName Отчество клиента
     * @param birthDate Дата рождения
     * @param gender Пол
     * @param birthPlace место рождения
     * @param birthCountry Страна рождения
     * @param countryCode Код страны по классификатору ОКСМО
     * @param birthRegion Регион
     * @param birthRegionOkato Код ОКАТО региона
     * @param birthArea Район рождения
     * @param resident Код страны резидентства (неиспользуется)
     * @param citizenship гражданство (Россия, Беларусь, иное)
     * @param citizenshipCountry страна гражданства (если гражданство - иное)
     * @param taxResidence Налоговое резиденство
     * @param residentRF Клиент является резидентом РФ
     * @param inn ИНН (неиспользуется)
     * @param snils СНИЛС
     * @param licenseDriver Водительское удостоверение (неиспользуется)
     * @param agreements Список соглашений на обработку персональных данных (неиспользуется)
     * @param taxPayerNumber ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ
     * @param migrationCard данные миграционной карты (если гражданство - иное)
     * @param foreignerDoc Данные документа, подтверждающие право иностранного гражданина или лица без гражданства на пребывание (проживание) в РФ.
     * @param documents Список документов
     * @param email Электронная почта
     * @param phones Список телефонов
     * @param addresses Список адресов
     * @param equalsAddresses Признак адрес фактического проживания совпадает с адресом регистрации
     * @param signIPDL Признак ИПДП
     * @param signPDL признак ПДЛ/родственник ИПДЛ или ПДЛ
     * @param adoptionPDLWrittenDecision наличия письменного решения о принятии ИПДЛ/ПДЛ на обслуживание
     * @param hasFATCA Наличие FATCA-статуса
     * @param hasBeneficialOwner Наличие бенефициарного владельца
     * @param hasBeneficiary Наличие выгодоприобретателя
     * @param bankruptcyInfo Сведения о банкротстве
     * @param bankruptcyStage Стадия банкротства
     * @param latinName Фамилия и имя на латинице
     * @param codeWord Кодовое слово
     * @param status Статус значимости клиента
     * @param lastUpdateDate Дата последнего обновления
     * @param publicOfficialStatus Признак принадлежности к публичным лицам
     * @param foreignPublicOfficialType Признак ИПДЛ
     * @param russianPublicOfficialType Признак РПДЛ
     * @param relations Степень родства
     * @param publicOfficialPosition должность
     * @param publicOfficialNameAndPosition ФИО, должность
     * @param beneficialOwner сведения о бенефициарном владельце
     * @param businessRelations сведения о характере деловых отношений
     * @param activitiesGoal сведения о целях ФХД
     * @param businessRelationsGoal сведения о целях деловых отношений
     * @param riskLevel Уровень риска
     * @param riskLevelDesc Уровень риска (описание)
     * @param businessReputation сведения о деловой репутации
     * @param financialStability сведения о финансовом положении
     * @param financesSource сведения об источниках происхождения средств и/или имущества
     * @param personalDataConsent согласие на обработку ПД
     * @param workflowAgreements Признак согласия на электронный документооборот
     * @param authId Идентификатор пользователя в сервисе авторизации
     */
    public Client(String id, String surName, String firstName, String middleName, LocalDate birthDate, Gender gender, String birthPlace, String birthCountry, String countryCode, String birthRegion, String birthRegionOkato, String birthArea, String resident, CitizenshipType citizenship, String citizenshipCountry, TaxResidenceType taxResidence, Boolean residentRF, String inn, String snils, String licenseDriver, List<Agreement> agreements, String taxPayerNumber, ForeignerDoc migrationCard, ForeignerDoc foreignerDoc, List<Document> documents, String email, List<Phone> phones, List<Address> addresses, Boolean equalsAddresses, Boolean signIPDL, Boolean signPDL, Boolean adoptionPDLWrittenDecision, Boolean hasFATCA, Boolean hasBeneficialOwner, Boolean hasBeneficiary, BankruptcyInfo bankruptcyInfo, String bankruptcyStage, String latinName, String codeWord, String status, LocalDate lastUpdateDate, PublicOfficialType publicOfficialStatus, ForeignPublicOfficialType foreignPublicOfficialType, RussianPublicOfficialType russianPublicOfficialType, RelationType relations, String publicOfficialPosition, String publicOfficialNameAndPosition, String beneficialOwner, String businessRelations, String activitiesGoal, String businessRelationsGoal, RiskLevel riskLevel, String riskLevelDesc, String businessReputation, String financialStability, String financesSource, Boolean personalDataConsent, Boolean workflowAgreements, Long authId) {
        this.id = id;
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.birthPlace = birthPlace;
        this.birthCountry = birthCountry;
        this.countryCode = countryCode;
        this.birthRegion = birthRegion;
        this.birthRegionOkato = birthRegionOkato;
        this.birthArea = birthArea;
        this.resident = resident;
        this.citizenship = citizenship;
        this.citizenshipCountry = citizenshipCountry;
        this.taxResidence = taxResidence;
        this.residentRF = residentRF;
        this.inn = inn;
        this.snils = snils;
        this.licenseDriver = licenseDriver;
        this.agreements = agreements;
        this.taxPayerNumber = taxPayerNumber;
        this.migrationCard = migrationCard;
        this.foreignerDoc = foreignerDoc;
        this.documents = documents;
        this.email = email;
        this.phones = phones;
        this.addresses = addresses;
        this.equalsAddresses = equalsAddresses;
        this.signIPDL = signIPDL;
        this.signPDL = signPDL;
        this.adoptionPDLWrittenDecision = adoptionPDLWrittenDecision;
        this.hasFATCA = hasFATCA;
        this.hasBeneficialOwner = hasBeneficialOwner;
        this.hasBeneficiary = hasBeneficiary;
        this.bankruptcyInfo = bankruptcyInfo;
        this.bankruptcyStage = bankruptcyStage;
        this.latinName = latinName;
        this.codeWord = codeWord;
        this.status = status;
        this.lastUpdateDate = lastUpdateDate;
        this.publicOfficialStatus = publicOfficialStatus;
        this.foreignPublicOfficialType = foreignPublicOfficialType;
        this.russianPublicOfficialType = russianPublicOfficialType;
        this.relations = relations;
        this.publicOfficialPosition = publicOfficialPosition;
        this.publicOfficialNameAndPosition = publicOfficialNameAndPosition;
        this.beneficialOwner = beneficialOwner;
        this.businessRelations = businessRelations;
        this.activitiesGoal = activitiesGoal;
        this.businessRelationsGoal = businessRelationsGoal;
        this.riskLevel = riskLevel;
        this.riskLevelDesc = riskLevelDesc;
        this.businessReputation = businessReputation;
        this.financialStability = financialStability;
        this.financesSource = financesSource;
        this.personalDataConsent = personalDataConsent;
        this.workflowAgreements = workflowAgreements;
        this.authId = authId;
    }

    /**
     * Id клиента
    * @return Id клиента
    **/
    @ApiModelProperty(value = "Id клиента")
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Фамилия клиента
    * @return Фамилия клиента
    **/
    @ApiModelProperty(required = true, value = "Фамилия клиента")
      @NotNull

 @Size(min=1,max=150)

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    /**
     * Имя клиента
    * @return Имя клиента
    **/
    @ApiModelProperty(required = true, value = "Имя клиента")
      @NotNull

 @Size(min=1,max=150)

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество клиента
    * @return Отчество клиента
    **/
    @ApiModelProperty(value = "Отчество клиента")
    
 @Size(min=1,max=150)

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Дата рождения
    * @return Дата рождения
    **/
    @ApiModelProperty(required = true, value = "Дата рождения")
      @NotNull

  @Valid


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Пол
    * @return Пол
    **/
    @ApiModelProperty(value = "Пол")
    
  @Valid


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    /**
     * место рождения
    * @return место рождения
    **/
    @ApiModelProperty(value = "место рождения")
    
 @Size(max=254)

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }


    /**
     * Страна рождения
    * @return Страна рождения
    **/
    @ApiModelProperty(value = "Страна рождения")
    
 @Size(max=100)

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }


    /**
     * Код страны по классификатору ОКСМО
    * @return Код страны по классификатору ОКСМО
    **/
    @ApiModelProperty(value = "Код страны по классификатору ОКСМО")
    
 @Size(max=3)

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    /**
     * Регион
    * @return Регион
    **/
    @ApiModelProperty(value = "Регион")
    
 @Size(max=100)

    public String getBirthRegion() {
        return birthRegion;
    }

    public void setBirthRegion(String birthRegion) {
        this.birthRegion = birthRegion;
    }


    /**
     * Код ОКАТО региона
    * @return Код ОКАТО региона
    **/
    @ApiModelProperty(value = "Код ОКАТО региона")
    
 @Size(max=11)

    public String getBirthRegionOkato() {
        return birthRegionOkato;
    }

    public void setBirthRegionOkato(String birthRegionOkato) {
        this.birthRegionOkato = birthRegionOkato;
    }


    /**
     * Район рождения
    * @return Район рождения
    **/
    @ApiModelProperty(value = "Район рождения")
    
 @Size(max=100)

    public String getBirthArea() {
        return birthArea;
    }

    public void setBirthArea(String birthArea) {
        this.birthArea = birthArea;
    }


    /**
     * Код страны резидентства (неиспользуется)
    * @return Код страны резидентства (неиспользуется)
    **/
    @ApiModelProperty(value = "Код страны резидентства (неиспользуется)")
    


    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }


    /**
     * гражданство (Россия, Беларусь, иное)
    * @return гражданство (Россия, Беларусь, иное)
    **/
    @ApiModelProperty(value = "гражданство (Россия, Беларусь, иное)")
    
  @Valid


    public CitizenshipType getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(CitizenshipType citizenship) {
        this.citizenship = citizenship;
    }


    /**
     * страна гражданства (если гражданство - иное)
    * @return страна гражданства (если гражданство - иное)
    **/
    @ApiModelProperty(value = "страна гражданства (если гражданство - иное)")
    
 @Size(max=100)

    public String getCitizenshipCountry() {
        return citizenshipCountry;
    }

    public void setCitizenshipCountry(String citizenshipCountry) {
        this.citizenshipCountry = citizenshipCountry;
    }


    /**
     * Налоговое резиденство
    * @return Налоговое резиденство
    **/
    @ApiModelProperty(value = "Налоговое резиденство")
    
  @Valid


    public TaxResidenceType getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(TaxResidenceType taxResidence) {
        this.taxResidence = taxResidence;
    }


    /**
     * Клиент является резидентом РФ
    * @return Клиент является резидентом РФ
    **/
    @ApiModelProperty(value = "Клиент является резидентом РФ")
    


    public Boolean isResidentRF() {
        return residentRF;
    }

    public void setResidentRF(Boolean residentRF) {
        this.residentRF = residentRF;
    }


    /**
     * ИНН (неиспользуется)
    * @return ИНН (неиспользуется)
    **/
    @ApiModelProperty(value = "ИНН (неиспользуется)")
    
 @Size(max=12)

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }


    /**
     * СНИЛС
    * @return СНИЛС
    **/
    @ApiModelProperty(value = "СНИЛС")
    
 @Size(max=11)

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }


    /**
     * Водительское удостоверение (неиспользуется)
    * @return Водительское удостоверение (неиспользуется)
    **/
    @ApiModelProperty(value = "Водительское удостоверение (неиспользуется)")
    
 @Size(max=10)

    public String getLicenseDriver() {
        return licenseDriver;
    }

    public void setLicenseDriver(String licenseDriver) {
        this.licenseDriver = licenseDriver;
    }


    public Client addAgreementsItem(Agreement agreementsItem) {
        if (this.agreements == null) {
            this.agreements = new ArrayList<Agreement>();
        }
        this.agreements.add(agreementsItem);
        return this;
    }

    /**
     * Список соглашений на обработку персональных данных (неиспользуется)
    * @return Список соглашений на обработку персональных данных (неиспользуется)
    **/
    @ApiModelProperty(value = "Список соглашений на обработку персональных данных (неиспользуется)")
    
  @Valid


    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }


    /**
     * ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ
    * @return ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ
    **/
    @ApiModelProperty(value = "ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ")
    
 @Size(max=25)

    public String getTaxPayerNumber() {
        return taxPayerNumber;
    }

    public void setTaxPayerNumber(String taxPayerNumber) {
        this.taxPayerNumber = taxPayerNumber;
    }


    /**
     * данные миграционной карты (если гражданство - иное)
    * @return данные миграционной карты (если гражданство - иное)
    **/
    @ApiModelProperty(value = "данные миграционной карты (если гражданство - иное)")
    
  @Valid


    public ForeignerDoc getMigrationCard() {
        return migrationCard;
    }

    public void setMigrationCard(ForeignerDoc migrationCard) {
        this.migrationCard = migrationCard;
    }


    /**
     * Данные документа, подтверждающие право иностранного гражданина или лица без гражданства на пребывание (проживание) в РФ.
    * @return Данные документа, подтверждающие право иностранного гражданина или лица без гражданства на пребывание (проживание) в РФ.
    **/
    @ApiModelProperty(value = "Данные документа, подтверждающие право иностранного гражданина или лица без гражданства на пребывание (проживание) в РФ.")
    
  @Valid


    public ForeignerDoc getForeignerDoc() {
        return foreignerDoc;
    }

    public void setForeignerDoc(ForeignerDoc foreignerDoc) {
        this.foreignerDoc = foreignerDoc;
    }


    public Client addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<Document>();
        }
        this.documents.add(documentsItem);
        return this;
    }

    /**
     * Список документов
    * @return Список документов
    **/
    @ApiModelProperty(value = "Список документов")
    
  @Valid


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }


    /**
     * Электронная почта
    * @return Электронная почта
    **/
    @ApiModelProperty(value = "Электронная почта")
    
 @Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(((\\w+)|(\\w+-\\w+))\\.)+[a-zA-Z]{2,6})$)") @Size(max=150)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Client addPhonesItem(Phone phonesItem) {
        if (this.phones == null) {
            this.phones = new ArrayList<Phone>();
        }
        this.phones.add(phonesItem);
        return this;
    }

    /**
     * Список телефонов
    * @return Список телефонов
    **/
    @ApiModelProperty(value = "Список телефонов")
    
  @Valid


    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }


    public Client addAddressesItem(Address addressesItem) {
        if (this.addresses == null) {
            this.addresses = new ArrayList<Address>();
        }
        this.addresses.add(addressesItem);
        return this;
    }

    /**
     * Список адресов
    * @return Список адресов
    **/
    @ApiModelProperty(value = "Список адресов")
    
  @Valid


    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }


    /**
     * Признак адрес фактического проживания совпадает с адресом регистрации
    * @return Признак адрес фактического проживания совпадает с адресом регистрации
    **/
    @ApiModelProperty(value = "Признак адрес фактического проживания совпадает с адресом регистрации")
    


    public Boolean isEqualsAddresses() {
        return equalsAddresses;
    }

    public void setEqualsAddresses(Boolean equalsAddresses) {
        this.equalsAddresses = equalsAddresses;
    }


    /**
     * Признак ИПДП
    * @return Признак ИПДП
    **/
    @ApiModelProperty(value = "Признак ИПДП")
    


    public Boolean isSignIPDL() {
        return signIPDL;
    }

    public void setSignIPDL(Boolean signIPDL) {
        this.signIPDL = signIPDL;
    }


    /**
     * признак ПДЛ/родственник ИПДЛ или ПДЛ
    * @return признак ПДЛ/родственник ИПДЛ или ПДЛ
    **/
    @ApiModelProperty(value = "признак ПДЛ/родственник ИПДЛ или ПДЛ")
    


    public Boolean isSignPDL() {
        return signPDL;
    }

    public void setSignPDL(Boolean signPDL) {
        this.signPDL = signPDL;
    }


    /**
     * наличия письменного решения о принятии ИПДЛ/ПДЛ на обслуживание
    * @return наличия письменного решения о принятии ИПДЛ/ПДЛ на обслуживание
    **/
    @ApiModelProperty(value = "наличия письменного решения о принятии ИПДЛ/ПДЛ на обслуживание")
    


    public Boolean isAdoptionPDLWrittenDecision() {
        return adoptionPDLWrittenDecision;
    }

    public void setAdoptionPDLWrittenDecision(Boolean adoptionPDLWrittenDecision) {
        this.adoptionPDLWrittenDecision = adoptionPDLWrittenDecision;
    }


    /**
     * Наличие FATCA-статуса
    * @return Наличие FATCA-статуса
    **/
    @ApiModelProperty(value = "Наличие FATCA-статуса")
    


    public Boolean isHasFATCA() {
        return hasFATCA;
    }

    public void setHasFATCA(Boolean hasFATCA) {
        this.hasFATCA = hasFATCA;
    }


    /**
     * Наличие бенефициарного владельца
    * @return Наличие бенефициарного владельца
    **/
    @ApiModelProperty(value = "Наличие бенефициарного владельца")
    


    public Boolean isHasBeneficialOwner() {
        return hasBeneficialOwner;
    }

    public void setHasBeneficialOwner(Boolean hasBeneficialOwner) {
        this.hasBeneficialOwner = hasBeneficialOwner;
    }


    /**
     * Наличие выгодоприобретателя
    * @return Наличие выгодоприобретателя
    **/
    @ApiModelProperty(value = "Наличие выгодоприобретателя")
    


    public Boolean isHasBeneficiary() {
        return hasBeneficiary;
    }

    public void setHasBeneficiary(Boolean hasBeneficiary) {
        this.hasBeneficiary = hasBeneficiary;
    }


    /**
     * Сведения о банкротстве
    * @return Сведения о банкротстве
    **/
    @ApiModelProperty(value = "Сведения о банкротстве")
    
  @Valid


    public BankruptcyInfo getBankruptcyInfo() {
        return bankruptcyInfo;
    }

    public void setBankruptcyInfo(BankruptcyInfo bankruptcyInfo) {
        this.bankruptcyInfo = bankruptcyInfo;
    }


    /**
     * Стадия банкротства
    * @return Стадия банкротства
    **/
    @ApiModelProperty(value = "Стадия банкротства")
    
 @Size(max=20)

    public String getBankruptcyStage() {
        return bankruptcyStage;
    }

    public void setBankruptcyStage(String bankruptcyStage) {
        this.bankruptcyStage = bankruptcyStage;
    }


    /**
     * Фамилия и имя на латинице
    * @return Фамилия и имя на латинице
    **/
    @ApiModelProperty(value = "Фамилия и имя на латинице")
    
 @Size(max=80)

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }


    /**
     * Кодовое слово
    * @return Кодовое слово
    **/
    @ApiModelProperty(value = "Кодовое слово")
    
 @Size(max=20)

    public String getCodeWord() {
        return codeWord;
    }

    public void setCodeWord(String codeWord) {
        this.codeWord = codeWord;
    }


    /**
     * Статус значимости клиента
    * @return Статус значимости клиента
    **/
    @ApiModelProperty(value = "Статус значимости клиента")
    
 @Size(max=20)

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * Дата последнего обновления
    * @return Дата последнего обновления
    **/
    @ApiModelProperty(value = "Дата последнего обновления")
    
  @Valid


    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }


    /**
     * Признак принадлежности к публичным лицам
    * @return Признак принадлежности к публичным лицам
    **/
    @ApiModelProperty(value = "Признак принадлежности к публичным лицам")
    
  @Valid


    public PublicOfficialType getPublicOfficialStatus() {
        return publicOfficialStatus;
    }

    public void setPublicOfficialStatus(PublicOfficialType publicOfficialStatus) {
        this.publicOfficialStatus = publicOfficialStatus;
    }


    /**
     * Признак ИПДЛ
    * @return Признак ИПДЛ
    **/
    @ApiModelProperty(value = "Признак ИПДЛ")
    
  @Valid


    public ForeignPublicOfficialType getForeignPublicOfficialType() {
        return foreignPublicOfficialType;
    }

    public void setForeignPublicOfficialType(ForeignPublicOfficialType foreignPublicOfficialType) {
        this.foreignPublicOfficialType = foreignPublicOfficialType;
    }


    /**
     * Признак РПДЛ
    * @return Признак РПДЛ
    **/
    @ApiModelProperty(value = "Признак РПДЛ")
    
  @Valid


    public RussianPublicOfficialType getRussianPublicOfficialType() {
        return russianPublicOfficialType;
    }

    public void setRussianPublicOfficialType(RussianPublicOfficialType russianPublicOfficialType) {
        this.russianPublicOfficialType = russianPublicOfficialType;
    }


    /**
     * Степень родства
    * @return Степень родства
    **/
    @ApiModelProperty(value = "Степень родства")
    
  @Valid


    public RelationType getRelations() {
        return relations;
    }

    public void setRelations(RelationType relations) {
        this.relations = relations;
    }


    /**
     * должность
    * @return должность
    **/
    @ApiModelProperty(value = "должность")
    
 @Size(max=200)

    public String getPublicOfficialPosition() {
        return publicOfficialPosition;
    }

    public void setPublicOfficialPosition(String publicOfficialPosition) {
        this.publicOfficialPosition = publicOfficialPosition;
    }


    /**
     * ФИО, должность
    * @return ФИО, должность
    **/
    @ApiModelProperty(value = "ФИО, должность")
    
 @Size(max=250)

    public String getPublicOfficialNameAndPosition() {
        return publicOfficialNameAndPosition;
    }

    public void setPublicOfficialNameAndPosition(String publicOfficialNameAndPosition) {
        this.publicOfficialNameAndPosition = publicOfficialNameAndPosition;
    }


    /**
     * сведения о бенефициарном владельце
    * @return сведения о бенефициарном владельце
    **/
    @ApiModelProperty(value = "сведения о бенефициарном владельце")
    
 @Size(max=200)

    public String getBeneficialOwner() {
        return beneficialOwner;
    }

    public void setBeneficialOwner(String beneficialOwner) {
        this.beneficialOwner = beneficialOwner;
    }


    /**
     * сведения о характере деловых отношений
    * @return сведения о характере деловых отношений
    **/
    @ApiModelProperty(value = "сведения о характере деловых отношений")
    
 @Size(max=50)

    public String getBusinessRelations() {
        return businessRelations;
    }

    public void setBusinessRelations(String businessRelations) {
        this.businessRelations = businessRelations;
    }


    /**
     * сведения о целях ФХД
    * @return сведения о целях ФХД
    **/
    @ApiModelProperty(value = "сведения о целях ФХД")
    
 @Size(max=50)

    public String getActivitiesGoal() {
        return activitiesGoal;
    }

    public void setActivitiesGoal(String activitiesGoal) {
        this.activitiesGoal = activitiesGoal;
    }


    /**
     * сведения о целях деловых отношений
    * @return сведения о целях деловых отношений
    **/
    @ApiModelProperty(value = "сведения о целях деловых отношений")
    
 @Size(max=50)

    public String getBusinessRelationsGoal() {
        return businessRelationsGoal;
    }

    public void setBusinessRelationsGoal(String businessRelationsGoal) {
        this.businessRelationsGoal = businessRelationsGoal;
    }


    /**
     * Уровень риска
    * @return Уровень риска
    **/
    @ApiModelProperty(value = "Уровень риска")
    
  @Valid


    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }


    /**
     * Уровень риска (описание)
    * @return Уровень риска (описание)
    **/
    @ApiModelProperty(value = "Уровень риска (описание)")
    
 @Size(max=500)

    public String getRiskLevelDesc() {
        return riskLevelDesc;
    }

    public void setRiskLevelDesc(String riskLevelDesc) {
        this.riskLevelDesc = riskLevelDesc;
    }


    /**
     * сведения о деловой репутации
    * @return сведения о деловой репутации
    **/
    @ApiModelProperty(value = "сведения о деловой репутации")
    
 @Size(max=50)

    public String getBusinessReputation() {
        return businessReputation;
    }

    public void setBusinessReputation(String businessReputation) {
        this.businessReputation = businessReputation;
    }


    /**
     * сведения о финансовом положении
    * @return сведения о финансовом положении
    **/
    @ApiModelProperty(value = "сведения о финансовом положении")
    
 @Size(max=50)

    public String getFinancialStability() {
        return financialStability;
    }

    public void setFinancialStability(String financialStability) {
        this.financialStability = financialStability;
    }


    /**
     * сведения об источниках происхождения средств и/или имущества
    * @return сведения об источниках происхождения средств и/или имущества
    **/
    @ApiModelProperty(value = "сведения об источниках происхождения средств и/или имущества")
    
 @Size(max=50)

    public String getFinancesSource() {
        return financesSource;
    }

    public void setFinancesSource(String financesSource) {
        this.financesSource = financesSource;
    }


    /**
     * согласие на обработку ПД
    * @return согласие на обработку ПД
    **/
    @ApiModelProperty(value = "согласие на обработку ПД")
    


    public Boolean isPersonalDataConsent() {
        return personalDataConsent;
    }

    public void setPersonalDataConsent(Boolean personalDataConsent) {
        this.personalDataConsent = personalDataConsent;
    }


    /**
     * Признак согласия на электронный документооборот
    * @return Признак согласия на электронный документооборот
    **/
    @ApiModelProperty(value = "Признак согласия на электронный документооборот")
    


    public Boolean isWorkflowAgreements() {
        return workflowAgreements;
    }

    public void setWorkflowAgreements(Boolean workflowAgreements) {
        this.workflowAgreements = workflowAgreements;
    }


    /**
     * Идентификатор пользователя в сервисе авторизации
    * @return Идентификатор пользователя в сервисе авторизации
    **/
    @ApiModelProperty(value = "Идентификатор пользователя в сервисе авторизации")
    


    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(this.id, client.id) &&
            Objects.equals(this.surName, client.surName) &&
            Objects.equals(this.firstName, client.firstName) &&
            Objects.equals(this.middleName, client.middleName) &&
            Objects.equals(this.birthDate, client.birthDate) &&
            Objects.equals(this.gender, client.gender) &&
            Objects.equals(this.birthPlace, client.birthPlace) &&
            Objects.equals(this.birthCountry, client.birthCountry) &&
            Objects.equals(this.countryCode, client.countryCode) &&
            Objects.equals(this.birthRegion, client.birthRegion) &&
            Objects.equals(this.birthRegionOkato, client.birthRegionOkato) &&
            Objects.equals(this.birthArea, client.birthArea) &&
            Objects.equals(this.resident, client.resident) &&
            Objects.equals(this.citizenship, client.citizenship) &&
            Objects.equals(this.citizenshipCountry, client.citizenshipCountry) &&
            Objects.equals(this.taxResidence, client.taxResidence) &&
            Objects.equals(this.residentRF, client.residentRF) &&
            Objects.equals(this.inn, client.inn) &&
            Objects.equals(this.snils, client.snils) &&
            Objects.equals(this.licenseDriver, client.licenseDriver) &&
            Objects.equals(this.agreements, client.agreements) &&
            Objects.equals(this.taxPayerNumber, client.taxPayerNumber) &&
            Objects.equals(this.migrationCard, client.migrationCard) &&
            Objects.equals(this.foreignerDoc, client.foreignerDoc) &&
            Objects.equals(this.documents, client.documents) &&
            Objects.equals(this.email, client.email) &&
            Objects.equals(this.phones, client.phones) &&
            Objects.equals(this.addresses, client.addresses) &&
            Objects.equals(this.equalsAddresses, client.equalsAddresses) &&
            Objects.equals(this.signIPDL, client.signIPDL) &&
            Objects.equals(this.signPDL, client.signPDL) &&
            Objects.equals(this.adoptionPDLWrittenDecision, client.adoptionPDLWrittenDecision) &&
            Objects.equals(this.hasFATCA, client.hasFATCA) &&
            Objects.equals(this.hasBeneficialOwner, client.hasBeneficialOwner) &&
            Objects.equals(this.hasBeneficiary, client.hasBeneficiary) &&
            Objects.equals(this.bankruptcyInfo, client.bankruptcyInfo) &&
            Objects.equals(this.bankruptcyStage, client.bankruptcyStage) &&
            Objects.equals(this.latinName, client.latinName) &&
            Objects.equals(this.codeWord, client.codeWord) &&
            Objects.equals(this.status, client.status) &&
            Objects.equals(this.lastUpdateDate, client.lastUpdateDate) &&
            Objects.equals(this.publicOfficialStatus, client.publicOfficialStatus) &&
            Objects.equals(this.foreignPublicOfficialType, client.foreignPublicOfficialType) &&
            Objects.equals(this.russianPublicOfficialType, client.russianPublicOfficialType) &&
            Objects.equals(this.relations, client.relations) &&
            Objects.equals(this.publicOfficialPosition, client.publicOfficialPosition) &&
            Objects.equals(this.publicOfficialNameAndPosition, client.publicOfficialNameAndPosition) &&
            Objects.equals(this.beneficialOwner, client.beneficialOwner) &&
            Objects.equals(this.businessRelations, client.businessRelations) &&
            Objects.equals(this.activitiesGoal, client.activitiesGoal) &&
            Objects.equals(this.businessRelationsGoal, client.businessRelationsGoal) &&
            Objects.equals(this.riskLevel, client.riskLevel) &&
            Objects.equals(this.riskLevelDesc, client.riskLevelDesc) &&
            Objects.equals(this.businessReputation, client.businessReputation) &&
            Objects.equals(this.financialStability, client.financialStability) &&
            Objects.equals(this.financesSource, client.financesSource) &&
            Objects.equals(this.personalDataConsent, client.personalDataConsent) &&
            Objects.equals(this.workflowAgreements, client.workflowAgreements) &&
            Objects.equals(this.authId, client.authId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, firstName, middleName, birthDate, gender, birthPlace, birthCountry, countryCode, birthRegion, birthRegionOkato, birthArea, resident, citizenship, citizenshipCountry, taxResidence, residentRF, inn, snils, licenseDriver, agreements, taxPayerNumber, migrationCard, foreignerDoc, documents, email, phones, addresses, equalsAddresses, signIPDL, signPDL, adoptionPDLWrittenDecision, hasFATCA, hasBeneficialOwner, hasBeneficiary, bankruptcyInfo, bankruptcyStage, latinName, codeWord, status, lastUpdateDate, publicOfficialStatus, foreignPublicOfficialType, russianPublicOfficialType, relations, publicOfficialPosition, publicOfficialNameAndPosition, beneficialOwner, businessRelations, activitiesGoal, businessRelationsGoal, riskLevel, riskLevelDesc, businessReputation, financialStability, financesSource, personalDataConsent, workflowAgreements, authId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Client {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    birthPlace: ").append(toIndentedString(birthPlace)).append("\n");
        sb.append("    birthCountry: ").append(toIndentedString(birthCountry)).append("\n");
        sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
        sb.append("    birthRegion: ").append(toIndentedString(birthRegion)).append("\n");
        sb.append("    birthRegionOkato: ").append(toIndentedString(birthRegionOkato)).append("\n");
        sb.append("    birthArea: ").append(toIndentedString(birthArea)).append("\n");
        sb.append("    resident: ").append(toIndentedString(resident)).append("\n");
        sb.append("    citizenship: ").append(toIndentedString(citizenship)).append("\n");
        sb.append("    citizenshipCountry: ").append(toIndentedString(citizenshipCountry)).append("\n");
        sb.append("    taxResidence: ").append(toIndentedString(taxResidence)).append("\n");
        sb.append("    residentRF: ").append(toIndentedString(residentRF)).append("\n");
        sb.append("    inn: ").append(toIndentedString(inn)).append("\n");
        sb.append("    snils: ").append(toIndentedString(snils)).append("\n");
        sb.append("    licenseDriver: ").append(toIndentedString(licenseDriver)).append("\n");
        sb.append("    agreements: ").append(toIndentedString(agreements)).append("\n");
        sb.append("    taxPayerNumber: ").append(toIndentedString(taxPayerNumber)).append("\n");
        sb.append("    migrationCard: ").append(toIndentedString(migrationCard)).append("\n");
        sb.append("    foreignerDoc: ").append(toIndentedString(foreignerDoc)).append("\n");
        sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    phones: ").append(toIndentedString(phones)).append("\n");
        sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
        sb.append("    equalsAddresses: ").append(toIndentedString(equalsAddresses)).append("\n");
        sb.append("    signIPDL: ").append(toIndentedString(signIPDL)).append("\n");
        sb.append("    signPDL: ").append(toIndentedString(signPDL)).append("\n");
        sb.append("    adoptionPDLWrittenDecision: ").append(toIndentedString(adoptionPDLWrittenDecision)).append("\n");
        sb.append("    hasFATCA: ").append(toIndentedString(hasFATCA)).append("\n");
        sb.append("    hasBeneficialOwner: ").append(toIndentedString(hasBeneficialOwner)).append("\n");
        sb.append("    hasBeneficiary: ").append(toIndentedString(hasBeneficiary)).append("\n");
        sb.append("    bankruptcyInfo: ").append(toIndentedString(bankruptcyInfo)).append("\n");
        sb.append("    bankruptcyStage: ").append(toIndentedString(bankruptcyStage)).append("\n");
        sb.append("    latinName: ").append(toIndentedString(latinName)).append("\n");
        sb.append("    codeWord: ").append(toIndentedString(codeWord)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    lastUpdateDate: ").append(toIndentedString(lastUpdateDate)).append("\n");
        sb.append("    publicOfficialStatus: ").append(toIndentedString(publicOfficialStatus)).append("\n");
        sb.append("    foreignPublicOfficialType: ").append(toIndentedString(foreignPublicOfficialType)).append("\n");
        sb.append("    russianPublicOfficialType: ").append(toIndentedString(russianPublicOfficialType)).append("\n");
        sb.append("    relations: ").append(toIndentedString(relations)).append("\n");
        sb.append("    publicOfficialPosition: ").append(toIndentedString(publicOfficialPosition)).append("\n");
        sb.append("    publicOfficialNameAndPosition: ").append(toIndentedString(publicOfficialNameAndPosition)).append("\n");
        sb.append("    beneficialOwner: ").append(toIndentedString(beneficialOwner)).append("\n");
        sb.append("    businessRelations: ").append(toIndentedString(businessRelations)).append("\n");
        sb.append("    activitiesGoal: ").append(toIndentedString(activitiesGoal)).append("\n");
        sb.append("    businessRelationsGoal: ").append(toIndentedString(businessRelationsGoal)).append("\n");
        sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
        sb.append("    riskLevelDesc: ").append(toIndentedString(riskLevelDesc)).append("\n");
        sb.append("    businessReputation: ").append(toIndentedString(businessReputation)).append("\n");
        sb.append("    financialStability: ").append(toIndentedString(financialStability)).append("\n");
        sb.append("    financesSource: ").append(toIndentedString(financesSource)).append("\n");
        sb.append("    personalDataConsent: ").append(toIndentedString(personalDataConsent)).append("\n");
        sb.append("    workflowAgreements: ").append(toIndentedString(workflowAgreements)).append("\n");
        sb.append("    authId: ").append(toIndentedString(authId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

