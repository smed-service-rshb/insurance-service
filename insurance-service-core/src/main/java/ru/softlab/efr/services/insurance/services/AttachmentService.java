package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Attachment;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.repositories.AttachmentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с сегментами программ страхования
 *
 * @author olshansky
 * @since 25.10.2018
 */
@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository repository;

    public boolean isExists(String id) {
        return repository.countById(id) > 0;
    }

    public Attachment findById(String id) {
        Attachment attachment = repository.findOneById(id);
        if (attachment == null) {
            throw new EntityNotFoundException();
        }
        return attachment;
    }

    public Attachment findByKindAndContract(String kind, Long contractId) {
        Attachment attachment = repository.findByKindAndContracts(kind, contractId, false);
        if (attachment == null) {
            throw new EntityNotFoundException();
        }
        return attachment;
    }

    public Attachment findByKindAndContract(String kind, String contractUuid) {
        Attachment attachment = repository.findByKindAndContracts(kind, contractUuid, false);
        if (attachment == null) {
            throw new EntityNotFoundException();
        }
        return attachment;
    }

    boolean isNotExistAttachByDocTypeAndContract(Long documentType, Long contract) {
        return repository.countByDocTypeAndContractId(documentType, contract) == 0;
    }

    boolean isNotExistAttachByDocTypeAndContract(Long documentType, String uuid) {
        return repository.countByDocTypeAndContractUuid(documentType, uuid) == 0;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteAttachmentForeignResident(Long contractId, String kindName) {
        List<Attachment> attachmentList = repository.findAllByDeletedAndContractIdAndKind(false, contractId, kindName);
        for (Attachment attachment : attachmentList) {
            attachment.setDeleted(true);
        }
        repository.save(attachmentList);
        repository.flush();
    }

    public byte[] findOneFormCertification(String kind, Long contractId) {
        Attachment attachment = repository.findByKindAndContracts(kind, contractId, false);
        if (attachment == null) {
            throw new EntityNotFoundException();
        }
        return repository.getOneFormCertification(kind, contractId, false);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void logicalDelete(String id) {
        Attachment existingEntity = findById(id);
        existingEntity.setDeleted(true);
        repository.saveAndFlush(existingEntity);
    }

    public Page<Attachment> findAllExceptDeleted(Pageable pageable) {
        return repository.findAllByDeleted(pageable, false);
    }

    public List<Attachment> findAllExceptDeletedByContract(Long contractId) {
        return repository.findAllByDeletedAndContractIdAndKind(false, contractId, "INSURANCE_CONTRACT");
    }

    public List<Attachment> findAllExceptDeletedByContract(String contractUuid) {
        return repository.findAllByDeletedAndContractUuidAndKind(false, contractUuid, "INSURANCE_CONTRACT");
    }

    public Integer getCountAttachmentsByContractId(Long contractId) {
        return repository.countAllByDeletedAndContractId(false, contractId);
    }

    public Integer getCountAttachmentsByContractUuid(String contractUuid) {
        return repository.countAllByDeletedAndContractUuid(false, contractUuid);
    }

    /**
     * Получить контент вложения по идентификатору
     * Если вложение не найдено, то выбрасывапется исключение EntityNotFoundException
     *
     * @param id идентификатор вложения
     * @return контент вложения
     */
    @Transactional(readOnly = true)
    public byte[] getContent(String id) {
        Attachment attachment = findById(id);
        if (attachment == null) {
            throw new EntityNotFoundException();
        }
        return repository.getContent(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public String save(Attachment attachment, byte[] content) {
        attachment.setId(UUID.randomUUID().toString());
        Attachment existingAttachment = repository.save(attachment);
        if (existingAttachment == null) {
            throw new EntityNotFoundException();
        }
        if (content != null) {
            repository.setContent(attachment.getId(), content);
        }
        return existingAttachment.getId();
    }

    @Transactional
    public void updateAttachment(Insurance insurance) {
        repository.findAllByDeletedAndContractUuidAndContractIsNull(false, insurance.getUuid())
                .forEach(attachment -> {
                    attachment.setContract(insurance);
                    repository.save(attachment);
                });
    }
}
