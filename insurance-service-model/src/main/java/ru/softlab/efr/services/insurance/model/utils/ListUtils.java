package ru.softlab.efr.services.insurance.model.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Утилитный класс для работы со списками
 * @author gladishev
 * @since 05.10.2017
 */
public class ListUtils {

    /**
     * Конвертация списка
     * @param from список
     * @param func функция конвертации
     * @param <T> - тип элементов списка
     * @param <U> - тип элементов, возвращаемых в результатах метода
     * @return список сконвертированных элементов
     */
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        if (CollectionUtils.isEmpty(from)) {
            return Collections.emptyList();
        }
        return from.stream().map(func).collect(Collectors.toList());
    }
}
