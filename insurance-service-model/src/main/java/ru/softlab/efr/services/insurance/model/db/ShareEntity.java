package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Сущность акции
 */
@Entity
@Table(name = "share")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShareEntity {
    /**
     * Идентификатор акции
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Наименование акции
     */
    @Column
    private String name;

    /**
     * Описание акции
     */
    @Column
    private String description;

    /**
     * Стратегии к которым относится акция
     */
    @JoinTable(
            name = "share_2_strategy",
            joinColumns = @JoinColumn(name = "share_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "strategy_id", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Strategy> strategies;

    /**
     * Список котировок акции
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "share", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Quote> quotes;

    public ShareEntity() {
    }

    public ShareEntity(String name) {
        this.name = name;
        this.quotes = new LinkedHashSet<>();
    }

    public ShareEntity(Long id, String name, String description) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.quotes = new LinkedHashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(Set<Quote> quotes) {
        this.quotes = quotes;
    }

    public Set<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(Set<Strategy> strategies) {
        this.strategies = strategies;
    }

    public void addStrategy(Set<Strategy> strategies) {
        if (this.strategies == null) {
            this.strategies = new HashSet<>();
        }
        this.strategies.addAll(strategies);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareEntity that = (ShareEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
