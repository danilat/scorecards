package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.core.domain.Fight;
import java.time.LocalDate;

public class RegisterFight {

  public Fight execute(RegisterFightParameters parameters) {
    return new Fight(parameters.getFirstBoxer(), parameters.getSecondBoxer(), parameters.happenAt);
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
  }
}
