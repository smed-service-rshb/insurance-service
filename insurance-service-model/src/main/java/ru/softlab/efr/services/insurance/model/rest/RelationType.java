package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Типы родственных отношений   * spouse - Супруг/супруга   * parent - Отец/мать   * child - Сын/дочь   * grandparent - Дедушка/бабушка   * grandchild - Внук/внучка   * sibling - Брат/сестра (в том числе неполнородные)   * stepparent - Отчим/мачеха 
 */
public enum RelationType {
  
  SPOUSE("spouse"),
  
  PARENT("parent"),
  
  CHILD("child"),
  
  GRANDPARENT("grandparent"),
  
  GRANDCHILD("grandchild"),
  
  SIBLING("sibling"),
  
  STEPPARENT("stepparent");

  private String value;

  RelationType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RelationType fromValue(String text) {
    for (RelationType b : RelationType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

