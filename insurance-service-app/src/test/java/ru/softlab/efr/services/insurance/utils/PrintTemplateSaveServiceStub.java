package ru.softlab.efr.services.insurance.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author olshansky
 * @since 11.12.2018
 */
@Profile("!needSaveTemplate")
@Component
public class PrintTemplateSaveServiceStub implements PrintTemplateSaveService {
    public void save(String fileName, byte[] bytes) throws IOException {
    }
}
