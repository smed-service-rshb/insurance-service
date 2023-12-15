package ru.softlab.efr.services.insurance.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Attachment;
import ru.softlab.efr.services.insurance.model.db.RequestsAttachmentEntity;
import ru.softlab.efr.services.insurance.model.rest.AttachedRequestAttachData;

import java.util.Objects;

@Service
public class RequestAttachConverter {
    public AttachedRequestAttachData toAttachedRequestAttachData(RequestsAttachmentEntity requestsAttachmentEntity) {
        AttachedRequestAttachData requestAttachData = new AttachedRequestAttachData();
        requestAttachData.setFileName(requestsAttachmentEntity.getAttachment().getFileName());
        requestAttachData.setRequestAttachId(requestsAttachmentEntity.getId());
        return requestAttachData;
    }

    public ru.softlab.efr.services.insurance.model.rest.StatementAttachment toStatusAttachment(Attachment attachment) {
        ru.softlab.efr.services.insurance.model.rest.StatementAttachment restAttachment = new ru.softlab.efr.services.insurance.model.rest.StatementAttachment();
        restAttachment.setId(attachment.getId());
        restAttachment.setName(attachment.getFileName());
        restAttachment.setAttachedDate(attachment.getCreateDate().toLocalDateTime().toLocalDate());
        restAttachment.setComment(attachment.getComment());
        if (Objects.nonNull(attachment.getDocumentType())) {
            restAttachment.setAttachmentTypeId(attachment.getDocumentType().getId());
        }
        return restAttachment;
    }
}
