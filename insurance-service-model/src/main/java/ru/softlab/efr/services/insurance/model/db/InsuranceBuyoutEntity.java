package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Информация по выкупным суммам по договору страхования
 */
@Entity
@Table(name = "insurance_2_buyout")
@Audited
public class InsuranceBuyoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Договор страхования
     */
    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    /**
     * Коэффициен выкупной суммы
     */
    @Column(name = "rate")
    private BigDecimal rate;

    /**
     * Срок
     */
    @Column(name = "period")
    private Integer period;

}
