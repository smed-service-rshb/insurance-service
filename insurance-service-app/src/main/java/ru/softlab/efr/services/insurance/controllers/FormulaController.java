package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.services.FormulaService;

/**
 * Контроллер для работы с формулами
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class FormulaController {

    private static final Logger LOGGER = Logger.getLogger(FormulaController.class);

    @Autowired
    private FormulaService service;

}
