package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.shared.domain.FieldError;

public class BoxerNotFoundError extends FieldError {

  public BoxerNotFoundError(BoxerId boxerId) {
    super("boxerId", "Boxer: " + boxerId + " not found");
  }

  public BoxerNotFoundError(String fieldName, BoxerId boxerId) {
    super(fieldName, "Boxer: " + boxerId + " not found");
  }
}
