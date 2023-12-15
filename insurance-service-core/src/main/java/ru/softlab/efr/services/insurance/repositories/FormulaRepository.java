package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.Formula;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью формул расчёта
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface FormulaRepository extends JpaRepository<Formula, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Formula> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Formula> findAllByDeleted(Boolean deleted);
}