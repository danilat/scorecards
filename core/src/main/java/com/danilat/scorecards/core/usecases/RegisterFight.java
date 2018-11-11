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
    validate(parameters);
    return new Fight(parameters.getFirstBoxer(), parameters.getSecondBoxer(),
        parameters.getHappenAt());
  }

  private void validate(RegisterFightParameters parameters){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<RegisterFightParameters>> violations = validator.validate(parameters);
    if (!violations.isEmpty()) {
      throw new InvalidFightException(violations);
    }
  }

  public static class RegisterFightParameters {

    @NotNull(message = "firstBoxer is mandatory")
    private final String firstBoxer;
    @NotNull(message = "secondBoxer is mandatory")
    private final String secondBoxer;
    @NotNull(message = "happenAt is mandatory")
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
  }
}
