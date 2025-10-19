package com.es.webhook.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlgorithmEnum {
  HMACSHA256("HmacSHA256"),

  HMACSHA384("HmacSHA384"),

  HMACSHA512("HmacSHA512");

  private String value;

  AlgorithmEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static AlgorithmEnum fromValue(String value) {
    for (AlgorithmEnum b : AlgorithmEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
