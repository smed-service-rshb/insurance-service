package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Налоговое резидентство    * russian - Налоговый резидент РФ    * foreign - Налоговый резидент иностранного государства    * other - Иное 
 */
public enum TaxResidenceType {
  
  RUSSIAN("russian"),
  
  FOREIGN("foreign"),
  
  OTHER("other");

  private String value;

  TaxResidenceType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TaxResidenceType fromValue(String text) {
    for (TaxResidenceType b : TaxResidenceType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

