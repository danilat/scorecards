package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class Fight extends Entity<FightId> {

  private final BoxerId firstBoxer;
  private final BoxerId secondBoxer;
  private Event event;
  private Integer numberOfRounds;

  public Fight(FightId id, BoxerId firstBoxer, BoxerId secondBoxer, Event event, Integer numberOfRounds) {
    super(id);
    this.firstBoxer = firstBoxer;
    this.secondBoxer = secondBoxer;
    this.event = event;
    this.numberOfRounds = numberOfRounds;
  }

  public BoxerId firstBoxer() {
    return this.firstBoxer;
  }

  public BoxerId secondBoxer() {
    return this.secondBoxer;
  }

  public FightId id() {
    return id;
  }

  public Event event() {
    return this.event;
  }

  public Integer numberOfRounds() {
    return this.numberOfRounds;
  }
}
