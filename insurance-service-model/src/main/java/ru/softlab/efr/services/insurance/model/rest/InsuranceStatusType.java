package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статусы договора страхования    * MADE - Оформлен    * NEED_WITHDRAW_APPLICATION - Требуется заявление о выплате    * WITHDRAW_APPLICATION_RECEIVED - Получено заявление о выплате    * PAYMENT_FULFILLED - Выплата произведена    * CHANGING_APPLICATION_RECEIVED - Получено заявление на изменение договора»    * REFUSING_APPLICATION_RECEIVED - Получено заявление об отказе    * CANCELLATION_APPLICATION_RECEIVED - Получено заявление о расторжении договора    * CANCELED - Расторгнут    * REVOKED - Аннулирован    * REVOKED_REPLACEMENT - Аннулирование по замене    * FINISHED - Окончен    * CLIENT_REFUSED - Отказ клиента    * CANCELED_IN_HOLD_PERIOD - Расторгнут в период охлаждения    * NOT_PAYED - Не оплачен    * NOT_ACCEPTED - Не акцептован    * PAYED - Оплачен    * PROJECT - Проект    * DRAFT - Черновик    * CRM_IMPORTED - Выгружен в CRM    * MADE_NOT_COMPLETED - 
 */
public enum InsuranceStatusType {
  
  MADE("MADE"),
  
  NEED_WITHDRAW_APPLICATION("NEED_WITHDRAW_APPLICATION"),
  
  WITHDRAW_APPLICATION_RECEIVED("WITHDRAW_APPLICATION_RECEIVED"),
  
  PAYMENT_FULFILLED("PAYMENT_FULFILLED"),
  
  CHANGING_APPLICATION_RECEIVED("CHANGING_APPLICATION_RECEIVED"),
  
  REFUSING_APPLICATION_RECEIVED("REFUSING_APPLICATION_RECEIVED"),
  
  CANCELLATION_APPLICATION_RECEIVED("CANCELLATION_APPLICATION_RECEIVED"),
  
  CANCELED("CANCELED"),
  
  REVOKED("REVOKED"),
  
  REVOKED_REPLACEMENT("REVOKED_REPLACEMENT"),
  
  FINISHED("FINISHED"),
  
  CLIENT_REFUSED("CLIENT_REFUSED"),
  
  CANCELED_IN_HOLD_PERIOD("CANCELED_IN_HOLD_PERIOD"),
  
  NOT_PAYED("NOT_PAYED"),
  
  NOT_ACCEPTED("NOT_ACCEPTED"),
  
  PAYED("PAYED"),
  
  PROJECT("PROJECT"),
  
  DRAFT("DRAFT"),
  
  CRM_IMPORTED("CRM_IMPORTED"),
  
  MADE_NOT_COMPLETED("MADE_NOT_COMPLETED");

  private String value;

  InsuranceStatusType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static InsuranceStatusType fromValue(String text) {
    for (InsuranceStatusType b : InsuranceStatusType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

