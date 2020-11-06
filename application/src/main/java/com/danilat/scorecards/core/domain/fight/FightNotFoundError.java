package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.shared.domain.Error;

public class FightNotFoundError extends Error {

  public FightNotFoundError(FightId fightId) {
    super("fightId", "Fight: " + fightId + " not found");
  }
}
