package ru.softlab.efr.services.insurance.model.enums;

/**
 * Календарная единица срока страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum CalendarUnitEnum {

    YEAR("лет"),

    MONTH("месяцев"),

    DAY("дней");

    private String value;

    CalendarUnitEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static CalendarUnitEnum fromValue(String text) {
        for (CalendarUnitEnum b : CalendarUnitEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}