package ru.softlab.efr.services.insurance.pojo;

import ru.softlab.efr.services.insurance.exception.StatusAlreadyExistsException;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Статусная модель
 */
public class StatusModel {
    private Map<InsuranceStatusCode, Set<Transition>> statusModel = new HashMap<>();

    /**
     * Добавить в модель переход
     * @param statusFrom - статус из которого выполняется переход
     * @param transition - описание перехода
     */
    public void addTransition(InsuranceStatusCode statusFrom, Transition transition) {
        if (statusModel.containsKey(statusFrom)) {
            Set<Transition> transitionSet = statusModel.get(statusFrom);
            if (transitionSet.stream().anyMatch(t -> t.getTargetStatus() == transition.getTargetStatus())) {
                throw new StatusAlreadyExistsException("Статус " + transition.getTargetStatus() + " уже существует в модели для перехода изь статуса " + statusFrom);
            }
            statusModel.get(statusFrom).add(transition);
        } else {
            Set<Transition> transitionSet = new HashSet<>();
            transitionSet.add(transition);
            statusModel.put(statusFrom, transitionSet);
        }
    }

    /**
     * Получить описание перехода из статуса в статус
     * @param statusFrom статус из которого необходимо выполнить переход
     * @param targetStatus статус в который необходимо перейти
     * @return описание перехода
     */
    public Transition getTransition(InsuranceStatusCode statusFrom, InsuranceStatusCode targetStatus) {
        if (!statusModel.containsKey(statusFrom)) return null;
        Set<Transition> transitionSet = statusModel.get(statusFrom);
        return transitionSet.stream().filter(t -> t.getTargetStatus() == targetStatus).findFirst().orElse(null);
    }

    public void deleteTransition(InsuranceStatusCode statusFrom, InsuranceStatusCode deletedStatus) {
        if (!statusModel.containsKey(statusFrom)) return;
        Set<Transition> transitionSet = statusModel.get(statusFrom);
        transitionSet.remove(transitionSet.stream().filter(t -> t.getTargetStatus() == deletedStatus).findFirst().orElse(null));
    }

    public Set<Transition> getTransitions(InsuranceStatusCode statusFrom) {
        return statusModel.get(statusFrom);
    }
}
