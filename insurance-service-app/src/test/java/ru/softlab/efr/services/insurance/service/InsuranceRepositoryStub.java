package ru.softlab.efr.services.insurance.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.InsuranceExtract;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;
import ru.softlab.efr.services.insurance.repositories.NonResidentDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author olshansky
 * @since 18.12.2018
 */
public class InsuranceRepositoryStub implements InsuranceRepository {

    @Override
    public List<Insurance> getByStatusAndDeleted(InsuranceStatusCode status, boolean deleted) {
        return null;
    }

    @Override
    public Insurance getById(Long id) {
        return null;
    }

    @Override
    public BigDecimal getCumulationSum(long holderId, long programId) {
        return null;
    }

    @Override
    public List<Insurance> findByStatusAndKindAndCreationDateAfter(InsuranceStatusCode status, ProgramKind kind, LocalDateTime creationDate) {
        return null;
    }

    @Override
    public Insurance findContractByIdAndDeleted(long id, boolean deleted) {
        return null;
    }

    @Override
    public boolean existsByIdAndDeleted(long id, boolean deleted) {
        return false;
    }

    @Override
    public Insurance findContractByIdAndDeleted(long id, boolean viewAllContracts, boolean isAdmin, Long employeeId, List<String> groups, Set<Long> offices, boolean deleted) {
        return null;
    }

    @Override
    public Insurance findContractByIdAndDeleted(long id, boolean viewAllContracts, boolean isAdmin, Set<Long> officesId, Long employeeId, List<String> groups, Set<Long> offices, boolean deleted) {
        return null;
    }


    @Override
    public Insurance findContractByCodeAndDeleted(String code, boolean deleted) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAll(boolean isAdmin, boolean deleted, Pageable pageable, List<String> groups, Long employeeId) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAllWithoutRevoked(boolean isAdmin, boolean deleted, Set<Long> officesId, Long employeeId, List<String> groups, Set<Long> offices, Pageable pageable) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAllWithoutRevoked(boolean isAdmin, boolean deleted, Long employeeId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAll(boolean isAdmin, Long clientId, String clientFirstName, String clientSurname, String clientMiddleName, String number, InsuranceStatusCode statusCode, ProgramKind programKind, Long programId,
                                          LocalDateTime startCreationDate, LocalDateTime endCreationDate, LocalDate startConclusionDate, LocalDate endConclusionDate, List<String> groups, Boolean fullSetDocument, Long employeeId, boolean deleted,
                                          Pageable pageable) {
        return null;
    }

    @Override
    public Insurance findByContractNumber(String number) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAllWithoutRevoked(boolean isAdmin, Long clientId, String clientFirstName, String clientSurname, String clientMiddleName, String number, InsuranceStatusCode statusCode, ProgramKind programKind, Long programId,
                                                        LocalDateTime startCreationDate, LocalDateTime endCreationDate, boolean deleted, Set<Long> officesId, Long employeeId, LocalDate startConclusionDate, LocalDate endConclusionDate,
                                                        List<String> groups, Set<Long> offices, Boolean fullSetDocument, Pageable pageable) {
        return null;
    }

    @Override
    public Page<InsuranceSummary> findAllWithoutRevoked(boolean isAdmin, Long clientId, String clientFirstName, String clientSurname, String clientMiddleName, String number, InsuranceStatusCode statusCode, ProgramKind programKind, Long programId,
                                                        LocalDateTime startCreationDate, LocalDateTime endCreationDate, boolean deleted, Long employeeId, LocalDate startConclusionDate, LocalDate endConclusionDate, Boolean fullSetDocument,
                                                        Pageable pageable) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllByConclusionDateBetween(boolean isAdmin, boolean deleted, Set<Long> subdivisionIds, Long employeeId, List<String> groups, Set<Long> offices, LocalDate startDate, LocalDate endDate, ProgramKind kind,
                                                                   Sort sort) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllFullByConclusionDateBetween(boolean isAdmin, boolean deleted, Long employeeId, List<String> groups, Set<Long> offices, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate startDate,
                                                                       LocalDate endDate, List<ProgramKind> kinds, Sort sort) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllFullByConclusionDateBetween(boolean isAdmin, boolean deleted, Set<Long> subdivisionIds, Long employeeId, List<String> groups, Set<Long> offices, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                                       LocalDate startDate, LocalDate endDate, List<ProgramKind> kinds, Sort sort) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllByConclusionDateBetween(boolean isAdmin, boolean deleted, Long employeeId, LocalDate startDate, LocalDate endDate, ProgramKind kind, List<String> groups, Set<Long> offices, Sort sort) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllByConclusionDateDateBetween(boolean isAdmin, boolean deleted, LocalDate startDate, LocalDate endDate, ProgramKind kind, List<String> groups, Set<Long> offices, Sort sort) {
        return null;
    }

    @Override
    public Stream<InsuranceExtract> findAllFullByConclusionDateDateBetween(boolean isAdmin, boolean deleted, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate startDate, LocalDate endDate, List<ProgramKind> kinds,
                                                                           List<String> groups, Set<Long> offices, Sort sort) {
        return null;
    }

    @Override
    public Stream<NonResidentDTO> findAllNonResidentHolders(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public Stream<NonResidentDTO> findAllNonResidentInsureds(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public Stream<NonResidentDTO> findAllNonResidentRecipients(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public List<Insurance> findAllByHolder(ClientEntity clientEntity) {
        return null;
    }

    @Override
    public Page<Insurance> findAllByHoldersPageable(List<ClientEntity> holders, List<InsuranceStatus> statuses, Pageable pageable) {
        return null;
    }

    @Override
    public List<Insurance> findAllByHolder(String surName, String firstName, String middleName, LocalDate birthDate, String phoneNumber, List<InsuranceStatus> statuses) {
        return null;
    }

    @Override
    public String generateCode() {
        return null;
    }

    @Override
    public List<Insurance> findAllByProgramAndClientAndEndDateMoreThan(boolean deleted, Long programId, Long clientId, LocalDate endDate, Sort sort) {
        return null;
    }

    @Override
    public List<Insurance> findAllByProgramKindAndClient(boolean deleted, ProgramKind programKind, Long clientId, Sort sort) {
        return null;
    }

    @Override
    public List<Insurance> findAllByClient(Long clientId) {
        return null;
    }

    @Override
    public LocalDate getFirstContractDateByClient(Long clientId) {
        return null;
    }

    @Override
    public Boolean existsByContractNumberAndIdNot(String contractNumber, Long id) {
        return false;
    }

    @Override
    public Long getReplacementNumber() {
        return null;
    }

    @Override
    public boolean existsByClient(Long clientId, InsuranceStatusCode status) {
        return false;
    }

    @Override
    public List<Insurance> findAll() {
        return null;
    }

    @Override
    public List<Insurance> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Insurance> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Insurance> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(Insurance insurance) {

    }

    @Override
    public void delete(Iterable<? extends Insurance> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Insurance> S save(S s) {
        return null;
    }

    @Override
    public <S extends Insurance> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Insurance findOne(Long aLong) {
        return null;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Insurance> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Insurance> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Insurance getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Insurance> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Insurance> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Insurance> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Insurance> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Insurance> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Insurance> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Revision<Integer, Insurance> findLastChangeRevision(Long aLong) {
        return null;
    }

    @Override
    public Revisions<Integer, Insurance> findRevisions(Long aLong) {
        return null;
    }

    @Override
    public Page<Revision<Integer, Insurance>> findRevisions(Long aLong, Pageable pageable) {
        return null;
    }

    @Override
    public Revision<Integer, Insurance> findRevision(Long aLong, Integer revisionNumber) {
        return null;
    }
}
