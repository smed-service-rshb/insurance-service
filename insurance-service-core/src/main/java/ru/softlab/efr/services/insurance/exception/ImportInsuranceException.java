package ru.softlab.efr.services.insurance.exception;

public class ImportInsuranceException extends Exception {

    /**
     * конструктор
     *
     * @param message сообщение об ошибке
     */
    public ImportInsuranceException(String message) {
        super(message);
    }

    /**
     * конструктор
     *
     * @param cause исключение
     */
    public ImportInsuranceException(Throwable cause) {
        super(cause);
    }
}
