package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.RequiredDocumentSetting;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью параметров обязательных документов
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface RequiredDocumentSettingRepository extends JpaRepository<RequiredDocumentSetting, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<RequiredDocumentSetting> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<RequiredDocumentSetting> findAllByDeleted(Boolean deleted);
}