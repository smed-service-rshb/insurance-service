package ru.softlab.efr.services.insurance.converter;

import org.apache.log4j.Logger;
import ru.softlab.efr.services.insurance.model.db.AvailProgramKindEntity;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.AvailProgramKindData;

import java.util.Arrays;

/**
 * Конвертер представления сущности доступности видов программ страхования
 *
 * @author olshansky
 * @since 27.03.2019
 */
public class AvailProgramConverter {

    private static final Logger LOGGER = Logger.getLogger(AvailProgramConverter.class);

    public static AvailProgramKindEntity convert(AvailProgramKindData frontEntity) {
        return new AvailProgramKindEntity(frontEntity.getId(), frontEntity.getCode(), frontEntity.isIsActive());
    }

    public static AvailProgramKindData convert(AvailProgramKindEntity backEntity) {
        AvailProgramKindData frontEntity = new AvailProgramKindData(backEntity.getId(), backEntity.getProgramKind(), null, backEntity.getActive());

        if (Arrays.stream(ProgramKind.values()).anyMatch(programKind -> programKind.name().equalsIgnoreCase(backEntity.getProgramKind()))) {
            frontEntity.setName(ProgramKind.valueOf(backEntity.getProgramKind()).toString());
        } else {
            LOGGER.warn("В таблице хранения сущности AvailProgramKindEntity есть записи, которые не заведены в ru.softlab.efr.services.insurance.model.enums.ProgramKind");
            frontEntity.setName(backEntity.getProgramKind());
        }
        return frontEntity;
    }
}
