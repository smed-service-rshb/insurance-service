package ru.softlab.efr.services.insurance.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Вид программы страхования
 */
public enum ProgramKind {

    ISJ("ИСЖ", Arrays.asList("surName","firstName", "birthDate", "phones")),

    NSJ("НСЖ", Arrays.asList("surName","firstName", "birthDate", "phones")),

    KSP("КСП", Arrays.asList("surName","firstName", "birthDate", "phones")),

    RENT("Рента", Arrays.asList("surName","firstName", "birthDate", "phones")),

    HOME("Страхование квартиры или дома", Arrays.asList("surName","firstName", "birthDate")),

    SMS("Медсоветник",  Arrays.asList("surName","firstName", "birthDate"));

    private String value;

    private List<String> requiredFields;

    ProgramKind(String value, List<String> requiredFields) {
        this.value = value;
        this.requiredFields = requiredFields;
    }


    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static ProgramKind fromValue(String text) {
        for (ProgramKind b : ProgramKind.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}