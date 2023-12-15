package ru.softlab.efr.services.insurance.services;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.CalculationContractTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.FilterProgramSettingsRq;
import ru.softlab.efr.services.insurance.repositories.ProgramSettingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.softlab.efr.services.insurance.services.InsuranceService.UNEXIST_USER_GROUP;

/**
 * Сервис для работы с параметрами программами страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class ProgramSettingService extends BaseService<ProgramSetting> {

    private Sort PROGRAM_SETTING_SORT_BY_ID = new Sort(Sort.Direction.DESC, "id");

    @Autowired
    public ProgramSettingService(ProgramSettingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ProgramSetting findProgramSettingById(long id, boolean viewAllContracts, boolean isAdmin, Set<Long> officesId, Long employeeId, List<String> groups, Set<Long> offices, boolean deleted) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }

        if (CollectionUtils.isEmpty(officesId)) {
            return ((ProgramSettingRepository) repository).findProgramSettingById(id, viewAllContracts, isAdmin, employeeId, groups, offices, deleted);
        }

        return ((ProgramSettingRepository) repository).findProgramSettingById(id, viewAllContracts, isAdmin, officesId, employeeId, groups, offices, deleted);
    }
    public Page<ProgramSetting> findAllExceptDeleted(Pageable pageable, List<String> employeeGroups, boolean isAdmin,
                                                     ProgramKind kind, String programName, Long strategyId, Boolean hasFilter) {
        if (CollectionUtils.isEmpty(employeeGroups)) {
            employeeGroups = UNEXIST_USER_GROUP;
        }
        hasFilter = Optional.ofNullable(hasFilter).orElse(false);
        if (hasFilter) {
            return ((ProgramSettingRepository) repository).findAllByDeletedFiltered(pageable, false, employeeGroups, isAdmin,
                    kind, programName, strategyId);
        } else {
            return ((ProgramSettingRepository) repository).findAllByDeleted(pageable, false, employeeGroups, isAdmin);
        }
    }

    public List<ProgramSetting> findAllExceptDeleted() {
        return ((ProgramSettingRepository) repository).findAllByDeleted(false);
    }

    public List<ProgramSetting> findByCriteria(ProgramKind programKind, Long program, Long currency, BigDecimal amount,
                                               Integer term, BigDecimal premium, CalculationContractTypeEnum type,
                                               CalendarUnitEnum calendarUnit, Integer insuredAge, Integer holderAge,
                                               PeriodicityEnum periodicity, Long strategy, LocalDate programDate) {
        return ((ProgramSettingRepository) repository).findByCriteria(
                programKind, program, currency, amount, term, premium, (type != null) ? type.name() : null, calendarUnit,
                insuredAge, holderAge, periodicity, strategy, programDate == null ?
                        Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()) :
                        Date.from(programDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                PROGRAM_SETTING_SORT_BY_ID
        );
    }
}
