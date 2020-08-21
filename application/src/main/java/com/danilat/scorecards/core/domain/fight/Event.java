package com.danilat.scorecards.core.domain.fight;

import java.time.LocalDate;

public class Event {

  private final LocalDate happenAt;
  private String place;

  public Event(LocalDate happenAt, String place) {
    this.happenAt = happenAt;
    this.place = place;
  }

  public String place() {
    return place;
  }

  public LocalDate happenAt() {
    return happenAt;
  }
}
