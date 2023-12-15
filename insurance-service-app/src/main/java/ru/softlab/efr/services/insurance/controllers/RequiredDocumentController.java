package ru.softlab.efr.services.insurance.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.DictConverter;
import ru.softlab.efr.services.insurance.model.db.RequiredDocument;
import ru.softlab.efr.services.insurance.model.rest.RequiredDocumentData;
import ru.softlab.efr.services.insurance.services.RequiredDocumentService;

import javax.persistence.EntityNotFoundException;

/**
 * Контроллер для работы со справочником обязательных документов.
 *
 * @author Kalantaev
 * @since 14.09.2018
 */
@RestController
public class RequiredDocumentController implements RequiredDocumentsApi {

    private static final Logger LOGGER = Logger.getLogger(RequiredDocumentController.class);

    @Autowired
    private RequiredDocumentService documentService;

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> createRequiredDocument(@RequestBody RequiredDocumentData createRequiredDocumentRq) {
        RequiredDocument document = DictConverter.convert(createRequiredDocumentRq);
        documentService.create(document);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> deleteRequiredDocuments(@PathVariable("id") Long id) {
        try {
            documentService.logicalDelete(id);
        } catch (EntityNotFoundException ex) {
            LOGGER.error(String.format("Ошибка обработки запроса: не удается найти запись " +
                    "справочника обязательных документов по идентификатору %s", id));
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<RequiredDocumentData> getRequiredDocument(@PathVariable("id") Long id) {
        RequiredDocument document = documentService.findById(id);
        if (document != null) {
            return ResponseEntity.ok(DictConverter.convert(document));
        } else {
            LOGGER.error(String.format("Не найдена запись справочника обязательных документов по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> putRequiredDocument(@PathVariable("id") Long id,
                                                    @RequestBody RequiredDocumentData updateRq) {
        RequiredDocument document = documentService.findById(id);
        if (document == null) {
            LOGGER.error(String.format("Обязательный документ не найден по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
        document.setType(updateRq.getType());
        document.setActiveFlag(updateRq.isActiveFlag());

        documentService.update(document);

        return ResponseEntity.ok().build();
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<Page<RequiredDocumentData>> requiredDocuments(Pageable pageable) {
        Page<RequiredDocument> documents = documentService.findAll(pageable);
        return ResponseEntity.ok(documents.map(DictConverter::convert));
    }
}
