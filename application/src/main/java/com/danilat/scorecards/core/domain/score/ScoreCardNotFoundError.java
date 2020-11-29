package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.shared.domain.Error;

public class ScoreCardNotFoundError extends Error {

  public ScoreCardNotFoundError(ScoreCardId id) {
    super("scoreCardId", "ScoreCard: " + id + " not found");
  }
}
