package ru.softlab.efr.services.insurance.controllers;

import java.time.LocalDate;

/**
 * Константы
 * @author gladishev
 * @since 04.05.2017
 */
public class Constants {
    public static final String CLIENT = "client";
    public static final String FIRST_NAME = "firstName";
    public static final String SUR_NAME = "surName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String GENDER = "gender";
    public static final String BIRTH_DATE = "birthDate";
    public static final String PAST = "past";
    public static final String AFTER = "after";
    public static final String BIRTHPLACE = "birthPlace";
    public static final String MOBILE_PHONE = "mobilePhone";
    public static final String LATIN_NAME = "latinName";
    public static final String CODE_WORD = "codeWord";
    public static final String NAME_AND_SURNAME = "nameAndSurName";
    public static final String CITIZENSHIP = "Citizenship";
    public static final String CITIZENSHIP_COUNTRY = "CitizenshipCountry";
    public static final String FOREIGN_PUBLIC_OFFICIAL_TYPE = "foreignPublicOfficialType";
    public static final String RUSSIAN_PUBLIC_OFFICIAL_TYPE = "russianPublicOfficialType";
    public static final String PUBLIC_OFFICIAL_POSITION = "publicOfficialPosition";
    public static final String PUBLIC_OFFICIAL_NAME_AND_POSITION = "publicOfficialNameAndPosition";
    public static final String RELATIONS = "relations";
    public static final String CHANGE_REPORT_STORE_PATH = "ChangeReportStorePath";
    public static final String UPLOAD_CHANGES_RESULT_STORE_PATH = "UploadChangesResultStorePath";

    public static final String EMPTY = "empty";

    public static final String PHONES = "phones";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";

    public static final String AGREEMENTS = "agreements";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    public static final String NATIONALITIES = "nationalities";
    public static final String INCORRECT = ".incorrect";
    public static final String INVALIDATE = ".invalidate";

    public static final String DOCUMENTS = "documents";
    public static final String MAIN_REQUIRED = "main.required";
    public static final String DOC_TYPE = "docType";
    public static final String DOC_SERIES = "docSeries";
    public static final String DOC_NUMBER = "docNumber";
    public static final String ISSUED_BY = "issuedBy";
    public static final String ISSUED_DATE = "issuedDate";
    public static final String DIVISION_CODE = "divisionCode";
    public static final String ISSUED_END_DATE = "issuedEndDate";
    public static final String SNILS = "snils";
    public static final String INN = "inn";
    public static final String PAYER_NUMBER = "payerNumber";
    public static final String LICENSE_DRIVER = "licenseDriver";
    public static final String STAY_START_DATE = "stayStartDate";
    public static final String STAY_END_DATE = "stayEndDate";
    public static final String EMAIL = "email";

    public static final String ADDRESSES = "addresses";
    public static final String ADDRESS_TYPE = "addressType";
    public static final String COUNTRY = "country";
    public static final String REGION = "region";
    public static final String AREA = "area";
    public static final String CITY = "city";
    public static final String LOCALITY = "locality";
    public static final String STREET = "street";
    public static final String HOUSE = "house";
    public static final String CONSTRUCTION = "construction";
    public static final String HOUSING = "housing";
    public static final String APARTMENT = "apartment";
    public static final String INDEX = "index";
    public static final String REGISTRATION_PERIOD_START = "registrationPeriodStart";
    public static final String REGISTRATION_PERIOD_END = "registrationPeriodEnd";

    public static final String MAX_FOUND_CLIENT_COUNT_SETTING = "maxFoundClientCount";
    public static final String CLIENT_CHECK_BATCH_SIZE = "clientCheckBatchSize";
    public static final String SECURITY_REPORT_STORE_PATH = "SecurityReportStorePath";
    public static final String CLIENT_DUPLICATE_NOTIFICATION_EMAIL = "clientDuplicateNotificationEmail";
    public static final String EMAIL_TEXT_CONTENT_TYPE = "text/html";


    public static final String SS = ".%s";
    public static final String VALIDATION_S_MESSAGES = "validation.%s.message";
    public static final String ADDRESSES_S = getSS(ADDRESSES, SS);
    public static final String PHONES_S= getSS(PHONES, SS);
    public static final String DOCUMENTS_S = getSS(DOCUMENTS, SS);
    public static final String NATIONALITIES_S = getSS(NATIONALITIES, SS);
    public static final String AGREEMENTS_S = getSS(AGREEMENTS, SS);
    public static final String BIRTH_DATE_S = getSS(BIRTH_DATE, SS);

    public static final String VALIDATION_CLIENT_S_MESSAGES = getStringS(VALIDATION_S_MESSAGES, getSS(CLIENT, SS));

    public static final LocalDate FROM_DATE = LocalDate.of(1890, 1, 1);
    public static final String PHONE_VALIDATOR_REGEX = "\\d{11}";

    private static final String CHECK_CLIENT_DECISION_S_ERROR = "check.client.decision.%s.error";

    /**
     * Формирование строки согласно шаблона
     * @param field значения параметра шаблона
     * @return строка
     */
    public static String getValidationClientSMessages(String field){
        return String.format(VALIDATION_CLIENT_S_MESSAGES, field);
    }

    /**
     * Формирование строки согласно шаблона
     * @param field значения параметра шаблона
     * @return строка
     */
    public static String getCheckClientDecisionSError(String field){
        return String.format(CHECK_CLIENT_DECISION_S_ERROR, field);
    }

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
     * @param path значения параметра шаблона
     * @param index значения параметра шаблона
     * @param s значения параметра шаблона
     * @return строка
     */
    public static String getStringSS(String path, int index, String s) {
        return String.format(path, index, s);
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

    /**
     * Формирование строки согласно шаблона
     * @param s1 значения параметра шаблона
     * @param s2 значения параметра шаблона
     * @return строка
     */
    public static String getSPointS(String s1, String s2){
        return String.format("%s.%s", s1, s2);
    }
}
