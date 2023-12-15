package ru.softlab.efr.services.insurance.pojo;

import java.text.MessageFormat;

/**
 * Представляет результаты выполнения операции
 */
public class Result {

    private final Boolean value;

    private Boolean failError = false;

    private String errorCode;

    private final StringBuffer description = new StringBuffer();

    private Result(Boolean value) {
        this.value = value;
    }

    public Result alterDescription(String description) {
        setDescription(description);
        return this;
    }

    public Result alterDescription(String description, Object... params) {
        setDescription(MessageFormat.format(description, params));
        return this;
    }

    /**
     * @return true, если операция была удачной
     */
    public boolean isSuccess() {
        return value;
    }

    /**
     * @return true, если операция была неудачной (в т.ч. в следствие ошибки)
     */
    public boolean isFail() {
        return !value;
    }

    /**
     * @return true, если операция была неудачной, в следствие ошибки
     */
    public boolean isFailError() {
        return failError;
    }

    /**
     * Возвращает объект для случая удачного выполнения операции
     *
     * @return результат удачной операции
     */
    public static Result success() {
        return new Result(Boolean.TRUE);
    }

    /**
     * Возвращает объект с сообщением для случая неудачной операции
     *
     * @param description форматная строка, объясняющая причину неудачной операции
     * @param params      набор параметров, которые будут подставлены в форматную строку
     * @return результат неудачной операции
     */
    public static Result fail(String description, Object... params) {
        Result result = new Result(Boolean.FALSE);
        result.alterDescription(description, params);
        return result;
    }

    public static Result fail(Result result, String delimeter, String description, Object... params) {
        if (result == null) {
            result = new Result(Boolean.FALSE);
        }
        if (result.isSuccess()) {
            result = new Result(Boolean.FALSE);
        }
        result.appendDescription(description, delimeter, params);
        return result;
    }

    /**
     * Возвращает объект с сообщением для случая если операция не выполнена в следствие ошибки
     *
     * @param description форматная строка, содержащая ошибку
     * @param params      набор параметров, которые будут подставлены в форматную строку
     * @return информация об ошибке
     */
    public static Result failError(String errorCode, String description, Object... params) {
        Result result = new Result(Boolean.FALSE);
        result.alterDescription(description, params);
        result.setFailError(true);
        result.setErrorCode(errorCode);
        return result;
    }

    private void appendDescription(String description2, String delimiter, Object[] params) {
        if (description.length() > 0)
            description.append(delimiter);

        description.append(MessageFormat.format(description2, params));
    }

    /**
     * Возвращает объект без сообщения для случая неудачной операции.
     *
     * @return результат неудачной проверки
     */
    public static Result fail() {
        return new Result(Boolean.FALSE);
    }

    public Boolean getValue() {
        return value;
    }

    public String getDescription() {
        return description.toString();
    }

    public void setDescription(String description) {
        this.description.append(description);
    }

    public void setFailError(Boolean failError) {
        this.failError = failError;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
