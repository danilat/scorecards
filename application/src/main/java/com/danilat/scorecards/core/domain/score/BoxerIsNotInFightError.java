package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class BoxerIsNotInFightError extends SimpleError {

  public BoxerIsNotInFightError(BoxerId boxerId, FightId fightId) {
    super(boxerId + " is not in the fight" + fightId);
  }
}
