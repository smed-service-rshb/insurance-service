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
import ru.softlab.efr.services.insurance.service.ChangesReportService;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Контроллер обработки пользовательских запросов, связанных с получением отчёта по изменениям.
 *
 * @author olshansky
 * @since 01.02.2019
 */
@RestController
public class ChangeReportController implements ChangeReportApi {

    private final ChangesReportService changesReportService;

    private final ExtractProcessInfoService extractProcessInfoService;

    @Autowired
    public ChangeReportController(ChangesReportService changesReportService, ExtractProcessInfoService extractProcessInfoService) {
        this.changesReportService = changesReportService;
        this.extractProcessInfoService = extractProcessInfoService;
    }

    @Override
    @HasRight(Right.CHANGE_REPORT)
    public ResponseEntity<UuidRs> generateChangeReport(@NotNull @RequestParam(value = "createDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate createDate,
                                                       @RequestParam(value = "endDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) throws Exception {
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        String fileName = changesReportService.getFileName();
        ExtractCreateProcessResult createProcessResult = extractProcessInfoService.createProcessInfo(ExtractType.CHANGE_REPORT, fileName, null);
        if (!createProcessResult.isAlreadyExists()) {
            changesReportService.createReportAsync(createDate.atStartOfDay(), endDate.plusDays(1L).atStartOfDay(), createProcessResult.getExtract().getUuid());
        }

        return ResponseEntity.ok().body(new UuidRs(createProcessResult.getExtract().getUuid()));
    }
}
