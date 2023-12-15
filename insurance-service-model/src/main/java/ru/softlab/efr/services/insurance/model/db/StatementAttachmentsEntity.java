package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;

@Entity
@Table(name = "statement_attachments")
public class StatementAttachmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statement_id")
    private StatementEntity satement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @Column(name = "is_deleted")
    private boolean isDeleted;


    public Long getId() {
        return this.id;
    }
    public StatementEntity getSatement() {
        return this.satement;
    }
    public Attachment getAttachment() {
        return this.attachment;
    }
    public boolean isDeleted() {
        return this.isDeleted;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setSatement(StatementEntity satement) {
        this.satement = satement;
    }
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
