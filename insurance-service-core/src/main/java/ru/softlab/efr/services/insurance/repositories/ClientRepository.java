package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * Интерфейс, описывающий CRUD-методы для взаимодествия с таблицей клиента
 *
 * @author basharin
 * @since 02.02.2018
 */
public interface ClientRepository extends CrudRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity>,
        RevisionRepository<ClientEntity, Long, Integer> {

    @Query("select new ru.softlab.efr.services.insurance.repositories.ClientForCheckSummary(" +
            "c.id," +
            "c.surName," +
            "c.firstName," +
            "c.middleName," +
            "c.birthDate," +
            "d.docSeries," +
            "d.docNumber" +
            ") from ClientEntity as c " +
            "left join c.documents d " +
            "order by c.id")
    @QueryHints(@javax.persistence.QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<ClientForCheckSummary> findAllClientsForCheck();

    @Query("select new ru.softlab.efr.services.insurance.repositories.ClientForCheckSummary(" +
            "c.id," +
            "c.surName," +
            "c.firstName," +
            "c.middleName," +
            "c.birthDate," +
            "d.docSeries," +
            "d.docNumber" +
            ") from ClientEntity as c " +
            "left join c.documents d " +
            "where c.id in :clientIds " +
            "order by c.id")
    @QueryHints(@javax.persistence.QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<ClientForCheckSummary> findClientsForCheckByIdIn(@Param("clientIds") List<Long> clientIds);

    Integer countClientEntitiesById(Long id);

    /**
     * Найти клиента
     *
     * @param surName     фамилия
     * @param firstName   имя
     * @param middleName  отчество
     * @param mobilePhone номер телефона
     * @return клиентские данные
     */
    @Query("select c from ClientEntity c " +
            "left join c.phones as p " +
            "where c.firstName = :firstName " +
            "and c.surName = :surName " +
            "and c.surName = :surName " +
            "and c.middleName = :middleName " +
            "and p.number = :mobilePhone " +
            "and p.main = 'true'" +
            "order by c.registrationDate desc ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ClientEntity> findTopByOrderByRegistrationDateDesc(
            @Param("firstName") String firstName,
            @Param("surName") String surName,
            @Param("middleName") String middleName,
            @Param("mobilePhone") String mobilePhone);

    /**
     * Найти клиента
     *
     * @param surName     фамилия
     * @param firstName   имя
     * @param middleName  отчество
     * @param mobilePhone номер телефона
     * @return клиентские данные
     */
    @Query("select c from ClientEntity c " +
            "left join c.phones as p " +
            "where c.firstName = :firstName " +
            "and c.surName = :surName " +
            "and c.surName = :surName " +
            "and c.birthDate = :birthDate " +
            "and c.middleName = :middleName " +
            "and p.number = :mobilePhone " +
            "and p.main = 'true'" +
            "order by c.registrationDate desc ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ClientEntity> findTopByOrderByRegistrationDateDesc(
            @Param("firstName") String firstName,
            @Param("surName") String surName,
            @Param("birthDate") LocalDate birthDate,
            @Param("middleName") String middleName,
            @Param("mobilePhone") String mobilePhone);


    @Query("select distinct i.holder from Insurance i where i.id in :contractIds")
    List<ClientEntity> findAllClientByContractIdIn(@Param("contractIds") List<Long> contractIds);

    ClientEntity getById(Long id);
}