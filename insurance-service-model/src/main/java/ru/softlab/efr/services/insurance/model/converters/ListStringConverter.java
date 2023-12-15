package ru.softlab.efr.services.insurance.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.model.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * Конвертор списка строк
 * @author basharin
 * @since 08.02.2018
 */
@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> results) {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }

        return JsonUtils.asJsonString(results);
    }

    @Override
    public List<String> convertToEntityAttribute(String results) {
        if (StringUtils.isEmpty(results)) {
            return new ArrayList<>();
        }

        return JsonUtils.fromJsonString(results, new TypeReference<ArrayList<String>>(){});
    }
}