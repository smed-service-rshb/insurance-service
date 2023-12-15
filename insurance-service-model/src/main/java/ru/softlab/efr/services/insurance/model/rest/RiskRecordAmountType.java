package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Учёт суммы премии, возможные значения:   TOTAL_PREMIUM - от полной премии   SINGLE_PREMIUM - от разовой премии 
 */
public enum RiskRecordAmountType {
  
  TOTAL_PREMIUM("TOTAL_PREMIUM"),
  
  SINGLE_PREMIUM("SINGLE_PREMIUM"),
  
  OTHER("OTHER");

  private String value;

  RiskRecordAmountType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RiskRecordAmountType fromValue(String text) {
    for (RiskRecordAmountType b : RiskRecordAmountType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

