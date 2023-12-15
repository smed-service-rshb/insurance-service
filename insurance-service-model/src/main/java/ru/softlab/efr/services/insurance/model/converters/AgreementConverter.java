package ru.softlab.efr.services.insurance.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.model.db.Agreement;
import ru.softlab.efr.services.insurance.model.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * Конвертор согласий
 * @author krutikov
 * @since 12.02.2018
 */
@Converter
public class AgreementConverter implements AttributeConverter<List<Agreement>, String> {

    @Override
    public String convertToDatabaseColumn(List<Agreement> attribute) {
        if(CollectionUtils.isEmpty(attribute)) {
            return null;
        }
        return JsonUtils.asJsonString(attribute);
    }

    @Override
    public List<Agreement> convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData)) {
            return new ArrayList<>();
        }
        return JsonUtils.fromJsonString(dbData, new TypeReference<ArrayList<Agreement>>() {});
    }
}
