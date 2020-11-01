package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.usecases.InvalidParametersException;

import java.util.Set;
import javax.validation.ConstraintViolation;

public class InvalidFightException extends InvalidParametersException {

  public InvalidFightException(Set<ConstraintViolation<RegisterFightParameters>> violations) {
    super();
    violations.forEach(violation -> addParameterViolation(violation));
  }
}
