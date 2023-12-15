package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.StatementEntity;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Long> {

    StatementEntity getByIdAndClient(Long statementId, ClientEntity client);

    StatementEntity getById(Long statementId);

    List<StatementEntity> getByInsuranceAndClientOrderById(Insurance insurance, ClientEntity client);

    List<StatementEntity> getByInsuranceOrderById(Insurance insurance);

}
