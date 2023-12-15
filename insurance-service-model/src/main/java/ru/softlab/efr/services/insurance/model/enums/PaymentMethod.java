package ru.softlab.efr.services.insurance.model.enums;

/**
 * Порядок выплаты по страховому риску
 */
public enum  PaymentMethod {

    ONCE("единовременно"),

    INSTALLMENT("рассроченная выплата"),

    SERVICE("организация предоставления услуг");

    private String value;

    PaymentMethod(String value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    public static PaymentMethod fromValue(String text) {
        for (PaymentMethod b : PaymentMethod.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}