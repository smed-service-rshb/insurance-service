package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип привязки программы страхования к элементам организационной структуры организации:   INCLUDE_ALL - Страхования программа доступна для оформления в любых подразделениях организации.   INCLUDE - Страховая программа доступна для оформления только в перечисленных подразделениях организации.   EXCLUDE_ALL - Страховая программа не доступна для оформления во всех подразделениях организации.   EXCLUDE - Страховая программа доступна для оформления по всех подразделениях организации за исключением перечисленных подразделений. 
 */
public enum RelatedOfficeFilterType {
  
  INCLUDE_ALL("INCLUDE_ALL"),
  
  INCLUDE("INCLUDE"),
  
  EXCLUDE_ALL("EXCLUDE_ALL"),
  
  EXCLUDE("EXCLUDE");

  private String value;

  RelatedOfficeFilterType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RelatedOfficeFilterType fromValue(String text) {
    for (RelatedOfficeFilterType b : RelatedOfficeFilterType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

