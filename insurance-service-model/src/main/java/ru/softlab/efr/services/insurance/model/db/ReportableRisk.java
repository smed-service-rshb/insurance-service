package ru.softlab.efr.services.insurance.model.db;

/**
 * @author olshansky
 * @since 29.10.2018
 */
public class ReportableRisk {

    private String name;
    private String fullName;
    private String amount;
    private String amountString;
    private String description;
    private boolean isBenefitsInsured;

    public ReportableRisk(String name, String fullName, String amount, String amountString, boolean isBenefitsInsured) {
        this.name = name;
        this.fullName = fullName;
        this.amount = amount;
        this.amountString = amountString;
        this.isBenefitsInsured = isBenefitsInsured;
    }

    public ReportableRisk(String name, String fullName, String amount, String amountString, String description) {
        this.name = name;
        this.fullName = fullName;
        this.amount = amount;
        this.amountString = amountString;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public boolean isBenefitsInsured() {
        return isBenefitsInsured;
    }

    public void setBenefitsInsured(boolean benefitsInsured) {
        isBenefitsInsured = benefitsInsured;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
