package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.db.AvailProgramKindEntity;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.repositories.AvailProgramKindRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isAdmin;


/**
 * Сервис для работы со справочником доступности видов программ страхования.
 *
 * @author olshansky
 * @since 27.03.2019
 */
@Service
public class AvailProgramKindService {

    private AvailProgramKindRepository repository;
    private ProgramService programService;

    private Sort SORT_BY_ID = new Sort(Sort.Direction.ASC, "id");

    @Autowired
    public AvailProgramKindService(AvailProgramKindRepository repository, ProgramService programService) {
        this.repository = repository;
        this.programService = programService;
    }

    public AvailProgramKindEntity findOne(Long id) {
        return repository.findOne(id);
    }

    public Page<AvailProgramKindEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private List<AvailProgramKindEntity> getActiveProgramKinds() {
        return repository.findAllByIsActive(true, SORT_BY_ID);
    }

    public AvailProgramKindEntity save(AvailProgramKindEntity entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (repository.findOne(id) == null) {
            throw new EntityNotFoundException();
        }
        repository.delete(id);
    }

    public List<AvailProgramKindEntity> getAvailProgramKind(PrincipalData principalData, Set<Long> offices) {
        PageRequest pageable = new PageRequest(0, 1000);

        Page<Program> foundPrograms;

        if (principalData.getRights().contains(Right.EDIT_PRODUCT_SETTINGS)) {
            foundPrograms = programService.findAllExceptDeleted(pageable, principalData.getGroups(), isAdmin(principalData));
        } else {
            foundPrograms = programService.findAllExceptDeleted(offices,
                    principalData.getGroups(), isAdmin(principalData), pageable);
        }

        return getActiveProgramKinds().stream()
                .filter(availProgramKind -> foundPrograms.getContent().stream()
                        .anyMatch(program -> program.getType().name().equalsIgnoreCase(availProgramKind.getProgramKind())))
                .collect(Collectors.toList());
    }
}
