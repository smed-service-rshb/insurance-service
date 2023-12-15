package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип расчета страховой суммы для риска, возможные значения:   CONSTANT   DECREASING   INCREASING 
 */
public enum MethodCalcRisk {
  
  CONSTANT("CONSTANT"),
  
  DECREASING("DECREASING"),
  
  INCREASING("INCREASING");

  private String value;

  MethodCalcRisk(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MethodCalcRisk fromValue(String text) {
    for (MethodCalcRisk b : MethodCalcRisk.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

