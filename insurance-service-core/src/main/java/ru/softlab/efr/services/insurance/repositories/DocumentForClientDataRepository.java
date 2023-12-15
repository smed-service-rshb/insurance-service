package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientEntity;

import java.util.List;

/**
 * Репозиторий для управления сущностью документов и ее исторических данных
 */
public interface DocumentForClientDataRepository extends CrudRepository<DocumentForClientEntity, Long>, RevisionRepository<DocumentForClientEntity, Long, Integer> {

    @Query(value = "SELECT doc.id,doc.rev from {h-schema}documents_for_client_aud as doc where doc.client_id=:clientId",
            nativeQuery = true)
    <T> List<T> findAllByClientId(@Param("clientId") Long clientId, Class<T> type);
}
