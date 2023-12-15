package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatusHistory;

import java.util.List;

/**
 * Репозиторий для работы с сущностью истории статусов документов
 *
 * @author krivenko
 * @since 14.10.2018
 */
public interface StatusHistoryRepository extends JpaRepository<InsuranceStatusHistory, Long> {

    InsuranceStatusHistory findTopByInsurance(Insurance insurance);

    List<InsuranceStatusHistory> findFirst2ByInsuranceOrderByIdDesc(Insurance insurance);


    Integer countAllByInsuranceAndStatusEquals(Insurance insurance, InsuranceStatus insuranceStatus);
}