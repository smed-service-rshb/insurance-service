package ru.softlab.efr.services.insurance.model.db;

import ru.softlab.efr.services.insurance.model.rest.AddRiskInfo;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Информация по дополнительным рискам
 */
@Entity
@Table(name = "insurance_2_add_risk_info")
public class InsuranceAddRiskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Риск
     */
    @ManyToOne
    @JoinColumn(name = "risk_id", nullable = false)
    private Risk risk;

    /**
     * Договор страхования
     */
    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

    /**
     * Страховая сумма в валюте договора
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * Страховая премия в валюте договора
     */
    @Column(name = "premium")
    private BigDecimal premium;

    public InsuranceAddRiskEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public AddRiskInfo convertToModel() {
        return new AddRiskInfo(this.getId(), this.getRisk() != null ? this.getRisk().getId() : null, this.getAmount(), this.getPremium(), this.getRisk().getName());
    }
}
