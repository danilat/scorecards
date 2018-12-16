package com.danilat.scorecards.core.domain;

public abstract class EntityId {
  private final String id;

  public EntityId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "EntityId{" +
        "id='" + id + '\'' +
        '}';
  }
}
