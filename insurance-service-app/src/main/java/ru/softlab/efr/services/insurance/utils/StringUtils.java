package ru.softlab.efr.services.insurance.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, предоставляющий методы работы со строками.
 *
 * @author gladishev
 * @since 04.06.2018
 */
public class StringUtils {

    private static final String NOT_SPECIFIED = "(нет данных)";
    /**
     * Проверка равенства двух объектов.
     *
     * @param obj1 объект 1
     * @param obj2 объект 2
     * @return true - объекты идентичны
     */
    // TODO: надо это метод вынести из класса с именем StringUtils или переименовать класс.
    public static <T> boolean equals(T obj1, T obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    /**
     * Проверка равенства двух строк без учёта регистра строк.
     *
     * @param str1 строка 1
     * @param str2 строка 2
     * @return true - строки идентичны
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Проверка наличия значения в строке.
     *
     * @param str строка для проверки
     * @return true - пустая строка или null
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().length() == 0;
    }

    public static String getClientFullName(String surname, String firstName, String middleName) {
        if (surname == null) {
            surname = "";
        }
        if (firstName == null) {
            firstName = "";
        }
        return surname.concat(" ")
                .concat(firstName)
                .concat(middleName != null && !middleName.trim().isEmpty() ? " ".concat(middleName) : "");
    }

}
