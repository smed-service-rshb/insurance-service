package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface RequestsRepository extends JpaRepository<RequestEntity, Long> {

    RequestEntity getById(Long id);

    @Query("select re from RequestEntity as re join re.client as c join c.phones as ph" +
            " where re.id = :id " +
            "and c.surName = :surName " +
            "and c.firstName = :firstName " +
            "and c.birthDate = :birthDate " +
            "and ph.number = :mobilePhone " +
            "and ph.main = 'true'")
    RequestEntity getByIdAndClientData(@Param("id") Long id,
                                       @Param("firstName") String firstName,
                                       @Param("surName") String surName,
                                       @Param("birthDate") LocalDate birthDate,
                                       @Param("mobilePhone") String mobilePhone);

    @Query("select re from RequestEntity as re join re.client as c join c.phones as ph" +
            " where c.surName = :surName" +
            " and c.firstName = :firstName" +
            " and c.birthDate = :birthDate " +
            "and ph.number = :mobilePhone " +
            "and ph.main = 'true'")
    Page<RequestEntity> getClientRequest(@Param("firstName") String firstName,
                                         @Param("surName") String surName,
                                         @Param("birthDate") LocalDate birthDate,
                                         @Param("mobilePhone") String mobilePhone, Pageable pageable);

    @Query("select re from RequestEntity as re join re.client as c join re.topic as t where " +
            "(:id is null or re.id = :id) and " +
            "(:clientSurname is null or :clientSurname = lower(c.surName)) and " +
            "(:clientName is null or :clientName = lower(c.firstName)) and " +
            "(:toticId is null or :toticId = t.id) and " +
            "(:clientId is null or :clientId = c.id) and " +
            "(:status is null or :status = re.status) and " +
            "(cast(:startDate as timestamp) is null or :startDate <= re.requestDate) and" +
            "(cast(:endDate as timestamp) is null or :endDate >= re.requestDate)")
    Page<RequestEntity> getRequestEntityList(@Param("id") Long id, @Param("toticId") Long topicId,
                                             @Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                             @Param("clientSurname") String clientSurname,
                                             @Param("clientName") String clientName, @Param("status") RequestStatus status,
                                             @Param("clientId") Long clientId,
                                             Pageable pageable);

}
