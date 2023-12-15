package ru.softlab.efr.services.insurance.model.enums;

/**
 * Признак РПДЛ
 * other - Должности в иных организациях, созданных РФ на основании федеральных законов, включенных в перечни должностей, определяемых Президентом РФ
 * stateOfficials - Государственные должности РФ
 * centralBankExecutives - Должности членов Совета директоров ЦБ РФ
 * federalStateOfficials - Должности федеральной государственной службы, назначение на которые и освобождение от которых осуществляется Президентом РФ и Правительством РФ
 * centralBankOfficials - Должности в ЦБ РФ, включенные в перечни должностей, определяемые Президентом РФ
 * nationalCorporationOfficials - Должности в государственных корпорациях, созданных РФ на основании федеральных законов, включенные в перечни должностей, определяемых Президентом РФ
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum RussianPublicOfficialTypeEnum {

    OTHER("other"),
    STATEOFFICIALS("stateOfficials"),
    CENTRALBANKEXECUTIVES("centralBankExecutives"),
    FEDERALSTATEOFFICIALS("federalStateOfficials"),
    CENTRALBANKOFFICIALS("centralBankOfficials"),
    NATIONALCORPORATIONOFFICIALS("nationalCorporationOfficials");

    private String value;

    RussianPublicOfficialTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static RussianPublicOfficialTypeEnum fromValue(String text) {
        for (RussianPublicOfficialTypeEnum b : RussianPublicOfficialTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
