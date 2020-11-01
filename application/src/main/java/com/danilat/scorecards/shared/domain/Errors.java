package com.danilat.scorecards.shared.domain;

import java.util.ArrayList;

public class Errors extends ArrayList<Error> {

  public boolean hasError(String fieldName) {
    return this.stream().anyMatch(error -> error.getFieldName().equals(fieldName));
  }
}
