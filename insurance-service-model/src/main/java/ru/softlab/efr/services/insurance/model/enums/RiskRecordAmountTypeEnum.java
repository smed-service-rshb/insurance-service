package ru.softlab.efr.services.insurance.model.enums;

/**
 * Учет суммы премии
 *
 * @author olshansky
 * @since 19.10.2018
 */
public enum RiskRecordAmountTypeEnum {

    TOTAL_PREMIUM("от полной премии"),
    SINGLE_PREMIUM("от разовой премии");

    private String value;

    RiskRecordAmountTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static RiskRecordAmountTypeEnum fromValue(String text) {
        for (RiskRecordAmountTypeEnum b : RiskRecordAmountTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}