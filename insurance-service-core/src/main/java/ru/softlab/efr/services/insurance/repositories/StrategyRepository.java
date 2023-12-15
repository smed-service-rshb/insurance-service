package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью стратегий программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    Page<Strategy> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Strategy> findAllByDeleted(Boolean deleted);

    @Query("from Strategy where deleted = :deleted and strategyType = :type")
    List<Strategy> findByTypeAndDeleted(@Param("type") StrategyType type, @Param("deleted") Boolean deleted);

    @Query("from Strategy where deleted = :deleted and strategyType in (:types)")
    List<Strategy> findByTypesAndDeleted(@Param("types") List<StrategyType> types, @Param("deleted") Boolean deleted);
}