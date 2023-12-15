package ru.softlab.efr.services.insurance.exception;


import java.util.List;

/**
 * Исключение, которое бросается конвертерами в случае, когда конвертация не может
 * быть выполнена из-за невалидных данных в объекте, содержащем данные для конвертации.
 */
public class ValidationException extends RuntimeException {

    private List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
