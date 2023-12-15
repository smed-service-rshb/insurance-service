package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.converter.UserTemplateConverter;
import ru.softlab.efr.services.insurance.model.db.UserTemplateEntity;
import ru.softlab.efr.services.insurance.model.rest.CreateUserTemplateResponse;
import ru.softlab.efr.services.insurance.model.rest.UserTemplateData;
import ru.softlab.efr.services.insurance.services.UserTemplatesService;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Контроллер для работы с пользовательскими шаблонами документов
 *
 * @author olshansky
 * @since 10.04.2019
 */
@RestController
public class UserTemplatesController implements UserTemplatesApi {

    private UserTemplatesService service;

    @Autowired
    public UserTemplatesController(UserTemplatesService service) {
        this.service = service;
    }


    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<CreateUserTemplateResponse> createUserTemplate(@Valid @RequestBody UserTemplateData createUserTemplateRq) throws Exception {
        try{
            return ResponseEntity.ok(new CreateUserTemplateResponse(service.saveUserTemplate(
                    service.save(UserTemplateConverter.convert(createUserTemplateRq))).getId()));
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }

    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<Void> deleteUserTemplate(@PathVariable("id") Long id) throws Exception {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserTemplateData> getUserTemplateById(@PathVariable("id") Long id) throws Exception {
        try {
            return ResponseEntity.ok(UserTemplateConverter.convert(service.findOne(id)));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<byte[]> getUserTemplateContent(@PathVariable("id") Long id) throws Exception {
        UserTemplateEntity templateEntity = service.findById(id);

        String sourceFileName = StringUtils.isEmpty(templateEntity.getFileName()) ? templateEntity.getName() : templateEntity.getFileName();
        String encodedFileName = URLEncoder.encode(sourceFileName, StandardCharsets.UTF_8.name())
                .replaceAll("\\+", " ");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .body(service.getContentById(id));
    }

    @Override
    public ResponseEntity<Page<UserTemplateData>> getUserTemplateList(@PageableDefault(value = 50, sort = "priority") Pageable pageable) throws Exception {
        Page<UserTemplateEntity> backEntities = service.findAll(pageable);
        return ResponseEntity.ok(backEntities.map(UserTemplateConverter::convert));
    }

    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<Void> updateUserTemplate(@PathVariable("id") Long id, @Valid @RequestBody UserTemplateData updateUserTemplateRequest) throws Exception {
        service.save(UserTemplateConverter.convert(updateUserTemplateRequest));
        return ResponseEntity.ok().build();
    }
}
