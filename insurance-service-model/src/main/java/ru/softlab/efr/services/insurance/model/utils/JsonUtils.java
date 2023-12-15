package ru.softlab.efr.services.insurance.model.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Вспомогательный класс для работы с сериализацией/десериализацией java-объектов в JSON
 * @author gladishev
 * @since 22.08.2017
 */
public class JsonUtils {

    /**
     * Сериализация объекта
     * @param obj - объект
     * @return объект в JSON-формате
     */
    public static String asJsonString(final Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Преобразует строку в объект
     * @param json строка в формате json
     * @param type class в который преобразуется объект
     * @param <T> тип класса
     * @return преобразованный объект
     */
    public static <T> T fromJsonString(final String json, Class<T> type) {
        try {
            return getMapper().readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Преобразует строку в объект
     * @param json строка в формате json
     * @param type класс, используется для получения полной информации типа генериков
     * @param <T> тип генериков
     * @return преобразованный объект
     */
    public static <T> T fromJsonString(final String json, TypeReference<T> type) {
        try {
            if (json == null) return null;
            return getMapper().readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
