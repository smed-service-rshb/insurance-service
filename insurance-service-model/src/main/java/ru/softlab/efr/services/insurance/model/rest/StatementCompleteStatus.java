package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статус заявления:   FULL - Полный набор документов   NOT_FULL - Не полный набор документов   NEUTRAL - Не проверено   NOT_CONTROLLED - не проверяется 
 */
public enum StatementCompleteStatus {
  
  FULL("FULL"),
  
  NOT_FULL("NOT_FULL"),
  
  NEUTRAL("NEUTRAL"),
  
  NOT_CONTROLLED("NOT_CONTROLLED");

  private String value;

  StatementCompleteStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatementCompleteStatus fromValue(String text) {
    for (StatementCompleteStatus b : StatementCompleteStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

