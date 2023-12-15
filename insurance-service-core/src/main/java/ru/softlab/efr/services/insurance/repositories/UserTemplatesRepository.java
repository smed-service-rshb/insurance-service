package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.UserTemplateEntity;

import javax.persistence.QueryHint;

/**
 * Репозиторий для работы с сущностью пользовательских шаблонов документов
 *
 * @author olshansky
 * @since 10.04.2019
 */
public interface UserTemplatesRepository extends JpaRepository<UserTemplateEntity, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<UserTemplateEntity> findAll(Pageable pageable);

}