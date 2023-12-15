package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Сущность для хранения данных выкупных коэффициентов.
 *
 * @author Kalantaev
 * @since 29.11.2018
 */
@Entity
@Table(name = "redemption_coefficient")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RedemptionCoefficientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Период расчета
     */
    @Column(name = "period", nullable = false)
    private Integer period;

    /**
     * Выкупной коэффициент
     */
    @Column(name = "coefficient", nullable = false)
    private BigDecimal coefficient;

    /**
     *
     */
    @ManyToOne
    @JoinColumn(name = "redemption_id", nullable = false)
    private RedemptionEntity redemption;

    public RedemptionCoefficientEntity() {
    }

    public RedemptionCoefficientEntity(Long id, Integer period, BigDecimal coefficient, RedemptionEntity redemption) {
        this.id = id;
        this.period = period;
        this.coefficient = coefficient;
        this.redemption = redemption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public RedemptionEntity getRedemption() {
        return redemption;
    }

    public void setRedemption(RedemptionEntity redemption) {
        this.redemption = redemption;
    }
}
