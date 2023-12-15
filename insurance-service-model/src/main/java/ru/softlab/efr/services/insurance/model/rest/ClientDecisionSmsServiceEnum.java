package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип   IGNORE - игнорировать ошибку и продолжить создание договора   WAIT - отложить создание договора, повторить отправку промокода на сервер телемедицины позднее 
 */
public enum ClientDecisionSmsServiceEnum {
  
  IGNORE("IGNORE"),
  
  WAIT("WAIT");

  private String value;

  ClientDecisionSmsServiceEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ClientDecisionSmsServiceEnum fromValue(String text) {
    for (ClientDecisionSmsServiceEnum b : ClientDecisionSmsServiceEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

