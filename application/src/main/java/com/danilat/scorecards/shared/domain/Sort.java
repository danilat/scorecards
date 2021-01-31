package com.danilat.scorecards.shared.domain;

import java.util.Objects;

public class Sort {

  public static final Sort DEFAULT = new Sort("id", Direction.ASC);
  private final String field;
  private final Direction direction;

  public Sort(String field, Direction direction) {
    this.field = field;
    this.direction = direction;
  }

  public String field() {
    return field;
  }

  public Direction direction() {
    return direction;
  }

  public enum Direction {
    ASC, DESC;

    public String value(){
      return this.name();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sort sort = (Sort) o;
    return Objects.equals(field, sort.field) &&
        direction == sort.direction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, direction);
  }
}
