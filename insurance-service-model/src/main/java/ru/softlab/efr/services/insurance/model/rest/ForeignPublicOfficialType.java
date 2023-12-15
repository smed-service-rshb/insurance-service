package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Признак ИПДЛ   * stateLeaders - Главы государств или правительств (независимо от формы государственного устройства)   * ministers - Министры, их заместители и помощники   * courtOfficials - Должностные лица судебных органов власти последней инстанции (Верховный, Конституционный суд)   * highOfficials - Высшие правительственные чиновники   * prosecutors - Государственный прокурор и его заместители   * highMilitaryOfficials - Высшие военные чиновники   * nationalBankLeaders - Руководители и члены Советов директоров Национальных Банков   * politicalLeaders - Лидер официально зарегистрированной политической партии движения, его заместитель   * nationalCorporationLeaders - Руководители государственных корпораций   * religiousLeaders - Глава религиозной организации (осуществляющей государственные управленческие функции), его заместитель   * ambassadors - Послы   * internationalOrganizationLeaders - Руководители, заместители руководителей международных организаций (ООН, ОЭСР, ОПЕК, Олимпийский комитет, Гаагский трибунал)   * internationalCourtLeaders - Руководители и члены международных судебных Организаций (Суд по правам человека, Гаагский трибунал) 
 */
public enum ForeignPublicOfficialType {
  
  STATELEADERS("stateLeaders"),
  
  MINISTERS("ministers"),
  
  COURTOFFICIALS("courtOfficials"),
  
  HIGHOFFICIALS("highOfficials"),
  
  PROSECUTORS("prosecutors"),
  
  HIGHMILITARYOFFICIALS("highMilitaryOfficials"),
  
  NATIONALBANKLEADERS("nationalBankLeaders"),
  
  POLITICALLEADERS("politicalLeaders"),
  
  NATIONALCORPORATIONLEADERS("nationalCorporationLeaders"),
  
  RELIGIOUSLEADERS("religiousLeaders"),
  
  AMBASSADORS("ambassadors"),
  
  INTERNATIONALORGANIZATIONLEADERS("internationalOrganizationLeaders"),
  
  INTERNATIONALCOURTLEADERS("internationalCourtLeaders");

  private String value;

  ForeignPublicOfficialType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ForeignPublicOfficialType fromValue(String text) {
    for (ForeignPublicOfficialType b : ForeignPublicOfficialType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

