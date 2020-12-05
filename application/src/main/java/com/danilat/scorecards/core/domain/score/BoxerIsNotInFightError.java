package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.FieldError;

public class BoxerIsNotInFightError extends FieldError {

  public BoxerIsNotInFightError(String fieldName, BoxerId boxerId, FightId fightId) {
    super(fieldName, boxerId + " is not in the fight" + fightId);
  }
}
