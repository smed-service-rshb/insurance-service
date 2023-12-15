package ru.softlab.efr.services.insurance.exception;

/**
 * Внутренняя ошибка приложения
 *
 * @author gladishev
 * @since 18.04.2018
 */
public class InternalException extends Exception {

    /**
     * конструктор
     * @param message сообщение об ошибке
     */
    public InternalException(String message) {
        super(message);
    }

    /**
     * конструктор
     * @param cause исключение
     */
    public InternalException(Throwable cause) {
        super(cause);
    }
}
