package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Гражданство (Гражданин России, Гражданин Республики Беларусь, Иностранный гражданин / лицо без гражданства)
 */
public enum CitizenshipType {
  
  RUSSIAN("russian"),
  
  BELARUSIAN("belarusian"),
  
  FOREIGN("foreign");

  private String value;

  CitizenshipType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CitizenshipType fromValue(String text) {
    for (CitizenshipType b : CitizenshipType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

