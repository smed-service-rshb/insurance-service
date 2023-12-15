package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.softlab.efr.services.insurance.model.db.RedemptionCoefficientEntity;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.repositories.RedemptionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author olshansky
 * @since 05.12.2018
 */
public class RedemptionRepositoryStub implements RedemptionRepository {
    @Override
    public RedemptionEntity findRedemptionEntitiesByIdAndDeleted(Long id, boolean deleted) {
        return null;
    }

    @Override
    public Page<RedemptionEntity> findAll(Pageable pageable, boolean deleted) {
        return null;
    }

    @Override
    public RedemptionCoefficientEntity findRate(Long programId, Integer duration, Long currency, PeriodicityEnum periodicity, Integer numberYear) {
        RedemptionCoefficientEntity redemptionCoefficientEntity = new RedemptionCoefficientEntity();
        redemptionCoefficientEntity.setCoefficient(BigDecimal.valueOf(1.43));
        return redemptionCoefficientEntity;
    }

    @Override
    public RedemptionEntity findRedemption(Long programId, Integer duration, Long currency, PeriodicityEnum periodicity) {
        RedemptionEntity redemptionEntity = new RedemptionEntity();
        redemptionEntity.setPaymentPeriod(periodicity);
        List<RedemptionCoefficientEntity> list = new ArrayList<>();
        int count = 1;
        switch (periodicity) {
            case ONCE:
                break;
            case YEARLY:
                count = duration;
                break;
            case TWICE_A_YEAR:
                count = duration * 2;
                break;
            case QUARTERLY:
                count = duration * 4;
                break;
            case MONTHLY:
                count = duration * 12;
                break;
        }
        for (int i = 0; i < count; i++) {
            list.add(new RedemptionCoefficientEntity());
        }
        redemptionEntity.setCoefficientList(list);
        return redemptionEntity;
    }


    @Override
    public List<RedemptionEntity> findAll() {
        return null;
    }

    @Override
    public List<RedemptionEntity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<RedemptionEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<RedemptionEntity> findAll(Iterable<Long> iterable) {
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
    public void delete(RedemptionEntity redemptionEntity) {

    }

    @Override
    public void delete(Iterable<? extends RedemptionEntity> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends RedemptionEntity> S save(S s) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public RedemptionEntity findOne(Long aLong) {
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
    public <S extends RedemptionEntity> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<RedemptionEntity> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public RedemptionEntity getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends RedemptionEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends RedemptionEntity> boolean exists(Example<S> example) {
        return false;
    }
}
