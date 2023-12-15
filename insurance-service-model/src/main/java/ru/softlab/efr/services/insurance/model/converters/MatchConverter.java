package ru.softlab.efr.services.insurance.model.converters;

import org.springframework.util.StringUtils;
import ru.softlab.efr.services.insurance.model.db.MatchType;
import ru.softlab.efr.services.insurance.model.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для совпадения
 */
@Converter
public class MatchConverter implements AttributeConverter<MatchType, String> {

    @Override
    public String convertToDatabaseColumn(MatchType match) {
        if (match == null) {
            return null;
        }
        return JsonUtils.asJsonString(match);
    }

    @Override
    public MatchType convertToEntityAttribute(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return JsonUtils.fromJsonString(s, MatchType.class);
    }
}
