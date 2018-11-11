package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.InvalidFightException;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

public class RegisterFight {

  public Fight execute(RegisterFightParameters parameters) {
    if (!parameters.areValid()) {
      throw new InvalidFightException();
    }
    return new Fight(parameters.getFirstBoxer(), parameters.getSecondBoxer(),
        parameters.getHappenAt());
  }

  public static class RegisterFightParameters {

    @NotNull
    private final String firstBoxer;
    @NotNull
    private final String secondBoxer;
    @NotNull
    private final LocalDate happenAt;

    public String getFirstBoxer() {
      return firstBoxer;
    }

    public String getSecondBoxer() {
      return secondBoxer;
    }

    public LocalDate getHappenAt() {
      return happenAt;
    }

    public RegisterFightParameters(String firstBoxer, String secondBoxer, LocalDate happenAt) {
      this.firstBoxer = firstBoxer;
      this.secondBoxer = secondBoxer;
      this.happenAt = happenAt;
    }

    public boolean areValid() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<RegisterFightParameters>> violations = validator.validate(this);
      return violations.isEmpty();
    }
  }
}
