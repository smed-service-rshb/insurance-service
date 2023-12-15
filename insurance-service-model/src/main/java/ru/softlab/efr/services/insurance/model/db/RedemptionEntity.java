package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для хранения данных справочника выкупных коэффициентов.
 *
 * @author Kalantaev
 * @since 29.11.2018
 */
@Entity
@Table(name = "redemption")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RedemptionEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Программа страхования
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    /**
     * Периодичность уплаты взносов, выбор одного значения:
     * ежемесячно, ежеквартально, «раз в полгода», ежегодно
     */
    @Column(name = "periodicity")
    @Enumerated(value = EnumType.STRING)
    private PeriodicityEnum periodicity =  PeriodicityEnum.YEARLY;

    /**
     * Периодичность расчета выкупных сумм, выбор одного значения:
     * единоразово, ежемесячно, ежеквартально, «раз в полгода», ежегодно
     */
    @Column(name = "payment_period")
    @Enumerated(value = EnumType.STRING)
    private PeriodicityEnum paymentPeriod;

    /**
     * Срок страхования
     */
    @Column(name = "duration", nullable = false)
    private Integer duration;

    /**
     * ID валюты договора
     */
    @Column(name = "currency_id")
    private Long currency;

    /**
     * Признак удаления записи
     */
    @Column(name = "deleted")
    private Boolean deleted;


    /**
     * Информация по выкупным коэффициентам
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "redemption", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RedemptionCoefficientEntity> coefficientList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public PeriodicityEnum getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PeriodicityEnum periodicity) {
        this.periodicity = periodicity;
    }

    public PeriodicityEnum getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PeriodicityEnum paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public List<RedemptionCoefficientEntity> getCoefficientList() {
        return coefficientList;
    }

    public void setCoefficientList(List<RedemptionCoefficientEntity> coefficientList) {
        this.coefficientList = coefficientList;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
