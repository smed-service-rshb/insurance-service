package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.services.RiskSettingService;

/**
 * Контроллер для работы с параметрами риска
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class RiskSettingController {

    private static final Logger LOGGER = Logger.getLogger(RiskSettingController.class);

    @Autowired
    private RiskSettingService service;

}
