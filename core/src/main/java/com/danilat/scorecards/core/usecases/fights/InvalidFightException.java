package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.ScoreCardsBusinessException;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

import javax.validation.ConstraintViolation;

public class InvalidFightException extends ScoreCardsBusinessException {

  private final Set<ConstraintViolation<RegisterFightParameters>> violations;

  public InvalidFightException(Set<ConstraintViolation<RegisterFightParameters>> violations) {
    this.violations = violations;
  }

  @Override
  public String getMessage() {
    List<String> messages = new ArrayList<>();
    violations.stream()
        .forEach(violation -> messages.add(violation.getMessage()));
    return messages.stream().collect(joining(". "));
  }
}
