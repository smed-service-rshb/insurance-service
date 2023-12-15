package ru.softlab.efr.services.insurance.model.enums;

/**
 * Статус банкрота
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum BankruptcyInfoEnum {

    BANKRUPT("bankrupt"),
    NOT_BANKRUPT("not_bankrupt"),
    MAYBE_BANKRUPT("maybe_bankrupt"),
    UNCLASSIFIED_STAGE("unclassified_stage");

    private String value;

    BankruptcyInfoEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static BankruptcyInfoEnum fromValue(String text) {
        for (BankruptcyInfoEnum b : BankruptcyInfoEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
