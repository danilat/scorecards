package com.danilat.scorecards.core.usecases;

public interface UseCase<T> {
  Object execute(T parameters);

  class Empty {

  }
}
