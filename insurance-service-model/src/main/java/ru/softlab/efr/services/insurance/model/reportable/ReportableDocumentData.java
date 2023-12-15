package ru.softlab.efr.services.insurance.model.reportable;

public class ReportableDocumentData {

    public static final String NOT_SPECIFIED = "(не указано)";

    private String policyHolderIdDocSeries = NOT_SPECIFIED;
    private String policyHolderIdDocNumber = NOT_SPECIFIED;
    private String policyHolderIdDocIssuedDate = NOT_SPECIFIED;
    private String policyHolderIdDocIssuedBy = NOT_SPECIFIED;
    private String policyHolderIdDivisionCode = NOT_SPECIFIED;

    public ReportableDocumentData(){}

    public ReportableDocumentData(String policyHolderIdDocSeries,
                                  String policyHolderIdDocNumber,
                                  String policyHolderIdDocIssuedDate,
                                  String policyHolderIdDocIssuedBy,
                                  String policyHolderIdDivisionCode) {
        this.policyHolderIdDocSeries = policyHolderIdDocSeries;
        this.policyHolderIdDocNumber = policyHolderIdDocNumber;
        this.policyHolderIdDocIssuedDate = policyHolderIdDocIssuedDate;
        this.policyHolderIdDocIssuedBy = policyHolderIdDocIssuedBy;
        this.policyHolderIdDivisionCode = policyHolderIdDivisionCode;
    }

    public String getPolicyHolderIdDocSeries() {
        return policyHolderIdDocSeries;
    }

    public void setPolicyHolderIdDocSeries(String policyHolderIdDocSeries) {
        this.policyHolderIdDocSeries = policyHolderIdDocSeries;
    }

    public String getPolicyHolderIdDocNumber() {
        return policyHolderIdDocNumber;
    }

    public void setPolicyHolderIdDocNumber(String policyHolderIdDocNumber) {
        this.policyHolderIdDocNumber = policyHolderIdDocNumber;
    }

    public String getPolicyHolderIdDocIssuedDate() {
        return policyHolderIdDocIssuedDate;
    }

    public void setPolicyHolderIdDocIssuedDate(String policyHolderIdDocIssuedDate) {
        this.policyHolderIdDocIssuedDate = policyHolderIdDocIssuedDate;
    }

    public String getPolicyHolderIdDocIssuedBy() {
        return policyHolderIdDocIssuedBy;
    }

    public void setPolicyHolderIdDocIssuedBy(String policyHolderIdDocIssuedBy) {
        this.policyHolderIdDocIssuedBy = policyHolderIdDocIssuedBy;
    }

    public String getPolicyHolderIdDivisionCode() {
        return policyHolderIdDivisionCode;
    }

    public void setPolicyHolderIdDivisionCode(String policyHolderIdDivisionCode) {
        this.policyHolderIdDivisionCode = policyHolderIdDivisionCode;
    }
}