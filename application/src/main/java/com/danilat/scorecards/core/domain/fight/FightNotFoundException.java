package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;

public class FightNotFoundException extends ScoreCardsBusinessException {

  private final FightId fightId;

  public FightNotFoundException(FightId fightId) {
    this.fightId = fightId;
  }

  @Override
  public String getMessage() {
    return this.fightId.toString() + " not found";
  }
}
