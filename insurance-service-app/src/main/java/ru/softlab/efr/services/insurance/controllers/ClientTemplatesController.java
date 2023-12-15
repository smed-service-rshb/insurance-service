package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import ru.softlab.efr.services.insurance.converter.ClientTemplateConverter;
import ru.softlab.efr.services.insurance.model.db.Attachment;
import ru.softlab.efr.services.insurance.model.db.ClientTemplate;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.repositories.ShortClientTemplates;
import ru.softlab.efr.services.insurance.services.AttachmentService;
import ru.softlab.efr.services.insurance.services.ClientTemplatesService;
import ru.softlab.efr.services.insurance.services.ProgramService;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientTemplatesController implements ClientTemplatesApi {

    private final ClientTemplatesService clientTemplatesService;
    private final ClientTemplateConverter clientTemplateConverter;
    private final AttachmentService attachmentService;
    private final ProgramService programService;
    private final PrincipalDataSource principalDataSource;

    public ClientTemplatesController(ClientTemplatesService clientTemplatesService, ClientTemplateConverter clientTemplateConverter, AttachmentService attachmentService, ProgramService programService, PrincipalDataSource principalDataSource) {
        this.clientTemplatesService = clientTemplatesService;
        this.clientTemplateConverter = clientTemplateConverter;
        this.attachmentService = attachmentService;
        this.programService = programService;
        this.principalDataSource = principalDataSource;
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<Page<ClientTemplateDataForList>> getClientTemplatesList(@PageableDefault(value = 50) Pageable pageable,
                                                                                  @Valid @RequestBody FilterClientTemplates filter,
                                                                                  @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) {

        Page<ShortClientTemplates> clientTemplates = clientTemplatesService.findAll(pageable,
                filter.getKind() != null ? ProgramKind.valueOf(filter.getKind().name()) : null,
                filter.getProgram(),
                filter.isIsTemplate());

        return ResponseEntity.ok(clientTemplates.map(clientTemplateConverter::convert));
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<ClientTemplateData> createClientTemplates(@Valid @RequestBody ClientTemplateData body) throws Exception {
        ClientTemplate template = new ClientTemplate();
        return createOrUpdate(body, template);
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<UuidRs> addTemplateContent(@Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {

        if (content == null) {
            return ResponseEntity.badRequest().build();
        }

        String attachmentId = attachmentService.save(getAttachment(content), content.getBytes());
        return ResponseEntity.ok(new UuidRs(attachmentId));
    }

    private Attachment getAttachment(MultipartFile content) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        return new Attachment(
                AttachmentKindEnum.CLIENT_TEMPLATE,
                null,
                new Timestamp(new Date().getTime()),
                null,
                null,
                new String(content.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                principalData.getId(),
                String.format("%s %s%s", principalData.getSecondName(), principalData.getFirstName(),
                        org.apache.commons.lang.StringUtils.isNotBlank(principalData.getMiddleName()) ? " ".concat(principalData.getMiddleName()) : ""),
                false, null,
                false
        );
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<Void> deleteTemplateContent(@PathVariable("attachId") String attachId) throws Exception {
        Attachment entity;
        try {
            entity = attachmentService.findById(attachId);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        if (entity.getKind() != AttachmentKindEnum.CLIENT_TEMPLATE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        attachmentService.logicalDelete(attachId);
        return ResponseEntity.ok().build();

    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<ClientTemplateData> getClientTemplate(@PathVariable("id") Long id) throws Exception {
        ClientTemplate template = clientTemplatesService.findById(id);
        if (template == null || Boolean.TRUE.equals(template.getDeleted())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientTemplateConverter.convert(template));
    }

    @Override
    @HasPermission(Permissions.VIEW_CLIENT_TEMPLATE)
    public ResponseEntity<ClientTemplateList> getClientTemplates(@PathVariable("insuranceId") Long insuranceId) throws Exception {
        List<ClientTemplate> clientList = clientTemplatesService.findClientList(insuranceId);
        return ResponseEntity.ok(new ClientTemplateList(clientList.stream().filter(ClientTemplate::getTemplate)
                .map(clientTemplateConverter::convertToClient).collect(Collectors.toList()),
                clientList.stream().filter(item -> !item.getTemplate())
                        .map(clientTemplateConverter::convertToClient).collect(Collectors.toList())));
    }

    @Override
    @HasPermission(Permissions.VIEW_TEMPLATE_CONTENT)
    public ResponseEntity<byte[]> getTemplateContent(@PathVariable("attachId") String attachId) throws Exception {
        Attachment entity;
        try {
            entity = attachmentService.findById(attachId);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        if (entity.getKind() != AttachmentKindEnum.CLIENT_TEMPLATE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (Boolean.TRUE.equals(entity.getDeleted())) {
            return ResponseEntity.notFound().build();
        }

        String extension = "";
        if (!StringUtils.isEmpty(entity.getFileName())){
            extension = FilenameUtils.getExtension(entity.getFileName());
            extension = "jpg".equals(extension) ? "jpeg" : extension;
        }
        return ResponseEntity
                .ok()
                .contentType(extension.equalsIgnoreCase("pdf") ? MediaType.APPLICATION_PDF : MediaType.valueOf("image/" + extension))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entity.getFileName() + "\"")
                .body(attachmentService.getContent(attachId));
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_TEMPLATES)
    public ResponseEntity<ClientTemplateData> updateClientTemplates(@PathVariable("id") Long id, @Valid @RequestBody ClientTemplateData body) throws Exception {
        ClientTemplate template = clientTemplatesService.findById(id);
        if (template == null) {
            return ResponseEntity.notFound().build();
        }
        return createOrUpdate(body, template);
    }

    private ResponseEntity<ClientTemplateData> createOrUpdate(ClientTemplateData body, ClientTemplate template) {
        ClientTemplate entity = clientTemplateConverter.convert(template, body);
        if (!StringUtils.isEmpty(body.getAttachId())) {
            Attachment attachment = attachmentService.findById(body.getAttachId());
            if (attachment != null) {
                entity.setAttachment(attachment);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        if (body.getProgram() != null) {
            try {
                Program program = programService.findById(body.getProgram());
                entity.setProgram(program);
            } catch (EntityNotFoundException ex) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(clientTemplateConverter.convert(clientTemplatesService.save(entity)));
    }
}
