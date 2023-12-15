package ru.softlab.efr.services.insurance.exception;

/**
 * Для случая когда стутус уже существует в описании переходов в статусной модели
 */
public class StatusAlreadyExistsException extends RuntimeException {

    public StatusAlreadyExistsException() {
    }

    public StatusAlreadyExistsException(String message) {
        super(message);
    }
}
