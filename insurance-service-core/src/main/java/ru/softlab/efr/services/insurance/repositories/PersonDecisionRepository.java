package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.PersonDecisionEntity;

import java.util.List;

/**
 * Интерфейс, описывающий CRUD-методы для взаимодествия с таблицей решений сотрудника
 * @author basharin
 * @since 09.02.2018
 */
public interface PersonDecisionRepository extends JpaRepository<PersonDecisionEntity, Long> {

}
