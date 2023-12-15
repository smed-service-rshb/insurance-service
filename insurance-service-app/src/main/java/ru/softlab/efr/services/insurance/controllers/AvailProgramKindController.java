package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.converter.AvailProgramConverter;
import ru.softlab.efr.services.insurance.model.db.AvailProgramKindEntity;
import ru.softlab.efr.services.insurance.model.rest.AvailProgramKindData;
import ru.softlab.efr.services.insurance.model.rest.AvailProgramKindListRs;
import ru.softlab.efr.services.insurance.model.rest.CreateAvailProgramKindResponse;
import ru.softlab.efr.services.insurance.service.EmployeeFilterService;
import ru.softlab.efr.services.insurance.services.AvailProgramKindService;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * Контроллер для работы со справочником доступности видов программ страхования.
 *
 * @author olshansky
 * @since 27.03.2019
 */
@RestController
public class AvailProgramKindController implements AvailProgramKindApi {

    private AvailProgramKindService availProgramKindService;
    private EmployeeFilterService employeeFilterService;
    private PrincipalDataSource principalDataSource;

    @Autowired
    public AvailProgramKindController(AvailProgramKindService availProgramKindService, PrincipalDataSource principalDataSource,
                                      EmployeeFilterService employeeFilterService) {
        this.availProgramKindService = availProgramKindService;
        this.employeeFilterService = employeeFilterService;
        this.principalDataSource = principalDataSource;
    }

    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<CreateAvailProgramKindResponse> createAvailProgramKind(
            @Valid @RequestBody AvailProgramKindData createAvailProgramKindRq) throws Exception {
        return ResponseEntity.ok(new CreateAvailProgramKindResponse(
                availProgramKindService.save(AvailProgramConverter.convert(createAvailProgramKindRq)).getId()));
    }

    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<Void> deleteAvailProgramKind(@PathVariable("id") Long id) throws Exception {
        availProgramKindService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AvailProgramKindData> getAvailProgramKindById(@PathVariable("id") Long id) throws Exception {
        AvailProgramKindEntity backEntity = availProgramKindService.findOne(id);
        return ResponseEntity.ok(AvailProgramConverter.convert(backEntity));
    }

    @Override
    public ResponseEntity<Page<AvailProgramKindData>> getAvailProgramKindDictList(
            @PageableDefault(value = 50) Pageable pageable) throws Exception {
        return ResponseEntity.ok(availProgramKindService.findAll(pageable).map(AvailProgramConverter::convert));
    }

    @Override
    public ResponseEntity<AvailProgramKindListRs> getAvailProgramKinds() throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        return ResponseEntity.ok(new AvailProgramKindListRs(
                availProgramKindService.getAvailProgramKind(principalData,
                        employeeFilterService.getOfficeIdsByNames(principalData.getOffices()))
                        .stream().map(AvailProgramConverter::convert)
                        .collect(Collectors.toList()))
        );
    }

    @Override
    @HasRight(Right.SET_ADMIN_ROLE)
    public ResponseEntity<Void> updateAvailProgramKind(@PathVariable("id") Long id,
                                                       @Valid @RequestBody AvailProgramKindData updateProgramRequest) throws Exception {
        availProgramKindService.save(AvailProgramConverter.convert(updateProgramRequest));
        return ResponseEntity.ok().build();
    }
}
