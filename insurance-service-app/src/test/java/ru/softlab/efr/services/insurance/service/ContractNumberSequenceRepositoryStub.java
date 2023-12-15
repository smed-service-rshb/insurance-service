package ru.softlab.efr.services.insurance.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.softlab.efr.services.insurance.model.db.ContractNumberSequence;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.ContractNumberSequenceRepository;

import java.util.List;


/**
 * @author olshansky
 * @since 19.12.2018
 */
public class ContractNumberSequenceRepositoryStub implements ContractNumberSequenceRepository {

    @Override
    public Long getSequenceValueByProgramKind(ProgramKind programKind) {
        if (programKind == null) {
            return null;
        }
        if (programKind.equals(ProgramKind.RENT)) {
            return 5L;
        }
        if (programKind.equals(ProgramKind.ISJ)) {
            return 2345L;
        }
        if (programKind.equals(ProgramKind.KSP)) {
            return 567867L;
        }
        if (programKind.equals(ProgramKind.NSJ)) {
            return 1234L;
        }
        return null;
    }

    @Override
    public void incrementSequenceValueByProgramKind(ProgramKind programKind) {

    }


    @Override
    public List<ContractNumberSequence> findAll() {
        return null;
    }

    @Override
    public List<ContractNumberSequence> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ContractNumberSequence> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ContractNumberSequence> findAll(Iterable<Long> iterable) {
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
    public void delete(ContractNumberSequence contractNumberSequence) {

    }

    @Override
    public void delete(Iterable<? extends ContractNumberSequence> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends ContractNumberSequence> S save(S s) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public ContractNumberSequence findOne(Long aLong) {
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
    public <S extends ContractNumberSequence> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<ContractNumberSequence> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ContractNumberSequence getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ContractNumberSequence> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ContractNumberSequence> boolean exists(Example<S> example) {
        return false;
    }
}
