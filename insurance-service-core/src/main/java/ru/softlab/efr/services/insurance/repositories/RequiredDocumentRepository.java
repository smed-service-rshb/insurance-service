package ru.softlab.efr.services.insurance.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.RequiredDocument;

import javax.persistence.QueryHint;

/**
 * Репозиторий для работы с сущностью справочника обязательных документов.
 *
 * @author Kalantaev
 * @since 14.09.2018
 */
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RequiredDocument findRequiredDocumentByIdAndDeleted(Long id, boolean deleted);


    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RequiredDocument findRequiredDocumentByTypeAndDeleted(String type, boolean deleted);


    @Query("from RequiredDocument d where d.deleted = :deleted")
    Page<RequiredDocument> findAll(Pageable pageable, @Param("deleted") boolean deleted);
}
