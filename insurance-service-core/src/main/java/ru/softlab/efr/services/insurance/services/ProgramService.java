package ru.softlab.efr.services.insurance.services;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.rest.FilterProgramsRq;
import ru.softlab.efr.services.insurance.repositories.ProgramRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.softlab.efr.services.insurance.services.InsuranceService.UNEXIST_USER_GROUP;


/**
 * Сервис для работы с программами страхования.
 *
 * @author Kalantaev
 * @since 19.09.2018
 */
@Service
public class ProgramService extends BaseService<Program> {

    @Autowired
    public ProgramService(ProgramRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Program findProgramById(long id, boolean viewAllContracts, boolean isAdmin, Set<Long> officesId, Long employeeId, List<String> groups, Set<Long> offices, boolean deleted) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }

        if (CollectionUtils.isEmpty(officesId)) {
            return ((ProgramRepository) repository).findProgramByIdAndDeleted(id, viewAllContracts, isAdmin, employeeId, groups, offices, deleted);
        }

        return ((ProgramRepository) repository).findProgramByIdAndDeleted(id, viewAllContracts, isAdmin, officesId, employeeId, groups, offices, deleted);
    }

    @Transactional(readOnly = true)
    public Page<Program> findAllExceptDeleted(Pageable pageable, List<String> groups, boolean isAdmin) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }
        return ((ProgramRepository) repository).findAllByDeleted(false, groups, isAdmin, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Program> findAllExceptDeleted(Set<Long> userOffices, List<String> groups, boolean isAdmin, Pageable pageable) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }
        return ((ProgramRepository) repository).findAllByDeleted(userOffices, groups, false, isAdmin, pageable);
    }

    @Transactional
    public void logicalDelete(Long id) {
        Program program = repository.findOne(id);
        program.setActive(false);
        program.setDeleted(true);
        repository.saveAndFlush(program);
    }

    @Transactional(readOnly = true)
    public int countExistedProgramStatusesByKeyCriterias(String programCode, String programTariff, boolean isActive) {
        return ((ProgramRepository) repository).countByProgramCodeAndProgramTariffAndIsActive(programCode, programTariff, isActive);
    }

    @Transactional(readOnly = true)
    public Page<Program> findAllExceptDeletedFiltered(Pageable pageable, List<String> groups, FilterProgramsRq filterData, boolean admin) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }

        if (Objects.nonNull(filterData.getKind())) {
            return ((ProgramRepository) repository).findAllByDeleted(false, groups, admin, pageable,
                    filterData.getKind(), filterData.getPolicyCode(), filterData.getProgramVariant(), filterData.getProgramName());
        } else {
            return ((ProgramRepository) repository).findAllByDeleted(false, groups, admin, pageable,
                    filterData.getPolicyCode(), filterData.getProgramVariant(), filterData.getProgramName());
        }
    }

    @Transactional(readOnly = true)
    public Page<Program> findAllExceptDeletedFiltered(Set<Long> userOffices, List<String> groups, FilterProgramsRq filterData, boolean admin, Pageable pageable) {
        if (CollectionUtils.isEmpty(groups)) {
            groups = UNEXIST_USER_GROUP;
        }

        if (Objects.nonNull(filterData.getKind())) {
            return ((ProgramRepository) repository).findAllByDeleted(userOffices, groups, false, admin, pageable,
                    filterData.getKind(), filterData.getPolicyCode(), filterData.getProgramVariant(), filterData.getProgramName());
        } else {
            return ((ProgramRepository) repository).findAllByDeleted(userOffices, groups, false, admin, pageable,
                    filterData.getPolicyCode(), filterData.getProgramVariant(), filterData.getProgramName());
        }
    }
}
