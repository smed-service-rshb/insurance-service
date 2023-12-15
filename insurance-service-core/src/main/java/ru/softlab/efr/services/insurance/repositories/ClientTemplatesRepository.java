package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ClientTemplate;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import java.util.List;

/**
 * Репозиторий для работы с справочником шаблонов заявлений и инструкций
 *
 * @author kalantaev
 * @since 12.04.2019
 */
public interface ClientTemplatesRepository extends JpaRepository<ClientTemplate, Long> {

    @Query("select new ru.softlab.efr.services.insurance.repositories.ShortClientTemplates(" +
            "ct.id, ct.kind, p.name, ct.isTemplate, ct.name, ct.startDate, ct.endDate, ct.sortPriority) " +
            "from ClientTemplate as ct left join ct.program as p " +
            "where (:kind is null or ct.kind = :kind) " +
            "and (:programId is null or p.id = :programId) " +
            "and (:isTemplate is null or ct.isTemplate = :isTemplate) " +
            "and (ct.deleted is null or ct.deleted = false) ")
    Page<ShortClientTemplates> findAllTemplates(@Param("kind") ProgramKind programKind,
                                                @Param("programId") Long programId,
                                                @Param("isTemplate") Boolean isTemplate, Pageable pageable);

    @Query("select ct from ClientTemplate as ct left join ct.program where " +
            "(ct.deleted is null or ct.deleted = false) " +
            "and ct.startDate <= CURRENT_DATE and " +
            "(ct.endDate is null or ct.endDate >= CURRENT_DATE) " +
            "and ((ct.kind is null) " +
            "or (ct.kind = (select p.type from Insurance as i join i.programSetting as ps join ps.program as p where i.id = :insuranceId ) and ct.program is null) " +
            "or (ct.program = (select ps.program from Insurance as i join i.programSetting as ps where i.id = :insuranceId)))")
    List<ClientTemplate> findByContractId(@Param("insuranceId") Long insuranceId);
}
