package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class RoundOutOfIntervalError extends SimpleError {

  public RoundOutOfIntervalError(Integer round, Integer maxNumberOfRounds) {
    super(round + " is out of the interval between 1 and " + maxNumberOfRounds);
  }
}
