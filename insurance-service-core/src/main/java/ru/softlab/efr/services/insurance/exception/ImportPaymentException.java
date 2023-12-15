package ru.softlab.efr.services.insurance.exception;

/**
 * Ошибка импорта платежа из XLSX файла
 *
 * @author olshansky
 * @since 15.06.2019
 */
public class ImportPaymentException extends Exception {

    /**
     * конструктор
     *
     * @param message сообщение об ошибке
     */
    public ImportPaymentException(String message) {
        super(message);
    }

    /**
     * конструктор
     *
     * @param cause исключение
     */
    public ImportPaymentException(Throwable cause) {
        super(cause);
    }
}
