package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.Entity;
import java.util.Objects;

public class Boxer extends Entity<BoxerId> {

  private final String name;

  public Boxer(BoxerId id, String name) {
    super(id);
    this.name = name;
  }

  @Override
  public BoxerId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Boxer boxer = (Boxer) o;
    return Objects.equals(name, boxer.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }

  @Override
  public String toString() {
    return "Boxer{" +
        "name='" + name + '\'' +
        ", id=" + id +
        '}';
  }
}
