package com.danilat.scorecards.shared;

import com.danilat.scorecards.shared.domain.FieldErrors;

public interface PrimaryPort<E> {

  void success(E response);

  default void error(FieldErrors errors) {
    throw new RuntimeException("The method error is not implemented");
  }
}
