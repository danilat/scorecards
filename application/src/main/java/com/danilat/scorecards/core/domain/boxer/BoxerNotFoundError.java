package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.errors.Error;

public class BoxerNotFoundError extends Error {

  public BoxerNotFoundError(BoxerId boxerId) {
    super("Boxer: " + boxerId + " not found");
  }
}
