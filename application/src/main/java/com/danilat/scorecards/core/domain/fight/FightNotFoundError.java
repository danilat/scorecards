package com.danilat.scorecards.core.domain.fight;


import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class FightNotFoundError extends SimpleError {

  public FightNotFoundError(FightId fightId) {
    super("Fight: " + fightId + " not found");
  }
}
