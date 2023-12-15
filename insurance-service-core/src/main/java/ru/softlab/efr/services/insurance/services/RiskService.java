package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Risk;
import ru.softlab.efr.services.insurance.repositories.RiskRepository;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

/**
 * Сервис для работы со справочником рисков.
 *
 * @author Kalantaev
 * @since 02.10.2018
 */
@Service
public class RiskService {

    @Resource
    private RiskRepository riskRepository;

    /**
     * Создание записи в справочнике рисков.
     *
     * @param risk сущность риска
     * @return сохраненная запись справочника
     */
    @Transactional
    public Risk create(Risk risk) {
        return riskRepository.save(risk);
    }

    /**
     * Получить запись из справочника рисков по указанному идентификатору.
     *
     * @param id идентификатор записи
     * @return запись справочника рисков
     */
    @Transactional(readOnly = true)
    public Risk findById(Long id) {
        return riskRepository.findRiskByIdAndDeleted(id, false);
    }

    /**
     * Обновить запись в справочнике рисков.
     *
     * @param document запись в справочнике рисков
     * @return обновленная запись
     */
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Risk update(Risk document) {
        Risk risk = riskRepository.findRiskByIdAndDeleted(document.getId(), false);
        if (risk == null) {
            throw new EntityNotFoundException();
        }
        return riskRepository.save(document);
    }

    /**
     * Получить постанично все неудаленные записи справочника рисков.
     *
     * @param pageable параметры постраничного вывода
     * @return страница с записями справочника
     */
    @Transactional(readOnly = true)
    public Page<Risk> findAll(Pageable pageable) {
        return riskRepository.findAll(pageable, false);
    }
}
