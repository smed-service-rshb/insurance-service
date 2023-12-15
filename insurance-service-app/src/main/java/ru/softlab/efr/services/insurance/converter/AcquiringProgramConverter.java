package ru.softlab.efr.services.insurance.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.rest.AcquiringProgramData;
import ru.softlab.efr.services.insurance.model.rest.AvailableProgram;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplate;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертер представлений программ страхования для оформления в ЛК
 */
@Service
public class AcquiringProgramConverter {

    private final ProgramSettingConverter programSettingConverter;

    @Autowired
    public AcquiringProgramConverter(ProgramSettingConverter programSettingConverter) {
        this.programSettingConverter = programSettingConverter;
    }

    /**
     * Преобразование представления программ страхования для оформления в ЛК из представления БД в представления API для отображения сотруднику
     *
     * @param data Даные для преобразования
     * @return результат конвертации
     */
    public AcquiringProgramData convertToList(AcquiringProgram data) {
        AcquiringProgramData result = new AcquiringProgramData();
        result.setId(data.getId());
        result.setKind(data.getKind() != null ? ProgramKind.valueOf(data.getKind().name()) : null);
        result.setProgramSettingId(data.getProgram().getId());
        result.setStartDate(data.getStartDate());
        result.setEndDate(data.getEndDate());
        result.setNotAuthorizedZoneEnable(data.getNotAuthorizedZoneEnable());
        return result;
    }

    /**
     * Преобразование представления программ страхования для оформления в ЛК из представления БД в представления API для отображения сотруднику
     *
     * @param data Даные для преобразования
     * @return результат конвертации
     */
    public AcquiringProgramData convert(AcquiringProgram data) {
        AcquiringProgramData result = new AcquiringProgramData();
        result.setId(data.getId());
        result.setTitle(data.getTitle());
        result.setKind(data.getKind() != null ? ProgramKind.valueOf(data.getKind().name()) : null);
        result.setProgramSettingId(data.getProgram().getId());
        result.setStartDate(data.getStartDate());
        result.setEndDate(data.getEndDate());
        result.setAuthorizedZoneEnable(data.getAuthorizedZoneEnable());
        result.setNotAuthorizedZoneEnable(data.getNotAuthorizedZoneEnable());
        result.setApplication(data.getApplication());
        result.setDescription(data.getDescription());
        result.setLink(data.getLink());
        result.setImage(data.getImage() == null || data.getImage().getDeleted() ? null : data.getImage().getId());
        result.setImageName(data.getImage() == null || data.getImage().getDeleted() ? null : data.getImage().getName());
        result.setInfoImage(data.getInfoImage() == null || data.getInfoImage().getDeleted() ? null : data.getInfoImage().getId());
        result.setInfoImageName(data.getInfoImage() == null || data.getInfoImage().getDeleted() ? null : data.getInfoImage().getName());
        result.setPriority(data.getPriority());
        List<ContractTemplate> frontTemplates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data.getDocumentTemplateList())) {
            frontTemplates = programSettingConverter.getDocumentTemplateById(data.getDocumentTemplateList());
        }
        result.setTemplates(frontTemplates);
        return result;
    }

    /**
     * Преобразование представления программ страхования для оформления в ЛК из представления API в представление БД
     *
     * @param data Даные для преобразования
     * @return результат конвертации
     */
    public AcquiringProgram convert(AcquiringProgramData data, AcquiringProgram result) {
        result.setTitle(data.getTitle());
        result.setKind(data.getKind() != null ? ru.softlab.efr.services.insurance.model.enums.ProgramKind.valueOf(data.getKind().name()) : null);
        result.setStartDate(data.getStartDate());
        result.setEndDate(data.getEndDate());
        result.setAuthorizedZoneEnable(data.isAuthorizedZoneEnable());
        result.setNotAuthorizedZoneEnable(data.isNotAuthorizedZoneEnable());
        result.setApplication(data.isApplication());
        result.setDescription(data.getDescription());
        result.setLink(data.getLink());
        result.setPriority(data.getPriority());
        if (!CollectionUtils.isEmpty(data.getTemplates())) {
            result.setDocumentTemplateList(data.getTemplates().stream()
                    .map(ContractTemplate::getId).collect(Collectors.toList()));
        } else {
            result.setDocumentTemplateList(null);
        }
        return result;
    }


    /**
     * Преобразование представления программ страхования для оформления в ЛК из представления БД в представления API для отображения клиенту
     *
     * @param data Даные для преобразования
     * @return результат конвертации
     */
    public AvailableProgram convertToClient(AcquiringProgram data) {
        AvailableProgram result = new AvailableProgram();
        result.setId(data.getId());
        result.setTitle(data.getTitle());
        result.setName(data.getProgram().getProgram().getNameForPrint());
        result.setProgramKind(ProgramKind.valueOf(data.getProgram().getProgram().getType().name()));
        result.setImage(data.getImage() == null || data.getImage().getDeleted() ? null : data.getImage().getId());
        result.setInfoImage(data.getInfoImage() == null || data.getInfoImage().getDeleted() ? null : data.getInfoImage().getId());
        result.setDescription(data.getDescription());
        result.setLink(data.getLink());
        result.setCanBay(!data.getApplication());
        result.setMaxAge(data.getProgram().getMaxAgeInsured());
        result.setMinAge(data.getProgram().getMinAgeInsured());
        result.setRisks(data.getProgram()
                .getRequiredRiskSettingList()
                .stream()
                .map(risk -> risk.getRisk().getName())
                .collect(Collectors.toList()));
        return result;
    }
}
