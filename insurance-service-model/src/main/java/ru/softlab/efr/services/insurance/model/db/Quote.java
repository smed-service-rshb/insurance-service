package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Сущность хранения котировки акции
 */
@Entity
@Table(name = "quote")
@IdClass(QuotePK.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quote {

    /**
     * Дата действия котировки
     */
    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;
    /**
     * Акция котировки
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_id", nullable = false)
    private ShareEntity share;
    /**
     * Значение котировки
     */
    @Column
    private BigDecimal value;

    public Quote() {
    }

    public Quote(LocalDate date, BigDecimal value, ShareEntity share) {
        this.date = date;
        this.value = value;
        this.share = share;
    }

    public ShareEntity getShare() {
        return share;
    }

    public void setShare(ShareEntity share) {
        this.share = share;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;
        Quote quote = (Quote) o;
        return Objects.equals(date, quote.date) &&
                Objects.equals(share, quote.share) &&
                Objects.equals(value, quote.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, share, value);
    }
}
