package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.EntityId;

public class FightId implements EntityId {

  private final String id;

  public FightId(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }
}
