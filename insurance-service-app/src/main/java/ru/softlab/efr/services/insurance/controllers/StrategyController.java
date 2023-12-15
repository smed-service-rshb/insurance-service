package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.converter.StrategyConverter;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.db.StrategyProperty;
import ru.softlab.efr.services.insurance.model.rest.StrategyData;
import ru.softlab.efr.services.insurance.repositories.StrategyPropertyRepository;
import ru.softlab.efr.services.insurance.services.StrategyService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для работы с стратегиями программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class StrategyController implements StrategiesApi {

    private static final Logger LOGGER = Logger.getLogger(StrategyController.class);

    private StrategyService service;
    private StrategyConverter converter;
    private StrategyPropertyRepository strategyPropertyRepository;


    @Autowired
    public StrategyController(StrategyService service,
                              StrategyConverter converter,
                              StrategyPropertyRepository strategyPropertyRepository) {
        this.converter = converter;
        this.service = service;
        this.strategyPropertyRepository = strategyPropertyRepository;
    }

    @Override
    public ResponseEntity<StrategyData> createStrategy(@RequestBody StrategyData createStrategyRq) throws Exception {
        service.checkRequest(createStrategyRq);
        return ResponseEntity.ok(converter.convert(service.create(converter.convert(createStrategyRq))));
    }

    @Override
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) throws Exception {
        service.logicalDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<StrategyData> getStrategyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(converter.convert(service.findById(id)));
    }

    @Override
    public ResponseEntity<Page<StrategyData>> getStrategyList(@PageableDefault(value = 50) Pageable pageable) throws Exception {
        Page<Strategy> backEntities = service.findAllExceptDeleted(pageable);
        return ResponseEntity.ok(backEntities.map(converter::convert));
    }

    @Override
    public ResponseEntity<List<StrategyData>> getStrategyListByRate(@RequestParam(value = "coefficient") BigDecimal rate) throws Exception {
        List<StrategyProperty> properties = strategyPropertyRepository.getByRate(rate);
        List<StrategyData> responseList = new ArrayList<>();
        for (StrategyProperty property : properties) {
            responseList.add(converter.convert(property.getStrategy()));
        }
        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<StrategyData> updateStrategyById(@PathVariable Long id, @RequestBody StrategyData updateStrategyRq) throws Exception {
        service.checkRequest(updateStrategyRq);
        service.save(converter.convert(updateStrategyRq, service.findById(id)));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
