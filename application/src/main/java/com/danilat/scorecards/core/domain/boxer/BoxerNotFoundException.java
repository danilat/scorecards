package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.ScoreCardsException;

public class BoxerNotFoundException extends ScoreCardsException {

  private final BoxerId boxerId;

  public BoxerNotFoundException(BoxerId boxerId) {
    this.boxerId = boxerId;
  }

  @Override
  public String getMessage() {
    return this.boxerId.toString() + " not found";
  }
}
