package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.PersonDecisionEntity;
import ru.softlab.efr.services.insurance.repositories.PersonDecisionRepository;

import java.util.List;

/**
 * Сервис предоставляющий методы для работы с таблицей person_decisions
 * @author basharin
 * @since 12.02.2018
 */

@Service
public class PersonDecisionService {

    private PersonDecisionRepository personDecisionRepository;

    @Autowired
    public void setPersonDecisionRepository(PersonDecisionRepository personDecisionRepository) {
        this.personDecisionRepository = personDecisionRepository;
    }


    /**
     * Получение решения сотрудника по id
     * @param id идентификатор решения
     * @return решение сотрудника
     */
    public PersonDecisionEntity get(Long id) {
        return personDecisionRepository.findOne(id);
    }

    /**
     * Сохранение решения сотрудника
     * @param personDecision решение
     * @return сохраненное решение
     */
    public PersonDecisionEntity save(PersonDecisionEntity personDecision) {
        return personDecisionRepository.save(personDecision);
    }

    /**
     * Сохранение списка решений сотрудника
     * @param personDecisions список решений
     * @return сохраненные решения
     */
    public List<PersonDecisionEntity> save(List<PersonDecisionEntity> personDecisions) {
        return personDecisionRepository.save(personDecisions);
    }

}
