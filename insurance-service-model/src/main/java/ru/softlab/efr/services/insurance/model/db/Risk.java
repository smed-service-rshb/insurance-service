package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import ru.softlab.efr.services.insurance.model.enums.PaymentMethod;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность для хранения данных справочника рисков.
 *
 * @author Kalantaev
 * @since 02.10.2018
 */
@Entity
@Table(name = "risk")
@Cacheable
@Audited
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "program_kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramKind programKind;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "benefits_insured")
    private Boolean benefitsInsured;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    @NotAudited
    private PaymentMethod paymentMethod;

    @Column(name = "deleted")
    private Boolean deleted;

    /**
     * Получить идентификатор риска.
     *
     * @return идентификатор риска
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить версию записи.
     *
     * @return версия записи
     */
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Получить вид программы риска.
     *
     * @return вид программы
     */
    public ProgramKind getProgramKind() {
        return programKind;
    }

    public void setProgramKind(ProgramKind programKind) {
        this.programKind = programKind;
    }

    /**
     * Получить краткое наименование риска.
     *
     * @return краткое наименование риска
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить полное наименование риска.
     *
     * @return польное наименование риска
     */
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Получить дату начала действия риска.
     *
     * @return дата начала действия риска
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Получить дату окончания действия риска.
     *
     * @return дата окончания действия риска
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getBenefitsInsured() {
        return benefitsInsured;
    }

    public void setBenefitsInsured(Boolean benefitsInsured) {
        this.benefitsInsured = benefitsInsured;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Получить признак удаления.
     *
     * @return признак удаления
     */
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
