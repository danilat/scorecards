package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.Entity;

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
}
