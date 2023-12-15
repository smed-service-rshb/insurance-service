package ru.softlab.efr.services.insurance.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;
import ru.softlab.efr.services.insurance.model.rest.StrategyData;
import ru.softlab.efr.services.insurance.model.rest.StrategyProperty;
import ru.softlab.efr.services.insurance.services.StrategyService;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Конвертер представления сущности стратегий
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class StrategyConverter {

    private final StrategyService strategyService;

    @Autowired
    public StrategyConverter(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    /**
     * Преобразование стратегии программ страхования из представления БД в представление API
     *
     * @param backEntity сущность стратегии программ страхования в представлении БД
     * @return сущность стратегии программ страхования в представлении API
     */
    public StrategyData convert(Strategy backEntity) {
        StrategyData frontEntity = new StrategyData();
        frontEntity.setId(backEntity.getId());
        frontEntity.setStartDate(AppUtils.mapTimestamp2LocalDate(backEntity.getStartDate()));
        frontEntity.setEndDate(AppUtils.mapTimestamp2LocalDate(backEntity.getEndDate()));
        frontEntity.setName(backEntity.getName());
        frontEntity.setDescription(backEntity.getDescription());
        frontEntity.setPolicyCode(backEntity.getPolicyCode());
        frontEntity.setType(backEntity.getStrategyType() != null ? ru.softlab.efr.services.insurance.model.rest.StrategyType.valueOf(backEntity.getStrategyType().name()) : null);
        if (!CollectionUtils.isEmpty(backEntity.getStrategyProperties())) {
            frontEntity.setStrategyProperties(backEntity.getStrategyProperties().stream().
                    map(StrategyConverter::getStrategyProperty).collect(Collectors.toList()));
        }
        return frontEntity;
    }

    public static StrategyProperty getStrategyProperty(ru.softlab.efr.services.insurance.model.db.StrategyProperty m) {
        return new StrategyProperty(m.getId(),
                m.getRate(),
                m.getTicker(),
                m.getExpirationDate() != null ? AppUtils.mapTimestamp2LocalDate(m.getExpirationDate()) : null,
                m.getNzbaDate() != null ? AppUtils.mapTimestamp2LocalDate(m.getNzbaDate()) : null);
    }

    public Strategy convert(StrategyData frontEntity) {
        Strategy backEntity = new Strategy();
        return convert(frontEntity, backEntity);
    }

    public Strategy convert(StrategyData frontEntity, Strategy backEntity) {
        if (backEntity.getId() == null) {
            backEntity.setId(frontEntity.getId());
        }
        backEntity.setStartDate(AppUtils.mapLocalDate2Timestamp(frontEntity.getStartDate()));
        backEntity.setEndDate(AppUtils.mapLocalDate2Timestamp(frontEntity.getEndDate()));
        backEntity.setName(frontEntity.getName());
        backEntity.setDescription(frontEntity.getDescription());
        backEntity.setPolicyCode(frontEntity.getPolicyCode());
        backEntity.setStrategyProperties(updateStrategyProperties(frontEntity, backEntity));
        backEntity.setDeleted(false);
        backEntity.setStrategyType(frontEntity.getType() != null ? StrategyType.valueOf(frontEntity.getType().name()) : null);
        return backEntity;
    }

    private ru.softlab.efr.services.insurance.model.db.StrategyProperty mapBackStrategyProperties(StrategyProperty frontStrategyProperty, Strategy backEntity) {
        return new ru.softlab.efr.services.insurance.model.db.StrategyProperty(
                frontStrategyProperty.getId(),
                backEntity,
                frontStrategyProperty.getCoefficient(),
                frontStrategyProperty.getTicker(),
                frontStrategyProperty.getExpirationDate() != null ? AppUtils.mapLocalDate2Timestamp(frontStrategyProperty.getExpirationDate()) : null,
                frontStrategyProperty.getNzbaDate() != null ? AppUtils.mapLocalDate2Timestamp(frontStrategyProperty.getNzbaDate()) : null
        );
    }

    /**
     * Обновить наборы данных стратегии
     *
     * @param frontStrategy
     */
    public List<ru.softlab.efr.services.insurance.model.db.StrategyProperty> updateStrategyProperties(StrategyData frontStrategy, Strategy backStrategy) {
        if (frontStrategy.getId() != null) {
            Strategy existedBackStrategy = strategyService.findById(frontStrategy.getId());
            if (!CollectionUtils.isEmpty(frontStrategy.getStrategyProperties())) {
                return frontStrategy.getStrategyProperties()
                        .stream()
                        .map(x -> {
                            Optional<ru.softlab.efr.services.insurance.model.db.StrategyProperty> orig = existedBackStrategy.getStrategyProperties().stream()
                                    .filter(el -> el.getId().equals(x.getId())).findFirst();
                            if (orig.isPresent()) {
                                ru.softlab.efr.services.insurance.model.db.StrategyProperty strategyProperty = orig.get();

                                strategyProperty.setExpirationDate(x.getExpirationDate() != null ? AppUtils.mapLocalDate2Timestamp(x.getExpirationDate()) : null);
                                strategyProperty.setNzbaDate(x.getNzbaDate() != null ? AppUtils.mapLocalDate2Timestamp(x.getNzbaDate()) : null);
                                strategyProperty.setTicker(x.getTicker());
                                strategyProperty.setRate(x.getCoefficient());

                                return strategyProperty;
                            } else {
                                return mapBackStrategyProperties(x, existedBackStrategy);
                            }
                        })
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
        return frontStrategy.getStrategyProperties().stream().map(m-> mapBackStrategyProperties(m, backStrategy)).collect(Collectors.toList());
    }
}
