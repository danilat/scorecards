package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.Entity;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class Fight extends Entity<FightId> {

  private final BoxerId firstBoxerId;
  private final BoxerId secondBoxerId;

  public Fight(FightId id, BoxerId firstBoxerId, BoxerId secondBoxerId) {
    super(id);
    this.firstBoxerId = firstBoxerId;
    this.secondBoxerId = secondBoxerId;
  }

  public BoxerId firstBoxerId() {
    return this.firstBoxerId;
  }

  public BoxerId secondBoxerId() {
    return this.secondBoxerId;
  }

  public FightId id() {
    return id;
  }
}
