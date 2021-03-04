package com.danilat.scorecards.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class Id {

  private final String id;

  public Id(String id) {
    this.id = id;
  }

  public String value() {
    return id;
  }
}
