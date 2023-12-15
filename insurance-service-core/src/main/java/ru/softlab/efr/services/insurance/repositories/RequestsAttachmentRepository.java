package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.db.RequestsAttachmentEntity;
import ru.softlab.efr.services.insurance.model.enums.AttachmentKindEnum;

import java.util.List;

@Repository
public interface RequestsAttachmentRepository extends JpaRepository<RequestsAttachmentEntity, Long> {

    RequestsAttachmentEntity getById(Long id);

    @Query("select ra from RequestsAttachmentEntity as ra join ra.request as re join ra.attachment as a" +
            " where re.id = :requestId and ra.isDeleted = false and a.kind = :kind")
    List<RequestsAttachmentEntity> getByRequestAndIsDeleted(@Param("requestId") Long requestId,
                                                            @Param("kind") AttachmentKindEnum kind);

    @Query("select ra from RequestsAttachmentEntity as ra join ra.request as re join ra.attachment as a" +
            " where re.id = :requestId and ra.isDeleted = false and a.kind = :kind")
    RequestsAttachmentEntity getByRequestDocument(@Param("requestId") Long requestId,
                                                  @Param("kind") AttachmentKindEnum kind);
}
