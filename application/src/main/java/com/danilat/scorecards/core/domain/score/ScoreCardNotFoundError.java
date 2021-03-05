package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class ScoreCardNotFoundError extends SimpleError {

  public ScoreCardNotFoundError(FightId fightId, AccountId accountId) {
    super("ScoreCard: " + fightId + " and " + accountId + " not found");
  }
}
