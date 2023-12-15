package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статус обращения:   PROCESSING - В работе   PROCESSED - Исполнено   NEW - Новое   REJECT - Отклонено 
 */
public enum RequestStatus {
  
  NEW("NEW"),
  
  PROCESSING("PROCESSING"),
  
  PROCESSED("PROCESSED"),
  
  REJECT("REJECT");

  private String value;

  RequestStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RequestStatus fromValue(String text) {
    for (RequestStatus b : RequestStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

