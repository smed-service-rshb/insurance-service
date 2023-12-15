package ru.softlab.efr.services.insurance.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.ClientTemplate;
import ru.softlab.efr.services.insurance.model.rest.ClientTemplateData;
import ru.softlab.efr.services.insurance.model.rest.ClientTemplateDataForList;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.ShortClientTemplates;

/**
 * Конвертер для преобразования представлений элементов справочника шаблонов заявлений и инструкций.
 *
 * @author kalantaev
 * @since 12.04.2019
 */
@Service
public class ClientTemplateConverter {

    /**
     * Преобразование представления краткой информации элементов справочника шаблонов заявлений и инструкций
     *
     * @param data значения в представлении БД
     * @return значение в представлении API
     */
    public ClientTemplateDataForList convert(ShortClientTemplates data) {
        ClientTemplateDataForList result = new ClientTemplateDataForList();
        result.setId(data.getId());
        result.setKind(data.getKind() != null ? ProgramKind.valueOf(data.getKind().name()) : null);
        result.setProgramName(data.getProgram());
        result.setIsTemplate(data.getTemplate());
        result.setName(data.getName());
        result.setEndDate(data.getEndDate());
        result.setStartDate(data.getStartDate());
        result.setSortPriority(data.getSortPriority());
        return result;
    }

    public ClientTemplate convert(ClientTemplate template, ClientTemplateData data) {
        template.setKind(data.getKind() != null ? ru.softlab.efr.services.insurance.model.enums.ProgramKind.valueOf(data.getKind().name()) : null);
        template.setTemplate(data.isIsTemplate());
        template.setName(data.getName());
        template.setDescription(data.getDescription());
        template.setLink(data.getLink());
        template.setStartDate(data.getStartDate());
        template.setEndDate(data.getEndDate());
        template.setSortPriority(data.getSortPriority());
        return template;
    }

    public ClientTemplateData convert(ClientTemplate data) {
        ClientTemplateData template = new ClientTemplateData();
        template.setId(data.getId());
        template.setKind(data.getKind() != null ? ProgramKind.valueOf(data.getKind().name()) : null);
        template.setIsTemplate(data.getTemplate());
        template.setName(data.getName());
        template.setDescription(data.getDescription());
        template.setLink(data.getLink());
        template.setStartDate(data.getStartDate());
        template.setEndDate(data.getEndDate());
        template.setSortPriority(data.getSortPriority());
        template.setProgram(data.getProgram() != null ? data.getProgram().getId() : null);
        if (data.getAttachment() != null) {
            template.setAttachId(data.getAttachment().getId());
            template.setAttachName(data.getAttachment().getFileName());
        }
        return template;
    }

    public ru.softlab.efr.services.insurance.model.rest.ClientTemplate convertToClient(ClientTemplate data) {
        ru.softlab.efr.services.insurance.model.rest.ClientTemplate template = new ru.softlab.efr.services.insurance.model.rest.ClientTemplate();
        template.setName(data.getName());
        template.setDescription(data.getDescription());
        template.setLink(data.getLink());
        template.setAttachId(data.getAttachment() != null ? data.getAttachment().getId() : null);
        template.setAttachName(data.getAttachment() != null ? data.getAttachment().getFileName() : null);
        template.setSortPriority(data.getSortPriority());
        return template;
    }
}
