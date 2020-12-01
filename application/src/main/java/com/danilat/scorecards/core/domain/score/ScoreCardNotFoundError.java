package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Error;

public class ScoreCardNotFoundError extends Error {

  public ScoreCardNotFoundError(ScoreCardId id) {
    super("scoreCardId", "ScoreCard: " + id + " not found");
  }

  public ScoreCardNotFoundError(FightId fightId, AccountId accountId) {
    super("scoreCardId", "ScoreCard: " + fightId + " and " + accountId +" not found");
  }
}
