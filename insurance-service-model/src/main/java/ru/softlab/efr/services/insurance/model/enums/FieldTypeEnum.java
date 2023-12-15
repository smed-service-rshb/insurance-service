package ru.softlab.efr.services.insurance.model.enums;

/**
 * Типы обязательных полей
 * Возможны следующие значения:
 * 1. Набор полей
 * 2. Поле
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum FieldTypeEnum {

    FIELD_SET("Набор полей"),

    FIELD("Поле");

    private String value;

    FieldTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static FieldTypeEnum fromValue(String text) {
        for (FieldTypeEnum b : FieldTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}