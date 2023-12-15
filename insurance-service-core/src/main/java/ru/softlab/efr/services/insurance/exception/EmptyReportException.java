package ru.softlab.efr.services.insurance.exception;

/**
 * Ошибка, возбуждаемая при отсутствии данных для формирования отчёта
 *
 * @author olshansky
 * @since 01.02.2019
 */
public class EmptyReportException extends Exception {

    /**
     * конструктор
     * @param message сообщение об ошибке
     */
    public EmptyReportException(String message) {
        super(message);
    }

    /**
     * конструктор
     * @param cause исключение
     */
    public EmptyReportException(Throwable cause) {
        super(cause);
    }
}
