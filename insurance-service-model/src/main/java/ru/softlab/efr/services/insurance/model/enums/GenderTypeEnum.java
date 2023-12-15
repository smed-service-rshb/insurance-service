package ru.softlab.efr.services.insurance.model.enums;

/**
 * Пол застрахованного
 *
 * Возможны следующие типы расчета:
 * - мужской
 * - женский
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum GenderTypeEnum {

    MALE("мужской"),

    FEMALE("женский");

    private String value;

    GenderTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static GenderTypeEnum fromValue(String text) {
        for (GenderTypeEnum b : GenderTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}