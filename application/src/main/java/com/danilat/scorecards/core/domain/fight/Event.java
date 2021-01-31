package com.danilat.scorecards.core.domain.fight;

import java.time.LocalDate;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(happenAt, event.happenAt) &&
        Objects.equals(place, event.place);
  }

  @Override
  public int hashCode() {
    return Objects.hash(happenAt, place);
  }

  @Override
  public String toString() {
    return "Event{" +
        "happenAt=" + happenAt +
        ", place='" + place + '\'' +
        '}';
  }
}
