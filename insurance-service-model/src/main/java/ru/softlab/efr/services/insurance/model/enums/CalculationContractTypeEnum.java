package ru.softlab.efr.services.insurance.model.enums;

/**
 * Тип расчета страховой суммы
 *
 * @author olshansky
 * @since 19.10.2018
 */
public enum CalculationContractTypeEnum {

    SUM("Расчёт по страховой сумме"),

    PREMIUM("Расчёт по страховой премии");

    private String value;

    CalculationContractTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static CalculationContractTypeEnum fromValue(String text) {
        for (CalculationContractTypeEnum b : CalculationContractTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}