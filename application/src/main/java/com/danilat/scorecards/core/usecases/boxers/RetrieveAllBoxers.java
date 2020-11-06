package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UseCase;
import java.util.Map;

public class RetrieveAllBoxers implements UseCase<UseCase.Empty> {

  private final BoxerRepository boxerRepository;

  public RetrieveAllBoxers(
      BoxerRepository boxerRepository) {
    this.boxerRepository = boxerRepository;
  }

  public void execute(PrimaryPort<Map<BoxerId, Boxer>> primaryPort) {
    primaryPort.success(boxerRepository.all());
  }

  @Override
  public Object execute(Empty parameters) {
    return null;
  }
}
