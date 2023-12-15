package ru.softlab.efr.services.insurance.model.db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class BaseIndexPK implements Serializable {

    private LocalDate date;

    private String strategyName;

    public BaseIndexPK() {
    }

    public BaseIndexPK(LocalDate date, String strategyName) {
        this.date = date;
        this.strategyName = strategyName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseIndexPK)) return false;
        BaseIndexPK that = (BaseIndexPK) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(strategyName, that.strategyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, strategyName);
    }
}
