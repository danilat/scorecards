package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.domain.errors.Error;
import org.springframework.stereotype.Component;

@Component
public class World {

  private Fight fight;
  private Error errors;

  public void setFight(Fight fight) {
    this.fight = fight;
  }

  public Fight getFight() {
    return fight;
  }

  public void setErrors(Error errors) {
    this.errors = errors;
  }

  public Error getErrors() {
    return errors;
  }
}
