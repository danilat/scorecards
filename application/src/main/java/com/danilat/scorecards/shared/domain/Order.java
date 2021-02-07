package com.danilat.scorecards.shared.domain;

import java.util.Objects;

public class Order {

  private final String direction;
  private final String field;

  public Order(String direction, String field) {
    this.direction = direction;
    this.field = field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(direction, order.direction) &&
        Objects.equals(field, order.field);
  }

  @Override
  public int hashCode() {
    return Objects.hash(direction, field);
  }

  public static Order desc(String field) {
    return new Order("desc", field);
  }
}
