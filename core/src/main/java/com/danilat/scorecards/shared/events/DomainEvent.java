package com.danilat.scorecards.shared.events;

import java.time.LocalDate;

public abstract class DomainEvent {
  private LocalDate happenedAt;
  private String eventId;

  protected DomainEvent(LocalDate happenedAt, String eventId) {
    this.happenedAt = happenedAt;
    this.eventId = eventId;
  }

  public LocalDate happenedAt() {
    return happenedAt;
  }

  public String eventId() {
    return eventId;
  }
}
