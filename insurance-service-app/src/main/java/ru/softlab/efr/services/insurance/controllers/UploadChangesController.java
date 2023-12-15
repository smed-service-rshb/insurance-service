package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.model.rest.ImportRs;
import ru.softlab.efr.services.insurance.service.UploadChangesService;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;


/**
 * Контроллер обработки запросов, связанных с импортом изменений из "Универсального отчёта"
 *
 * @author olshansky
 * @since 07.03.2019.
 */
@Validated
@RestController
public class UploadChangesController implements UploadChangesApi {

    private final UploadChangesService uploadChangesService;
    private final ExtractProcessInfoService extractProcessInfoService;

    @Autowired
    public UploadChangesController(UploadChangesService uploadChangesService, ExtractProcessInfoService extractProcessInfoService) {
        this.uploadChangesService = uploadChangesService;
        this.extractProcessInfoService = extractProcessInfoService;
    }

    @HasRight(Right.UPLOAD_INSURANCE_REPORT)
    @Override
    public ResponseEntity<ImportRs> uploadChanges(MultipartFile content) throws Exception {

        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.IMPORT_INSURANCE, content.getOriginalFilename(), null);
        if (!createProcessResult.isAlreadyExists()) {
            uploadChangesService.importInsuranceAsync(content.getBytes(), createProcessResult.getExtract().getUuid());
        }

        return ResponseEntity.ok().body(new ImportRs(createProcessResult.getExtract().getUuid(), uploadChangesService.getReportStoreFolder()));
    }
}