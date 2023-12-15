package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.services.RequiredDocumentSettingService;

/**
 * Контроллер для работы с обязательными полями.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class RequiredFieldController {

    private static final Logger LOGGER = Logger.getLogger(RequiredFieldController.class);

    @Autowired
    private RequiredDocumentSettingService service;

}
