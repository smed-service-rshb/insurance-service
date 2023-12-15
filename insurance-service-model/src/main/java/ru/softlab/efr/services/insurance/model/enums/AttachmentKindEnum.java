package ru.softlab.efr.services.insurance.model.enums;

/**
 * Вид вложений
 *
 * @author olshansky
 * @since 25.10.2018
 */
public enum AttachmentKindEnum {

    INSURANCE_CONTRACT("Договор страхования"),
    FORM_CERTIFICATION_POLICYHOLDER("Форма самосертификации страхователя"),
    FORM_CERTIFICATION_INSURED("Форма самосертификации застрахованного"),
    REQUEST_ATTACHMENT("Вложенный файл обращения"),
    REQUEST_DOCUMENT("Вложенный документ обращения"),
    CLIENT_TEMPLATE("Шаблоны заявлений и инструкций");


    private String value;

    AttachmentKindEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static AttachmentKindEnum fromValue(String text) {
        for (AttachmentKindEnum b : AttachmentKindEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}