package ru.softlab.efr.services.insurance.model.db;

import java.util.Calendar;

/**
 * согласие на хранение, обработку и передачу персональных данных
 * @author gladishev
 * @since 20.04.2017
 */
public class DataProcessingConsent {

    private Long id; // id записи
    private Calendar startDate; //дата начала действия согласия
    private Calendar endDate; //дата окончания действия согласия

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}
