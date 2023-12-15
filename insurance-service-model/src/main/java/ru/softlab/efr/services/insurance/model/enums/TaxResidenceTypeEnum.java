package ru.softlab.efr.services.insurance.model.enums;

/**
 * Налоговое резидентство
 * russian - Налоговый резидент РФ
 * foreign - Налоговый резидент иностранного государства
 * other - Иное
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum TaxResidenceTypeEnum {

    RUSSIAN("Налоговый резидент РФ"),
    FOREIGN("Налоговый резидент иностранного государства"),
    OTHER("Иное");

    private String value;

    TaxResidenceTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static TaxResidenceTypeEnum fromValue(String text) {
        for (TaxResidenceTypeEnum b : TaxResidenceTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
