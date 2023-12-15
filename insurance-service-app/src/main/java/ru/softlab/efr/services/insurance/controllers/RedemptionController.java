package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.DictConverter;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.RedemptionCoefficientEntity;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.rest.RedemptionData;
import ru.softlab.efr.services.insurance.services.ProgramService;
import ru.softlab.efr.services.insurance.services.RedemptionService;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * Контроллер для работы со справочником выкупных коэффициенттов.
 *
 * @author Kalantaev
 * @since 29.11.2018
 */
@RestController
@PropertySource(value = {"classpath:application.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
public class RedemptionController implements RedemptionApi {

    private static final Logger LOGGER = Logger.getLogger(RedemptionController.class);

    private final RedemptionService redemptionService;

    private final ProgramService programService;

    @Value("${redemption.program.not.found.error}")
    private String PROGRAM_NOT_FOUND_ERROR;
    @Value("${redemption.not.found.error}")
    private String REDEMPTION_NOT_FOUND_ERROR;

    @Autowired
    public RedemptionController(RedemptionService redemptionService, ProgramService programService) {
        this.redemptionService = redemptionService;
        this.programService = programService;
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<RedemptionData> getRedemption(@PathVariable("id") Long id) {
        RedemptionEntity entity = redemptionService.findById(id);
        if (entity == null) {
            LOGGER.error(String.format(REDEMPTION_NOT_FOUND_ERROR, id));
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DictConverter.convert(entity));
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<Page<RedemptionData>> getRedemptionList(@PageableDefault(value = 50) Pageable pageable) {
        Page<RedemptionEntity> page = redemptionService.findAll(pageable);
        return ResponseEntity.ok(page.map(DictConverter::convert));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<RedemptionData> createRedemption(@Valid @RequestBody RedemptionData updateRedemptionRq) {
        Program program = programService.findById(updateRedemptionRq.getProgramId());
        if (program == null) {
            LOGGER.error(String.format(PROGRAM_NOT_FOUND_ERROR, updateRedemptionRq.getProgramId()));
            return ResponseEntity.notFound().build();
        }
        RedemptionEntity entity = DictConverter.convert(updateRedemptionRq);
        entity.setProgram(program);
        try {
            redemptionService.create(entity);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(DictConverter.convert(entity));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> deleteRedemption(@PathVariable("id") Long id) {
        RedemptionEntity entity = redemptionService.findById(id);
        if (entity == null) {
            LOGGER.error(String.format(REDEMPTION_NOT_FOUND_ERROR, id));
            return ResponseEntity.notFound().build();
        }
        redemptionService.logicalDelete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<RedemptionData> putRedemption(@PathVariable("id") Long id, @Valid @RequestBody RedemptionData updateRedemptionRq) {
        RedemptionEntity entity = redemptionService.findById(id);
        if (entity == null) {
            LOGGER.error(String.format(REDEMPTION_NOT_FOUND_ERROR, id));
            return ResponseEntity.notFound().build();
        }
        Program program = programService.findById(updateRedemptionRq.getProgramId());
        if (program == null) {
            LOGGER.error(String.format(PROGRAM_NOT_FOUND_ERROR, updateRedemptionRq.getProgramId()));
            return ResponseEntity.notFound().build();
        }
        entity.setCurrency(updateRedemptionRq.getCurrencyId());
        entity.setDuration(updateRedemptionRq.getDuration());
        entity.setPeriodicity(PeriodicityEnum.valueOf(updateRedemptionRq.getPeriodicity().name()));
        entity.setPaymentPeriod(PeriodicityEnum.valueOf(updateRedemptionRq.getPaymentPeriod().name()));
        entity.setCoefficientList(updateRedemptionRq.getCoefficientList().stream().map(item ->
                new RedemptionCoefficientEntity(item.getId(), item.getPeriod(), item.getCoefficient(), entity))
                .collect(Collectors.toList()));
        entity.setProgram(program);
        try {
            redemptionService.update(entity);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(DictConverter.convert(entity));
    }
}
