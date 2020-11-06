package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.Error;

public class BoxerNotFoundError extends Error {

  public BoxerNotFoundError(BoxerId boxerId) {
    super("boxerId", "Boxer: " + boxerId + " not found");
  }

  public BoxerNotFoundError(String fieldName, BoxerId boxerId) {
    super(fieldName, "Boxer: " + boxerId + " not found");
  }
}
