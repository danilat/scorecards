package com.danilat.scorecards.core.domain;

import java.time.LocalDate;

public class Fight {

  private final String firstBoxer;
  private final String secondBoxer;
  private final LocalDate happenAt;
  private String id;

  public Fight(String id, String firstBoxer, String secondBoxer, LocalDate happenAt) {
    this.id = id;
    this.firstBoxer = firstBoxer;
    this.secondBoxer = secondBoxer;
    this.happenAt = happenAt;
  }

  public LocalDate happenAt() {
    return this.happenAt;
  }

  public String firstBoxer() {
    return this.firstBoxer;
  }

  public String secondBoxer() {
    return this.secondBoxer;
  }

  public String id() {
    return id;
  }
}
