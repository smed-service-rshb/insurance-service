package ru.softlab.efr.services.insurance.model.enums;

/**
 * Календарная единица срока страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum PeriodicityEnum {

    ONCE("единовременно"),

    MONTHLY("ежемесячно"),

    QUARTERLY("ежеквартально"),

    TWICE_A_YEAR("раз в полгода"),

    YEARLY("ежегодно");

    private String value;

    PeriodicityEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static PeriodicityEnum fromValue(String text) {
        for (PeriodicityEnum b : PeriodicityEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}