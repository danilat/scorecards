package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class Fight {

  private final BoxerId firstBoxer;
  private final BoxerId secondBoxer;
  private FightId id;
  private Event event;

  public Fight(FightId id, BoxerId firstBoxer, BoxerId secondBoxer, Event event) {
    this.id = id;
    this.firstBoxer = firstBoxer;
    this.secondBoxer = secondBoxer;
    this.event = event;
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
}
