package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Set;

import static ru.softlab.efr.services.insurance.repositories.InsuranceRepository.FILTER_BY_GROUPS_WHERE_CONDITION;
import static ru.softlab.efr.services.insurance.repositories.InsuranceRepository.FILTER_BY_OFFICE_WHERE_CONDITION;

/**
 * Репозиторий для работы с сущностью программы страхования.
 *
 * @author Kalantaev
 * @since 19.09.2018
 */
@Repository
public interface ProgramRepository  extends JpaRepository<Program, Long> {

    String FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION1 = " (NOT EXISTS (" +
            "SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps4 " +
            "JOIN ps4.program p4 " +
            "WHERE p4.id = p.id " +
            ") OR " +
            "(EXISTS ( " +
                "SELECT c " +
                "FROM Insurance c " +
                "JOIN c.programSetting ps " +
                "JOIN ps.program p2 " +
                "WHERE p2.id = p.id " +
                "AND (:isAdmin is true or :viewAllContracts is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)))))) ";

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT p " +
            "FROM Program p " +
            "WHERE p.id = :id " +
            "AND p.deleted = :deleted AND ( " +
            FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION1 +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " )")
    Program findProgramByIdAndDeleted(@Param("id") long id,
                                      @Param("viewAllContracts") boolean viewAllContracts,
                                      @Param("isAdmin") boolean isAdmin,
                                      @Param("employeeId") Long employeeId,
                                      @Param("groups") List<String> groups,
                                      @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                      @Param("deleted") boolean deleted);

    String FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION2 = " (NOT EXISTS (" +
            "SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps4 " +
            "JOIN ps4.program p3 " +
            "WHERE p3.id = p.id " +
            ") OR " +
            "(EXISTS ( " +
                "SELECT c " +
                "FROM Insurance c " +
                "JOIN c.programSetting ps " +
                "JOIN ps.program p2 " +
                "WHERE p2.id = p.id " +
                "AND (:isAdmin is true or :viewAllContracts is true or c.subdivisionId in :officesId or (:employeeId <> -1L and c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))))) ";

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT p " +
            "FROM Program p " +
            "WHERE p.id = :id " +
            "AND p.deleted = :deleted AND ( " +
            FILTER_BY_EXISTING_ACCESSED_CONTRACTS_WHERE_CONDITION2 +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " )")
    Program findProgramByIdAndDeleted(@Param("id") long id,
                                         @Param("viewAllContracts") boolean viewAllContracts,
                                         @Param("isAdmin") boolean isAdmin,
                                         @Param("officesId") Set<Long> officesId, // Фильтрация по полю "subdivisionId" - ВСП, в котором создан договор
                                         @Param("employeeId") Long employeeId,
                                         @Param("groups") List<String> groups,
                                         @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                         @Param("deleted") boolean deleted);
    /**
     * Получить список программ страхования, удовлетворяющий настройкам пейджинга.
     *
     * @param deleted  Признак "удалённости" программы страхования.
     * @param pageable Настройки пейджинга.
     * @return Список программ страхования, удовлетворяющий настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p where p.deleted = :deleted " +
            FILTER_BY_GROUPS_WHERE_CONDITION)
    Page<Program> findAllByDeleted(@Param("deleted") Boolean deleted,
                                   @Param("groups") List<String> groups,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable);

    /**
     * Получить список программ страхования, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     *
     * @param userOffices Имя подразделелния в организационной структуре организации.
     * @param deleted    Признак "удалённости" программы страхования.
     * @param pageable   Настройки пейджинга.
     * @return Список программ страхования, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p " +
            "where p.deleted = :deleted " +
            FILTER_BY_OFFICE_WHERE_CONDITION +
            FILTER_BY_GROUPS_WHERE_CONDITION)
    Page<Program> findAllByDeleted(@Param("offices") Set<Long> userOffices,
                                   @Param("groups") List<String> groups,
                                   @Param("deleted") Boolean deleted,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable);


    /**
     * Получить список программ страхования, по кодировке полиса и варианту страхования и статусу активности,
     * для проверки на существования
     */
    int countByProgramCodeAndProgramTariffAndIsActive(String programCode, String programTariff, boolean isActive);


    /**
     * Получить отфильтрованый список программ страхования, удовлетворяющий настройкам пейджинга.
     *
     * @param deleted        Признак "удалённости" программы страхования.
     * @param pageable       Настройки пейджинга.
     * @param kind           вид страхования.
     * @param policyCode     наименование программы страхования.
     * @param programVariant  кодировка программы.
     * @param programName    вариант программы.
     * @return Список программ страхования, удовлетворяющий настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p where p.deleted = :deleted " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            "and p.type = :kind " +
            "and (:policyCode is null or :policyCode = p.policyCode)" +
            "and (:programVariant is null or :programVariant = p.variant)" +
            "and (:programName is null or :programName = p.name)")
    Page<Program> findAllByDeleted(@Param("deleted") Boolean deleted,
                                   @Param("groups") List<String> groups,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable,
                                   @Param("kind") ProgramKind kind,
                                   @Param("policyCode") String policyCode,
                                   @Param("programVariant") String programVariant,
                                   @Param("programName") String programName);



    /**
     * Получить отфильтрованый список программ страхования, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     *
     * @param offices     Имя подразделелния в организационной структуре организации.
     * @param deleted        Признак "удалённости" программы страхования.
     * @param pageable       Настройки пейджинга.
     * @param kind           вид страхования.
     * @param policyCode     наименование программы страхования.
     * @param programVariant  кодировка программы.
     * @param programName    вариант программы.
     * @return Список программ страхования, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p " +
            "where p.deleted = :deleted " +
            "and p.type = :kind " +
            "and (:policyCode is null or :policyCode = p.policyCode)" +
            "and (:programVariant is null or :programVariant = p.variant)" +
            "and (:programName is null or :programName = p.name)" +
            FILTER_BY_OFFICE_WHERE_CONDITION +
            FILTER_BY_GROUPS_WHERE_CONDITION)
    Page<Program> findAllByDeleted(@Param("offices") Set<Long> offices,
                                   @Param("groups") List<String> groups,
                                   @Param("deleted") Boolean deleted,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable,
                                   @Param("kind") ProgramKind kind,
                                   @Param("policyCode") String policyCode,
                                   @Param("programVariant") String programVariant,
                                   @Param("programName") String programName);


    /**
     * Получить отфильтрованый список программ страхования, если kind не указан, удовлетворяющий настройкам пейджинга.
     *
     * @param deleted        Признак "удалённости" программы страхования.
     * @param pageable       Настройки пейджинга.
     * @param policyCode     наименование программы страхования.
     * @param programVariant  кодировка программы.
     * @param programName    вариант программы.
     * @return Список программ страхования, удовлетворяющий настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p where p.deleted = :deleted " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            "and (:policyCode is null or :policyCode = p.policyCode)" +
            "and (:programVariant is null or :programVariant = p.variant)" +
            "and (:programName is null or :programName = p.name)")
    Page<Program> findAllByDeleted(@Param("deleted") Boolean deleted,
                                   @Param("groups") List<String> groups,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable,
                                   @Param("policyCode") String policyCode,
                                   @Param("programVariant") String programVariant,
                                   @Param("programName") String programName);



    /**
     * Получить отфильтрованый список программ страхования, если kind не указан, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     *
     * @param offices     Имя подразделелния в организационной структуре организации.
     * @param deleted        Признак "удалённости" программы страхования.
     * @param pageable       Настройки пейджинга.
     * @param policyCode     наименование программы страхования.
     * @param programVariant  кодировка программы.
     * @param programName    вариант программы.
     * @return Список программ страхования, которые доступны для оформления в указанном подразделении, удовлетворяющий
     * настройкам пейджинга.
     */
    @EntityGraph(attributePaths = {"segment"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Program p " +
            "where p.deleted = :deleted " +
            "and (:policyCode is null or :policyCode = p.policyCode)" +
            "and (:programVariant is null or :programVariant = p.variant)" +
            "and (:programName is null or :programName = p.name)" +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION)
    Page<Program> findAllByDeleted(@Param("offices") Set<Long> offices,
                                   @Param("groups") List<String> groups,
                                   @Param("deleted") Boolean deleted,
                                   @Param("isAdmin") boolean isAdmin,
                                   Pageable pageable,
                                   @Param("policyCode") String policyCode,
                                   @Param("programVariant") String programVariant,
                                   @Param("programName") String programName);

}