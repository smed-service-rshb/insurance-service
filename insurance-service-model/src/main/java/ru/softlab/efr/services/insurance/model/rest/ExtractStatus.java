package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип   CREATING - Процес формирования отчета запущен   SAVE - Отчет сформирован и сохранен   ERROR - При формировании отчета произошла ошибка   UPLOADED - Отчет выгружен пользователем 
 */
public enum ExtractStatus {
  
  CREATING("CREATING"),
  
  SAVE("SAVE"),
  
  ERROR("ERROR"),
  
  UPLOADED("UPLOADED");

  private String value;

  ExtractStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ExtractStatus fromValue(String text) {
    for (ExtractStatus b : ExtractStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

