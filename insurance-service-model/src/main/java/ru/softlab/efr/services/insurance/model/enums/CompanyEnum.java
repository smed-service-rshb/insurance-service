package ru.softlab.efr.services.insurance.model.enums;

/**
 * Компании
 *
 * @author olshansky
 * @since 10.11.2019
 */
public enum CompanyEnum {

    RSHBINSLIFE("РСХБ-Страхование жизни"),
    RSHBINS("РСХБ-Страхование"),
    SMS("СоюзМедСервис");
    private String value;

    CompanyEnum(String value) {
        this.value = value;
    }

    public static CompanyEnum fromValue(String text) {
        for (CompanyEnum b : CompanyEnum.values()) {
            if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String toString() {
        return String.valueOf(value);
    }
}