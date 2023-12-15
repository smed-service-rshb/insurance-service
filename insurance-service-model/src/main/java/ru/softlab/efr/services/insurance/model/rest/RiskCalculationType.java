package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип расчета страховой суммы и страховой премии для риска, возможные значения:   NOT_CALCULATED - Не рассчитывается   FIXED - Фиксированные значения   DEPENDS_ON_RISK - Зависит от СС другого риска   DEPENDS_ON_CONTRACT_SUM - Зависит от СС договора   DEPENDS_ON_CONTRACT_PREMIUM - Зависит от СП договора   BY_FORMULA - По формуле   BY_COMPLEX_FORMULA - По сложной формуле   DEPENDS_ON_TERM - Зависит от срока выплаты 
 */
public enum RiskCalculationType {
  
  NOT_CALCULATED("NOT_CALCULATED"),
  
  FIXED("FIXED"),
  
  DEPENDS_ON_RISK("DEPENDS_ON_RISK"),
  
  DEPENDS_ON_CONTRACT_SUM("DEPENDS_ON_CONTRACT_SUM"),
  
  DEPENDS_ON_CONTRACT_PREMIUM("DEPENDS_ON_CONTRACT_PREMIUM"),
  
  BY_FORMULA("BY_FORMULA"),
  
  BY_COMPLEX_FORMULA("BY_COMPLEX_FORMULA"),
  
  DEPENDS_ON_TERM("DEPENDS_ON_TERM");

  private String value;

  RiskCalculationType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RiskCalculationType fromValue(String text) {
    for (RiskCalculationType b : RiskCalculationType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

