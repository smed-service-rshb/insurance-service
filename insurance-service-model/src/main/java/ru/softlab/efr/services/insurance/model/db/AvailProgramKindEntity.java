package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Сущность для хранения данных справочника доступности видов программ страхования.
 *
 * @author olshansky
 * @since 27.03.2019
 */

@Entity
@Table(name = "programKind_avail")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AvailProgramKindEntity {

    /**
     * Идентификатор формулы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код вида программы страхования
     */
    @Column
    private String programKind;

    /**
     * Признак доступности вида программы
     */
    @Column
    private Boolean isActive;

    public AvailProgramKindEntity() {
    }

    public AvailProgramKindEntity(Long id, String programKind, Boolean isActive) {
        this.id = id;
        this.programKind = programKind;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramKind() {
        return programKind;
    }

    public void setProgramKind(String programKind) {
        this.programKind = programKind;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
