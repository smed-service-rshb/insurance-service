package ru.softlab.efr.services.insurance.services;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.RelatedEmployeeGroupFilterType;
import ru.softlab.efr.services.insurance.model.enums.RelatedOfficeFilterType;
import ru.softlab.efr.services.insurance.model.rest.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис проверки действия договора для данного пользователя
 *
 * @author olshansky
 * @since 25.02.2019
 */
@Service
public class InsuranceAvailabilityService {

    /*
     * При оформление программы из статуса проект необходимо проверять:
     * 1.  программа страхования действующая
     * 2. параметры программы страхования действующие.
     * 3. программа страхования доступна для оформления данному пользователю
     * 4. программа страхования доступна для оформления в ВСП и РФ данного пользователя.
     * Если какое-то из условий не выполнено, то необходимо вывести уведомление об ошибке:
     * 1. "Выбранная программа страхования на текущий момент не действует. Продолжение оформления договора невозможно"
     * 2.  "Выбранная программа страхования на текущий момент не действует. Продолжение оформления договора невозможно"
     * 3 - 4.  "Выбранная программа страхования недоступна для оформления Продолжение оформления договора невозможно"
     * Продолжение оформления должно быть недоступно. Возможность редактирования должна быть доступна.
     *
     * Проверки необходимо выполнять до выполнения всех остальных проверок.
     */
    public List<CheckModel> getContractErrors(Insurance insuranceContract, String nextStatus, PrincipalData principalData) {
        List<CheckModel> contractErrors = new ArrayList<>();
        InsuranceStatus status = insuranceContract.getStatus();
        InsuranceStatusCode statusCode = status != null ? status.getCode() : null;
        if ((InsuranceStatusCode.MADE.name().equals(nextStatus) &&
                (statusCode == null
                        || InsuranceStatusCode.DRAFT.equals(statusCode)
                        || InsuranceStatusCode.PROJECT.equals(statusCode)
                        || InsuranceStatusCode.MADE_NOT_COMPLETED.equals(statusCode)))
                || InsuranceStatusCode.MADE_NOT_COMPLETED.name().equals(nextStatus)) {
            contractErrors.addAll(getProgramSettingErrors(insuranceContract));
            contractErrors.addAll(getProgramErrors(insuranceContract, principalData));
        }
        return contractErrors;
    }

    private List<CheckModel> getProgramSettingErrors(Insurance insuranceContract) {
        List<CheckModel> contractErrors = new ArrayList<>();

        if (Boolean.TRUE.equals(insuranceContract.getProgramSetting().getDeleted())
                || Boolean.TRUE.equals(insuranceContract.getProgramSetting().getProgram().getDeleted())
                || Boolean.FALSE.equals(insuranceContract.getProgramSetting().getProgram().getActive())
                || (insuranceContract.getProgramSetting().getStartDate() != null
                && insuranceContract.getProgramSetting().getStartDate().toLocalDateTime().isAfter(LocalDateTime.now()))
                || (insuranceContract.getProgramSetting().getEndDate() != null
                && insuranceContract.getProgramSetting().getEndDate().toLocalDateTime().isBefore(LocalDateTime.now()))) {
            contractErrors.add(new CheckModel("insurance.programSetting.program",
                    "Выбранная программа страхования на текущий момент не действует. Продолжение оформления договора невозможно", CheckModelErrorType.CRITICAL));
        }
        return contractErrors;
    }

    private List<CheckModel> getProgramErrors(Insurance insuranceContract, PrincipalData principalData) {
        List<CheckModel> contractErrors = new ArrayList<>();
        String errorText = "Выбранная программа страхования недоступна для оформления. Продолжение оформления договора невозможно";
        if (!isProgramAllowable(insuranceContract.getProgramSetting().getProgram(), principalData)) {
            contractErrors.add(new CheckModel("insurance.programSetting.program", errorText,
                    CheckModelErrorType.CRITICAL));
        }
        return contractErrors;
    }

    /**
     * Проверка того, что программа страхования доступна данному пользователю.
     *
     * @param program       Программа страхования.
     * @param principalData Информация о вызывающем пользователе.
     * @return true - программа доступна; false - программа недоступна.
     */
    public static boolean isProgramAllowable(Program program, PrincipalData principalData) {
        if (isNotAdmin(principalData)) {
            if (!isProgramAllowableByOrgUnit(program, principalData)) {
                return false;
            }

            if (!isProgramAllowableByGroup(program, principalData)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка того, что программа страхования доступна данному пользователю по ВСП.
     *
     * @param program       Программа страхования.
     * @param principalData Информация о вызывающем пользователе.
     * @return true - программа доступна; false - программа недоступна.
     */
    static boolean isProgramAllowableByOrgUnit(Program program, PrincipalData principalData) {
        if (isNotAdmin(principalData)) {
            RelatedOfficeFilterType relatedOfficeFilterType = program.getRelatedOfficeFilterType();
            Long officeId = principalData.getOfficeId();

            boolean contains = program.getRelatedOffices()
                    .stream().anyMatch(relatedOffice -> relatedOffice.getOfficeId().equals(officeId));

            if (RelatedOfficeFilterType.EXCLUDE_ALL.equals(relatedOfficeFilterType)) {
                return false;
            } else if (RelatedOfficeFilterType.EXCLUDE.equals(relatedOfficeFilterType)) {
                if (contains) {
                    return false;
                }
            } else if (RelatedOfficeFilterType.INCLUDE.equals(relatedOfficeFilterType)) {
                if (!contains) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Проверка того, что программа страхования доступна данному пользователю по пользовательской группе.
     *
     * @param program       Программа страхования.
     * @param principalData Информация о вызывающем пользователе.
     * @return true - программа доступна; false - программа недоступна.
     */
    static boolean isProgramAllowableByGroup(Program program, PrincipalData principalData) {
        if (isNotAdmin(principalData)) {
            List<String> groups = CollectionUtils.isEmpty(principalData.getGroups()) ? new ArrayList<>() : principalData.getGroups();

            RelatedEmployeeGroupFilterType relatedEmployeeGroupFilterType = program.getRelatedEmployeeGroupFilterType();
            if (relatedEmployeeGroupFilterType != null) {
                if (RelatedEmployeeGroupFilterType.EXCLUDE_ALL.equals(relatedEmployeeGroupFilterType)) {
                    return false;
                }
                boolean contains = program.getRelatedEmployeeGroups()
                        .stream().anyMatch(relatedEmployeeGroup -> groups.contains(relatedEmployeeGroup.getGroupCode()));
                if (RelatedEmployeeGroupFilterType.EXCLUDE.equals(relatedEmployeeGroupFilterType)) {
                    if (contains) {
                        return false;
                    }
                } else if (RelatedEmployeeGroupFilterType.INCLUDE.equals(relatedEmployeeGroupFilterType)) {
                    if (!contains) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean isAdmin(PrincipalData principalData) {
        if (CollectionUtils.isNotEmpty(principalData.getRights())) {
            return principalData.getRights().contains(Right.EDIT_PRODUCT_SETTINGS)
                    || principalData.getRights().contains(Right.SET_ADMIN_ROLE);
        }
        return false;
    }

    private static boolean isNotAdmin(PrincipalData principalData) {
        return !isAdmin(principalData);
    }
}
