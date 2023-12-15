package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;

@Entity
@Table(name = "request_attachments")
public class RequestsAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private RequestEntity request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Long getId() {
        return this.id;
    }

    public RequestEntity getRequest() {
        return this.request;
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

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
