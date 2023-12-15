package ru.softlab.efr.services.insurance.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Набор утилитарных методов, использующихся при написании тестов.
 *
 * @author Andrey Grigorov
 */
public class TestUtils {

    /**
     * Маршалинг объекта в json-документ, представленный в виде массива байт.
     *
     * @param obj объект
     * @return строка, содержащая json-представление объекта
     * @throws IOException ошибка при маршалинге
     */
    public static String convertObjectToJson(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JacksonAnnotationIntrospector jacksonAnnotationIntrospector = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(jacksonAnnotationIntrospector);
        return mapper.writeValueAsString(obj);
    }

    /**
     * Получение значения json path выражения, вычисленного на json'е, возвращённом в ответе на запрос.
     *
     * @param resultActions Результат выполнения запроса.
     * @param expression    Json path выражение. Пример: <code>$.id</code>.
     * @param <T>           Тип результата.
     * @return Результат вычисления json path на json'е, возвращённом в ответе на запрос.
     * @throws IOException Ошибка при вычисления json path.
     */
    public static <T> T extractDataFromResultJson(ResultActions resultActions, String expression) throws IOException {
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JsonPath jsonPath = JsonPath.compile(expression);
        return jsonPath.read(contentAsString);
    }

}
