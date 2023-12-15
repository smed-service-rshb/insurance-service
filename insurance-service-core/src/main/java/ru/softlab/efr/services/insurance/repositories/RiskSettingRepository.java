package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью параметров риска
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface RiskSettingRepository extends JpaRepository<RiskSetting, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<RiskSetting> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<RiskSetting> findAllByDeleted(Boolean deleted);
}