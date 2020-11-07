package com.danilat.scorecards.core.domain.fight.events;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;

public class FightCreated extends DomainEvent {

  private Fight fight;

  public FightCreated(Fight fight, Instant happenedAt, DomainEventId eventId) {
    super(happenedAt, eventId);
    this.fight = fight;
  }

  public Fight fight() {
    return fight;
  }


}
