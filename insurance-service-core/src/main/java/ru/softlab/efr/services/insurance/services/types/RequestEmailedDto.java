package ru.softlab.efr.services.insurance.services.types;

import java.sql.Timestamp;

public class RequestEmailedDto {

    private String requestTopic;
    private Timestamp requestDate;
    private String insuranceNumber;
    private String phone;
    private String email;
    private String text;

    public String getRequestTopic() {
        return this.requestTopic;
    }
    public Timestamp getRequestDate() {
        return this.requestDate;
    }
    public String getInsuranceNumber() {
        return this.insuranceNumber;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getEmail() {
        return this.email;
    }
    public String getText() {
        return this.text;
    }
    public void setRequestTopic(String requestTopic) {
        this.requestTopic = requestTopic;
    }
    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setText(String text) {
        this.text = text;
    }
}
