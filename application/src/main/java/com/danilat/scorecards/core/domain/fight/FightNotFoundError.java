package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.shared.domain.FieldError;

public class FightNotFoundError extends FieldError {

  public FightNotFoundError(FightId fightId) {
    super("fightId", "Fight: " + fightId + " not found");
  }
}
