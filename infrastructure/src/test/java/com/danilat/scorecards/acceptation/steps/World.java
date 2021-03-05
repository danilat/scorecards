package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.domain.Errors;
import org.springframework.stereotype.Component;

@Component
public class World {

  private Fight fight;
  private Errors errors;

  public void setFight(Fight fight) {
    this.fight = fight;
  }

  public Fight getFight() {
    return fight;
  }

  public void setErrors(Errors errors) {
    this.errors = errors;
  }

  public Errors getErrors() {
    return errors;
  }
}
