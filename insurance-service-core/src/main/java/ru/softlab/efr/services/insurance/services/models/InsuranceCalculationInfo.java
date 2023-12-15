package ru.softlab.efr.services.insurance.services.models;

import java.math.BigDecimal;

public class InsuranceCalculationInfo {

    private BigDecimal insuranceAmount;
    private BigDecimal premiumPerPeriod;
    private BigDecimal totalPremium;

    public InsuranceCalculationInfo(BigDecimal insuranceAmount, BigDecimal premiumPerPeriod, BigDecimal totalPremium) {
        this.insuranceAmount = insuranceAmount;
        this.premiumPerPeriod = premiumPerPeriod;
        this.totalPremium = totalPremium;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public BigDecimal getPremiumPerPeriod() {
        return premiumPerPeriod;
    }

    public BigDecimal getTotalPremium() {
        return totalPremium;
    }
}
