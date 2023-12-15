package ru.softlab.efr.services.insurance.config;

import org.springframework.web.WebApplicationInitializer;
import ru.softlab.efr.infrastructure.transport.server.init.AbstractTransportInitializer;

/**
 * Класс загрузки конфигурации
 *
 * @author khudyakov
 * @since 25.07.2017
 */
public class TestInitializer extends AbstractTransportInitializer implements WebApplicationInitializer {
    @Override
    protected Class<?> getConfiguration() {
        return TestApplicationConfig.class;
    }
}
