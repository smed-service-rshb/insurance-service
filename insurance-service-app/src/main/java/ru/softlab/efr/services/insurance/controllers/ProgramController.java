package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.ProgramConverter;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.rest.CreateProgramResponse;
import ru.softlab.efr.services.insurance.model.rest.FilterProgramsRq;
import ru.softlab.efr.services.insurance.model.rest.ProgramData;
import ru.softlab.efr.services.insurance.service.EmployeeFilterService;
import ru.softlab.efr.services.insurance.services.ProgramService;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collections;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isAdmin;


/**
 * Контроллер для работы с программами страхования.
 *
 * @author Kalantaev
 * @since 19.09.2018
 */

@RestController
@PropertySource(value = {"classpath:application.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
public class ProgramController implements ProgramApi {

    private static final Logger LOGGER = Logger.getLogger(ProgramController.class);

    private ProgramConverter programConverter;
    private ProgramService programService;
    private PrincipalDataSource principalDataSource;

    @Autowired
    private EmployeeFilterService employeeFilterService;

    @Autowired
    public ProgramController(ProgramConverter programConverter, ProgramService programService, PrincipalDataSource principalDataSource) {
        this.programConverter =programConverter;
        this.programService = programService;
        this.principalDataSource = principalDataSource;
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<Page<ProgramData>> getPrograms(@PageableDefault(value = 50) Pageable pageable,
                                                         @Valid @RequestBody FilterProgramsRq filterData,
                                                         @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) throws Exception {
        Page<Program> programs;
        PrincipalData principalData = principalDataSource.getPrincipalData();

        if (principalData.getRights().contains(Right.EDIT_PRODUCT_SETTINGS)) {
            // Если у пользователя есть право редактировать справочник программ страхования, то ему должны быть
            // доступны все записи в данном справочнике (за исключением удалённых).
            if (hasFilter) {
                programs = programService.findAllExceptDeletedFiltered(pageable, principalData.getGroups(), filterData, isAdmin(principalData));
            } else {
                programs = programService.findAllExceptDeleted(pageable, principalData.getGroups(), isAdmin(principalData));
            }
        } else {
            // Если у пользователя нет прав на редактирование записей справочника программ страхования, то ему
            // должны отображаться только те программы страхования, которые доступны в подразделениях пользователя.
            // Доступность программы страхования пользователю определяется на основе настроек, которые выполняются в
            // программе страхования. В частности для программы страхования может быть определён список подразделений
            // организации, где данная программа действует или наоборот не действует.
            if (hasFilter) {
                programs = programService.findAllExceptDeletedFiltered(employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                        principalData.getGroups(), filterData, isAdmin(principalData), pageable);
            } else {
                programs = programService.findAllExceptDeleted(employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                        principalData.getGroups(), isAdmin(principalData), pageable);
            }
        }

        return ResponseEntity.ok(programs.map(programConverter::convert));
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<ProgramData> getProgramById(@PathVariable Long id) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        Program program = programService.findProgramById(id, filter.canViewAllContract(),
                filter.isAdmin(),
                filter.getEmployeeOfficesFilter(),
                filter.getEmployeeIdFilter(),
                filter.getEmployeeGroupFilter(),
                employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                false);

        if (program == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(programConverter.convert(program));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<CreateProgramResponse> createProgram(@Valid @RequestBody ProgramData createProgramRequest) throws Exception {
        int count = programService.countExistedProgramStatusesByKeyCriterias(
                createProgramRequest.getProgramCode(),
                createProgramRequest.getProgramTariff(),
                true);
        if (count == 0) {
            Program program = programService.create(programConverter.convert(createProgramRequest));
            return ResponseEntity.ok(new CreateProgramResponse(program.getId()));
        } else {
            String errorMessage = "Выполнялась попытка добавления программы страхования с кодировкой программы " + createProgramRequest.getProgramCode()
                    + " и тарифом " + createProgramRequest.getProgramTariff() + ", которые уже связаны с существующей программой страхования.";
            LOGGER.warn(errorMessage);
            throw new ValidationException(Collections.singletonList(errorMessage));
        }
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> updateProgram(@PathVariable Long id, @Valid @RequestBody ProgramData updateProgramRequest) throws Exception {
        Program program;
        try {
            program = programService.findById(id);
        } catch (EntityNotFoundException ex) {
            LOGGER.warn("Программа страхования не найдена по идентификатору " + id);
            return ResponseEntity.notFound().build();
        }

        programConverter.fill(program, updateProgramRequest);

        programService.save(program);

        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        programService.logicalDelete(id);
        return ResponseEntity.ok().build();
    }
}
