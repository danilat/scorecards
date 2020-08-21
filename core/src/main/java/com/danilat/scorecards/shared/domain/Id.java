package com.danilat.scorecards.shared.domain;

import java.util.Objects;

public abstract class Id {

  private final String id;

  public Id(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Id{"
        + "id='" + id + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Id id = (Id) o;
    return Objects.equals(this.id, id.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }

  public String value() {
    return id;
  }
}
