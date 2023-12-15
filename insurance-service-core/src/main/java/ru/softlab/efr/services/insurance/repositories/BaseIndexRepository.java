package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.BaseIndex;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;

public interface BaseIndexRepository extends JpaRepository<BaseIndex, Long> {

    @Query("select i from BaseIndex as i left join i.strategies as s where s.id = :strategyId and i.date between :startDate and :endDate")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<BaseIndex> findAllByDateBetweenAndStrategy(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("strategyId") Long strategyId,
                                                    Sort sort);
}
