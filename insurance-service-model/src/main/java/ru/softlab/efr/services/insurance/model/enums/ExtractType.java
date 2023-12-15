package ru.softlab.efr.services.insurance.model.enums;

public enum ExtractType {

    /**
     * Отчет по продажам
     */
    SALE_REPORT,

    /**
     * Универсальный отчет
     */
    UNIVERSAL_REPORT,

    /**
     * Отчет по изменениям
     */
    CHANGE_REPORT,
    /**
     * Отчёт о налоговых нерезидентах
     */
    NON_RESIDENT_REPORT,
    /**
     * Импорт договоров
     */
    IMPORT_INSURANCE,

    /**
     * Импорт статусов по договорам страхования
     */
    IMPORT_INSURANCE_CONTRACT_STATUSES
}
