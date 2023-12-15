package ru.softlab.efr.services.insurance.utils;

import java.io.IOException;

/**
 * @author olshansky
 * @since 11.12.2018
 */
public interface PrintTemplateSaveService {
    void save(String fileName, byte[] bytes) throws IOException;
}
