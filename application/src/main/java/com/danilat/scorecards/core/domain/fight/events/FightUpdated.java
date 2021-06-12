package com.danilat.scorecards.core.domain.fight.events;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@ToString
@EqualsAndHashCode
public class FightUpdated extends DomainEvent {

  private Fight fight;

  public FightUpdated(Fight fight, Instant happenedAt, DomainEventId eventId) {
    super(happenedAt, eventId);
    this.fight = fight;
  }

  public Fight fight() {
    return fight;
  }

  public static FightUpdated create(Fight fight, Instant happenedAt) {
    return new FightUpdated(fight, happenedAt, new DomainEventId(fight.id().value() + happenedAt));
  }
}
