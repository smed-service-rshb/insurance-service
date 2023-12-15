package ru.softlab.efr.services.insurance.model.enums;

/**
 * Тип расчета страховой суммы и страховой премии для риска.
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum RiskCalculationTypeEnum {

    NOT_CALCULATED("Не рассчитывается"),
    FIXED("Фиксированные значения"),
    DEPENDS_ON_RISK("Зависит от СС другого риска"),
    DEPENDS_ON_CONTRACT_SUM("Зависит от СС договора"),
    DEPENDS_ON_CONTRACT_PREMIUM("Зависит от СП договора"),
    BY_FORMULA("По формуле"),
    BY_COMPLEX_FORMULA("По сложной формуле"),
    DEPENDS_ON_TERM("Зависит от срока выплаты");

    private String value;

    RiskCalculationTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static RiskCalculationTypeEnum fromValue(String text) {
        for (RiskCalculationTypeEnum b : RiskCalculationTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}