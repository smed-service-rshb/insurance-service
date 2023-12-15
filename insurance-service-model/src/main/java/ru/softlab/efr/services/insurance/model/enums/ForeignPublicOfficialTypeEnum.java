package ru.softlab.efr.services.insurance.model.enums;

/**
 * Признак ИПДЛ
 * stateLeaders - Главы государств или правительств (независимо от формы государственного устройства)
 * ministers - Министры, их заместители и помощники
 * courtOfficials - Должностные лица судебных органов власти последней инстанции (Верховный, Конституционный суд)
 * highOfficials - Высшие правительственные чиновники
 * prosecutors - Государственный прокурор и его заместители
 * highMilitaryOfficials - Высшие военные чиновники
 * nationalBankLeaders - Руководители и члены Советов директоров Национальных Банков
 * politicalLeaders - Лидер официально зарегистрированной политической партии движения, его заместитель
 * nationalCorporationLeaders - Руководители государственных корпораций
 * religiousLeaders - Глава религиозной организации (осуществляющей государственные управленческие функции), его заместитель
 * ambassadors - Послы
 * internationalOrganizationLeaders - Руководители, заместители руководителей международных организаций (ООН, ОЭСР, ОПЕК, Олимпийский комитет, Гаагский трибунал)
 * internationalCourtLeaders - Руководители и члены международных судебных Организаций (Суд по правам человека, Гаагский трибунал)
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum ForeignPublicOfficialTypeEnum {

    STATELEADERS("stateLeaders"),
    MINISTERS("ministers"),
    COURTOFFICIALS("courtOfficials"),
    HIGHOFFICIALS("highOfficials"),
    PROSECUTORS("prosecutors"),
    HIGHMILITARYOFFICIALS("highMilitaryOfficials"),
    NATIONALBANKLEADERS("nationalBankLeaders"),
    POLITICALLEADERS("politicalLeaders"),
    NATIONALCORPORATIONLEADERS("nationalCorporationLeaders"),
    RELIGIOUSLEADERS("religiousLeaders"),
    AMBASSADORS("ambassadors"),
    INTERNATIONALORGANIZATIONLEADERS("internationalOrganizationLeaders"),
    INTERNATIONALCOURTLEADERS("internationalCourtLeaders");

    private String value;

    ForeignPublicOfficialTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static ForeignPublicOfficialTypeEnum fromValue(String text) {
        for (ForeignPublicOfficialTypeEnum b : ForeignPublicOfficialTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
