package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.AvailProgramKindEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностью доступности видов программ страхования.
 *
 * @author olshansky
 * @since 27.03.2019
 */
public interface AvailProgramKindRepository extends JpaRepository<AvailProgramKindEntity, Long> {

    List<AvailProgramKindEntity> findAllByIsActive(Boolean isActive, Sort sort);

}