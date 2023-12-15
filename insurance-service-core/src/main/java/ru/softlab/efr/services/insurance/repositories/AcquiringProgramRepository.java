package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с программами для покупки в ЛК.
 */
public interface AcquiringProgramRepository extends JpaRepository<AcquiringProgram, Long> {

    AcquiringProgram findById(Long id);

    @Query("from AcquiringProgram where startDate <= :date and endDate >= :date and notAuthorizedZoneEnable = true")
    List<AcquiringProgram> findAllAllowedProgram(@Param("date") LocalDate date);

    @Query("select ap from AcquiringProgram as ap join ap.program as ps where ap.startDate <= :date and ap.endDate >= :date " +
            "and ap.authorizedZoneEnable = true " +
            "and ((ps.minAgeInsured <= :clientAge and ps.maxAgeInsured >= :clientAge) or (:clientAge is null or ps.minAgeInsured is null or ps.maxAgeInsured is null)) " +
            "and ((ps.minAgeHolder <= :clientAge and ps.maxAgeHolder >= :clientAge) or (:clientAge is null or ps.minAgeHolder is null or ps.maxAgeHolder is null))")
    List<AcquiringProgram> findAllAllowedProgram(@Param("clientAge") Integer clientAge, @Param("date") LocalDate date);

    @Query("select ap from AcquiringProgram as ap where ap.program = :programSetting and ap.startDate <= :date and ap.endDate >= :date " +
            "and ((:isAuthorized is true and  ap.authorizedZoneEnable = true) or (:isAuthorized is false and  ap.notAuthorizedZoneEnable = true))")
    List<AcquiringProgram> findByProgram(@Param("programSetting") ProgramSetting programSetting,
                                         @Param("date") LocalDate date,
                                         @Param("isAuthorized") Boolean isAuthorized);
}
