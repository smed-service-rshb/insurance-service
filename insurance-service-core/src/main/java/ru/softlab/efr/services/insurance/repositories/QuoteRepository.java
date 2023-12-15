package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.Quote;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("select q from Quote as q join q.share as sh join sh.strategies as str where q.date between :startDate and :endDate and str.id = :strategyId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Quote> findAllByStrategyIdAndDateBetween(@Param("strategyId") Long strategyId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate, Sort sort);
}
