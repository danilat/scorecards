package com.danilat.scorecards.core.domain;

import java.util.Objects;

public abstract class EntityId {

  private final String id;

  public EntityId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "EntityId{"
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
    EntityId entityId = (EntityId) o;
    return Objects.equals(id, entityId.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }

  public String value() {
    return id;
  }
}
