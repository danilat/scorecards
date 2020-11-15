package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.util.Collection;

public class RetrieveAllBoxers implements UseCase<Collection<Boxer>, Empty> {

  private final BoxerRepository boxerRepository;

  public RetrieveAllBoxers(
      BoxerRepository boxerRepository) {
    this.boxerRepository = boxerRepository;
  }

  public void execute(PrimaryPort<Collection<Boxer>> primaryPort) {
    primaryPort.success(boxerRepository.all().values());
  }

  @Override
  public void execute(PrimaryPort<Collection<Boxer>> primaryPort, Empty parameters) {
    execute(primaryPort);
  }
}
