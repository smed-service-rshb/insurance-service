package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Вид андеррайтинга, возможные значения:   NO_STATEMENT - Без заявления   DECLARATION - Декларация   APPLICATION - Заполнение заявления (анкета МЕДО)   MEDO - Проведение МЕДО 
 */
public enum UnderwritingKind {
  
  NO_STATEMENT("NO_STATEMENT"),
  
  DECLARATION("DECLARATION"),
  
  APPLICATION("APPLICATION"),
  
  MEDO("MEDO");

  private String value;

  UnderwritingKind(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static UnderwritingKind fromValue(String text) {
    for (UnderwritingKind b : UnderwritingKind.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

