package ru.softlab.efr.services.insurance.model.db;


import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.*;

/**
 * Сущность счётчиков номеров договоров в разрезе типов программ страхования
 *
 * @author olshansky
 * @since 19.12.2018
 */
@Entity
@Table(name = "contract_number_sequence")
public class ContractNumberSequence {

    @Column
    private long lastId;
    @Id
    @Enumerated(EnumType.STRING)
    private ProgramKind programKind;

    public ContractNumberSequence() {
    }

    public ContractNumberSequence(long lastId, ProgramKind programKind) {
        this.lastId = lastId;
        this.programKind = programKind;
    }

    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public ProgramKind getProgramKind() {
        return programKind;
    }

    public void setProgramKind(ProgramKind programKind) {
        this.programKind = programKind;
    }
}
