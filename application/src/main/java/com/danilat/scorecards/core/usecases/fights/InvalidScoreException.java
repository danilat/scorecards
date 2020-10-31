package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class InvalidScoreException extends ScoreCardsBusinessException {
    private final Errors errors;

    public InvalidScoreException(Set<ConstraintViolation<ScoreRound.ScoreFightParameters>> violations) {
        errors = new Errors();
        violations.stream().forEach(violation -> {
            Error error = new Error(violation);
            errors.add(error);
        });
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
