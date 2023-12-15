package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.softlab.efr.services.insurance.model.db.RequiredField;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью обязательных полей
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface RequiredFieldRepository extends JpaRepository<RequiredField, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<RequiredField> findAllByDeleted(Pageable pageable, Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<RequiredField> findAllByDeleted(Boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RequiredField findFirstByStrId(String strId);

    List<RequiredField>  findAllByParentId(Long parentId);
}
