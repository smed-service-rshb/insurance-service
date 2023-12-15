package ru.softlab.efr.services.insurance.converter;

import ru.softlab.efr.services.insurance.model.db.UserTemplateEntity;
import ru.softlab.efr.services.insurance.model.rest.UserTemplateData;


/**
 * Конвертер представления сущности пользовательских шаблонов документов
 *
 * @author olshansky
 * @since 10.04.2019
 */
public class UserTemplateConverter {

    public static UserTemplateData convert(UserTemplateEntity backEntity) {
        UserTemplateData frontEntity = new UserTemplateData();
        frontEntity.setId(backEntity.getId());
        frontEntity.setName(backEntity.getName());
        frontEntity.setPriority(backEntity.getPriority());
        frontEntity.setTemplateId(backEntity.getTemplateId());
        frontEntity.setFileName(backEntity.getFileName());
        return frontEntity;
    }

    public static UserTemplateEntity convert(UserTemplateData frontEntity) {
        UserTemplateEntity backEntity = new UserTemplateEntity();
        backEntity.setId(frontEntity.getId());
        backEntity.setName(frontEntity.getName());
        backEntity.setPriority(frontEntity.getPriority());
        backEntity.setTemplateId(frontEntity.getTemplateId());
        backEntity.setFileName(frontEntity.getFileName());
        return backEntity;
    }
}
