package com.danilat.scorecards.shared;

import java.time.Instant;
import java.time.LocalDate;

public class Clock {

  public Instant now() {
    return Instant.now();
  }

  public LocalDate today() {
    return LocalDate.now();
  }
}
