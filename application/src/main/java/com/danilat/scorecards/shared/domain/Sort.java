package com.danilat.scorecards.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Sort {

  public static final Sort DEFAULT = new Sort("id", Direction.ASC);
  private final String field;
  private final Direction direction;

  public Sort(String field, Direction direction) {
    this.field = field;
    this.direction = direction;
  }

  public static Sort desc(String field) {
    return new Sort(field, Direction.DESC);
  }

  public String field() {
    return field;
  }

  public Direction direction() {
    return direction;
  }

  public enum Direction {
    ASC, DESC;

    public String value() {
      return this.name();
    }
  }
}
