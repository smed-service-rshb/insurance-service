package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.softlab.efr.services.insurance.model.enums.FieldTypeEnum;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника обязательных полей
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "required_field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RequiredField implements BaseEntity {

    /**
     * Идентификатор поля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Тип поля
     * Возможны следующие значения:
     * 1. FIELD_SET = Набор полей
     * 2. FIELD = Поле
     * Обязательно для заполнения
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FieldTypeEnum type;

    /**
     * Человекопонятное название поля в интерфейсе системы
     * Обязательно для заполнения
     */
    @Column
    private String name;

    /**
     * Название поля в системе
     * Обязательно для заполнения
     */
    @Column
    private String strId;


    /**
     * Родительский объект,
     * Заполняется, если type = FIELD_SET
     * Необязательно для заполнения
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "parent", nullable = true)
    private RequiredField parent;


    /**
     * Признак удалённого обязательного поля
     */
    @Column
    private Boolean deleted;

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

    public FieldTypeEnum getType() {
        return type;
    }

    public void setType(FieldTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public RequiredField getParent() {
        return parent;
    }

    public void setParent(RequiredField parent) {
        this.parent = parent;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
