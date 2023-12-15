package ru.softlab.efr.services.insurance.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author olshansky
 * @since 11.12.2018
 */
@Profile("needSaveTemplate")
@Component
public class PrintTemplateSaveServiceImpl implements PrintTemplateSaveService {
    public void save(String fileName, byte[] bytes) throws IOException {
        FileUtils.writeByteArrayToFile(new File("target/".concat(fileName.concat(".pdf"))), bytes);
    }
}
