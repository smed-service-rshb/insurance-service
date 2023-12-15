package ru.softlab.efr.services.insurance.model.enums;

/**
 * Уровень риска
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum RiskLevelEnum {

    NORMAL("normal"),
    HIGH("high");

    private String value;

    RiskLevelEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static RiskLevelEnum fromValue(String text) {
        for (RiskLevelEnum b : RiskLevelEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
