package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.Segment;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью сегментов программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Segment> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Segment> findAllByDeleted(Boolean deleted);
}