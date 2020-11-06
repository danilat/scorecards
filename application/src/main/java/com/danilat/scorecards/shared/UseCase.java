package com.danilat.scorecards.shared;

public interface UseCase<OUTPUT, INPUT> {

  void execute(PrimaryPort<OUTPUT> primaryPort, INPUT parameters);

  class Empty {

  }
}
