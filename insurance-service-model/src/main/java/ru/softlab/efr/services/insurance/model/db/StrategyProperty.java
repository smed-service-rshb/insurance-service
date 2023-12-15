package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Сущность для хранения набора данных стратегий страхования.
 *
 * @author olshansky
 * @since 24.12.2018
 */

@Entity
@Table(name = "strategy_property")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class StrategyProperty {

    /**
     * Идентификатор набора данных стратегии. Заполняется автоматически Системой.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Ссылка на стратегию-родительский элемент, к которому подчинена данная запись.
     */
    @ManyToOne
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    /**
     * Ссылка на стратегию, которая хранит набор базовых индексов
     * для набора, у которого стратегия-родитель содержит более одного набора данных. Данная ссылка
     * указывает на стратегию, из которой необходимо брать перечень индексов для расчета инвестиционного дохода.
     */
    @ManyToOne
    @JoinColumn(name = "base_index_source_strategy_id")
    private Strategy baseIndexSource;

    /**
     * Коэффициент участия. В данном поле может задаваться коэффициент участия или размер инвестиционного купона.
     * Значение задается в процентах. Обязательно для заполнения.
     */
    @Column
    private BigDecimal rate;

    /**
     * Тикер. Длиной 20 символов. Текстовое поле. Отображается только в печатных формах.
     * Необязательно для заполнения.
     */
    @Column
    private String ticker;

    /**
     * Дата экспирации.
     * Необязательно для заполнения.
     */
    @Column
    private Timestamp expirationDate;

    /**
     * Дата НЗБА (начальное значение базового актива).
     * Необязательно для заполнения.
     */
    @Column
    private Timestamp nzbaDate;

    public StrategyProperty(){}

    public StrategyProperty(Long version, Strategy strategy, BigDecimal rate, String ticker, Timestamp expirationDate, Timestamp nzbaDate) {
        this.version = version;
        this.strategy = strategy;
        this.rate = rate;
        this.ticker = ticker;
        this.expirationDate = expirationDate;
        this.nzbaDate = nzbaDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Timestamp getNzbaDate() {
        return nzbaDate;
    }

    public void setNzbaDate(Timestamp nzbaDate) {
        this.nzbaDate = nzbaDate;
    }

    public Strategy getBaseIndexSource() {
        return baseIndexSource;
    }

    public void setBaseIndexSource(Strategy baseIndexSource) {
        this.baseIndexSource = baseIndexSource;
    }
}
