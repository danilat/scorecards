package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Error;

public class BoxerIsNotInFightError extends Error {

  public BoxerIsNotInFightError(String fieldName, BoxerId boxerId, FightId fightId) {
    super(fieldName, boxerId + " is not in the fight" + fightId);
  }
}
