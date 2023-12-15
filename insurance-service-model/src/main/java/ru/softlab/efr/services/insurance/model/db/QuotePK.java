package ru.softlab.efr.services.insurance.model.db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class QuotePK implements Serializable {

    private LocalDate date;

    private Long share;

    public QuotePK() {
    }

    public QuotePK(LocalDate date, Long share) {
        this.date = date;
        this.share = share;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuotePK)) return false;
        QuotePK quotePK = (QuotePK) o;
        return Objects.equals(date, quotePK.date) &&
                Objects.equals(share, quotePK.share);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, share);
    }
}
