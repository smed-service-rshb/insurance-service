package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
import ru.softlab.efr.services.insurance.converter.ProgramSettingConverter;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.service.EmployeeFilterService;
import ru.softlab.efr.services.insurance.services.ProgramSettingService;
import ru.softlab.efr.services.insurance.utils.AppUtils;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isAdmin;
import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isProgramAllowable;


/**
 * Контроллер для работы с параметрами программ страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class ProgramSettingController implements ProgramSettingApi {

    @Autowired
    private ProgramSettingService programSettingService;

    @Autowired
    private ProgramSettingConverter programSettingConverter;

    @Autowired
    private PrincipalDataSource principalDataSource;

    @Autowired
    private EmployeeFilterService employeeFilterService;

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<Page<ProgramSettingDataForList>> getProgramSettingList(@PageableDefault(value = 50) Pageable pageable,
                                                                                 @Valid @RequestBody FilterProgramSettingsRq filterData,
                                                                                 @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) throws Exception {
        List<String> groups = CollectionUtils.isEmpty(principalDataSource.getPrincipalData().getGroups()) ? new ArrayList<>() :
                principalDataSource.getPrincipalData().getGroups();
        Page<ProgramSetting> programSettings = programSettingService.findAllExceptDeleted(
                pageable,
                groups,
                isAdmin(principalDataSource.getPrincipalData()),
                filterData.getKind()!= null ? ProgramKind.valueOf(filterData.getKind().toString()): null,
                filterData.getProgramName(),
                filterData.getStrategyId(),
                hasFilter);
        return ResponseEntity.ok(programSettings.map(programSettingConverter::convert));
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<ProgramSettingData> getProgramSettingById(@PathVariable Long id) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        EmployeeFilter filter = employeeFilterService.getFilterForPrincipal(principalData);
        ProgramSetting programSetting = programSettingService.findProgramSettingById(id,
                filter.canViewAllContract(),
                filter.isAdmin(),
                filter.getEmployeeOfficesFilter(),
                filter.getEmployeeIdFilter(),
                filter.getEmployeeGroupFilter(),
                employeeFilterService.getOfficeIdsByNames(principalData.getOffices()),
                false);

        if (programSetting == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(programSettingConverter.convertDetail(programSetting));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> updateProgramSetting(@PathVariable Long id, @RequestBody ProgramSettingData updateProgramRequest) throws Exception {
        ProgramSetting programSetting = programSettingService.findById(id);
        programSettingConverter.convertDetail(updateProgramRequest, programSetting);
        programSettingService.save(programSetting);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<CreateProgramSettingRs> createProgramSetting(@RequestBody ProgramSettingData createProgramSettingRq) throws Exception {
        ProgramSetting programSetting = programSettingService.create(programSettingConverter.convertDetail(createProgramSettingRq));
        return ResponseEntity.ok(new CreateProgramSettingRs(programSetting.getId()));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> deleteProgramSetting(@PathVariable Long id) throws Exception {
        programSettingService.logicalDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<FindProgramSettingRs> findProgramSetting(@Valid @RequestBody FindProgramSettingRq findProgramSettingRq) throws Exception {
        List<ProgramSetting> backProgramSettings = programSettingService.findByCriteria(
                findProgramSettingRq.getProgramKind() != null ?
                        ProgramKind.valueOf(findProgramSettingRq.getProgramKind().name()) : null,

                findProgramSettingRq.getProgram(),
                findProgramSettingRq.getCurrency(),

                findProgramSettingRq.getType() != null && FindProgramType.SUM.equals(findProgramSettingRq.getType()) ?
                        findProgramSettingRq.getAmount() : null,

                findProgramSettingRq.getTerm(),

                findProgramSettingRq.getType() != null && FindProgramType.PREMIUM.equals(findProgramSettingRq.getType()) ?
                        findProgramSettingRq.getAmount() : null,

                findProgramSettingRq.getType() != null ?
                        CalculationContractTypeEnum.valueOf(findProgramSettingRq.getType().name()) : null,

                findProgramSettingRq.getCalendarUnit() != null ?
                        CalendarUnitEnum.valueOf(findProgramSettingRq.getCalendarUnit().name()) : null,

                AppUtils.getAgeByToday(findProgramSettingRq.getInsuredBirthDate()),
                AppUtils.getAgeByToday(findProgramSettingRq.getPolicyHolderBirthDate()),

                findProgramSettingRq.getPeriodicity() != null && FindProgramType.PREMIUM.equals(findProgramSettingRq.getType()) ?
                        PeriodicityEnum.valueOf(findProgramSettingRq.getPeriodicity().name()) : null,
                findProgramSettingRq.getStrategy(),

                findProgramSettingRq.getProgramDate()
        );

        // При формирования итогового списка необходимо выполнять фильтрацию найденных параметров программ страхования
        // в соответствии с настройками привязки программ страхования к подразделениям организации.
        // Фильтрация производится на основе текущего (выбранного при авторизации) подразделения пользователя.
        PrincipalData principalData = principalDataSource.getPrincipalData();
        List<FindProgramSettingResult> programs = backProgramSettings
                .stream()
                .filter(programSetting -> isProgramAllowable(programSetting.getProgram(), principalData))
                //если при подборе не указан срок, считаем, что срок страхования равен минимальному сроку страхования в настройки программы
                .filter(programSetting -> findProgramSettingRq.getTerm() != null || programSetting.getMinimumTerm() != null)
                //если при подборе не указана сумма или премия, отображаем только программы с фиксированным значением суммы и премии
                .filter(programSetting -> findProgramSettingRq.getAmount() != null || PremiumMethodEnum.FIXED == programSetting.getPremiumMethod())
                .map(m -> programSettingConverter.convertFindProgram(m, findProgramSettingRq))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new FindProgramSettingRs(programs));
    }


}
