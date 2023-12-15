package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.enums.TaxResidenceEnum;
import ru.softlab.efr.services.insurance.model.rest.InsuranceRecipient;
import ru.softlab.efr.services.insurance.model.rest.TaxResidenceType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Информация по выгодоприобретателям по договору страхования
 */
@Entity
@Table(name = "insurance_2_recipient")
@Audited
public class InsuranceRecipientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Договор страхования
     */
    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    /**
     * Фамилия выгодоприобретателя
     */
    @Column(name = "surname", nullable = false, length = 255)
    private String surName;

    /**
     * Имя выгодоприобретателя
     */
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    /**
     * Отчество выгодоприобретателя
     */
    @Column(name = "middle_name", length = 255)
    private String middleName;

    /**
     * Дата рождения выгодоприобретателя
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Место рождения выгодоприобретателя
     */
    @Column(name = "birth_place", length = 255)
    private String birthPlace;

    /**
     * Страна рождения выгодоприобретателя
     */
    @Column(name = "birth_country", length = 100)
    private String birthCountry;

    /**
     * Налоговое резиденство выгодоприобретателя
     */
    @Column(name = "tax_residence")
    @Enumerated(value = EnumType.STRING)
    private TaxResidenceEnum taxResidence;

    /**
     * Отношение к застрахованному лицу
     */
    @Column(name = "relationship", length = 50)
    private String relationship;

    /**
     * Доля в %
     */
    @Column(name = "share")
    private BigDecimal share;

    public InsuranceRecipientEntity(Insurance insurance, String surName, String firstName, String middleName,
                                    LocalDate birthDate, String birthPlace, TaxResidenceEnum taxResidence,
                                    String relationship, BigDecimal share, String birthCountry) {
        this.insurance = insurance;
        update(surName, firstName, middleName, birthDate, birthPlace, taxResidence, relationship, share, birthCountry);
    }

    public InsuranceRecipientEntity() {
    }

    public void update(String surName, String firstName, String middleName, LocalDate birthDate, String birthPlace,
                       TaxResidenceEnum taxResidence, String relationship, BigDecimal share, String birthCountry) {
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.taxResidence = taxResidence;
        this.relationship = relationship;
        this.share = share;
        this.birthCountry = birthCountry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
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

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public TaxResidenceEnum getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(TaxResidenceEnum taxResidence) {
        this.taxResidence = taxResidence;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public InsuranceRecipient convertToModel() {
        return new InsuranceRecipient(this.getId(), this.getSurName(), this.getFirstName(),
                this.getMiddleName(), this.getBirthDate(), this.getBirthPlace(),
                this.getTaxResidence() != null ? TaxResidenceType.valueOf(this.getTaxResidence().name()) : null, this.getRelationship(), this.getShare(), this.getBirthCountry());
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }
}
