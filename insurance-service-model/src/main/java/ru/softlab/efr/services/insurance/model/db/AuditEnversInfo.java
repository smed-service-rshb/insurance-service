package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.softlab.efr.services.insurance.listener.UserRevisionListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@RevisionEntity(UserRevisionListener.class)
@Table(name = "audit_envers_info")
public class AuditEnversInfo implements Serializable {

    private static final long serialVersionUID = -7604731515258123883L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

    @Column(name = "user_id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Transient
    public Date getRevisionDate() {
        return new Date( timestamp );
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEnversInfo that = (AuditEnversInfo) o;
        return id == that.id &&
                timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp);
    }

    @Override
    public String toString() {
        return "AuditEnversInfo(id = " + id
                + ", revisionDate = " + DateFormat.getDateTimeInstance().format( getRevisionDate() ) + ")";
    }

}
