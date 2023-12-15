package ru.softlab.efr.services.insurance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Константы
 * @author gladishev
 * @since 04.05.2017
 */
public class Constants {

    public static final String UPLOAD_CHANGES_RESULT_STORE_PATH = "UploadChangesResultStorePath";
    public static final String DEFAULT_REPORT_RESULT_STORE_PATH = System.getProperty("java.io.tmpdir");
    public static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DDMMYYYYHHMMSS = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static final String FIRST_NAME = "firstName";
    public static final String SUR_NAME = "surName";
    public static final String BIRTH_DATE = "birthDate";
    public static final String PAST = "past";
    public static final String AFTER = "after";
    public static final String NAME_AND_SURNAME = "nameAndSurName";

    public static final String EMPTY = "empty";

    public static final String DOC_TYPE = "docType";
    public static final String DOC_SERIES = "docSeries";
    public static final String DOC_NUMBER = "docNumber";

    public static final String SS = ".%s";
    public static final String VALIDATION_S_MESSAGES = "validation.%s.message";
    public static final String BIRTH_DATE_S = getSS(BIRTH_DATE, SS);

    public static final LocalDate FROM_DATE = LocalDate.of(1890, 1, 1);

    /**
     * Формирование строки согласно шаблона
     * @param s1 значения параметра шаблона
     * @param s2 значения параметра шаблона
     * @return строка
     */
    public static String getStringS(String s1, String s2){
        return String.format(s1, s2);
    }


    /**
     * Формирование строки согласно шаблона
     * @param s1 значения параметра шаблона
     * @param s2 значения параметра шаблона
     * @return строка
     */
    public static String getSS(String s1, String s2){
        return String.format("%s%s", s1, s2);
    }

}
