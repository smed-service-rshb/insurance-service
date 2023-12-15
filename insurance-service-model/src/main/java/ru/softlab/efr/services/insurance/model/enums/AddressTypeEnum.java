package ru.softlab.efr.services.insurance.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author olshansky
 * @since 02.12.2018
 */
public enum AddressTypeEnum {
    REGISTRATION("registration"),
    RESIDENCE("residence"),
    MAIL("mail");

    private String value;

    private AddressTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static AddressTypeEnum fromValue(String text) {
        AddressTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            AddressTypeEnum b = var1[var3];
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
