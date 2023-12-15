package ru.softlab.efr.services.insurance.model.enums;

/**
 * Источники оформления договора
 */
public enum SourceEnum {
    OFFICE("Офис"),
    MOBILE_CLIENT("Мобильное приложение"),
    INTERNET_CLIENT("Интернет-клиент");

    private String description;

    SourceEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
