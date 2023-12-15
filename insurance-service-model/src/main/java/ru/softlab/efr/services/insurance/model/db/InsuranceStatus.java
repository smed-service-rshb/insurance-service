package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;

import javax.persistence.*;

/**
 * Статусы договоров страхования
 */
@Entity
@Table(name = "insurance_status")
@Audited
public class InsuranceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код
     */
    @Column(name = "code", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private InsuranceStatusCode code;

    /**
     * Наименование статуса
     */
    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InsuranceStatusCode getCode() {
        return code;
    }

    public void setCode(InsuranceStatusCode code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
