package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ChangeReportItem;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Репозиторий для работы с сущностью отчёта по изменениям
 *
 * @author olshansky
 * @since 01.02.2019
 */
public interface ChangesReportRepository extends JpaRepository<ChangeReportItem, Long> {

    Stream<ChangeReportItem> findByChangeDateBetween(@Param("createDate") LocalDateTime createDate, @Param("endDate") LocalDateTime endDate, Sort sort);
}