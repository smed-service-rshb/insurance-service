package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип   MAYBE_SAVE_AND_CONCLUSION_BUT_SOMETHING_IS_WRONG - Ошибка, суть которой может существенно повлиять на корректность договора, но не препятствующая оформлению и сохранению.   MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION - Ошибка, из-за которой невозможно оформление договора, но возможно сохранение   CRITICAL - Ошибка, из-за которой невозможно оформление договора и его сохранение 
 */
public enum CheckModelErrorType {
  
  MAYBE_SAVE_AND_CON_LUSION_BUT_SOMETHING_IS_WRONG("MAYBE_SAVE_AND_CONСLUSION_BUT_SOMETHING_IS_WRONG"),
  
  MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION("MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION"),
  
  CRITICAL("CRITICAL");

  private String value;

  CheckModelErrorType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CheckModelErrorType fromValue(String text) {
    for (CheckModelErrorType b : CheckModelErrorType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

