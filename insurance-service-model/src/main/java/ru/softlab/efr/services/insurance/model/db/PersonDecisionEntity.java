package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.softlab.efr.services.insurance.model.enums.CheckStateEnum;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;

import javax.persistence.*;

/**
 * Решения сотрудника
 * @author basharin
 * @since 05.02.2018
 */

@Entity
@Table(name = "person_decisions")
public class PersonDecisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="check_unit_type")
    @Enumerated(EnumType.STRING)
    private CheckUnitTypeEnum checkUnitType;

    @Column(name="decision")
    @Enumerated(EnumType.STRING)
    private CheckStateEnum decision;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    protected ClientEntity client;

    @Column(name="comment")
    private String comment;

    public PersonDecisionEntity() {}

    public PersonDecisionEntity(CheckUnitTypeEnum checkUnitType, CheckStateEnum decision, ClientEntity client, String comment) {
        this.checkUnitType = checkUnitType;
        this.decision = decision;
        this.client = client;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CheckUnitTypeEnum getCheckUnitType() {
        return checkUnitType;
    }

    public void setCheckUnitType(CheckUnitTypeEnum checkUnitType) {
        this.checkUnitType = checkUnitType;
    }

    public CheckStateEnum getDecision() {
        return decision;
    }

    public void setDecision(CheckStateEnum decision) {
        this.decision = decision;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
