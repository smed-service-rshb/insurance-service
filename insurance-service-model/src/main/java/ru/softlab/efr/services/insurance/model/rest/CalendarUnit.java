package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Календарная единица срока страхования:   YEAR - год   MONTH - месяц   DAY - день 
 */
public enum CalendarUnit {
  
  YEAR("YEAR"),
  
  MONTH("MONTH"),
  
  DAY("DAY");

  private String value;

  CalendarUnit(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CalendarUnit fromValue(String text) {
    for (CalendarUnit b : CalendarUnit.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

