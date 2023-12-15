package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;

/**
 * Репозиторий для работы с сущностью статусов документов
 *
 * @author krivenko
 * @since 14.10.2018
 */
public interface StatusRepository extends JpaRepository<InsuranceStatus, Long> {

    InsuranceStatus findByCode(InsuranceStatusCode code);

    InsuranceStatus findByName(String name);
}