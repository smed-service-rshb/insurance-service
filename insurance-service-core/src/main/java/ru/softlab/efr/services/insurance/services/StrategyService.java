package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.rest.StrategyData;
import ru.softlab.efr.services.insurance.repositories.StrategyRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы со стратегиями программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class StrategyService extends BaseService<Strategy> {

    @Autowired
    public StrategyService(StrategyRepository repository) {
        this.repository = repository;
    }


    public Page<Strategy> findAllExceptDeleted(Pageable pageable) {
        return ((StrategyRepository) repository).findAllByDeleted(pageable, false);
    }

    public List<Strategy> findAllExceptDeleted() {
        return ((StrategyRepository) repository).findAllByDeleted(false);
    }

    public void checkRequest(StrategyData frontStrategy) {
        List<String> errors = new ArrayList<>();
        if (frontStrategy == null) {
            errors.add("Ошибка - пустой запрос создания стратегии.");
        } else {
            if (CollectionUtils.isEmpty(frontStrategy.getStrategyProperties())) {
                errors.add("Не указаны наборы данных для стратегии.");
            } else if (frontStrategy.getStrategyProperties().stream().anyMatch(a -> a.getCoefficient() == null)) {
                errors.add("В одном из наборов не указано обязательное поле - коэффициент участия");
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
