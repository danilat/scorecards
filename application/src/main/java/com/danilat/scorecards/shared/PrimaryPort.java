package com.danilat.scorecards.shared;

import com.danilat.scorecards.shared.domain.Errors;

public interface PrimaryPort<E> {

  void success(E entity);

  default void error(Errors errors){
    throw new RuntimeException("The method error is not implemented");
  }
}