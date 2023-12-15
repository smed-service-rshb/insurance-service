package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.insurance.model.db.Attachment;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.StatementAttachmentsEntity;
import ru.softlab.efr.services.insurance.model.db.StatementEntity;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.StatementCompleteStatus;
import ru.softlab.efr.services.insurance.repositories.AttachmentRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.StatementAttachmentsRepository;
import ru.softlab.efr.services.insurance.repositories.StatementRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StatementService {

    private static final Logger LOGGER = Logger.getLogger(StatementService.class);

    private StatementRepository statementRepository;
    private StatementAttachmentsRepository statementAttachmentsRepository;
    private InsuranceRepository insuranceRepository;
    private StatusService statusService;
    private AttachmentService attachmentService;
    private AttachmentRepository attachmentRepository;
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    public StatementService(StatementRepository statementRepository,
                            StatementAttachmentsRepository statementAttachmentsRepository,
                            InsuranceRepository insuranceRepository,
                            StatusService statusService,
                            AttachmentService attachmentService,
                            AttachmentRepository attachmentRepository,
                            RequiredDocumentService requiredDocumentService) {
        this.statementRepository = statementRepository;
        this.statementAttachmentsRepository = statementAttachmentsRepository;
        this.insuranceRepository = insuranceRepository;
        this.statusService = statusService;
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
        this.requiredDocumentService = requiredDocumentService;
    }

    @Transactional(readOnly = true)
    public StatementEntity getStatementById(Long statementId) {
        return statementRepository.getById(statementId);
    }

    @Transactional(readOnly = true)
    public List<StatementEntity> getStatementsByInsuranceId(Long insuranceId) {
        Insurance insurance = insuranceRepository.getById(insuranceId);
        return statementRepository.getByInsuranceOrderById(insurance);
    }

    @Transactional(readOnly = true)
    public List<Attachment> getAttachmentsListByStatementId(Long statementId) {
        StatementEntity statement = getStatementById(statementId);
        return statementAttachmentsRepository.getBySatementAndIsDeletedOrderById(statement, false)
                .stream()
                .map(StatementAttachmentsEntity::getAttachment)
                .collect(Collectors.toList());
    }

    @Transactional
    public StatementEntity addStatementByInsuranceIdAndStatusCode(Long insuranceId, InsuranceStatusCode statusCode) {
        Insurance insurance = insuranceRepository.getById(insuranceId);
        if (InsuranceStatusCode.isStatementStatus(statusCode)) {
            StatementEntity statement = new StatementEntity();
            statement.setClient(insurance.getClient());
            statement.setInsurance(insurance);
            statement.setInsuranceStatus(statusService.getByCode(statusCode));
            statement.setStatementCompleteStatus(StatementCompleteStatus.NEUTRAL);
            statement = statementRepository.save(statement);
            return statement;
        } else {
            LOGGER.warn("Статус не поддерживается");
            return null;
        }
    }

    @Transactional
    public boolean attachToStatement(Long statementId, MultipartFile content, Long documentType) throws IOException {
        StatementEntity statement = getStatementById(statementId);
        List<Attachment> attachmentList = getAttachmentsListByStatementId(statementId);
        List<String> attachmentNamesList = attachmentList
                .stream()
                .map(Attachment::getFileName)
                .collect(Collectors.toList());
        String originalFilename = new String(content.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        if (!attachmentNamesList.contains(originalFilename)) {
            String savedUUID = attachmentService.save(
                    new Attachment(
                            AttachmentKindEnum.REQUEST_ATTACHMENT,
                            requiredDocumentService.findById(documentType),
                            new Timestamp(new Date().getTime()),
                            null,
                            UUID.randomUUID().toString(),
                            originalFilename,
                            null,
                            null,
                            false,
                            null,
                            false),
                    content.getBytes());
            StatementAttachmentsEntity statementAttachments = new StatementAttachmentsEntity();
            statementAttachments.setAttachment(attachmentRepository.getById(savedUUID));
            statementAttachments.setSatement(statement);
            statementAttachments.setDeleted(false);
            statementAttachmentsRepository.save(statementAttachments);
            LOGGER.info(String.format("К заявлению %s добавлено вложение %s", statementId, statementAttachments.getId()));
            return true;
        } else {
            LOGGER.warn(String.format("Комплект документов заявления %s уже содержит файл %s",
                    statementId,
                    originalFilename));
            return false;
        }
    }

    @Transactional
    public void changeStatementComment(Long statementId, String comment) {
        StatementEntity statement = getStatementById(statementId);
        statement.setComment(comment);
        statementRepository.save(statement);
        LOGGER.info(String.format("Комментарий к заявлению %s добавлен", statementId));
    }

    @Transactional
    public void setStatementStatus(Long statementId, String value) {
        StatementEntity statement = getStatementById(statementId);
        statement.setStatementCompleteStatus(StatementCompleteStatus.fromValue(value));
        if (StatementCompleteStatus.FULL.equals(statement.getStatementCompleteStatus()) ||
                StatementCompleteStatus.NOT_CONTROLLED.equals(statement.getStatementCompleteStatus())) {
            statement.setComment(null);
        }
        statementRepository.save(statement);
        LOGGER.info(String.format("Статус %s полного пакета документов добавлен к заявлению %s", value, statementId));
    }

}
