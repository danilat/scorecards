package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.fight.Fight;
import org.springframework.stereotype.Component;

@Component
public class World {

  private Fight fight;

  public void setFight(Fight fight) {
    this.fight = fight;
  }

  public Fight getFight() {
    return fight;
  }
}
