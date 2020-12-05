package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.shared.domain.FieldErrors;
import org.springframework.stereotype.Component;

@Component
public class World {

  private Fight fight;
  private FieldErrors errors;

  public void setFight(Fight fight) {
    this.fight = fight;
  }

  public Fight getFight() {
    return fight;
  }

  public void setErrors(FieldErrors errors) {
    this.errors = errors;
  }

  public FieldErrors getErrors() {
    return errors;
  }
}
