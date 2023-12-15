package ru.softlab.efr.services.insurance.exception;

/**
 * Ошибка взаимодействия между микросервисами
 *
 * @author krivenko
 * @since 21.12.2018
 */
public class InteractionException extends RuntimeException {

    /**
     * конструктор
     * @param message сообщение об ошибке
     */
    public InteractionException(String message) {
        super(message);
    }

    /**
     * конструктор
     * @param cause исключение
     */
    public InteractionException(Throwable cause) {
        super(cause);
    }
}
