package ru.softlab.efr.services.insurance.model.enums;

/**
 * Вид андеррайтинга
 * Возможны следующие значения:
 * 1. Без заявления
 * 2. Декларация
 * 3. Заполнение заявления (анкета МЕДО)
 * 4. Проведение МЕДО,
 * Значение по умолчанию - «Без заявления».
 *
 * @author olshansky
 * @since 14.10.2018
 */
public enum UnderwritingEnum {

    NO_STATEMENT("Без заявления"),

    DECLARATION("Декларация"),

    APPLICATION("Заполнение заявления (анкета МЕДО)"),

    MEDO("Проведение МЕДО");

    private String value;

    UnderwritingEnum(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static UnderwritingEnum fromValue(String text) {
        for (UnderwritingEnum b : UnderwritingEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}