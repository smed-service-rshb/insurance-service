package ru.softlab.efr.services.insurance.model.db;

/**
 * Вспомогательный интерфейс, обеспечивающий необходимость реализации метода getId()
 * для работы ru.softlab.efr.services.insurance.services.BaseService
 *
 * @author olshansky
 * @since 14.10.2018
 */

public interface BaseEntity {

    Long getId();
    void setDeleted(Boolean deleted);
    Boolean getDeleted();

}
