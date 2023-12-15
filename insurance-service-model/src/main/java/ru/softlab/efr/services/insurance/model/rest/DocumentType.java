package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Виды документов, удостоверяющих личность:   PASSPORT_RF - Паспорт гражданина РФ.   TEMPORARY_IDENTITY_CARD - Временное удостоверение личности.   SOLDIER_ID - Удостоверение личности военнослужащего.   MILITARY_ID - Военный билет.   SAILOR_ID - Удостоверение личности моряка.   CERTIFICATE_BIRTH - Свидетельство о рождении.   INTERNATIONAL_PASSPORT_RF - Заграничный паспорт гражданина РФ.   DIPLOMATIC_PASSPORT - Дипломатический паспорт.   CERTIFICATE_EMPLOYEE_PROSECUTOR - Служебное удостоверение работника прокуратуры.   SERVICE_PASSPORT - Служебный паспорт.   RESIDENCE - Вид на жительство.   TEMPORARY_ASYLUM_CERTIFICATE - Свидетельство о предоставлении временного убежища   FOREIGN_PASSPORT - Паспорт иностранного гражданина.   TEMPORARY_RESIDENCE_PERMIT - Разрешение на временное проживание в РФ.   REFUGEE_CERTIFICATE - Удостоверение беженца.   MIGRATION_CARD - Миграционная Карта.   FOREIGN_DOCUMENT - Документ, выданный иностранным государством и признаваемый в соответствии с международным договором Российской Федерации в качестве документа, удостоверяющего личность лица без гражданства.   OTHERS - Иное. 
 */
public enum DocumentType {
  
  PASSPORT_RF("PASSPORT_RF"),
  
  TEMPORARY_IDENTITY_CARD("TEMPORARY_IDENTITY_CARD"),
  
  SOLDIER_ID("SOLDIER_ID"),
  
  MILITARY_ID("MILITARY_ID"),
  
  SAILOR_ID("SAILOR_ID"),
  
  CERTIFICATE_BIRTH("CERTIFICATE_BIRTH"),
  
  INTERNATIONAL_PASSPORT_RF("INTERNATIONAL_PASSPORT_RF"),
  
  DIPLOMATIC_PASSPORT("DIPLOMATIC_PASSPORT"),
  
  CERTIFICATE_EMPLOYEE_PROSECUTOR("CERTIFICATE_EMPLOYEE_PROSECUTOR"),
  
  SERVICE_PASSPORT("SERVICE_PASSPORT"),
  
  RESIDENCE("RESIDENCE"),
  
  TEMPORARY_ASYLUM_CERTIFICATE("TEMPORARY_ASYLUM_CERTIFICATE"),
  
  FOREIGN_PASSPORT("FOREIGN_PASSPORT"),
  
  TEMPORARY_RESIDENCE_PERMIT("TEMPORARY_RESIDENCE_PERMIT"),
  
  REFUGEE_CERTIFICATE("REFUGEE_CERTIFICATE"),
  
  MIGRATION_CARD("MIGRATION_CARD"),
  
  FOREIGN_DOCUMENT("FOREIGN_DOCUMENT"),
  
  OTHERS("OTHERS");

  private String value;

  DocumentType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DocumentType fromValue(String text) {
    for (DocumentType b : DocumentType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

