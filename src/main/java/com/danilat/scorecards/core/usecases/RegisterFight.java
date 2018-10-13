package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.InvalidFightException;
import java.time.LocalDate;

public class RegisterFight {

  public Fight execute(RegisterFightParameters parameters) {
    if (!parameters.areValid()) {
      throw new InvalidFightException();
    }
    return new Fight(parameters.getFirstBoxer(), parameters.getSecondBoxer(),
        parameters.getHappenAt());
  }

  public static class RegisterFightParameters {

    private final String firstBoxer;
    private final String secondBoxer;
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
      return this.getHappenAt() != null && this.getFirstBoxer() != null
          && this.getSecondBoxer() != null;
    }
  }
}
