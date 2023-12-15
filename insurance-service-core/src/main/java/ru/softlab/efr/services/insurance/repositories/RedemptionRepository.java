package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.RedemptionCoefficientEntity;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;

import javax.persistence.QueryHint;

public interface RedemptionRepository extends JpaRepository<RedemptionEntity, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RedemptionEntity findRedemptionEntitiesByIdAndDeleted(Long id, boolean deleted);

    @Query("from RedemptionEntity d where d.deleted = :deleted")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<RedemptionEntity> findAll(Pageable pageable, @Param("deleted") boolean deleted);

    @Query("from RedemptionCoefficientEntity rc " +
            "join rc.redemption r " +
            "join r.program p " +
            "where p.id = :programId " +
            "and rc.period = :numberYear " +
            "and r.periodicity = :periodicity " +
            "and r.currency = :currency " +
            "and r.duration = :duration " +
            "and r.deleted = false " +
            "and p.deleted = false")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RedemptionCoefficientEntity findRate(@Param("programId") Long programId,
                                         @Param("duration") Integer duration,
                                         @Param("currency") Long currency,
                                         @Param("periodicity") PeriodicityEnum periodicity,
                                         @Param("numberYear") Integer numberYear);

    @Query("from RedemptionEntity r " +
            "join r.program p " +
            "where p.id = :programId " +
            "and r.periodicity = :periodicity " +
            "and r.currency = :currency " +
            "and r.duration = :duration " +
            "and r.deleted = false " +
            "and p.deleted = false")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    RedemptionEntity findRedemption(@Param("programId") Long programId,
                                    @Param("duration") Integer duration,
                                    @Param("currency") Long currency,
                                    @Param("periodicity") PeriodicityEnum periodicity);
}
