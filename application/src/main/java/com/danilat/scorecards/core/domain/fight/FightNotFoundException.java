package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.shared.domain.ScoreCardsException;

public class FightNotFoundException extends ScoreCardsException {

  private final FightId fightId;

  public FightNotFoundException(FightId fightId) {
    this.fightId = fightId;
  }

  @Override
  public String getMessage() {
    return this.fightId.toString() + " not found";
  }
}
