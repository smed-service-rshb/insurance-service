package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;

import java.util.List;

/**
 * Репозиторий для управления сущностью телефонов и ее исторических данных
 */
public interface PhoneForClientDataRepository extends CrudRepository<PhoneForClaimEntity, Long>,
        RevisionRepository<PhoneForClaimEntity, Long, Integer> {

    @Query(value = "SELECT doc.id,doc.rev from {h-schema}phones_for_client_aud as doc where doc.client_id=:clientId",
            nativeQuery = true)
    <T> List<T> findAllByClientId(@Param("clientId") Long clientId, Class<T> type);
}
