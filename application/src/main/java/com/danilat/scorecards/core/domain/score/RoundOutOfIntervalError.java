package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.shared.domain.errors.Error;

public class RoundOutOfIntervalError extends Error {

  public RoundOutOfIntervalError(Integer round, Integer maxNumberOfRounds) {
    super(round + " is out of the interval between 1 and " + maxNumberOfRounds);
  }
}
