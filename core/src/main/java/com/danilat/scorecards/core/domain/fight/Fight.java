package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import java.time.LocalDate;

public class Fight {

  private final BoxerId firstBoxer;
  private final BoxerId secondBoxer;
  private final LocalDate happenAt;
  private FightId id;

  public Fight(FightId id, BoxerId firstBoxer, BoxerId secondBoxer, LocalDate happenAt) {
    this.id = id;
    this.firstBoxer = firstBoxer;
    this.secondBoxer = secondBoxer;
    this.happenAt = happenAt;
  }

  public LocalDate happenAt() {
    return this.happenAt;
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
}
