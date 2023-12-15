package ru.softlab.efr.services.insurance.model.utils;

/**
 * Утилитный метод предоставляющий методы работы со строками
 * @author gladishev
 * @since 04.06.2018
 */
public class StringUtils {

    /**
     * Проверка равенства двух строк
     * @param str1 строка 1
     * @param str2 строка 2
     * @return true - строки идентичны
     */
    public static boolean compare(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }

}
