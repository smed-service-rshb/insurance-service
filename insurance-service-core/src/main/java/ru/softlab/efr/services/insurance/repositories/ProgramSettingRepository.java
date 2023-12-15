package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.QueryHint;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static ru.softlab.efr.services.insurance.repositories.InsuranceRepository.FILTER_BY_GROUPS_WHERE_CONDITION;
import static ru.softlab.efr.services.insurance.repositories.InsuranceRepository.FILTER_BY_OFFICE_WHERE_CONDITION;

/**
 * Репозиторий для работы с сущностью параметров программы страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */
public interface ProgramSettingRepository extends JpaRepository<ProgramSetting, Long> {

    String FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION1 = " (NOT EXISTS (" +
            "SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps1 " +
            "WHERE ps1.id = ps0.id " +
            ") OR " +
            "(EXISTS ( " +
                "SELECT c " +
                "FROM Insurance c " +
                "JOIN c.programSetting ps3 " +
                "WHERE ps3.id = ps0.id " +
                "AND (:isAdmin is true or :viewAllContracts is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)))))) " ;
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT ps0 " +
            "FROM ProgramSetting ps0 " +
            "JOIN ps0.program p " +
            "WHERE ps0.id = :id " +
            "AND ps0.deleted = :deleted AND (( " +
            FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION1  +
            " ) or (:employeeId = -1L " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    ProgramSetting findProgramSettingById(@Param("id") long id,
                                      @Param("viewAllContracts") boolean viewAllContracts,
                                      @Param("isAdmin") boolean isAdmin,
                                      @Param("employeeId") Long employeeId,
                                      @Param("groups") List<String> groups,
                                      @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                      @Param("deleted") boolean deleted);

    String FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION2 = " (NOT EXISTS (" +
            "SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps1 " +
            "WHERE ps1.id = ps0.id " +
            ") OR " +
            "(EXISTS ( " +
                "SELECT c " +
                "FROM Insurance c " +
                "JOIN c.programSetting ps3 " +
                "WHERE ps3.id = ps0.id " +
                "AND (:isAdmin is true or :viewAllContracts is true or c.subdivisionId in :officesId or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)))))) ";
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT ps0 " +
            "FROM ProgramSetting ps0 " +
            "JOIN ps0.program p " +
            "WHERE ps0.id = :id " +
            "AND ps0.deleted = :deleted AND ((" +
            FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION2 +
            " ) or (:employeeId = -1L " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    ProgramSetting findProgramSettingById(@Param("id") long id,
                                         @Param("viewAllContracts") boolean viewAllContracts,
                                         @Param("isAdmin") boolean isAdmin,
                                         @Param("officesId") Set<Long> officesId, // Фильтрация по полю "subdivisionId" - ВСП, в котором создан договор
                                         @Param("employeeId") Long employeeId,
                                         @Param("groups") List<String> groups,
                                         @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                         @Param("deleted") boolean deleted);

    @Query("SELECT ps " +
            "FROM ProgramSetting AS ps " +
            "INNER JOIN ps.program AS p " +
            "WHERE ps.deleted = :deleted " +
            FILTER_BY_GROUPS_WHERE_CONDITION)
    Page<ProgramSetting> findAllByDeleted(Pageable pageable, @Param("deleted") Boolean deleted,
                                          @Param("groups") List<String> employeeGroups,
                                          @Param("isAdmin") boolean isAdmin);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ProgramSetting> findAllByDeleted(Boolean deleted);

    @Query(value = "select distinct ps from ProgramSetting ps " +
            "inner join ps.program as p " +
            "left join ps.strategyList s " +
            "where ps.deleted = false and p.deleted = false and p.isActive = true " +
            "and (ps.startDate is null or ps.startDate <= :programDate) " +
            "and (ps.endDate is null or ps.endDate >= :programDate) " +
            "and ((ps.currency = :currency or :currency is null) or ps.currency is null) " +
            "and (s.id = :strategy or :strategy is null) " +
            "and (p.id = :program or :program is null) " +
            "and (p.type = :programKind or :programKind is null) " +
            "and (ps.periodicity = :periodicity or :periodicity is null) " +
            "and ((ps.minAgeInsured <= :insuredAge and ps.maxAgeInsured >= :insuredAge) or (:insuredAge is null or ps.minAgeInsured is null or ps.maxAgeInsured is null)) " +
            "and ((ps.minAgeHolder <= :holderAge and ps.maxAgeHolder >= :holderAge) or (:holderAge is null or ps.minAgeHolder is null or ps.maxAgeHolder is null)) " +
            "and ((ps.minimumTerm <= :term and ps.maximumTerm >= :term and ps.calendarUnit = :calendarUnit) or (:term is null or :calendarUnit is null or ps.minimumTerm is null or ps.maximumTerm is null)) " +
            "and ((:type = 'SUM' and ((ps.minSum <= :amount and ps.maxSum >= :amount) or (:amount is null or ps.minSum is null or ps.maxSum is null))) " +
            "or (:type = 'PREMIUM' and ((ps.minPremium <= :premium and ps.maxPremium >= :premium) or (:premium is null or ps.minPremium is null or ps.maxPremium is null))))")

    List<ProgramSetting> findByCriteria(@Param("programKind") ProgramKind programKind, @Param("program") Long program,
                                        @Param("currency") Long currency, @Param("amount") BigDecimal amount,
                                        @Param("term") Integer term, @Param("premium") BigDecimal premium,
                                        @Param("type") String type, @Param("calendarUnit") CalendarUnitEnum calendarUnit,
                                        @Param("insuredAge") Integer insuredAge, @Param("holderAge") Integer holderAge,
                                        @Param("periodicity")PeriodicityEnum periodicity, @Param("strategy") Long strategy,
                                        @Param("programDate") Date programDate,
                                        Sort sort);


    @Query("SELECT ps " +
            "FROM ProgramSetting AS ps " +
            "INNER JOIN ps.program AS p " +
            "LEFT JOIN ps.strategyList AS sl " +
            "WHERE ps.deleted = :deleted " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            " and (:kind is null or :kind = p.type)" +
            " and (:programName is null or :programName = p.name)" +
            " and (:strategyId is null or sl.id = :strategyId)")
    Page<ProgramSetting> findAllByDeletedFiltered(Pageable pageable,
                                                  @Param("deleted") Boolean deleted,
                                                  @Param("groups") List<String> employeeGroups,
                                                  @Param("isAdmin") boolean isAdmin,
                                                  @Param("kind") ProgramKind kind,
                                                  @Param("programName") String programName,
                                                  @Param("strategyId") Long strategyId);

}

