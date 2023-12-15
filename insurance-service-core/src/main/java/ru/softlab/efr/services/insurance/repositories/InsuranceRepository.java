package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import javax.persistence.QueryHint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Репозиторий для работы с сущностью договора страхования.
 *
 * @author Krivenko
 * @since 19.10.2018
 */

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long>,
        RevisionRepository<Insurance, Long, Integer> {

    @Query("select c from Insurance as c join c.status as s where s.code = :status and c.deleted = :deleted")
    List<Insurance> getByStatusAndDeleted(@Param("status") InsuranceStatusCode status,
                                          @Param("deleted") boolean deleted);

    Insurance getById(Long id);

    @Query("select sum(c.rurAmount) from Insurance c " +
            "join c.holder as h " +
            "join c.status as s " +
            "join c.programSetting as ps " +
            "join ps.program as p " +
            "where c.deleted = false " +
            "and ((s.code = 'NOT_PAYED') or (s.code = 'PAYED')) " +
            "and h.id = :holderId " +
            "and p.id = :programId")
    BigDecimal getCumulationSum(@Param("holderId") long holderId, @Param("programId") long programId);

    @Query("select c from Insurance as c join c.status as s join c.programSetting as ps join ps.program as p where p.type = :kind and s.code = :status and c.creationDate < :creationDate")
    List<Insurance> findByStatusAndKindAndCreationDateAfter(@Param("status") InsuranceStatusCode status,
                                                            @Param("kind") ProgramKind kind,
                                                            @Param("creationDate") LocalDateTime creationDate);

    String INSURANCE_SUMMARY_QUERY_PREFIX = "select new ru.softlab.efr.services.insurance.repositories.InsuranceSummary(" +
            "c.id," +
            "c.contractNumber," +
            "c.branchId," +
            "c.branchName," +
            "c.subdivisionId," +
            "c.subdivisionName," +
            "c.startDate," +
            "c.closeDate," +
            "c.creationDate," +
            "c.conclusionDate," +
            "c.duration," +
            "ps.paymentTerm," +
            "c.employeeName," +
            "c.employeeId," +
            "p.type," +
            "c.amount," +
            "c.rurPremium," +
            "s.code," +
            "c.calendarUnit," +
            "h.id, " +
            "h.firstName, " +
            "h.surName, " +
            "h.middleName, " +
            "h.birthDate, " +
            "h.gender, " +
            "d.docType, " +
            "d.docSeries, " +
            "d.docNumber, " +
            "ph.number, " +
            "h.email, " +
            "p.name, " +
            "c.initialContractNumber, " +
            "c.currency) " +
            "from Insurance as c " +
            "join c.programSetting as ps " +
            "join ps.program as p " +
            "join c.status as s " +
            "join c.holder as h " +
            "left join h.documents as d with (d.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = h and doc.isMain = true and doc.isActive = true)) " +
            "left join h.phones as ph with (ph.id in (select min(phone.id) from PhoneForClaimEntity phone where phone.client = h and phone.main = true)) " +
            "where c.deleted = :deleted ";

    String NON_RESIDENT_QUERY_PREFIX = "SELECT new ru.softlab.efr.services.insurance.repositories.NonResidentDTO" +
            "(cli.firstName," +
            "cli.surName," +
            "cli.middleName," +
            "ins.contractNumber," +
            "ins.conclusionDate," +
            "ins.endDate," +
            "ins.amount," +
            "cli.citizenshipCountry," +
            "cli.taxPayerNumber, " +
            "ins.id) from Insurance as ins ";
    String INSURANCE_EXTRACT_SHORT_QUERY_PREFIX = "select new ru.softlab.efr.services.insurance.repositories.InsuranceExtract(" +
            "c.id," +
            "c.contractNumber," +
            "c.branchId," +
            "c.branchName," +
            "c.subdivisionId," +
            "c.subdivisionName," +
            "c.startDate," +
            "c.closeDate," +
            "c.creationDate," +
            "c.duration," +
            "ps.paymentTerm," +
            "c.employeeName," +
            "c.employeeId," +
            "p.type," +
            "c.premium," +
            "c.rurPremium," +
            "s.name," +
            "c.calendarUnit, " +
            "h.id, " +
            "h.firstName, " +
            "h.surName, " +
            "h.middleName, " +
            "h.birthDate, " +
            "h.gender, " +
            "d.docType, " +
            "d.docSeries, " +
            "d.docNumber, " +
            "ph.number, " +
            "h.email, " +
            "p.name, " +
            "c.callCenterBranchName, " +
            "c.callCenterSubdivisionName, " +
            "c.callCenterEmployeeName, " +
            "c.callCenterEmployeeNumber, " +
            "c.source, " +
            "str.name, " +
            "c.conclusionDate," +
            "c.endDate," +
            "c.periodicity, " +
            "c.currency, " +
            "c.amount, " +
            "c.rurAmount, " +
            "prop.rate) " +
            "from Insurance as c " +
            "join c.programSetting as ps " +
            "join ps.program as p " +
            "join c.status as s " +
            "join c.holder as h " +
            "left join c.strategy as str " +
            "left join str.strategyProperties as prop with (prop.id in (select min(strprop.id) from StrategyProperty strprop where strprop.strategy = str)) " +
            "left join h.documents as d with (d.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = h and doc.isMain = true and doc.isActive = true)) " +
            "left join h.phones as ph with (ph.id in (select min(phone.id) from PhoneForClaimEntity phone where phone.client = h and phone.main = true)) " +
            "where c.deleted = :deleted " +
            "and s.code <> 'REVOKED_REPLACEMENT' ";


    String INSURANCE_EXTRACT_FULL_QUERY_PREFIX = "select new ru.softlab.efr.services.insurance.repositories.InsuranceExtract(" +
            "c.id," +
            "c.contractNumber," +
            "c.branchId," +
            "c.branchName," +
            "c.subdivisionId," +
            "c.subdivisionName," +
            "c.startDate," +
            "c.closeDate," +
            "c.creationDate," +
            "c.duration," +
            "ps.paymentTerm," +
            "c.employeeName," +
            "c.employeeId," +
            "p.type," +
            "c.premium," +
            "c.rurPremium," +
            "s.name," +
            "c.calendarUnit, " +
            "h.id, " +
            "h.firstName, " +
            "h.surName, " +
            "h.middleName, " +
            "h.birthDate, " +
            "h.gender, " +
            "d.docType, " +
            "d.docSeries, " +
            "d.docNumber, " +
            "ph.number, " +
            "h.email, " +
            "p.name, " +
            "c.callCenterBranchName, " +
            "c.callCenterSubdivisionName, " +
            "c.callCenterEmployeeName, " +
            "c.callCenterEmployeeNumber, " +
            "c.source, " +
            "str.name, " +
            "c.conclusionDate," +
            "c.endDate," +
            "c.periodicity, " +
            "c.currency, " +
            "c.amount, " +
            "c.rurAmount, " +
            "prop.rate, " +
            "p.policyCode, " +
            "h.resident, " +
            "h.taxResidence, " +
            "h.taxPayerNumber, " +
            "h.snils, " +
            "h.birthCountry, " +
            "h.birthPlace, " +
            "hadr.postIndex, " +
            "hadr.region, hadr.area, hadr.city, hadr.locality, hadr.street, hadr.house, hadr.construction, hadr.housing, hadr.apartment, " +
            "hresadr.postIndex, " +
            " hresadr.region, hresadr.area, hresadr.city, hresadr.locality, hresadr.street, hresadr.house, hresadr.construction, hresadr.housing, hresadr.apartment, " +
            "d.issuedDate, " +
            "d.issuedBy, " +
            "hmc.docSeries, hmc.docNumber, hmc.stayStartDate, hmc.stayEndDate, " +
            "hfd.docName, hfd.stayStartDate, hfd.stayEndDate, " +
            "h.publicOfficialStatus, " +
            "h.publicOfficialPosition, " +
            "h.beneficialOwner, " +
            "h.businessRelations, " +
            "h.activitiesGoal, " +
            "h.businessRelationsGoal, " +
            "h.riskLevelDesc, " +
            "h.businessReputation, " +
            "h.financialStability, " +
            "h.financesSource, " +
            "c.holderEqualsInsured, " +
            "i.id, i.firstName, i.surName, " +
            "i.middleName, i.resident, " +
            "i.taxResidence, i.birthDate, i.gender, i.taxPayerNumber, i.snils, i.birthCountry, i.birthPlace, phi.number, i.email, " +
            "iadr.postIndex, " +
            "iadr.region, iadr.area, iadr.city, iadr.locality, iadr.street, iadr.house, iadr.construction, iadr.housing, iadr.apartment, " +
            "iresadr.postIndex, " +
            "iresadr.region, iresadr.area, iresadr.city, iresadr.locality, iresadr.street, iresadr.house, iresadr.construction, iresadr.housing, iresadr.apartment, " +
            "id.docType, id.docSeries, id.docNumber, id.issuedDate, id.issuedBy, " +
            "imc.docSeries, imc.docNumber, imc.stayStartDate, imc.stayEndDate, " +
            "ifd.docName, ifd.stayStartDate, ifd.stayEndDate, " +
            "i.publicOfficialStatus, i.publicOfficialPosition, i.beneficialOwner, i.businessRelations, i.activitiesGoal, i.businessRelationsGoal, i.riskLevelDesc, " +
            "i.businessReputation, i.financialStability, i.financesSource, " +
            "d.divisionCode, " +
            "id.divisionCode, " +
            "r1.surName || ' ' ||  r1.firstName || ' ' || r1.middleName, r1.birthDate, r1.birthPlace, r1.taxResidence, r1.share, " +
            "r2.surName || ' ' ||  r2.firstName || ' ' || r2.middleName, r2.birthDate, r2.birthPlace, r2.taxResidence, r2.share, " +
            "r3.surName || ' ' ||  r3.firstName || ' ' || r3.middleName, r3.birthDate, r3.birthPlace, r3.taxResidence, r3.share, " +
            "r4.surName || ' ' ||  r4.firstName || ' ' || r4.middleName, r4.birthDate, r4.birthPlace, r4.taxResidence, r4.share, " +
            "r5.surName || ' ' ||  r5.firstName || ' ' || r5.middleName, r5.birthDate, r5.birthPlace, r5.taxResidence, r5.share, " +
            "segment.name, " +
            "deathRisk.amount, " +
            "risk3.amount, " +
            "risk4.amount, " +
            "survivalToRiskDict.benefitsInsured, " +
            "deathRiskDict.benefitsInsured, " +
            "survivalToSettingRisk.calculationCoefficient, " +
            "survivalToSettingRisk.type, " +
            "survivalToSettingRisk.calculationType, " +
            "p.coolingPeriod, " +
            "survivalRiskDict.benefitsInsured, " +
            "survivalRiskDict.name, " +
            "deathRiskDict.name, " +
            "c.exchangeRate, " +
            "c.fullSetDocument, " +
            "prop.nzbaDate, " +
            "prop.expirationDate, " +
            "prop2.rate, " +
            "prop2.nzbaDate, " +
            "prop2.expirationDate, " +
            "c.commentForNotFullSetDocument," +
            "deathRisk.rurAmount, " +
            "risk3.rurAmount, " +
            "risk4.rurAmount, " +
            "accidentRisk.amount) " +
            "from Insurance as c " +
            "join c.programSetting as ps " +
            "join ps.program as p " +
            "left join p.segment as segment " +
            "join c.status as s " +
            "join c.holder as h " +
            "left join c.insured as i " +
            "left join c.strategy as str " +
            "left join str.strategyProperties as prop with (prop.id in (select min(strprop.id) from StrategyProperty strprop where strprop.strategy = str)) " +
            "left join str.strategyProperties as prop2 with (prop2.id in (select min(strprop.id) from StrategyProperty strprop where strprop.strategy = str and strprop.id <> prop.id)) " +
            "left join i.addresses as iadr with (iadr.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = i and adr.addressType = 'REGISTRATION')) " +
            "left join h.addresses as hadr with (hadr.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = h and adr.addressType = 'REGISTRATION')) " +
            "left join h.addresses as hresadr with (hresadr.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = h and adr.addressType = 'RESIDENCE')) " +
            "left join i.addresses as iresadr with (iresadr.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = i and adr.addressType = 'RESIDENCE')) " +
            "left join h.documents as d with (d.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = h and doc.isMain = true and doc.isActive = true)) " +
            "left join i.documents as id with (id.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = i and doc.isMain = true and doc.isActive = true)) " +
            "left join h.documents as hmc with (hmc.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = h and doc.docType = 'MIGRATION_CARD')) " +
            "left join i.documents as imc with (imc.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = i and doc.docType = 'MIGRATION_CARD')) " +
            "left join h.documents as hfd with (hfd.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = h and doc.docType = 'TEMPORARY_RESIDENCE_PERMIT')) " +
            "left join i.documents as ifd with (ifd.id in (select min(doc.id) from DocumentForClientEntity doc where doc.client = i and doc.docType = 'TEMPORARY_RESIDENCE_PERMIT')) " +
            "left join h.phones as ph with (ph.id in (select min(phone.id) from PhoneForClaimEntity phone where phone.client = h and phone.main = true)) " +
            "left join i.phones as phi with (phi.id in (select min(phone.id) from PhoneForClaimEntity phone where phone.client = i and phone.main = true)) " +
            "left join c.recipientList as r1 with (r1.id in (select min(recipient.id) from InsuranceRecipientEntity recipient where recipient.insurance = c)) " +
            "left join c.recipientList as r2 with (r2.id in (select min(recipient.id) from InsuranceRecipientEntity recipient where recipient.insurance = c and recipient.id != r1.id)) " +
            "left join c.recipientList as r3 with (r3.id in (select min(recipient.id) from InsuranceRecipientEntity recipient where recipient.insurance = c and recipient.id != r1.id and recipient.id != r2.id)) " +
            "left join c.recipientList as r4 with (r4.id in (select min(recipient.id) from InsuranceRecipientEntity recipient where recipient.insurance = c and recipient.id != r1.id and recipient.id != r2.id and recipient.id != r3.id)) " +
            "left join c.recipientList as r5 with (r5.id in (select min(recipient.id) from InsuranceRecipientEntity recipient where recipient.insurance = c and recipient.id != r1.id and recipient.id != r2.id and recipient.id != r3.id and recipient.id != r4.id)) " +
            "left join c.riskInfoList as survivalRisk with (survivalRisk.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name not like '% до %' and risk.name like '%ожитие%'))) " +
            "left join c.riskInfoList as survivalToRisk with (survivalToRisk.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name like '% до %' and risk.name like '%ожитие%'))) " +
            "left join ps.requiredRiskSettingList as survivalToSettingRisk with (survivalToSettingRisk.id in (select min(risks.id) from RiskSetting risks where risks.risk.name like '% до%' and risks.risk.name like 'ожитие%')) " +
            "left join c.riskInfoList as deathRisk with (deathRisk.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name like '%по любой причине%' and risk.name like '%мерть%'))) " +
            "left join c.riskInfoList as accidentRisk with (accidentRisk.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name like '%мерть от несчастного случая%'))) " +
            "left join c.riskInfoList as risk3 with (risk3.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name like '%несчастн%'))) " +
            "left join c.riskInfoList as risk4 with (risk4.id in (select min(riskSetting.id) from InsuranceRiskEntity riskSetting where riskSetting.insurance = c and riskSetting.risk " +
            "in ( select risk from Risk risk where risk.name like '%кораблекруш%'))) " +
            "left join deathRisk.risk as deathRiskDict " +
            "left join survivalToRisk.risk as survivalToRiskDict " +
            "left join survivalRisk.risk as survivalRiskDict " +
            "where c.deleted = :deleted " +
            "and s.code <> 'REVOKED_REPLACEMENT'";

    String FILTER_BY_GROUPS_WHERE_CONDITION =
            " and (:isAdmin is true or ((p.relatedEmployeeGroupFilterType = 'EXCLUDE' AND NOT EXISTS(SELECT 1 FROM RelatedEmployeeGroup group WHERE group.program = p.id and group.groupCode in (:groups))) " +
                    "or (p.relatedEmployeeGroupFilterType = 'INCLUDE' AND EXISTS(SELECT 1 FROM RelatedEmployeeGroup group WHERE group.program = p.id and group.groupCode in (:groups))) " +
                    "or (p.relatedEmployeeGroupFilterType = 'INCLUDE_ALL' OR p.relatedEmployeeGroupFilterType IS NULL))) ";

    String FILTER_BY_OFFICE_WHERE_CONDITION =
            " and (:isAdmin is true or ((p.relatedOfficeFilterType = 'EXCLUDE' AND NOT EXISTS(SELECT 1 FROM RelatedOffice office WHERE office.program = p.id and office.officeId in (:offices))) " +
                    "or (p.relatedOfficeFilterType = 'INCLUDE' AND EXISTS(SELECT 1 FROM RelatedOffice office WHERE office.program = p.id and office.officeId in (:offices))) " +
                    "or (p.relatedOfficeFilterType = 'INCLUDE_ALL' OR p.relatedOfficeFilterType IS NULL))) ";

    Sort CONTRACT_SORT_BY_ID = new Sort(Sort.Direction.DESC, "id");

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Insurance findContractByIdAndDeleted(long id, boolean deleted);

    boolean existsByIdAndDeleted(long id, boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps " +
            "JOIN ps.program p " +
            "WHERE c.id = :id " +
            "and c.deleted = :deleted " +
            "and ((:isAdmin is true or :viewAllContracts is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    Insurance findContractByIdAndDeleted(@Param("id") long id,
                                         @Param("viewAllContracts") boolean viewAllContracts,
                                         @Param("isAdmin") boolean isAdmin,
                                         @Param("employeeId") Long employeeId,
                                         @Param("groups") List<String> groups,
                                         @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                         @Param("deleted") boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT c " +
            "FROM Insurance c " +
            "JOIN c.programSetting ps " +
            "JOIN ps.program p " +
            "WHERE c.id = :id " +
            "and c.deleted = :deleted " +
            "and (:isAdmin is true or :viewAllContracts is true or c.subdivisionId in :officesId) " +
            "and ((:isAdmin is true or :viewAllContracts is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    Insurance findContractByIdAndDeleted(@Param("id") long id,
                                         @Param("viewAllContracts") boolean viewAllContracts,
                                         @Param("isAdmin") boolean isAdmin,
                                         @Param("officesId") Set<Long> officesId, // Фильтрация по полю "subdivisionId" - ВСП, в котором создан договор
                                         @Param("employeeId") Long employeeId,
                                         @Param("groups") List<String> groups,
                                         @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                         @Param("deleted") boolean deleted);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Insurance findContractByCodeAndDeleted(String code, boolean deleted);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 " +
            FILTER_BY_GROUPS_WHERE_CONDITION + " ))")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAll(@Param("isAdmin") boolean isAdmin,
                                   @Param("deleted") boolean deleted,
                                   Pageable pageable,
                                   @Param("groups") List<String> groups,
                                   @Param("employeeId") Long employeeId);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and s.code <> 'REVOKED_REPLACEMENT' " +
            "and (:isAdmin is true or c.subdivisionId in :officesId) " +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 " +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAllWithoutRevoked(@Param("isAdmin") boolean isAdmin,
                                                 @Param("deleted") boolean deleted,
                                                 @Param("officesId") Set<Long> officesId, // Фильтрация по полю "subdivisionId" - ВСП, в котором создан договор
                                                 @Param("employeeId") Long employeeId,
                                                 @Param("groups") List<String> groups,
                                                 @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                                 Pageable pageable);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and s.code <> 'REVOKED_REPLACEMENT' " +
            "and (:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAllWithoutRevoked(@Param("isAdmin") boolean isAdmin,
                                                 @Param("deleted") boolean deleted,
                                                 @Param("employeeId") Long employeeId,
                                                 Pageable pageable);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and (:number is null or ((c.contractNumber like concat('%', :number, '%') and s.code <> 'REVOKED_REPLACEMENT') " +
            "or c.initialContractNumber like concat('%', :number, '%') ) or (:number is '' and c.contractNumber is null)) " +
            "and (:clientId is null or :clientId = h.id) " +
            "and (:clientFirstName is null or :clientFirstName = lower(h.firstName)) " +
            "and (:clientSurname is null or :clientSurname = lower(h.surName)) " +
            "and (:clientMiddleName is null or :clientMiddleName = lower(h.middleName)) " +
            "and (:statusCode is null or :statusCode = s.code) " +
            "and (:programKind is null or :programKind = p.type) " +
            "and (:programId is null or :programId = p.id) " +
            "and (:fullSetDocument is null or c.fullSetDocument = :fullSetDocument) " +
            "and (cast(:startCreationDate as timestamp) is null or :startCreationDate <= c.creationDate) " +
            "and (cast(:endCreationDate as timestamp) is null or :endCreationDate > c.creationDate)" +
            "and (cast(:startConclusionDate as timestamp) is null or :startConclusionDate <= c.conclusionDate) " +
            "and (cast(:endConclusionDate as timestamp) is null or :endConclusionDate >= c.conclusionDate) " +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION + " ))"
    )
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAll(@Param("isAdmin") boolean isAdmin,
                                   @Param("clientId") Long clientId,
                                   @Param("clientFirstName") String clientFirstName,
                                   @Param("clientSurname") String clientSurname,
                                   @Param("clientMiddleName") String clientMiddleName,
                                   @Param("number") String number,
                                   @Param("statusCode") InsuranceStatusCode statusCode,
                                   @Param("programKind") ProgramKind programKind,
                                   @Param("programId") Long programId,
                                   @Param("startCreationDate") LocalDateTime startCreationDate,
                                   @Param("endCreationDate") LocalDateTime endCreationDate,
                                   @Param("startConclusionDate") LocalDate startConclusionDate,
                                   @Param("endConclusionDate") LocalDate endConclusionDate,
                                   @Param("groups") List<String> groups,
                                   @Param("fullSetDocument") Boolean fullSetDocument,
                                   @Param("employeeId") Long employeeId,
                                   @Param("deleted") boolean deleted, Pageable pageable);

    Insurance findByContractNumber(String number);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and (:number is null or c.contractNumber like concat('%', :number, '%') or (:number is '' and c.contractNumber is null)) " +
            "and (:clientId is null or :clientId = h.id) " +
            "and (:clientFirstName is null or :clientFirstName = lower(h.firstName)) " +
            "and (:clientSurname is null or :clientSurname = lower(h.surName)) " +
            "and (:clientMiddleName is null or :clientMiddleName = lower(h.middleName))" +
            "and (:statusCode is null or :statusCode = s.code) " +
            "and (:programKind is null or :programKind = p.type) " +
            "and (:fullSetDocument is null or c.fullSetDocument = :fullSetDocument) " +
            "and (:programId is null or :programId = p.id) " +
            "and (cast(:startCreationDate as timestamp) is null or :startCreationDate <= c.creationDate) " +
            "and (cast(:endCreationDate as timestamp) is null or :endCreationDate > c.creationDate) " +
            "and (cast(:startConclusionDate as timestamp) is null or :startConclusionDate <= c.conclusionDate) " +
            "and (cast(:endConclusionDate as timestamp) is null or :endConclusionDate >= c.conclusionDate) " +
            "and s.code <> 'REVOKED_REPLACEMENT' " +
            "and (:isAdmin is true or c.subdivisionId in :officesId) " +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)))" +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAllWithoutRevoked(@Param("isAdmin") boolean isAdmin,
                                                 @Param("clientId") Long clientId,
                                                 @Param("clientFirstName") String clientFirstName,
                                                 @Param("clientSurname") String clientSurname,
                                                 @Param("clientMiddleName") String clientMiddleName,
                                                 @Param("number") String number,
                                                 @Param("statusCode") InsuranceStatusCode statusCode,
                                                 @Param("programKind") ProgramKind programKind,
                                                 @Param("programId") Long programId,
                                                 @Param("startCreationDate") LocalDateTime startCreationDate,
                                                 @Param("endCreationDate") LocalDateTime endCreationDate,
                                                 @Param("deleted") boolean deleted,
                                                 @Param("officesId") Set<Long> officesId,// Фильтрация по полю "subdivisionId" - подразделение, в котором создан договор
                                                 @Param("employeeId") Long employeeId,
                                                 @Param("startConclusionDate") LocalDate startConclusionDate,
                                                 @Param("endConclusionDate") LocalDate endConclusionDate,
                                                 @Param("groups") List<String> groups,
                                                 @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
                                                 @Param("fullSetDocument") Boolean fullSetDocument,
                                                 Pageable pageable);

    @Query(INSURANCE_SUMMARY_QUERY_PREFIX +
            "and (:number is null or c.contractNumber like concat('%', :number, '%') or (:number is '' and c.contractNumber is null)) " +
            "and (:clientId is null or :clientId = h.id) " +
            "and (:clientFirstName is null or :clientFirstName = lower(h.firstName)) " +
            "and (:clientSurname is null or :clientSurname = lower(h.surName)) " +
            "and (:clientMiddleName is null or :clientMiddleName = lower(h.middleName))" +
            "and (:statusCode is null or :statusCode = s.code) " +
            "and (:fullSetDocument is null or c.fullSetDocument = :fullSetDocument) " +
            "and (:programKind is null or :programKind = p.type) " +
            "and (:programId is null or :programId = p.id) " +
            "and (cast(:startCreationDate as timestamp) is null or :startCreationDate <= c.creationDate) " +
            "and (cast(:endCreationDate as timestamp) is null or :endCreationDate > c.creationDate) " +
            "and (cast(:startConclusionDate as timestamp) is null or :startConclusionDate <= c.conclusionDate) " +
            "and (cast(:endConclusionDate as timestamp) is null or :endConclusionDate >= c.conclusionDate) " +
            "and s.code <> 'REVOKED_REPLACEMENT' " +
            "and (:isAdmin is true or :employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)) ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<InsuranceSummary> findAllWithoutRevoked(@Param("isAdmin") boolean isAdmin,
                                                 @Param("clientId") Long clientId,
                                                 @Param("clientFirstName") String clientFirstName,
                                                 @Param("clientSurname") String clientSurname,
                                                 @Param("clientMiddleName") String clientMiddleName,
                                                 @Param("number") String number,
                                                 @Param("statusCode") InsuranceStatusCode statusCode,
                                                 @Param("programKind") ProgramKind programKind,
                                                 @Param("programId") Long programId,
                                                 @Param("startCreationDate") LocalDateTime startCreationDate,
                                                 @Param("endCreationDate") LocalDateTime endCreationDate,
                                                 @Param("deleted") boolean deleted,
                                                 @Param("employeeId") Long employeeId,
                                                 @Param("startConclusionDate") LocalDate startConclusionDate,
                                                 @Param("endConclusionDate") LocalDate endConclusionDate,
                                                 @Param("fullSetDocument") Boolean fullSetDocument,
                                                 Pageable pageable);

    @Query(INSURANCE_EXTRACT_SHORT_QUERY_PREFIX +
            "and (:programKind is null or :programKind = p.type) " +
            "and c.conclusionDate between :startDate and :endDate " +
            "and (:isAdmin is true or c.subdivisionId in :subdivisionIds) " +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllByConclusionDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("subdivisionIds") Set<Long> subdivisionIds,// Фильтрация по полю "subdivisionId" - подразделение, в котором создан договор
            @Param("employeeId") Long employeeId,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKind") ProgramKind kind,
            Sort sort);

    @Query(INSURANCE_EXTRACT_FULL_QUERY_PREFIX +
            "and p.type in (:programKinds) " +
            "and ((c.conclusionDate between :startDate and :endDate) " +
            "or (c.conclusionDate is null and c.creationDate between :startDateTime and :endDateTime))" +
            "and ((:isAdmin is true or :employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)) " +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllFullByConclusionDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("employeeId") Long employeeId,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKinds") List<ProgramKind> kinds,
            Sort sort);

    @Query(INSURANCE_EXTRACT_FULL_QUERY_PREFIX +
            "and p.type in (:programKinds) " +
            "and ((c.conclusionDate between :startDate and :endDate) " +
            "or (c.conclusionDate is null and c.creationDate between :startDateTime and :endDateTime))" +
            "and (:isAdmin is true or c.subdivisionId in :subdivisionIds) " +
            "and ((:isAdmin is true or (:employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId))) " +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllFullByConclusionDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("subdivisionIds") Set<Long> subdivisionIds,// Фильтрация по полю "subdivisionId" - подразделение, в котором создан договор
            @Param("employeeId") Long employeeId,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKinds") List<ProgramKind> kinds,
            Sort sort);

    @Query(INSURANCE_EXTRACT_SHORT_QUERY_PREFIX +
            "and (:programKind is null or :programKind = p.type) " +
            "and c.conclusionDate between :startDate and :endDate " +
            "and ((:isAdmin is true or :employeeId <> -1L and (c.employeeId = :employeeId or c.callCenterEmployeeId = :employeeId)) " +
            "or (1 = 1 "+
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION + " ))")
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllByConclusionDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKind") ProgramKind kind,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            Sort sort);

    @Query(INSURANCE_EXTRACT_SHORT_QUERY_PREFIX +
            "and (:programKind is null or :programKind = p.type) " +
            "and c.conclusionDate between :startDate and :endDate" +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION)
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllByConclusionDateDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKind") ProgramKind kind,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            Sort sort);

    @Query(INSURANCE_EXTRACT_FULL_QUERY_PREFIX +
            "and p.type in (:programKinds) " +
            "and ((c.conclusionDate between :startDate and :endDate) " +
            "or (c.conclusionDate is null and c.creationDate between :startDateTime and :endDateTime))" +
            FILTER_BY_GROUPS_WHERE_CONDITION +
            FILTER_BY_OFFICE_WHERE_CONDITION)
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "500"))
    Stream<InsuranceExtract> findAllFullByConclusionDateDateBetween(
            @Param("isAdmin") boolean isAdmin,
            @Param("deleted") boolean deleted,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("programKinds") List<ProgramKind> kinds,
            @Param("groups") List<String> groups,
            @Param("offices") Set<Long> offices, // Фильтрация по ВСП из таблицы RelatedOffice
            Sort sort);

    @Transactional(readOnly = true)
    @Query(value = NON_RESIDENT_QUERY_PREFIX +
            "inner join ins.holder as cli " +
            "where cli.taxResidence !='RUSSIAN' " +
            "and ins.creationDate between :startDate and :endDate")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Stream<NonResidentDTO> findAllNonResidentHolders(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Transactional(readOnly = true)
    @Query(value = NON_RESIDENT_QUERY_PREFIX +
            "inner join ins.insured as cli " +
            "where cli.taxResidence !='RUSSIAN' " +
            "and ins.creationDate between :startDate and :endDate")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Stream<NonResidentDTO> findAllNonResidentInsureds(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ru.softlab.efr.services.insurance.repositories.NonResidentDTO" +
            "(cli.firstName," +
            "cli.surName," +
            "cli.middleName," +
            "ins.contractNumber," +
            "ins.conclusionDate," +
            "ins.endDate," +
            "ins.amount," +
            "cli.taxResidence, " +
            "ins.id ) " +
            "from Insurance ins " +
            "inner join ins.recipientList as cli " +
            "where cli.taxResidence != 'RUSSIAN' and cli.taxResidence is not null " +
            "and ins.creationDate between :startDate and :endDate")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Stream<NonResidentDTO> findAllNonResidentRecipients(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Получить список договоров клиента
     *
     * @param clientEntity клиент (страхователь)
     * @return список догоаоров
     */
    List<Insurance> findAllByHolder(ClientEntity clientEntity);

    /**
     * Получить список договоров клиента
     *
     * @param holders клиент (страхователь)
     * @return список догоаоров
     */
    @Query("select c from Insurance c where c.holder in (:holders) and c.status not in (:statuses)")
    Page<Insurance> findAllByHoldersPageable(@Param("holders") List<ClientEntity> holders, @Param("statuses") List<InsuranceStatus> statuses, Pageable pageable);

    /**
     * Получить список договоров по данным клиента за исключением указанных статусов указанных статусах
     * @param surName фамилия клиента
     * @param firstName имя клиента
     * @param middleName отчество клиента
     * @param birthDate дата рождения клиента
     * @param phoneNumber номер мобильного телефона клиента
     * @param statuses список статусов по которым не следует проводить поиск
     * @return список страховок
     */
    @Query("select c from Insurance as c join c.holder as h " +
            "left join h.phones as ph with (ph.id in (select min(phone.id) from PhoneForClaimEntity phone where phone.client = h and phone.main = true)) " +
            "where h.surName = :surName and h.firstName = :firstName and h.middleName = :middleName and h.birthDate = :birthDate " +
            "and ph.number = :phoneNumber and c.status not in (:statuses)")
    List<Insurance> findAllByHolder(@Param("surName") String surName, @Param("firstName") String firstName,
                                             @Param("middleName") String middleName, @Param("birthDate") LocalDate birthDate,
                                             @Param("phoneNumber") String phoneNumber,
                                             @Param("statuses") List<InsuranceStatus> statuses);

    @Query(value = "SELECT new_code from " +
            "(SELECT to_char(trunc((random() * (999999 - 1)) + 1), '000000') as new_code FROM generate_series(1, 999999)) as generated " +
            "WHERE new_code NOT IN (SELECT code FROM {h-schema}insurance where code is not null) limit 1",
            nativeQuery = true)
    String generateCode();

    /**
     * Договора по застрахованному лицу и программе
     */
    @Query("select c from Insurance c join c.programSetting ps " +
            "where c.deleted = :deleted " +
            "and ps.program.id = :programId " +
            "and (c.insured.id = :clientId or (c.insured is null and c.holder.id = :clientId)) " +
            "and c.endDate > :endDate")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Insurance> findAllByProgramAndClientAndEndDateMoreThan(
            @Param("deleted") boolean deleted,
            @Param("programId") Long programId,
            @Param("clientId") Long clientId,
            @Param("endDate") LocalDate endDate,
            Sort sort);

    /**
     * Договора по застрахованному лицу и виду программы
     */
    @Query("select c from Insurance c join c.programSetting ps join ps.program p " +
            "join c.status as s " +
            "where c.deleted = :deleted " +
            "and p.type = :programKind " +
            "and (c.insured.id = :clientId or (c.insured.id is null and c.holder.id = :clientId)) " +
            "and s.code <> 'REVOKED_REPLACEMENT' " +
            "and c.closeDate is null")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Insurance> findAllByProgramKindAndClient(
            @Param("deleted") boolean deleted,
            @Param("programKind") ProgramKind programKind,
            @Param("clientId") Long clientId,
            Sort sort);

    /**
     * Договоры по застрахованному лицу или страхователю
     */
    @Query("select c from Insurance c join c.programSetting ps join ps.program p " +
            "where c.deleted = false " +
            "and c.conclusionDate is not null " +
            "and (c.insured.id = :clientId or (c.insured.id is null and c.holder.id = :clientId)) ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Insurance> findAllByClient(@Param("clientId") Long clientId);

    /**
     * Получить дату первого оформленного договор клиента
     */
    @Query("select min(c.conclusionDate) from Insurance c " +
            "where c.deleted = false " +
            "and c.conclusionDate is not null " +
            "and (c.insured.id = :clientId or c.holder.id = :clientId) ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    LocalDate getFirstContractDateByClient(@Param("clientId") Long clientId);


    /**
     * Проверка наличия существования номера договора
     */
    @Query("select true from Insurance c where (c.contractNumber = :contractNumber and c.id <> :id) or " +
            "(substring(c.contractNumber, 2, 3) = substring(:contractNumber, 2, 3) and " +
            "substring(c.contractNumber, 8) = substring(:contractNumber, 8) and " +
            "c.id <> :id) ")
    Boolean existsByContractNumberAndIdNot(@Param("contractNumber") String contractNumber, @Param("id") Long id);

    /**
     * Получение очередного технического номера для договора в статусе "Аннулирован по замене".
     *
     * @return Технический номер.
     */
    @Query(value = "SELECT max(to_number(contract_number, '000000000000000')) FROM {h-schema}insurance where status_id =" +
            " (select id from {h-schema}insurance_status where code = 'REVOKED_REPLACEMENT');",
            nativeQuery = true)
    Long getReplacementNumber();

    /**
     * Вернуть признак наличия оформленного договора страхования для клиента
     *
     * @param clientId идентификатор клиента
     * @return признак наличия оформленного договора
     */
    @Query("select (count(id) > 0) from Insurance " +
            "where holder.id = :clientId " +
            "and status.code = :status")
    boolean existsByClient(
            @Param("clientId") Long clientId,
            @Param("status") InsuranceStatusCode status);
}
