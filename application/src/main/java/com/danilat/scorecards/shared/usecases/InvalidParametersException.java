package com.danilat.scorecards.shared.usecases;

import static java.util.stream.Collectors.joining;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.domain.ScoreCardsException;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;

public abstract class InvalidParametersException extends ScoreCardsException {
  private final Errors errors;

  protected InvalidParametersException() {
    errors = new Errors();
  }

  @Override
  public String getMessage() {
    List<String> messages = new ArrayList<>();
    errors.forEach(error -> messages.add(error.getMessage()));
    return messages.stream().collect(joining(". "));
  }

  public Errors getErrors() {
    return errors;
  }

  public void addParameterViolation(ConstraintViolation violation) {
    String fieldName = violation.getPropertyPath().toString();
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    Error error = new Error(fieldName, message, messageTemplate);
    getErrors().add(error);
  }
}
