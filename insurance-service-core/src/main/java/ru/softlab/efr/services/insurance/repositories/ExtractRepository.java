package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Date;
import java.util.concurrent.Future;

public interface ExtractRepository extends JpaRepository<Extract, String> {

    @Modifying
    @Query(value = "update {h-schema}extract set content = :content where uuid = :uuid", nativeQuery = true)
    void setContent(@Param("uuid") String uuid, @Param("content") byte[] content);

    @Query(value = "SELECT content from {h-schema}extract where uuid=:uuid", nativeQuery = true)
    byte[] getContent(@Param("uuid") String uuid);

    Extract findTopByRequestDigestAndStatus(String requestDigest, ExtractStatus status);

    Extract findByUuid(String uuid);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "from Extract where uuid = :uuid")
    Extract findByUuidWithWriteLock(@Param("uuid") String uuid);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "0")})
    @Query(value = "from Extract where uuid = :uuid")
    @Transactional
    @Async("pessimisticLockTaskExecutor")
    Future<Extract> getLockExtract(@Param("uuid") String uuid);

    @Modifying
    @Query("delete from Extract where createDate <= :date")
    void deleteByDate(@Param("date") Date date);
}
