package ru.softlab.efr.services.insurance.config;

import org.springframework.web.WebApplicationInitializer;
import ru.softlab.efr.infrastructure.transport.server.init.AbstractTransportInitializer;

/**
 * Загрузчик контекста сервлета
 *
 * @author khudyakov
 * @since 28.07.2017
 */
public class ApplicationInitializer extends AbstractTransportInitializer implements WebApplicationInitializer {
    protected Class<?> getConfiguration() {
        return ApplicationConfig.class;
    }
}