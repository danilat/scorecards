package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.shared.PrimaryPort;

public interface UseCase<OUTPUT, INPUT> {

  void execute(PrimaryPort<OUTPUT> primaryPort, INPUT parameters);

  class Empty {

  }
}
