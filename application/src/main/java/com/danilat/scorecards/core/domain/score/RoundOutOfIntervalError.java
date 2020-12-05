package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.shared.domain.FieldError;

public class RoundOutOfIntervalError extends FieldError {

  public RoundOutOfIntervalError(Integer round, Integer maxNumberOfRounds) {
    super("round", round + " is out of the interval between 1 and " + maxNumberOfRounds);
  }
}
