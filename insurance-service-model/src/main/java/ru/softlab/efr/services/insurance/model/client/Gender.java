package ru.softlab.efr.services.insurance.model.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Пол застрахованного, возможные значения:   MALE - мужской   FEMALE - женский   BOTH - оба пола, для случая заполнения параметров программ страхования 
 */
public enum Gender {
  
  MALE("MALE"),
  
  FEMALE("FEMALE"),
  
  BOTH("BOTH");

  private String value;

  Gender(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Gender fromValue(String text) {
    for (Gender b : Gender.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

