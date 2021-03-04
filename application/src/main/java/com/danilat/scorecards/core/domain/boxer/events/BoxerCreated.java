package com.danilat.scorecards.core.domain.boxer.events;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;

public class BoxerCreated extends DomainEvent {

  private Boxer boxer;

  public BoxerCreated(Instant happenedAt, DomainEventId eventId, Boxer boxer) {
    super(happenedAt, eventId);
    this.boxer = boxer;
  }

  public Boxer boxer() {
    return boxer;
  }
}
