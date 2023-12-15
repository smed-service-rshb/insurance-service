package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.RedemptionCoefficientEntity;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.repositories.RedemptionRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

/**
 * Сервис для работы со справочником выкупных коэффициентов.
 *
 * @author Kalantaev
 * @since 29.11.2018
 */
@Service
public class RedemptionService {

    private final RedemptionRepository redemptionRepository;

    @Autowired
    public RedemptionService(RedemptionRepository redemptionRepository) {
        this.redemptionRepository = redemptionRepository;
    }

    /**
     * Создать запись в справочнике.
     *
     * @param entity сущность для сохранения
     * @return запись справочника
     */
    @Transactional
    public RedemptionEntity create(RedemptionEntity entity) {
        return redemptionRepository.save(entity);
    }

    /**
     * Получить запись из справочника по указанному идентификатору.
     *
     * @param id идентификатор записи
     * @return запись справочника
     */
    @Transactional(readOnly = true)
    public RedemptionEntity findById(Long id) {
        return redemptionRepository.findRedemptionEntitiesByIdAndDeleted(id, false);
    }

    /**
     * Обновить запись в справочнике.
     *
     * @param document запись в справочнике
     * @return обновленная запись
     */
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public RedemptionEntity update(RedemptionEntity document) {
        RedemptionEntity entity = redemptionRepository.findRedemptionEntitiesByIdAndDeleted(document.getId(), false);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return redemptionRepository.save(document);
    }

    /**
     * Получить постанично все неудаленные записи справочника.
     *
     * @param pageable параметры постраничного вывода
     * @return страница с записями справочника
     */
    @Transactional(readOnly = true)
    public Page<RedemptionEntity> findAll(Pageable pageable) {
        return redemptionRepository.findAll(pageable, false);
    }

    /**
     * Логическое удаление сущности записи справочника
     *
     * @param id идентификатор сущности
     * @return true запись удалена, false - запись не удалена
     */
    @Transactional
    public boolean logicalDelete(Long id) {
        RedemptionEntity entity = redemptionRepository.findOne(id);
        if (entity == null || entity.getDeleted()) {
            throw new EntityNotFoundException();
        }
        entity.setDeleted(true);
        redemptionRepository.save(entity);
        return true;
    }

    /**
     * Найти коэффициент расчёта выкупных сумм.
     *
     * @param programId   Идентификатор программы страхования.
     * @param duration    Срок действия договора страхования.
     * @param currency    Валюта договора.
     * @param periodicity Периодичность уплаты взносов.
     * @param numberYear  Период расчёта.
     * @return Коэффициент расчёта выкупных сумм.
     */
    @Transactional(readOnly = true)
    public BigDecimal findRate(Long programId, Integer duration, Long currency, PeriodicityEnum periodicity, Integer numberYear) {
        RedemptionCoefficientEntity entity = redemptionRepository.findRate(programId, duration, currency, periodicity, numberYear);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity.getCoefficient();
    }

    /**
     * Найти запись справочника выкупных коэффициентов.
     *
     * @param programId   Идентификатор программы страхования.
     * @param duration    Срок действия договора страхования.
     * @param currency    Валюта договора.
     * @param periodicity Периодичность уплаты взносов.
     * @return Запись справочника.
     */
    @Transactional(readOnly = true)
    public RedemptionEntity findRedemption(Long programId, Integer duration, Long currency, PeriodicityEnum periodicity) {
        return redemptionRepository.findRedemption(programId, duration, currency, periodicity);
    }

}
