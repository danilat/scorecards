package com.danilat.scorecards.shared.events;

import java.time.Instant;

public abstract class DomainEvent {

  private Instant happenedAt;
  private DomainEventId eventId;

  protected DomainEvent(Instant happenedAt, String eventId) {
    this.happenedAt = happenedAt;
    this.eventId = new DomainEventId(eventId);
  }

  protected DomainEvent(Instant happenedAt, DomainEventId eventId) {
    this.happenedAt = happenedAt;
    this.eventId = eventId;
  }

  public Instant happenedAt() {
    return happenedAt;
  }

  public DomainEventId eventId() {
    return eventId;
  }

  public String type(){
    return this.getClass().getSimpleName();
  }
}
