package ru.softlab.efr.services.insurance.model.db;

/**
 * @author olshansky
 * @since 02.12.2018
 */
public class ReportableRedemption {

    /**
     * Номер периода, год страхования
     */
    private String periodNumber;

    /**
     * Период страхования - Начало периода
     */
    private String startPeriod;

    /**
     * Период страхования - Окончание периода
     */
    private String endPeriod;

    /**
     * Размер выкупной суммы
     */
    private String redemptionAmount;

    /**
     * Валюта договора
     */
    private String redemptionCurrency;

    public ReportableRedemption() {}

    /**
     * @param periodNumber - Номер периода, год страхования
     * @param startPeriod - Период страхования - Начало периода
     * @param endPeriod - Период страхования - Окончание периода
     * @param redemptionAmount - Размер выкупной суммы
     * @param redemptionCurrency - Валюта договора
     */
    public ReportableRedemption(String periodNumber, String startPeriod, String endPeriod, String redemptionAmount, String redemptionCurrency) {
        this.periodNumber = periodNumber;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.redemptionAmount = redemptionAmount;
        this.redemptionCurrency = redemptionCurrency;
    }

    public String getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getRedemptionAmount() {
        return redemptionAmount;
    }

    public void setRedemptionAmount(String redemptionAmount) {
        this.redemptionAmount = redemptionAmount;
    }

    public String getRedemptionCurrency() {
        return redemptionCurrency;
    }

    public void setRedemptionCurrency(String redemptionCurrency) {
        this.redemptionCurrency = redemptionCurrency;
    }
}
