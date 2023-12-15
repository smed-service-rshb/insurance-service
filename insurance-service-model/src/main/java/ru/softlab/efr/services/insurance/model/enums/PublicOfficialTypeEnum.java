package ru.softlab.efr.services.insurance.model.enums;

/**
 * Признак принадлежности к публичным лицам
 * none - Не является ПДЛ или его ближайшим окружением/в ином случае
 * foreign - ИПДЛ
 * foreignRelative - Ближайшее окружение иностранного публичного должностного лица
 * russian - РПДЛ
 * russianRelative - Ближайшее окружение РПДЛ
 * international - МПДЛ
 * internationalRelative - Ближайшее окружение МПДЛ
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum PublicOfficialTypeEnum {

    NONE("none"),
    FOREIGN("foreign"),
    FOREIGNRELATIVE("foreignRelative"),
    RUSSIAN("russian"),
    RUSSIANRELATIVE("russianRelative"),
    INTERNATIONAL("international"),
    INTERNATIONALRELATIVE("internationalRelative");

    private String value;

    PublicOfficialTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static String convertEnumToName (PublicOfficialTypeEnum type){
        if(type != null){
            switch (type){
                case FOREIGN:
                    return "ИПДЛ";
                case FOREIGNRELATIVE:
                    return "Ближайшее окружение ИПДЛ";
                case RUSSIAN:
                    return "РПДЛ";
                case RUSSIANRELATIVE:
                    return "Ближайшее окружение РПДЛ";
                case INTERNATIONAL:
                    return "МПДЛ";
                case INTERNATIONALRELATIVE:
                    return "Ближайшее окружение МПДЛ";
                default:
                    return "Не является ПДЛ";
            }
        }
        return "Не является ПДЛ";
    }

    public static PublicOfficialTypeEnum fromValue(String text) {
        for (PublicOfficialTypeEnum b : PublicOfficialTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
