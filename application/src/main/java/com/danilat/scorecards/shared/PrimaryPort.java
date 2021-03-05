package com.danilat.scorecards.shared;

import com.danilat.scorecards.shared.domain.errors.Errors;

public interface PrimaryPort<E> {

  void success(E response);

  default void error(Errors errors) {
    throw new RuntimeException("The method error is not implemented");
  }
}
