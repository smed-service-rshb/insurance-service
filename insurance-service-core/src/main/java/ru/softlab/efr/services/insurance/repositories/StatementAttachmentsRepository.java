package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.StatementAttachmentsEntity;
import ru.softlab.efr.services.insurance.model.db.StatementEntity;

import java.util.List;

@Repository
public interface StatementAttachmentsRepository extends JpaRepository<StatementAttachmentsEntity, Long> {

    List<StatementAttachmentsEntity> getBySatementAndIsDeletedOrderById(StatementEntity statement, boolean deleted);

}
