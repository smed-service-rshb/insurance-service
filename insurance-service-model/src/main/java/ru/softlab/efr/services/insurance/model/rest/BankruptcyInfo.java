package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статус банкрота
 */
public enum BankruptcyInfo {
  
  BANKRUPT("bankrupt"),
  
  NOT_BANKRUPT("not_bankrupt"),
  
  MAYBE_BANKRUPT("maybe_bankrupt"),
  
  UNCLASSIFIED_STAGE("unclassified_stage");

  private String value;

  BankruptcyInfo(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BankruptcyInfo fromValue(String text) {
    for (BankruptcyInfo b : BankruptcyInfo.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

