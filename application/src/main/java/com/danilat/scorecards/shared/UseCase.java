package com.danilat.scorecards.shared;

public interface UseCase<T> {

  Object execute(T parameters);

  class Empty {

  }
}
