package ru.softlab.efr.services.insurance.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.converter.AcquiringProgramConverter;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.db.DbImage;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.rest.AcquiringProgramData;
import ru.softlab.efr.services.insurance.services.AcquiringProgramService;
import ru.softlab.efr.services.insurance.services.DbImageService;
import ru.softlab.efr.services.insurance.services.ProgramSettingService;

import javax.validation.Valid;

/**
 * Контроллер для работы со справочником программ доступных для оформления в ЛК
 */
@RestController
public class AcquiringProgramController implements AcquiringProgramApi {

    private final AcquiringProgramService acquiringProgramService;
    private final AcquiringProgramConverter acquiringProgramConverter;
    private final ProgramSettingService programSettingService;
    private final DbImageService dbImageService;

    public AcquiringProgramController(AcquiringProgramService acquiringProgramService, AcquiringProgramConverter acquiringProgramConverter, ProgramSettingService programSettingService, DbImageService dbImageService) {
        this.acquiringProgramService = acquiringProgramService;
        this.acquiringProgramConverter = acquiringProgramConverter;
        this.programSettingService = programSettingService;
        this.dbImageService = dbImageService;
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<AcquiringProgramData> createProgram(@Valid @RequestBody AcquiringProgramData request) {
        AcquiringProgram program = new AcquiringProgram();
        return createOrUpdateProgram(request, program);
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<AcquiringProgramData> getProgram(@PathVariable("id") Long id) {
        AcquiringProgram program = acquiringProgramService.findById(id);
        if (program == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(acquiringProgramConverter.convert(program));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Page<AcquiringProgramData>> getProgramList(@PageableDefault(value = 50) Pageable pageable) {
        Pageable descIdPageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok().body(acquiringProgramService.getPageAcquiringProgram(descIdPageable).map(acquiringProgramConverter::convertToList));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<AcquiringProgramData> updateProgram(@PathVariable("id") Long id, @Valid @RequestBody AcquiringProgramData request) {
        AcquiringProgram program = acquiringProgramService.findById(id);
        if (program == null) {
            return ResponseEntity.notFound().build();
        }
        return createOrUpdateProgram(request, program);
    }

    private ResponseEntity<AcquiringProgramData> createOrUpdateProgram(AcquiringProgramData request, AcquiringProgram program) {
        acquiringProgramConverter.convert(request, program);
        ProgramSetting programSetting = request.getProgramSettingId() != null ? programSettingService.findById(request.getProgramSettingId()) : null;
        if (programSetting == null) {
            return ResponseEntity.badRequest().build();
        }
        DbImage image = request.getImage() != null ? dbImageService.findImageById(request.getImage()) : null;
        if (image == null || image.getDeleted()) {
            return ResponseEntity.badRequest().build();
        }
        DbImage infoImage = request.getInfoImage() != null ? dbImageService.findImageById(request.getInfoImage()) : null;
        if (infoImage != null && !infoImage.getDeleted()) {
            program.setInfoImage(infoImage);
        }
        program.setProgram(programSetting);
        program.setImage(image);
        return ResponseEntity.ok(acquiringProgramConverter.convert(acquiringProgramService.save(program)));
    }
}
