package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.errors.Error;

public class BoxerIsNotInFightError extends Error {

  public BoxerIsNotInFightError(BoxerId boxerId, FightId fightId) {
    super(boxerId + " is not in the fight" + fightId);
  }
}
