package com.danilat.scorecards.core.domain.boxer;

public class BoxerNotFoundException extends RuntimeException {

  private final BoxerId boxerId;

  public BoxerNotFoundException(BoxerId boxerId) {
    this.boxerId = boxerId;
  }

  @Override
  public String getMessage() {
    return this.boxerId.toString() + " not found";
  }
}
