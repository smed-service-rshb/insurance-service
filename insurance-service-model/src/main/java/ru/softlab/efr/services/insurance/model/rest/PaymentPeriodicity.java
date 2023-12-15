package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Периодичность уплаты взносов, возможные значения:   ONCE - единовременно   MONTHLY - ежемесячно   QUARTERLY - ежеквартально   TWICE_A_YEAR - раз в полгода   YEARLY - ежегодно 
 */
public enum PaymentPeriodicity {
  
  ONCE("ONCE"),
  
  MONTHLY("MONTHLY"),
  
  QUARTERLY("QUARTERLY"),
  
  TWICE_A_YEAR("TWICE_A_YEAR"),
  
  YEARLY("YEARLY");

  private String value;

  PaymentPeriodicity(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PaymentPeriodicity fromValue(String text) {
    for (PaymentPeriodicity b : PaymentPeriodicity.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

