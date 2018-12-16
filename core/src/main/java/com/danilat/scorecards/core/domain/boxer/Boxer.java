package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.core.domain.Entity;

public class Boxer extends Entity<BoxerId> {

  private String name;

  public Boxer(BoxerId id, String name) {
    super(id);
    this.name = name;
  }

  @Override
  public BoxerId getId() {
    return this.id;
  }
}
