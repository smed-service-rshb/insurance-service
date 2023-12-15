package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * Сущность базового индекса стратегии
 */
@Entity
@Table(name = "base_index")
@Cacheable
@IdClass(BaseIndexPK.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaseIndex {

    /**
     * Дата действия индекса
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * Наименование группы стратегий
     */
    @Id
    @Column(name = "strategy_name", nullable = false)
    private String strategyName;

    /**
     * Стратегии к которым относится базовые индексы
     */
    @JoinTable(
            name = "base_index_2_strategy",
            joinColumns = {
                    @JoinColumn(name = "base_index_date", referencedColumnName = "date"),
                    @JoinColumn(name = "strategy_name", referencedColumnName = "strategy_name")
            },
            inverseJoinColumns = @JoinColumn(name = "strategy_id", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Strategy> strategies;
    /**
     * Значение индекса
     */
    @Column
    private BigDecimal value;

    public BaseIndex() {
    }

    public BaseIndex(LocalDate date, Set<Strategy> strategies, String strategyName, BigDecimal value) {
        this.date = date;
        this.strategyName = strategyName;
        this.strategies = strategies;
        this.value = value;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(Set<Strategy> strategies) {
        this.strategies = strategies;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
