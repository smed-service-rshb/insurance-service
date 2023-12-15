package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.services.SegmentService;

/**
 * Контроллер для работы с сегментами программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
@RestController
public class SegmentController {

    private static final Logger LOGGER = Logger.getLogger(SegmentController.class);

    @Autowired
    private SegmentService service;

}
