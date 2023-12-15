package ru.softlab.efr.services.insurance.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author olshansky
 * @since 07.12.2018
 */
public enum CitizenshipTypeEnum {
    RUSSIAN("РФ"),

    BELARUSIAN("Республика Беларусь"),

    FOREIGN("Иностранный гражданин");

    private String value;

    private CitizenshipTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static CitizenshipTypeEnum fromValue(String text) {
        CitizenshipTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CitizenshipTypeEnum b = var1[var3];
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
