package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.rest.RiskInfo;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Информация по обязательным рискам по договору страхования
 */
@Entity
@Table(name = "insurance_2_risk_info")
@Audited
public class InsuranceRiskEntity {

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
     * Андеррайтерский коэффициент
     */
    @Column(name = "underwriting_rate")
    private BigDecimal underwritingRate;

    /**
     * Страховая премия в валюте договора
     */
    @Column(name = "premium")
    private BigDecimal premium;

    /**
     * Другие параметры риска
     */
    @Column
    private String otherRiskParam;

    /**
     * Страховая премия в национальной валюте по риску
     */
    @Column(name = "rur_premium")
    private BigDecimal rurPremium;


    /**
     * Страховая сумма в национальной валюте по риску
     */
    @Column(name = "rur_amount")
    private BigDecimal rurAmount;


    public InsuranceRiskEntity() {
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

    public BigDecimal getUnderwritingRate() {
        return underwritingRate;
    }

    public void setUnderwritingRate(BigDecimal underwritingRate) {
        this.underwritingRate = underwritingRate;
    }

    public RiskInfo convertToModel() {
        return new RiskInfo(this.getId(), this.getRisk() !=null ? this.getRisk().getId() : null, this.getAmount(), this.getPremium(), this.getUnderwritingRate(), this.getRisk().getName(), this.getRisk().getBenefitsInsured(), this.getOtherRiskParam());
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public String getOtherRiskParam() {
        return otherRiskParam;
    }

    public void setOtherRiskParam(String otherRiskParam) {
        this.otherRiskParam = otherRiskParam;
    }

    public BigDecimal getRurPremium() {
        return rurPremium;
    }

    public void setRurPremium(BigDecimal rurPremium) {
        this.rurPremium = rurPremium;
    }

    public BigDecimal getRurAmount() {
        return rurAmount;
    }

    public void setRurAmount(BigDecimal rurAmount) {
        this.rurAmount = rurAmount;
    }
}
