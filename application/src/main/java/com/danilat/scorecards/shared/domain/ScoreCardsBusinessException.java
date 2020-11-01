package com.danilat.scorecards.shared.domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public abstract class ScoreCardsBusinessException extends RuntimeException {
    private final Errors errors;

    protected ScoreCardsBusinessException() {
        errors = new Errors();
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
