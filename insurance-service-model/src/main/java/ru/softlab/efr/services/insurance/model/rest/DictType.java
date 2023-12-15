package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Вид справочника
 */
public enum DictType {
  
  TERRORIST("TERRORIST"),
  
  BLOCKAGE("BLOCKAGE"),
  
  INVALID_IDENTITY_DOC("INVALID_IDENTITY_DOC");

  private String value;

  DictType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DictType fromValue(String text) {
    for (DictType b : DictType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

