package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;

import java.util.List;
import java.util.Set;

/**
 * Репозиторий для работы с сокращённой информацией о клиенте.
 *
 * @author Andrey Grigorov
 */
public interface ClientShortDataRepository extends JpaRepository<ClientShortData, Long> {

    @Query("select c from ClientShortData as c " +
            "where id in :ids " +
            "order by c.id")
    List<ClientShortData> findByIds(@Param("ids") Set<Long> ids);
}