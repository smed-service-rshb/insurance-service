package ru.softlab.efr.services.insurance.exception;

/**
 * Исключение получаемое при оформлении договора клиентом
 */
public class AcquiringException extends Exception {

    /**
     * конструктор
     * @param message сообщение об ошибке
     */
    public AcquiringException(String message) {
        super(message);
    }

    /**
     * конструктор
     * @param cause исключение
     */
    public AcquiringException(Throwable cause) {
        super(cause);
    }
}