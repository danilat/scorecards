package com.danilat.scorecards.core.usecases.fights;

import static java.util.stream.Collectors.joining;

import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class InvalidFightException extends ScoreCardsBusinessException {

  private final Errors errors;

  public InvalidFightException(Set<ConstraintViolation<RegisterFightParameters>> violations) {
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
