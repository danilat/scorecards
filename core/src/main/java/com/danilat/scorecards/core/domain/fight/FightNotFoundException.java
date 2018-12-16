package com.danilat.scorecards.core.domain.fight;

public class FightNotFoundException extends RuntimeException {

  private final FightId fightId;

  public FightNotFoundException(FightId fightId) {
    this.fightId = fightId;
  }

  @Override
  public String getMessage() {
    return this.fightId.toString() + " not found";
  }
}
