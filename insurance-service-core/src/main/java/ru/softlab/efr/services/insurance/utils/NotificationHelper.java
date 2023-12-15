package ru.softlab.efr.services.insurance.utils;

import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;

public class NotificationHelper {

    /**
     * Определение способа обращения к клиенту в зависимости от пола и отчества, если пол не известен.
     *
     * @param gender     Пол клиента.
     * @param middleName Отчество клеинта.
     * @return Обращение к клиенту.
     */
    public static String getGreetingsByMiddleName(GenderTypeEnum gender, String middleName) {
        if (gender != null) {
            return (GenderTypeEnum.MALE == gender) ? "Уважаемый" : "Уважаемая";
        }

        String result = "Уважаемый(-ая)";
        if ((middleName != null) && !middleName.trim().isEmpty()) {
            if (middleName.trim().toUpperCase().endsWith("ИЧ") || middleName.trim().toUpperCase().endsWith("ЛЫ")) {
                result = "Уважаемый";
            }
            if (middleName.trim().toUpperCase().endsWith("НА") || middleName.trim().toUpperCase().endsWith("ЗЫ")) {
                result = "Уважаемая";
            }
        }

        return result;
    }

}
