package com.danilat.scorecards.core.domain;

public abstract class Entity<ID extends EntityId> {

  protected ID id;

  public Entity(ID id) {
    this.id = id;
  }

  abstract public ID id();
}
