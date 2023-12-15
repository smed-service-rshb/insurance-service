package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.EmploeeDataWithOrgUnits;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;
import ru.softlab.efr.services.insurance.repositories.RequestsAttachmentRepository;
import ru.softlab.efr.services.insurance.repositories.RequestsRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
public class RequestAttachService {

    private static final Logger LOGGER = Logger.getLogger(RequestAttachService.class);

    private RequestsAttachmentRepository requestsAttachmentRepository;
    private RequestsRepository requestsRepository;
    private PrincipalDataSource principalDataSource;
    private AttachmentService attachmentService;
    private EmployeesClient employeesClient;

    @Autowired
    public RequestAttachService(RequestsAttachmentRepository requestsAttachmentRepository,
                                RequestsRepository requestsRepository,
                                PrincipalDataSource principalDataSource,
                                AttachmentService attachmentService, EmployeesClient employeesClient) {
        this.requestsAttachmentRepository = requestsAttachmentRepository;
        this.requestsRepository = requestsRepository;
        this.principalDataSource = principalDataSource;
        this.attachmentService = attachmentService;
        this.employeesClient = employeesClient;
    }

    @Transactional
    public Long addAttachmentToRequest(RequestEntity request, String fileName, byte[] content) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        AttachmentKindEnum attachmentKind = principalData.getRights().contains(Right.CLIENT_REQUEST_PROCESSING) ?
                AttachmentKindEnum.REQUEST_DOCUMENT : AttachmentKindEnum.REQUEST_ATTACHMENT;
        Attachment attachment = new Attachment(
                attachmentKind,
                null,
                new Timestamp(new Date().getTime()),
                null,
                UUID.randomUUID().toString(),
                fileName,
                principalData.getId(),
                null,
                false,
                null,
                false);
        attachmentService.save(attachment, content);

        RequestsAttachmentEntity entity = new RequestsAttachmentEntity();
        entity.setAttachment(attachment);
        entity.setRequest(request);
        entity.setDeleted(false);
        requestsAttachmentRepository.save(entity);
        return entity.getId();
    }

    @Transactional(readOnly = true)
    public List<RequestsAttachmentEntity> getRequestAttachments(long requestId) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        RequestEntity request = requestsRepository.getById(requestId);
        if (principalData.getRights().contains(Right.VIEW_CONTRACT_REPORT_ALL)
                || isEqualsClient(request.getClient(), principalData)) {
            return requestsAttachmentRepository.getByRequestAndIsDeleted(requestId, AttachmentKindEnum.REQUEST_ATTACHMENT);
        }
        return null;
    }

    @Transactional
    public String deleteAttachmentFromRequest(long requestAttachId) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        RequestsAttachmentEntity requestAttachment = requestsAttachmentRepository.getById(requestAttachId);
        if (isEqualsClient(requestAttachment.getRequest().getClient(), principalData) ||
                (principalData.getRights().contains(Right.CLIENT_REQUEST_PROCESSING) &&
                        AttachmentKindEnum.REQUEST_DOCUMENT.equals(requestAttachment.getAttachment().getKind()))
        ) {
            requestAttachment.setDeleted(true);
            requestsAttachmentRepository.save(requestAttachment);
            return requestAttachment.getAttachment().getId();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Attachment getAttachmentFromRequest(long requestAttachId) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        RequestsAttachmentEntity reportsAttachment = requestsAttachmentRepository.getById(requestAttachId);
        if (principalData.getRights().contains(Right.VIEW_CONTRACT_REPORT_ALL)
                || isEqualsClient(reportsAttachment.getRequest().getClient(), principalData)) {
            return reportsAttachment.getAttachment();
        }
        return null;
    }


    private boolean isEqualsClient(ClientEntity holder, PrincipalData principalData) {
        String phoneNumber = holder.getPhones().stream().filter(PhoneForClaimEntity::isMain).map(PhoneForClaimEntity::getNumber).findFirst().orElse(null);
        EmploeeDataWithOrgUnits client = null;
        try {
            client = employeesClient.getEmployeeByIdWithOutPermission(principalData.getId(), 10L);
        } catch (RestClientException e) {
            LOGGER.warn("При получении данных о клиенте произошла ошибка:", e);
            return false;
        }
        return Objects.equals(holder.getSurName().toUpperCase(), principalData.getSecondName().toUpperCase())
                && Objects.equals(holder.getFirstName().toUpperCase(), principalData.getFirstName().toUpperCase())
                && Objects.equals(holder.getBirthDate(), client.getBirthDate()) &&
                Objects.equals(phoneNumber, principalData.getMobilePhone());
    }


    @Transactional(readOnly = true)
    public RequestsAttachmentEntity getRequestDocument(Long requestId) {
        return requestsAttachmentRepository.getByRequestDocument(requestId, AttachmentKindEnum.REQUEST_DOCUMENT);
    }
}
