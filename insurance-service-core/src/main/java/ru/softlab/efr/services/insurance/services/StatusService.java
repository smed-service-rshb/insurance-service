package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.pojo.Result;
import ru.softlab.efr.services.insurance.pojo.StatusModel;
import ru.softlab.efr.services.insurance.pojo.Transition;
import ru.softlab.efr.services.insurance.repositories.StatusRepository;

import javax.transaction.NotSupportedException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы со статусами программ страхования
 *
 * @author krivenko
 * @since 24.10.2018
 */
@Service
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class StatusService {

    @Value("${status.not.available.transition}")
    private String notAvailableTransition;

    @Value("${status.not.have.right}")
    private String notHaveRightError;

    private StatusRepository repository;
    private StatusModelBuilder statusModelBuilder;

    @Autowired
    public StatusService(StatusRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setStatusModelBuilder(StatusModelBuilder statusModelBuilder) {
        this.statusModelBuilder = statusModelBuilder;
    }

    public InsuranceStatus getById(Long id) {
        return repository.findOne(id);
    }

    public InsuranceStatus getByCode(InsuranceStatusCode code) {
        return repository.findByCode(code);
    }

    public InsuranceStatus getByName(String name) {
        return repository.findByName(name);
    }

    public Set<InsuranceStatusCode> getAvailableStatuses(Insurance insurance, PrincipalData principalData) {
        if (!insurance.getProgramSetting().getProgram().getType().equals(ProgramKind.SMS)) {
            if (insurance.getStatus() == null || insurance.getStatus().getCode() == null)
                return Collections.singleton(InsuranceStatusCode.DRAFT);
        }
        StatusModel statusModel = statusModelBuilder.getModelByProgramKind(insurance.getProgramSetting().getProgram().getType());
        InsuranceStatusCode currentStatus = insurance.getStatus().getCode();
        Set<Transition> transitions = statusModel.getTransitions(currentStatus);
        if (CollectionUtils.isEmpty(transitions)) {
            return new HashSet<>();
        }
        transitions = transitions.stream().filter(t -> isTransitionAvailable(insurance, principalData, t).isSuccess()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(transitions)) {
            return new HashSet<>();
        }
        return transitions.stream().map(t -> t.getTargetStatus()).collect(Collectors.toSet());
    }


    public Result isTransitionAvailable(Insurance insurance, PrincipalData principalData, InsuranceStatusCode targetStatus) {
        StatusModel statusModel = statusModelBuilder.getModelByProgramKind(insurance.getProgramSetting().getProgram().getType());
        InsuranceStatusCode statusFrom = insurance.getStatus() != null ? insurance.getStatus().getCode() : null;
        Transition transition = statusModel.getTransition(statusFrom, targetStatus);
        if (transition == null) {
            return Result.fail("Переход в статус %s не возможен", targetStatus.getNameStatus());
        }
        return isTransitionAvailable(insurance, principalData, transition);
    }

    public Result isTransitionAvailable(Insurance insurance, PrincipalData principalData, Transition transition) {
        //Проверяем наличие прав
        if (!transition.availableForRights(principalData.getRights())) {
            return Result.fail("Переход в статус %s не возможен т.к. прав не достаточно", transition.getTargetStatusName());
        }
        //Запускаем функцию, которая проверяет возможность перехода
        return transition.checkTransitionPossibility(insurance);
    }
}
