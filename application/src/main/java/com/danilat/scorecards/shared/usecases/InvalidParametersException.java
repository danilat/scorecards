package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;

import javax.validation.ConstraintViolation;

public abstract class InvalidParametersException extends ScoreCardsBusinessException {
    public void addParameterViolation(ConstraintViolation violation) {
        Error error = new Error(violation);
        getErrors().add(error);
    }
}
