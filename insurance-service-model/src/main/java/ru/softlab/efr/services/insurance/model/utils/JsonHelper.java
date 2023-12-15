package ru.softlab.efr.services.insurance.model.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Optional;

/**
 * Вспомогательный класс для работы с сериализацией/десериализацией java-объектов в JSON
 * @author gladishev
 * @since 22.08.2017
 */
@Component
public class JsonHelper {

    private ObjectMapper mapper;

    @Autowired
    public void setMapper(RequestMappingHandlerAdapter handlerAdapter) {
        this.mapper = getMapper(handlerAdapter);
        if (mapper == null) {
            throw new ApplicationContextException("Не найден ObjectMapper");
        }
    }

    /**
     * Сериализация объекта
     * @param obj - объект
     * @return объект в JSON-формате
     */
    public String serialize(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
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
    public <T> T deserialize(final String json, Class<T> type) {
        try {
            if (json == null) return null;
            return mapper.readValue(json, type);
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
    public <T> T deserialize(final String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getMapper(RequestMappingHandlerAdapter handlerAdapter) {
        Optional<HttpMessageConverter<?>> converter = handlerAdapter.getMessageConverters()
                        .stream().filter(x -> x.getClass() == MappingJackson2HttpMessageConverter.class).findFirst();
        return converter.isPresent() ? ((MappingJackson2HttpMessageConverter) converter.get()).getObjectMapper(): null;
    }
}
