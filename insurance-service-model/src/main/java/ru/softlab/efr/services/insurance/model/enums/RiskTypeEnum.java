package ru.softlab.efr.services.insurance.model.enums;

/**
 * Вид риска
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum RiskTypeEnum {

    REQUIRED("Обязательный"),

    ADDITIONAL("Дополнительный");

    private String value;

    RiskTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static RiskTypeEnum fromValue(String text) {
        for (RiskTypeEnum b : RiskTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}