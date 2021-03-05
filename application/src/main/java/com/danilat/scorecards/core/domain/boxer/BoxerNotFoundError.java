package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class BoxerNotFoundError extends SimpleError {

  public BoxerNotFoundError(BoxerId boxerId) {
    super("Boxer: " + boxerId + " not found");
  }
}
