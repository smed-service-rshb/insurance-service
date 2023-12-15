package ru.softlab.efr.services.insurance.model.enums;

/**
 * Виды дней
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum DayTypeEnum {

    CALENDAR("Календарные дни"),
    WORKDAY("Рабочие дни");

    private String value;

    DayTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static DayTypeEnum fromValue(String text) {
        for (DayTypeEnum b : DayTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}