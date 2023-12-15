package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Признак РПДЛ   * other - Должности в иных организациях, созданных РФ на основании федеральных законов, включенных в перечни должностей, определяемых Президентом РФ   * stateOfficials - Государственные должности РФ   * centralBankExecutives - Должности членов Совета директоров ЦБ РФ   * federalStateOfficials - Должности федеральной государственной службы, назначение на которые и освобождение от которых осуществляется Президентом РФ и Правительством РФ   * centralBankOfficials - Должности в ЦБ РФ, включенные в перечни должностей, определяемые Президентом РФ   * nationalCorporationOfficials - Должности в государственных корпорациях, созданных РФ на основании федеральных законов, включенные в перечни должностей, определяемых Президентом РФ 
 */
public enum RussianPublicOfficialType {
  
  OTHER("other"),
  
  STATEOFFICIALS("stateOfficials"),
  
  CENTRALBANKEXECUTIVES("centralBankExecutives"),
  
  FEDERALSTATEOFFICIALS("federalStateOfficials"),
  
  CENTRALBANKOFFICIALS("centralBankOfficials"),
  
  NATIONALCORPORATIONOFFICIALS("nationalCorporationOfficials");

  private String value;

  RussianPublicOfficialType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RussianPublicOfficialType fromValue(String text) {
    for (RussianPublicOfficialType b : RussianPublicOfficialType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

