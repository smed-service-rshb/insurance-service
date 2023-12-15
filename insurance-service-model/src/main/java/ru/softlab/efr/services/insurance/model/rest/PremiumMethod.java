package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип расчета страховой премии:   NOT_CALCULATED - не рассчитывается (ввод в ручном режиме)   MULTIPLIED - страховая сумма, умноженная на коэффициент   FIXED - фиксированное значение   BY_FORMULA - по формуле   BY_RISK - По риску 
 */
public enum PremiumMethod {
  
  NOT_CALCULATED("NOT_CALCULATED"),
  
  MULTIPLIED("MULTIPLIED"),
  
  FIXED("FIXED"),
  
  BY_FORMULA("BY_FORMULA"),
  
  BY_RISK("BY_RISK");

  private String value;

  PremiumMethod(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PremiumMethod fromValue(String text) {
    for (PremiumMethod b : PremiumMethod.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

