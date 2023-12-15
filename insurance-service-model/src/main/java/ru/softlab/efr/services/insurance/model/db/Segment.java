package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника сегментов программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */

@Entity
@Table(name = "segment")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Segment implements BaseEntity {

    /**
     * Идентификатор сегмента.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Код сегмента в системе. Текстовое поле длиной 50 символов.
     * Обязательно для заполнения
     */
    @Column
    private String code;

    /**
     * Наименование сегмента. Текстовое поле длиной 254 символа.
     * Обязательно для заполнения
     */
    @Column
    private String name;

    /**
     * Признак действующего сегмента. По умолчанию признак установлен.
     * Обязательно для заполнения
     */
    @Column
    private Boolean isActive;

    /**
     * Признак удалённого сегмента программы страхования
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }
}
