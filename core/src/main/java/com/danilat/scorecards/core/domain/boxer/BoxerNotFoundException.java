package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.core.domain.ScoreCardsBusinessException;

public class BoxerNotFoundException extends ScoreCardsBusinessException {

  private final BoxerId boxerId;

  public BoxerNotFoundException(BoxerId boxerId) {
    this.boxerId = boxerId;
  }

  @Override
  public String getMessage() {
    return this.boxerId.toString() + " not found";
  }
}
