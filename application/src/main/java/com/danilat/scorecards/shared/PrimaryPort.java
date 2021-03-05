package com.danilat.scorecards.shared;

import com.danilat.scorecards.shared.domain.errors.Error;

public interface PrimaryPort<E> {

  void success(E response);

  default void error(Error errors) {
    throw new RuntimeException("The method error is not implemented");
  }
}
