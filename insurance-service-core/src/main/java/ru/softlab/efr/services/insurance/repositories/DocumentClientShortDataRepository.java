package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientShortData;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.stream.Stream;

/**
 * Репозитрий для работы со списком ДУЛов клиента.
 *
 * @author Andrey Grigorov
 */
public interface DocumentClientShortDataRepository extends Repository<DocumentForClientShortData, Long> {

    /**
     * Получение списка паспортов РФ и связанных с ним клиентов, который должен использоваться
     * для проверки по справочнику недействительных паспортов.
     *
     * @return Список паспортов РФ и связанных с ним клиентов.
     */
    @Query("select d from DocumentForClientShortData as d " +
            "join d.client c " +
            "where d.docType = 'PASSPORT_RF' " +
            "order by c.id")
    @EntityGraph(attributePaths = "client", type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<DocumentForClientShortData> findAllRFPasswordForCheck();

    /**
     * Получение списка паспортов РФ и связанных с ним клиентов, который должен использоваться
     * для проверки по справочнику недействительных паспортов.
     *
     * @param clientIds Список идентификаторов клиентов, для которых необходимо возвратить список паспортов РФ.
     * @return Список паспортов РФ и связанных с ним клиентов.
     */
    @Query("select d from DocumentForClientShortData as d " +
            "join d.client c " +
            "where (d.docType = 'PASSPORT_RF' and c.id in :clientIds) " +
            "order by c.id")
    @EntityGraph(attributePaths = "client", type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<DocumentForClientShortData> findRFPasswordForCheckByClientIds(@Param("clientIds") List<Long> clientIds);
}