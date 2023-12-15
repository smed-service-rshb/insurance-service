package ru.softlab.efr.services.insurance.model.enums;

/**
 * «Тип расчета». Выбор из значений: "постоянная", "убывающая", "возрастающая".
 * Если «Тип расчета» = «постоянная», то необходимо задать значение:
 * Коэффициент для расчета СС.  Обязательно для задания.
 *
 * @author olshansky
 * @since 19.10.2018
 */
public enum RiskCalculationSumTypeEnum {

    CONSTANT("постоянная"),

    DECREASING("убывающая"),

    INCREASING("возрастающая");

    private String value;

    RiskCalculationSumTypeEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static RiskCalculationSumTypeEnum fromValue(String text) {
        for (RiskCalculationSumTypeEnum b : RiskCalculationSumTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}