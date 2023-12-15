package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Виды типов стратегии
 */
public enum StrategyType {
  
  CLASSIC("CLASSIC"),
  
  COUPON("COUPON"),
  
  LOCOMOTIVE("LOCOMOTIVE");

  private String value;

  StrategyType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StrategyType fromValue(String text) {
    for (StrategyType b : StrategyType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

