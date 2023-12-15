package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для хранения данных справочника стратегий страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "strategy")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Strategy implements BaseEntity {

    /**
     * Идентификатор стратегии. Заполняется автоматически Системой
     * Обязательно для заполнения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Наименование стратегии. Текстовое поле длиной 50 символов
     * Обязательно для заполнения
     */
    @Column
    private String name;

    /**
     * Описание. Текстовое поле длиной 500 символов
     * Необязательно для заполнения
     */
    @Column
    private String description;

    /**
     * Кодировка полиса (стратегия). Числовое поле формата 99, где 9 – цифра от нуля до 9. Данное поле участвует в формировании номера договора
     * Обязательно для заполнения
     */
    @Column
    private Integer policyCode;

    /**
     * Наборы параметров стратегий
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "strategy", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<StrategyProperty> strategyProperties = new ArrayList<>();

    /**
     * Дата начала действия стратегии.
     * Обязательно для заполнения
     */
    @Column
    private Timestamp startDate;

    /**
     * Дата окончания действия стратегии.
     * Обязательно для заполнения
     */
    @Column
    private Timestamp endDate;

    /**
     * Признак удалённой стратегии
     */
    @Column
    private Boolean deleted;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private StrategyType strategyType;

    public Strategy(){}

    public Strategy(Long version, String name, String description, Integer policyCode, List<StrategyProperty> strategyProperties, Timestamp startDate, Timestamp endDate, Boolean deleted) {
        this.version = version;
        this.name = name;
        this.description = description;
        this.policyCode = policyCode;
        this.strategyProperties = strategyProperties;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleted = deleted;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(Integer policyCode) {
        this.policyCode = policyCode;
    }

    public List<StrategyProperty> getStrategyProperties() {
        return strategyProperties;
    }

    public void setStrategyProperties(List<StrategyProperty> strategyProperties) {
        this.strategyProperties = strategyProperties;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }
}
