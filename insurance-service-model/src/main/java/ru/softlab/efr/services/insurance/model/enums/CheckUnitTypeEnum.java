package ru.softlab.efr.services.insurance.model.enums;

/**
 * Тип проверки
 *
 * @author olshansky
 * @since 09.11.2018
 */
public enum CheckUnitTypeEnum {

    BLOCKAGE("Присутствует ли в справочнике заморозок/блокировок"),
    TERRORIST("Присутствует ли в справочнике экстремистов/террористов"),
    INVALID_IDENTITY_DOC("Присутствует ли в справочнике недействительных паспортов");

    private String value;

    CheckUnitTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static CheckUnitTypeEnum fromValue(String text) {
        for (CheckUnitTypeEnum b : CheckUnitTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}