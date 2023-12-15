package ru.softlab.efr.services.insurance.model.utils;

import ru.rshb.csm.fln.asoa.uploaddoc._201805.req.DocFieldType;
import ru.softlab.efr.services.insurance.model.enums.DocType;

import java.util.List;

import static ru.softlab.efr.clients.model.Constants.*;

/**
 * Вспомогательный класс для генерации ключа поиска скана документа/согласия клиента
 * @author bazanova
 * @since 30.07.2018
 */
public class ResourceKeyHelper {

    /**
     * Сгенерировать ключ для поиска скана документа клиента
     * @param documentType - Тип документа
     * @param documentSeries - Серия документа
     * @param documentNumber - Номер документа
     * @return Ключ для поиска скана документа клиента
     */
    public static String generateDocumentScanKey(String documentType, String documentSeries, String documentNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("documentType:").append(documentType).append(";");
        if (documentSeries != null) {
            stringBuilder.append("documentSeries:").append(documentSeries).append(";");
        }
        if (documentNumber != null) {
            stringBuilder.append("documentNumber:").append(documentNumber).append(";");
        }
        return stringBuilder.toString();
    }

    /**
     * Сгенерировать ключ для поиска скана согласия клиента
     * @param resourceType - Скан согласия или отзыва согласия
     * @param agreementType - Тип согласия клиента
     * @param agreementDate - Дата согласия клиента
     * @return Ключ для поиска скана согласия клиента
     */
    public static String generateAgreementScanKey(String resourceType, String agreementType, String agreementDate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("resourceType:").append(resourceType).append(";");
        if (agreementType != null) {
            stringBuilder.append("agreementType:").append(agreementType).append(";");
        }
        if (agreementDate != null) {
            stringBuilder.append("agreementDate:").append(agreementDate).append(";");
        }
        return stringBuilder.toString();
    }

    /**
     * Сгенерировать ключ для поиска аватара клиента
     * @return Ключ для поиска аватара клиента
     */
    public static String generateAvatarKey() {
        return "resourceType:" + DocType.PHOTO.name() + ";";
    }

    /**
     * Сгенерировать ключ для поиска аватара/скана
     * @param docType - Тип ресурса
     * @param docFields - Поля
     * @return Ключ
     */
    public static String generateResourceKey(DocType docType, List<DocFieldType> docFields) {
        switch (docType) {
            case PHOTO:
                return generateAvatarKey();
            case PERS_PROC_COMPL:
            case RECALL_PERS_PROC_COMPL:
                return generateAgreementScanKey(docType.name(),
                        getFieldValue(docFields, AGREEMENT_TYPE_FIELD_NAME), getFieldValue(docFields, AGREEMENT_DATE_FIELD_NAME));
            default:
                return generateDocumentScanKey(docType.name(),
                        getFieldValue(docFields, DOC_SERIES_FIELD_NAME), getFieldValue(docFields, DOC_NUMBER_FIELD_NAME));
        }
    }

    private static String getFieldValue(List<DocFieldType> docFields, String fieldName) {
        for (DocFieldType docField: docFields) {
            if (fieldName.equals(docField.getName())) {
                return docField.getValue();
            }
        }
        return null;
    }
}
