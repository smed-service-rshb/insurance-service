package ru.softlab.efr.services.insurance.converter;


import ru.softlab.efr.services.insurance.model.db.RedemptionCoefficientEntity;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.enums.PaymentMethod;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.db.RequiredDocument;
import ru.softlab.efr.services.insurance.model.db.Risk;
import ru.softlab.efr.services.insurance.model.rest.*;

import java.util.stream.Collectors;

/**
 * Ковертер, предназначенный для преобразования внутреннего представления справочников в БД
 * в модель публичного API микросервиса и обратно.
 *
 * @author Kalantaev
 * @since 14.09.2018
 */
public class DictConverter {

    /**
     * Преобразует запись справочника обязательных документов из представления модели API во внутренюю модель.
     *
     * @param data представление записи справочника модели API.
     * @return внутреннее представление записи справочника.
     */
    public static RequiredDocument convert(RequiredDocumentData data) {
        RequiredDocument document = new RequiredDocument();
        document.setId(data.getId());
        document.setType(data.getType());
        document.setActiveFlag(data.isActiveFlag());
        document.setDeleted(false);
        return document;
    }

    /**
     * Преобразует запись справочника обязательных документов из представления внутренней модели в модель API.
     *
     * @param document внутреннее представление записи справочника.
     * @return представление записи справочника модели API.
     */
    public static RequiredDocumentData convert(RequiredDocument document) {
        RequiredDocumentData data = new RequiredDocumentData();
        data.setId(document.getId());
        data.setType(document.getType());
        data.setActiveFlag(document.getActiveFlag());
        return data;
    }

    /**
     * Преобразует запись справочника рисков из представления модели API во внутренюю модель.
     *
     * @param data представление записи справочника рисков модели API.
     * @return внутреннее представление записи справочника рисков.
     */
    public static Risk convert(RiskData data) {
        Risk risk = new Risk();
        risk.setId(data.getId());
        risk.setProgramKind(ProgramKind.valueOf(data.getProgram().toString()));
        risk.setName(data.getName());
        risk.setFullName(data.getFullName());
        risk.setStartDate(data.getStartDate());
        risk.setEndDate(data.getEndDate());
        risk.setBenefitsInsured(data.isBenefitsInsured());
        if (data.getPaymentMethod() != null) {
            risk.setPaymentMethod(PaymentMethod.valueOf(data.getPaymentMethod().toString()));
        }
        risk.setDeleted(false);
        return risk;
    }

    /**
     * Преобразует запись справочника рисков из представления внутренней модели в модель API.
     *
     * @param data внутреннее представление записи справочника рисков.
     * @return представление записи справочника рисков модели API.
     */
    public static RiskData convert(Risk data) {
        RiskData risk = new RiskData();
        risk.setId(data.getId());
        risk.setProgram(ru.softlab.efr.services.insurance.model.rest.ProgramKind.valueOf(data.getProgramKind().name()));
        risk.setName(data.getName());
        risk.setFullName(data.getFullName());
        risk.setStartDate(data.getStartDate());
        risk.setBenefitsInsured(data.getBenefitsInsured());
        if (data.getPaymentMethod() != null) {
            risk.setPaymentMethod(ru.softlab.efr.services.insurance.model.rest.PaymentMethod.valueOf(data.getPaymentMethod().name()));
        }
        risk.setEndDate(data.getEndDate());
        return risk;
    }

    /**
     * Преобразует запись справочника рисков из представления модели API во внутренюю модель.
     *
     * @param data представление записи справочника рисков модели API.
     * @return внутреннее представление записи справочника рисков.
     */
    public static RedemptionEntity convert(RedemptionData data) {
        RedemptionEntity redemption = new RedemptionEntity();
        redemption.setId(data.getId());
        redemption.setId(data.getId());
        redemption.setCurrency(data.getCurrencyId());
        redemption.setDuration(data.getDuration());
        redemption.setPeriodicity(PeriodicityEnum.valueOf(data.getPeriodicity().name()));
        redemption.setPaymentPeriod(PeriodicityEnum.valueOf(data.getPaymentPeriod().name()));
        redemption.setCoefficientList(data.getCoefficientList().stream().map(item->
                new RedemptionCoefficientEntity(item.getId(), item.getPeriod(), item.getCoefficient(), redemption))
                .collect(Collectors.toList()));
        redemption.setDeleted(false);
        return redemption;
    }

    /**
     * Преобразует запись справочника рисков из представления внутренней модели в модель API.
     *
     * @param data внутреннее представление записи справочника рисков.
     * @return представление записи справочника рисков модели API.
     */
    public static RedemptionData convert(RedemptionEntity data) {
        RedemptionData redemption = new RedemptionData();
        redemption.setId(data.getId());
        redemption.setProgramId(data.getProgram().getId());
        redemption.setProgramName(data.getProgram().getName());
        redemption.setCurrencyId(data.getCurrency());
        redemption.setDuration(data.getDuration());
        redemption.setPeriodicity(PaymentPeriodicity.valueOf(data.getPeriodicity().name()));
        redemption.setPaymentPeriod(PaymentPeriodicity.valueOf(data.getPaymentPeriod().name()));
        redemption.setCoefficientList(data.getCoefficientList().stream().map(item->
                new RedemptionCoefficientData(item.getId(), item.getPeriod(), item.getCoefficient()))
                .collect(Collectors.toList()));

        return redemption;
    }
}
