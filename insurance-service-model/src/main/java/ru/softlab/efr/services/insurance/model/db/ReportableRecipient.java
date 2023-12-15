package ru.softlab.efr.services.insurance.model.db;

/**
 * @author olshansky
 * @since 02.12.2018
 */
public class ReportableRecipient {

    private String index;
    private String FIO;
    private String birthDateAndBirthPlaceAndBirthCountry;
    private String address;
    private String taxResidence;
    private String riskName;
    private String share;

    public ReportableRecipient() {}

    public ReportableRecipient(String FIO, String birthDateAndBirthPlaceAndBirthCountry, String address, String taxResidence, String riskName, String share) {
        this.FIO = FIO;
        this.birthDateAndBirthPlaceAndBirthCountry = birthDateAndBirthPlaceAndBirthCountry;
        this.address = address;
        this.taxResidence = taxResidence;
        this.riskName = riskName;
        this.share = share;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getBirthDateAndBirthPlaceAndBirthCountry() {
        return birthDateAndBirthPlaceAndBirthCountry;
    }

    public void setBirthDateAndBirthPlaceAndBirthCountry(String birthDateAndBirthPlaceAndBirthCountry) {
        this.birthDateAndBirthPlaceAndBirthCountry = birthDateAndBirthPlaceAndBirthCountry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(String taxResidence) {
        this.taxResidence = taxResidence;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
