package com.danilat.scorecards.core.domain.fight.events;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.events.DomainEvent;
import java.time.LocalDate;

public class FightCreated extends DomainEvent {

  private Fight fight;

  public FightCreated(Fight fight, LocalDate happenedAt, String eventId) {
    super(happenedAt, eventId);
    this.fight = fight;
  }

  public Fight fight() {
    return fight;
  }


}
