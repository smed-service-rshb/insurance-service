package ru.softlab.efr.services.insurance.model.enums;

/**
 * Результат проверки
 *
 * @author olshansky
 * @since 09.11.2018
 */
public enum CheckStateEnum {

    TRUE("Объект не прошёл проверку (к примеру, клиент входит в справочник террористов)"),
    FALSE("Объект прошёл проверку (к примеру, клиент не входит в справочник террористов)"),
    NEED_EMPLOYEE_DECISION("Требуется перепроверка сотрудником"),
    EMPLOYEE_TRUE("Сотрудник указал, что объект не прошёл проверку (к примеру, клиент входит в справочник террористов)"),
    EMPLOYEE_FALSE("Сотрудник указал, что объект прошёл проверку (к примеру, клиент не входит в справочник террористов)");
    private String value;

    CheckStateEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static CheckStateEnum fromValue(String text) {
        for (CheckStateEnum b : CheckStateEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}