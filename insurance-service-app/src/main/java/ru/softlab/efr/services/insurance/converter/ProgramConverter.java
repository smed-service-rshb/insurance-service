package ru.softlab.efr.services.insurance.converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.RelatedEmployeeGroup;
import ru.softlab.efr.services.insurance.model.db.RelatedOffice;
import ru.softlab.efr.services.insurance.model.rest.ProgramData;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RelatedOfficeFilterType;
import ru.softlab.efr.services.insurance.model.rest.Segment;
import ru.softlab.efr.services.insurance.service.EmployeeFilterService;
import ru.softlab.efr.services.insurance.services.SegmentService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Конвертер представления сущности программ страхования.
 */
@Service
public class ProgramConverter {

    private static final Logger LOGGER = Logger.getLogger(ProgramConverter.class);

    @Autowired
    private SegmentService segmentService;
    @Autowired
    private EmployeeFilterService employeeFilterService;

    /**
     * Преобразование программы страхования из представления БД в представление API.
     *
     * @param program Сущность программы страхования в представлении БД.
     * @return Сущность программы страхования в представлении API.
     */
    public ProgramData convert(Program program) throws ValidationException {
        ProgramData data = new ProgramData();
        data.setId(program.getId());
        data.setSegment(getFrontSegment(program));
        data.setKind(ProgramKind.valueOf(program.getType().name()));
        data.setOption(program.getVariant());
        data.setWaitingPeriod(program.getWaitingPeriod());
        data.setCoolingPeriod(program.getCoolingPeriod());
        data.setActiveFlag(program.getActive());
        data.setNumber(program.getNumber());
        data.setName(program.getName());
        data.setNameForPrint(program.getNameForPrint());
        data.setPolicyCode(program.getPolicyCode());
        if (Objects.nonNull(program.getRelatedOfficeFilterType())) {
            data.setRelatedOfficeFilterType(RelatedOfficeFilterType.valueOf(program.getRelatedOfficeFilterType().name()));
            if (RelatedOfficeFilterType.INCLUDE.equals(data.getRelatedOfficeFilterType())
                    || RelatedOfficeFilterType.EXCLUDE.equals(data.getRelatedOfficeFilterType())) {
                try {
                    data.setRelatedOffices(employeeFilterService.getOfficeNamesByIds(program.getRelatedOffices()
                            .stream()
                            .map(RelatedOffice::getOfficeId)
                            .collect(Collectors.toList())));
                } catch (Exception ex) {
                    LOGGER.error("Произошла ошибка во время конвертации программы страхования сущности БД в API", ex);
                }
            }
        }
        if (Objects.nonNull(program.getRelatedEmployeeGroupFilterType())) {
            data.setRelatedGroupFilterType(RelatedOfficeFilterType.valueOf(program.getRelatedEmployeeGroupFilterType().name()));
            if (RelatedOfficeFilterType.INCLUDE.equals(data.getRelatedGroupFilterType())
                    || RelatedOfficeFilterType.EXCLUDE.equals(data.getRelatedGroupFilterType())) {
                data.setRelatedGroups(program.getRelatedEmployeeGroups()
                        .stream()
                        .map(RelatedEmployeeGroup::getGroupCode)
                        .collect(Collectors.toList()));
            }
        }

        data.setComulation(program.getComulation());
        data.setProgramCharCode(program.getProgramCharCode());

        data.setProgramCode(program.getProgramCode());
        data.setProgramTariff(program.getProgramTariff());

        return data;
    }

    private Segment getFrontSegment(Program program) {
        if (Objects.nonNull(program.getSegment())) {
            Segment frontSegment = new Segment();
            frontSegment.setId(program.getSegment().getId());
            frontSegment.setName(program.getSegment().getName());
            return frontSegment;
        }
        return null;
    }

    /**
     * Преобразование программы страхования из представления API в представление БД.
     *
     * @param programData Сущность программы страхования в представлении API.
     * @return Сущность программы страхования в представлении БД.
     * @throws ValidationException Ошибка валидации, если программа страхования в представлении API
     *                             содержит невалидные данные.
     */
    public Program convert(ProgramData programData) throws Exception {
        Program program = new Program();
        fill(program, programData);
        return program;
    }

    /**
     * Обновление представления программы страхования в БД из представления в API.
     *
     * @param program     Сущность программы страхования в представлении БД.
     * @param programData Сущность программы страхования в представлении API.
     * @throws ValidationException Ошибка валидации, если программа страхования в представлении API
     *                             содержит невалидные данные.
     */
    public void fill(Program program, ProgramData programData) throws Exception {
        List<String> errorMessages = new ArrayList<>();

        program.setName(programData.getName());
        program.setNameForPrint(programData.getNameForPrint());
        program.setType(ru.softlab.efr.services.insurance.model.enums.ProgramKind.valueOf(programData.getKind().name()));
        program.setNumber(programData.getNumber());
        program.setPolicyCode(programData.getPolicyCode());
        program.setVariant(programData.getOption());
        program.setCoolingPeriod(programData.getCoolingPeriod());
        program.setWaitingPeriod(programData.getWaitingPeriod());
        program.setComulation(programData.getComulation());

        if (Objects.nonNull(programData.getSegment())) {
            Long segmentId = programData.getSegment().getId();
            ru.softlab.efr.services.insurance.model.db.Segment segment = segmentService.findById(segmentId);
            program.setSegment(segment);
            if (Objects.isNull(segment)) {
                String errorMessage = String.format("Сегмент программы страхования не найден по идентификатору %s", segmentId);
                LOGGER.warn(errorMessage);
                errorMessages.add(errorMessage);
            }
        } else {
            program.setSegment(null);
        }

        program.setProgramCharCode(programData.getProgramCharCode());

        if (Objects.nonNull(programData.getRelatedOfficeFilterType())) {
            program.setRelatedOfficeFilterType(ru.softlab.efr.services.insurance.model.enums.RelatedOfficeFilterType
                    .valueOf(programData.getRelatedOfficeFilterType().name()));
            if (RelatedOfficeFilterType.INCLUDE.equals(programData.getRelatedOfficeFilterType())
                    || RelatedOfficeFilterType.EXCLUDE.equals(programData.getRelatedOfficeFilterType())) {

                if (Objects.isNull(program.getRelatedOffices())) {
                    program.setRelatedOffices(new ArrayList<>());
                }

                Set<Long> officeIds = employeeFilterService.getOfficeIdsByNames(programData.getRelatedOffices());

                List<RelatedOffice> relatedOffices = officeIds
                        .stream()
                        .map(officeId ->
                                program.getRelatedOffices()
                                        .stream()
                                        .filter(relatedOffice -> relatedOffice.getOfficeId().equals(officeId))
                                        .findFirst()
                                        .orElse(new RelatedOffice(program, officeId))
                        )
                        .collect(Collectors.toList());
                program.getRelatedOffices().clear();
                program.getRelatedOffices().addAll(relatedOffices);

            } else {
                if (program.getRelatedOffices() != null) {
                    program.getRelatedOffices().clear();
                }
            }
        }
        if (Objects.nonNull(programData.getRelatedGroupFilterType())) {
            program.setRelatedEmployeeGroupFilterType(ru.softlab.efr.services.insurance.model.enums.RelatedEmployeeGroupFilterType
                    .valueOf(programData.getRelatedGroupFilterType().name()));
            if (RelatedOfficeFilterType.INCLUDE.equals(programData.getRelatedGroupFilterType())
                    || RelatedOfficeFilterType.EXCLUDE.equals(programData.getRelatedGroupFilterType())) {

                if (Objects.isNull(program.getRelatedEmployeeGroups())) {
                    program.setRelatedEmployeeGroups(new ArrayList<>());
                }

                List<RelatedEmployeeGroup> relatedEmployeeGroups = programData.getRelatedGroups()
                        .stream()
                        .map(officeName -> program.getRelatedEmployeeGroups()
                                .stream()
                                .filter(relatedEmployeeGroup -> relatedEmployeeGroup.getGroupCode().equals(officeName))
                                .findFirst()
                                .orElse(new RelatedEmployeeGroup(program, officeName))
                        )
                        .collect(Collectors.toList());
                program.getRelatedEmployeeGroups().clear();
                program.getRelatedEmployeeGroups().addAll(relatedEmployeeGroups);

            } else {
                if (Objects.nonNull(program.getRelatedEmployeeGroups())) {
                    program.getRelatedEmployeeGroups().clear();
                }
            }
        }
        program.setActive(programData.isActiveFlag());
        program.setDeleted(false);

        program.setProgramCode(programData.getProgramCode());
        program.setProgramTariff(programData.getProgramTariff());

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

}
