package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Признак принадлежности к публичным лицам    * none - Не является ПДЛ или его ближайшим окружением/в ином случае    * foreign - ИПДЛ    * foreignRelative - Ближайшее окружение иностранного публичного должностного лица    * russian - РПДЛ    * russianRelative - Ближайшее окружение РПДЛ    * international - МПДЛ    * internationalRelative - Ближайшее окружение МПДЛ 
 */
public enum PublicOfficialType {
  
  NONE("none"),
  
  FOREIGN("foreign"),
  
  FOREIGNRELATIVE("foreignRelative"),
  
  RUSSIAN("russian"),
  
  RUSSIANRELATIVE("russianRelative"),
  
  INTERNATIONAL("international"),
  
  INTERNATIONALRELATIVE("internationalRelative");

  private String value;

  PublicOfficialType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PublicOfficialType fromValue(String text) {
    for (PublicOfficialType b : PublicOfficialType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

