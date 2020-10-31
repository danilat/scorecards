package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.shared.usecases.InvalidParametersException;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class InvalidScoreException extends InvalidParametersException {
    public InvalidScoreException(Set<ConstraintViolation<ScoreRound.ScoreFightParameters>> violations) {
        violations.stream().forEach(violation -> {
            addParameterViolation(violation);
        });
    }
}
