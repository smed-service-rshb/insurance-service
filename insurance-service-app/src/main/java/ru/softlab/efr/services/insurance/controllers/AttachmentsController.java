package ru.softlab.efr.services.insurance.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.RequestAttachConverter;
import ru.softlab.efr.services.insurance.converter.StatementConverter;
import ru.softlab.efr.services.insurance.model.db.Attachment;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.utils.AppUtils;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с вложениями, сканами документов
 *
 * @author olshansky
 * @since 25.10.2018
 */
@RestController
public class AttachmentsController implements AttachmentsApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AttachmentService attachmentService;
    private RequiredDocumentService requiredDocumentService;
    private InsuranceService contractService;
    private PrincipalDataSource principalDataSource;
    private RequestAttachService requestAttachService;
    private RequestAttachConverter requestAttachConverter;
    private StatementService statementService;
    private StatementConverter statementConverter;
    private RequestService requestService;

    @Autowired
    public AttachmentsController(AttachmentService attachmentService,
                                 RequiredDocumentService requiredDocumentService,
                                 InsuranceService contractService,
                                 PrincipalDataSource principalDataSource,
                                 RequestAttachService requestAttachService,
                                 RequestAttachConverter requestAttachConverter,
                                 StatementService statementService,
                                 StatementConverter statementConverter, RequestService requestService) {
        this.attachmentService = attachmentService;
        this.contractService = contractService;
        this.requiredDocumentService = requiredDocumentService;
        this.principalDataSource = principalDataSource;
        this.requestAttachService = requestAttachService;
        this.requestAttachConverter = requestAttachConverter;
        this.statementService = statementService;
        this.statementConverter = statementConverter;
        this.requestService = requestService;
    }

    @Override
    public ResponseEntity<Void> deleteAttachment(@PathVariable String attachId) throws Exception {
        if (attachmentService.isExists(attachId)) {
            attachmentService.logicalDelete(attachId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> getAttachmentContent(@PathVariable String attachId) throws Exception {
        Attachment entity;
        try {
            entity = attachmentService.findById(attachId);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entity.getFileName() + "\"")
                .body(attachmentService.getContent(attachId));
    }


    @Override
    public ResponseEntity<AttachedDocRs> getAttachmentList(@PathVariable Long id) throws Exception {
        Integer countAttachmentsByContractId = attachmentService.getCountAttachmentsByContractId(id);
        if (countAttachmentsByContractId == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(new AttachedDocRs(convert(attachmentService.findAllExceptDeletedByContract(id))));
    }

    @Override
    public ResponseEntity<ru.softlab.efr.services.insurance.model.rest.Attachment> getCheckExistenceAttachment(
            @PathVariable("contractId") Long contractId,
            @NotNull @Valid @RequestParam(value = "type") String type) throws Exception {
        return getAttachments(contractId, null, type);
    }

    @Override
    public ResponseEntity<ru.softlab.efr.services.insurance.model.rest.Attachment> getCheckExistenceTempAttachment(
            @PathVariable("uuid") String uuid, @NotNull @Valid @RequestParam(value = "type") String type) throws Exception {
        return getAttachments(null, uuid, type);
    }


    @Override
    public ResponseEntity<String> uploadAttachment(@PathVariable Long contractId, @PathVariable Long documentType,
                                                   @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        //Кастомная валидация сделана, тк спринг без аннотации Validated на классе не проиодит валидацию параметров.
        //Навешивание аннотации Validated приводит к созданию прокси, который
        // 1) ломает проверку прав (доработка auth-attachmentService)
        // 2) не поднимает контроллеры вообще, тк в базовом интерфейсе нет аннотации RestController (доработка сваггера)
        if (contractId == null || documentType == null || content == null) {
            return ResponseEntity.badRequest().build();
        }


        String attachmentId = attachmentService.save(getAttachment(content, AttachmentKindEnum.INSURANCE_CONTRACT,
                requiredDocumentService.findById(documentType), contractService.findById(contractId), null), content.getBytes());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
                .body(objectMapper.writeValueAsString(attachmentId));
    }

    private Attachment getAttachment(MultipartFile content, AttachmentKindEnum attachmentKindEnum, RequiredDocument documentType,
                                     Insurance insuranceContract, String contractUid) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        return new Attachment(
                attachmentKindEnum,
                documentType,
                new Timestamp(new Date().getTime()),
                insuranceContract,
                contractUid,
                new String(content.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                principalData.getId(),
                String.format("%s %s%s", principalData.getSecondName(), principalData.getFirstName(), org.apache.commons.lang.StringUtils.isNotBlank(principalData.getMiddleName()) ? " ".concat(principalData.getMiddleName()) : ""),
                false,
                attachmentKindEnum.toString().concat(documentType != null ? " - ".concat(documentType.getType()) : ""),
                false
        );
    }

    @Override
    public ResponseEntity<String> uploadForeignResidentAttachment(@PathVariable("contractId") Long contractId,
                                                                  @NotNull @Valid @RequestParam(value = "blockId") String blockId,
                                                                  @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if ((contractId == null) || (content == null)) {
            return ResponseEntity.badRequest().build();
        }

        String attachmentId = attachmentService.save(getAttachment(content, blockId.equals("policyHolder") ? AttachmentKindEnum.FORM_CERTIFICATION_POLICYHOLDER : AttachmentKindEnum.FORM_CERTIFICATION_INSURED, requiredDocumentService.findByType("Форма самосертификации"), contractService.findById(contractId), null), content.getBytes());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
                .body(objectMapper.writeValueAsString(attachmentId));
    }

    @Override
    public ResponseEntity<UuidRs> uploadForeignResidentTempAttachment(@PathVariable("uuid") String uuid,
                                                                      @NotNull @Valid @RequestParam(value = "blockId") String blockId,
                                                                      @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if (content == null) {
            return ResponseEntity.badRequest().build();
        }

        if (StringUtils.isEmpty(uuid) || uuid.equals("undefined")) {
            uuid = UUID.randomUUID().toString();
        }
        PrincipalData principalData = principalDataSource.getPrincipalData();
        attachmentService.save(new Attachment(
                blockId.equals("policyHolder") ? AttachmentKindEnum.FORM_CERTIFICATION_POLICYHOLDER : AttachmentKindEnum.FORM_CERTIFICATION_INSURED,
                null,
                new Timestamp(new Date().getTime()),
                null,
                uuid,
                new String(content.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                principalData.getId(),
                String.format("%s %s%s", principalData.getSecondName(), principalData.getFirstName(), org.apache.commons.lang.StringUtils.isNotBlank(principalData.getMiddleName()) ? " ".concat(principalData.getMiddleName()) : ""),
                false,
                "",
                false
        ), content.getBytes());
        return ResponseEntity.ok().body(new UuidRs(uuid));
    }

    @Override
    public ResponseEntity<UuidRs> uploadTempAttachment(@PathVariable("uuid") String uuid,
                                                       @PathVariable("documentType") Long documentType,
                                                       @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if ((documentType == null) || (content == null)) {
            return ResponseEntity.badRequest().build();
        }

        if (StringUtils.isEmpty(uuid) || uuid.equals("undefined")) {
            uuid = UUID.randomUUID().toString();
        }

        attachmentService.save(getAttachment(content, AttachmentKindEnum.INSURANCE_CONTRACT,
                requiredDocumentService.findById(documentType), null, uuid), content.getBytes());
        return ResponseEntity.ok().body(new UuidRs(uuid));

    }

    @Override
    public ResponseEntity<AttachedDocRs> getTempAttachmentList(@PathVariable("uuid") String uuid) throws Exception {
        Integer countAttachmentsByContractId = attachmentService.getCountAttachmentsByContractUuid(uuid);
        if (countAttachmentsByContractId == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(new AttachedDocRs(convert(attachmentService.findAllExceptDeletedByContract(uuid))));
    }

    @Override
    @HasPermission(Permissions.UPDATE_CLIENT_REQUEST_ATTACHMENT)
    public ResponseEntity<Void> deleteRequestAttachment(@PathVariable("requestAttachId") Long requestAttachId) throws Exception {
        String attachId = requestAttachService.deleteAttachmentFromRequest(requestAttachId);
        if (Objects.nonNull(attachId)) {
            deleteAttachment(attachId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_VIEW)
    public ResponseEntity<byte[]> getRequestAttachmentContent(@PathVariable("requestAttachId") Long requestAttachId) throws Exception {
        Attachment attachment = requestAttachService.getAttachmentFromRequest(requestAttachId);
        return getAttachmentContent(attachment.getId());
    }

    @Override
    @HasPermission(Permissions.UPDATE_CLIENT_REQUEST_ATTACHMENT)
    public ResponseEntity<AttachedRequestAttachData> uploadRequestAttachment(@PathVariable("requestId") Long requestId,
                                                                             @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        RequestEntity request = requestService.getRequestById(requestId);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long id = requestAttachService.addAttachmentToRequest(
                request,
                new String(content.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                content.getBytes());

        return ResponseEntity.ok().body(new AttachedRequestAttachData(id, content.getOriginalFilename()));
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_CREATE)
    public ResponseEntity<AttachedRequestAttachData> uploadRequestAttachmentBase64(@PathVariable("requestId") Long requestId,
                                                                                   @Valid @RequestBody AttachmentData attachment) {
        RequestEntity request = requestService.getRequestById(requestId);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long id = requestAttachService.addAttachmentToRequest(
                request,
                attachment.getFileName(),
                Base64.getDecoder().decode(attachment.getContent()));

        return ResponseEntity.ok().body(new AttachedRequestAttachData(id, attachment.getFileName()));
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_VIEW)
    public ResponseEntity<AttachedList> getRequestAttachmentList(@PathVariable("requestId") Long requestId) throws Exception {
        AttachedList response = new AttachedList();
        List<RequestsAttachmentEntity> requestsAttachmentEntity = requestAttachService.getRequestAttachments(requestId);
        if (Objects.nonNull(requestsAttachmentEntity)) {
            response.setElements(requestsAttachmentEntity
                    .stream()
                    .map(requestAttachConverter::toAttachedRequestAttachData)
                    .collect(Collectors.toList()));
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Statement> getStatementById(@PathVariable("statementId") Long statementId) throws Exception {
        StatementEntity returnedStatement = statementService.getStatementById(statementId);
        if (Objects.nonNull(returnedStatement)) {
            Statement statement = statementConverter.convertToStatement(returnedStatement);
            statement.setDocuments(
                    statementService.getAttachmentsListByStatementId(statement.getId())
                            .stream()
                            .map(requestAttachConverter::toStatusAttachment)
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok().body(statement);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<StatementsData> getStatementAttachmentList(@PathVariable("insuranceId") Long insuranceId) throws Exception {
        List<Statement> statements = statementService.getStatementsByInsuranceId(insuranceId)
                .stream()
                .map(statementConverter::convertToStatement)
                .collect(Collectors.toList());
        statements
                .forEach(statement -> statement.setDocuments(
                        statementService.getAttachmentsListByStatementId(statement.getId())
                                .stream()
                                .map(requestAttachConverter::toStatusAttachment)
                                .collect(Collectors.toList())
                ));
        StatementsData response = new StatementsData();
        response.setStatementTypes(statements);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Statement> createStatement(@PathVariable("insuranceId") Long insuranceId,
                                                     @PathVariable("statementType") String statementType) throws Exception {
        StatementEntity statement = statementService.addStatementByInsuranceIdAndStatusCode(insuranceId, InsuranceStatusCode.valueOf(statementType));
        if (Objects.nonNull(statement)) {
            return ResponseEntity.ok().body(statementConverter.convertToStatement(statement));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Override
    public ResponseEntity<Void> uploadStatementAttachment(@PathVariable("statementId") Long statementId,
                                                          @PathVariable("documentType") Long documentType,
                                                          @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if (statementService.attachToStatement(statementId, content, documentType)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Override
    @HasRight(Right.STATEMENT_CONTROL)
    public ResponseEntity<Void> changeStatementComment(@PathVariable("statementId") Long statementId,
                                                       @NotNull @Valid @RequestParam(value = "comment") String comment) throws Exception {
        statementService.changeStatementComment(statementId, comment);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.STATEMENT_CONTROL)
    public ResponseEntity<Void> setStatementDocumentStatus(@PathVariable("statementId") Long statementId,
                                                           @NotNull @Valid @RequestParam(value = "statementCompleteStatus") String statementCompleteStatus) throws Exception {
        statementService.setStatementStatus(statementId, statementCompleteStatus);
        return ResponseEntity.ok().build();
    }

    private List<AttachedDocumentType> convert(List<Attachment> attachments) {
        return attachments.stream()
                .filter(AppUtils.distinctByKey(Attachment::getDocumentType))
                .map(attachment ->
                        new AttachedDocumentType(attachment.getDocumentType().getId(),
                                attachment.getDocumentType().getType(),
                                attachments.stream()
                                        .filter(f -> f.getDocumentType().getId().equals(attachment.getDocumentType().getId()))
                                        .map(m -> new ru.softlab.efr.services.insurance.model.rest.Attachment(m.getId(),
                                                m.getFileName(),
                                                AppUtils.mapTimestamp2LocalDate(m.getCreateDate()),
                                                m.getComment()))
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList());
    }

    private ResponseEntity<ru.softlab.efr.services.insurance.model.rest.Attachment> getAttachments(Long contractId, String uuid, String type) {
        Attachment attachment;
        String kindEnum = type.equals("policyHolder") ? AttachmentKindEnum.FORM_CERTIFICATION_POLICYHOLDER.name() : AttachmentKindEnum.FORM_CERTIFICATION_INSURED.name();
        try {
            attachment = (contractId == null) ? attachmentService.findByKindAndContract(kindEnum, uuid)
                    : attachmentService.findByKindAndContract(kindEnum, contractId);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        ru.softlab.efr.services.insurance.model.rest.Attachment singleAttachment =
                new ru.softlab.efr.services.insurance.model.rest.Attachment(
                        attachment.getId(),
                        attachment.getFileName(),
                        AppUtils.mapTimestamp2LocalDate(attachment.getCreateDate()),
                        attachment.getComment());
        return ResponseEntity.ok(singleAttachment);
    }
}
