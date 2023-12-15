package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Способ расчёта по договору, возможные значения:   SUM - расчёт по страховой сумме   PREMIUM - расчёт по страховой премии 
 */
public enum FindProgramType {
  
  SUM("SUM"),
  
  PREMIUM("PREMIUM");

  private String value;

  FindProgramType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static FindProgramType fromValue(String text) {
    for (FindProgramType b : FindProgramType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

