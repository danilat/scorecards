package com.danilat.scorecards.shared.domain;

public abstract class Entity<I extends Id> {

  protected I id;

  public Entity(I id) {
    this.id = id;
  }

  public abstract I id();

  @Override
  public String toString() {
    return getClass().getName() + "{" +
        "id=" + id +
        '}';
  }
}
