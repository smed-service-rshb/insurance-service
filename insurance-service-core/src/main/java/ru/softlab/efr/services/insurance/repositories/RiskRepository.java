package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.Risk;

import javax.persistence.QueryHint;

/**
 * Репозиторий для работы с сущностью справочника рисков.
 *
 * @author Kalantaev
 * @since 02.10.2018
 */
public interface RiskRepository extends RevisionRepository<Risk, Long, Integer>, JpaRepository<Risk, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Risk findRiskByIdAndDeleted(Long id, boolean deleted);

    @Query("from Risk d where d.deleted = :deleted")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Risk> findAll(Pageable pageable, @Param("deleted") boolean deleted);
}
