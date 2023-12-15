package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.AddressForClientEntity;

import java.util.List;

public interface AddressesForClientEntityRepository extends JpaRepository<AddressForClientEntity, Long>,
        RevisionRepository<AddressForClientEntity, Long, Integer> {

    @Query(value = "SELECT doc.id,doc.rev from {h-schema}addresses_for_client_aud as doc where doc.client_id=:clientId and doc.address_type='REGISTRATION'",
            nativeQuery = true)
    <T> List<T> findAllByClientId(@Param("clientId") Long clientId, Class<T> type);
}
