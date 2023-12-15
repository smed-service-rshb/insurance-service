package ru.softlab.efr.services.insurance.model.enums;

/**
 * Налоговое резидентство    * russian - Налоговый резидент РФ    * foreign - Налоговый резидент иностранного государства
 */
public enum TaxResidenceEnum {

  RUSSIAN("Налоговый резидент РФ<sup>1</sup>"), //Налоговый резидент РФ

  FOREIGN("Налоговый резидент иностранного государства<sup>2</sup>"),  //Налоговый резидент иностранного государства

  OTHER("Иное<sup>3</sup>");  //Иное

  private String value;

  TaxResidenceEnum(String value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

}

