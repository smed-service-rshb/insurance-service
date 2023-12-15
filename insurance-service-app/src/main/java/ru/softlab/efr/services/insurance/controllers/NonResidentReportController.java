package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.model.rest.UuidRs;
import ru.softlab.efr.services.insurance.service.OnNonResidentService;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Контроллер обработки пользовательских запросов, связанных с получением отчёта о налоговых нерезидентах.
 *
 * @author Dzhemaletdinov
 * @since 03.02.2019
 */
@RestController
public class NonResidentReportController implements NonResidentReportApi {

    private final OnNonResidentService residentService;

    private final ExtractProcessInfoService extractProcessInfoService;

    @Autowired
    public NonResidentReportController(OnNonResidentService residentService, ExtractProcessInfoService extractProcessInfoService) {
        this.residentService = residentService;
        this.extractProcessInfoService = extractProcessInfoService;
    }

    @Override
    @HasRight(Right.NON_RESIDENT_REPORT)
    public ResponseEntity<UuidRs> generateNonResidentReport(@NotNull @Valid @RequestParam(value = "startDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                            @NotNull @Valid @RequestParam(value = "endDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) {

        String fileName = residentService.getFileName();
        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.NON_RESIDENT_REPORT, fileName, null);
        if (!createProcessResult.isAlreadyExists()) {
            residentService.createReportAsync(startDate.atStartOfDay(), endDate.plusDays(1L).atStartOfDay(), createProcessResult.getExtract().getUuid());
        }

        return ResponseEntity.ok().body(new UuidRs(createProcessResult.getExtract().getUuid()));
    }

}
