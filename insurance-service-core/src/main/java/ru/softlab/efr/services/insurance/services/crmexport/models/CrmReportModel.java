package ru.softlab.efr.services.insurance.services.crmexport.models;


/**
 * Внутренняя модель элемента отчета для экспорта в CRM
 */
public class CrmReportModel {

    private String clientCode;
    private String clientGenderRequest;
    private String gotProgram;
    private String clientSecondname;
    private String clientName;
    private String clientThirdname;
    private String clientBirthDate;
    private String clientMobilePhone;
    private String clientEmail;
    private String resource;
    private String exported;
    private String availability;
    private String registrationAddress;
    private String registrationAddressRegion;
    private String passportSeries;
    private String passportCreationDate;
    private String passportRegistrationChair;
    private String passportDivisionCode;
    private String clientPayedSum;
    private String payedDate;
    private String programKindName;


    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getGotProgram() {
        return gotProgram;
    }

    public void setGotProgram(String gotProgram) {
        this.gotProgram = gotProgram;
    }

    public String getClientSecondname() {
        return clientSecondname;
    }

    public void setClientSecondname(String clientSecondname) {
        this.clientSecondname = clientSecondname;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientThirdname() {
        return clientThirdname;
    }

    public void setClientThirdname(String clientThirdname) {
        this.clientThirdname = clientThirdname;
    }

    public String getClientBirthDate() {
        return clientBirthDate;
    }

    public void setClientBirthDate(String clientBirthDate) {
        this.clientBirthDate = clientBirthDate;
    }

    public String getClientPayedSum() {
        return clientPayedSum;
    }

    public void setClientPayedSum(String clientPayedSum) {
        this.clientPayedSum = clientPayedSum;
    }

    public String getClientMobilePhone() {
        return clientMobilePhone;
    }

    public void setClientMobilePhone(String clientMobilePhone) {
        this.clientMobilePhone = clientMobilePhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getExported() {
        return exported;
    }

    public void setExported(String exported) {
        this.exported = exported;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportCreationDate() {
        return passportCreationDate;
    }

    public void setPassportCreationDate(String passportCreationDate) {
        this.passportCreationDate = passportCreationDate;
    }

    public String getPassportRegistrationChair() {
        return passportRegistrationChair;
    }

    public void setPassportRegistrationChair(String passportRegistrationChair) {
        this.passportRegistrationChair = passportRegistrationChair;
    }

    public String getPassportDivisionCode() {
        return passportDivisionCode;
    }

    public void setPassportDivisionCode(String passportDivisionCode) {
        this.passportDivisionCode = passportDivisionCode;
    }

    public String getClientGenderRequest() {
        return clientGenderRequest;
    }

    public void setClientGenderRequest(String clientGenderRequest) {
        this.clientGenderRequest = clientGenderRequest;
    }

    public String getRegistrationAddressRegion() {
        return registrationAddressRegion;
    }

    public void setRegistrationAddressRegion(String registrationAddressRegion) {
        this.registrationAddressRegion = registrationAddressRegion;
    }

    public String getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(String payedDate) {
        this.payedDate = payedDate;
    }

    public String getProgramKindName() {
        return programKindName;
    }

    public void setProgramKindName(String programKindName) {
        this.programKindName = programKindName;
    }
}
