package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Вид проверки клиентов:   TERRORIST - По справочнику террористов и экстримистов,   BLOCKAGES - По справочнику блокировок,   INVALID_IDENTITY_DOC - По справочнику недействительных паспортов 
 */
public enum CheckType {
  
  TERRORIST("TERRORIST"),
  
  BLOCKAGES("BLOCKAGES"),
  
  INVALID_IDENTITY_DOC("INVALID_IDENTITY_DOC");

  private String value;

  CheckType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CheckType fromValue(String text) {
    for (CheckType b : CheckType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

