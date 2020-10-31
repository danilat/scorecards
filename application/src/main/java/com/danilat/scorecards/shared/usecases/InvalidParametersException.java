package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public abstract class InvalidParametersException extends ScoreCardsBusinessException {
    private final Errors errors;

    public InvalidParametersException() {
        errors = new Errors();
    }

    public void addParameterViolation(ConstraintViolation violation) {
        Error error = new Error(violation);
        errors.add(error);
    }

    @Override
    public String getMessage() {
        List<String> messages = new ArrayList<>();
        errors.stream()
                .forEach(error -> messages.add(error.getMessage()));
        return messages.stream().collect(joining(". "));
    }

    public Errors getErrors() {
        return errors;
    }
}
