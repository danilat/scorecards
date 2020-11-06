package com.danilat.scorecards.shared;

public interface UseCase<INPUT, OUTPUT> {
  
  void execute(INPUT parameters, PrimaryPort<OUTPUT> primaryPort);

  class Empty {

  }
}
