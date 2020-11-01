package com.danilat.scorecards.shared.events;

import java.time.LocalDate;

public abstract class DomainEvent {

  private LocalDate happenedAt;
  private DomainEventId eventId;

  protected DomainEvent(LocalDate happenedAt, String eventId) {
    this.happenedAt = happenedAt;
    this.eventId = new DomainEventId(eventId);
  }

  protected DomainEvent(LocalDate happenedAt, DomainEventId eventId) {
    this.happenedAt = happenedAt;
    this.eventId = eventId;
  }

  public LocalDate happenedAt() {
    return happenedAt;
  }

  public DomainEventId eventId() {
    return eventId;
  }
}
