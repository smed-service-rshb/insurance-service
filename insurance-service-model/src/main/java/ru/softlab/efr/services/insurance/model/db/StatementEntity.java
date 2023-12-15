package ru.softlab.efr.services.insurance.model.db;

import ru.softlab.efr.services.insurance.model.rest.StatementCompleteStatus;

import javax.persistence.*;

@Entity
@Table(name = "statements")
public class StatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_id")
    Insurance insurance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_status")
    InsuranceStatus insuranceStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    ClientEntity client;

    @Column(name = "complete_status")
    @Enumerated(EnumType.STRING)
    private StatementCompleteStatus statementCompleteStatus;

    @Column(name = "comment")
    private String comment;


    public Long getId() {
        return this.id;
    }
    public Insurance getInsurance() {
        return this.insurance;
    }
    public InsuranceStatus getInsuranceStatus() {
        return this.insuranceStatus;
    }
    public ClientEntity getClient() {
        return this.client;
    }
    public String getComment() {
        return this.comment;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
    public void setInsuranceStatus(InsuranceStatus insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }
    public void setClient(ClientEntity client) {
        this.client = client;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public StatementCompleteStatus getStatementCompleteStatus() {
        return statementCompleteStatus;
    }
    public void setStatementCompleteStatus(StatementCompleteStatus statementCompleteStatus) {
        this.statementCompleteStatus = statementCompleteStatus;
    }
}
