package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Вид программы страхования:   ISJ - ИСЖ,   NSJ - НСЖ,   KSP - КСП,   RENT - Рента,   HOME - Страхование квартиры или дома   SMS - Союз Мед Сервис 
 */
public enum ProgramKind {
  
  ISJ("ISJ"),
  
  NSJ("NSJ"),
  
  KSP("KSP"),
  
  RENT("RENT"),
  
  HOME("HOME"),
  
  SMS("SMS");

  private String value;

  ProgramKind(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ProgramKind fromValue(String text) {
    for (ProgramKind b : ProgramKind.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

