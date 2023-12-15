package ru.softlab.efr.services.insurance.model.enums;

/**
 * Класс для мапинга типов документов из clients-data-service в АСОА
 * @author bazanova
 * @since 19.07.2018
 */
public enum DocType {

    /**
     * Паспорт гражданина РФ
     */
    PASSPORT_RF("LbEpaDocClientId", "Паспорт РФ"),
    /**
     * Фото
     */
    PHOTO("Photo", "Фото"),
    /**
     * Документ, подтверждающий действительность ДУЛа
     */
    CONF_DUL_DOC("ConfDulDoc", "Документ, подтверждающий действительность ДУЛа"),
    /**
     * Согласие на обработку персональных данных
     */
    PERS_PROC_COMPL("PersProcCompl", "Согласие на обработку ПД"),
    /**
     * Заявление клиента об отзыве согласия на обработку персональных данных
     */
    RECALL_PERS_PROC_COMPL("RecallPersProcCompl", "Заявление клиента об отзыве согласия на обработку персональных данных"),
    /**
     * Документ-основание проведения операции 3-им лицом
     */
    OPER_BASE_DOC("OperBaseDoc", "Документ-основание проведения операции 3-им лицом"),
    /**
     * Документ-основание
     */
    BASE_DOC("BaseDoc", "Документ-основание"),
    /**
     * Водительское удостоверение
     */
    DRIVER_LICENCE("DriverLicence", "Водительское удостоверение"),
    /**
     * Военный билет солдата (матроса, сержанта, старшины)
     */
    MILITARY_DOCUMENT("MilitaryDocument", "Военный билет солдата"),
    /**
     * Виза
     */
    VISA("Visa", "Виза"),
    /**
     * Свидетельство о рождении, выданное уполномоченным органом иностранного государства
     */
    BIRTH_CERTIFICATE_NON_RUS("BirthCertificateNonRUS", "Свидетельство о рождении"),
    /**
     * Загранпаспорт гражданина СССР
     */
    INTERNATIONAL_PASSPORT_RF("PassportUSSR", "Загранпаспорт гражданина СССР"),
    /**
     * Удостоверение вынужденных переселенцев
     */
    FORCED_MIGRANTS_DOCUMENT("ForcedMigrantsDocument", "Удостоверение вынужденных переселенцев"),
    /**
     * Дипломатический паспорт гражданина РФ
     */
    DIPLOMATIC_PASSPORT("DiplomaticPassportRUS", "Дипломатический паспорт гражданина РФ"),
    /**
     * Миграционная карта
     */
    MIGRATION_CARD("MigrationCard", "Миграционная карта"),
    /**
     * Загранпаспорт
     */
    FOREIGN_PASSPORT("Passport", "Загранпаспорт"),
    /**
     * Паспорт гражданина СССР
     */
    USSR_PASSPORT("USSRPassport", "Паспорт гражданина СССР"),
    /**
     * Паспорт моряка
     */
    SEAMAN_PASSPORT("SeamanPassport", "Паспорт моряка"),
    /**
     * Свидетельство о рождении
     */
    BIRTH_CERTIFICATE("BirthCertificate", "Свидетельство о рождении"),
    /**
     * Удостоверение личности военнослужащего
     */
    MILITARY_IDENTIFICATION_CARD("MilitaryIdentificationCard", "Удостоверение личности военнослужащего"),
    /**
     * Иные документы, удостоверяющие личность лица без гражданства
     */
    OTHER_STATELESS_ID("OtherStatelessID", "Иные документы"),
    /**
     * Иные документы, выдаваемые органами МВД
     */
    OTHER_IDENTIFICATION_DOCUMENT_RUS("OtherIdentificationDocumentRUS", "Иные документы, выдаваемые органами МВД");

    private String asoaDocType;
    private String docName;

    DocType(String asoaDocType, String docName) {
        this.asoaDocType = asoaDocType;
        this.docName = docName;
    }

    public String getAsoaDocType() {
        return asoaDocType;
    }

    public String getDocName() {
        return docName;
    }

    /**
     * Получить тип документа АСОА из типа документа clients-data-service
     * @param docTypeStr - тип документа в clients-data-service
     * @return тип документа в АСОА
     */
    public static String getAsoaDocType(String docTypeStr) {
        if (docTypeStr == null) {
            return null;
        }
        return DocType.valueOf(docTypeStr).getAsoaDocType();
    }

    /**
     * Получить русское название документа
     * @param docTypeStr - Тип документа
     * @return Название документа
     */
    public static String getDocName(String docTypeStr) {
        if (docTypeStr == null) {
            return null;
        }
        return DocType.valueOf(docTypeStr).getDocName();
    }

    /**
     * Получить тип документа по его типу в АСОА
     * @param asoaDocType - Тип документа в АСОА
     * @return Тип документа
     */
    public static DocType getByAsoaDocType(String asoaDocType) {
        for (DocType docType: DocType.values()) {
            if (docType.getAsoaDocType().equals(asoaDocType)) {
                return docType;
            }
        }
        return null;
    }
}