package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;

public class RoundOutOfIntervalException extends ScoreCardsBusinessException {

    public RoundOutOfIntervalException(Integer maxNumberOfRounds, Integer round) {
        String message = round + " is not in the interval of rounds between 1 and " + maxNumberOfRounds;
        Error error = new Error("round", message);
        getErrors().add(error);
    }
}
