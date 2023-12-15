package ru.softlab.efr.services.insurance.model.db;

import ru.softlab.efr.services.insurance.model.rest.RequestStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "request")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_date")
    private Timestamp requestDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_topic")
    private TopicRequestEntity topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "request_text")
    private String requestText;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Transient
    private RequestsAttachmentEntity document;

    @Column
    private String comment;

    public Long getId() {
        return this.id;
    }

    public Timestamp getRequestDate() {
        return this.requestDate;
    }

    public TopicRequestEntity getTopic() {
        return this.topic;
    }

    public Program getProduct() {
        return this.product;
    }

    public Insurance getInsurance() {
        return this.insurance;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public ClientEntity getClient() {
        return this.client;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRequestText() {
        return this.requestText;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public void setTopic(TopicRequestEntity topic) {
        this.topic = topic;
    }

    public void setProduct(Program product) {
        this.product = product;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public RequestsAttachmentEntity getDocument() {
        return document;
    }

    public void setDocument(RequestsAttachmentEntity document) {
        this.document = document;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
