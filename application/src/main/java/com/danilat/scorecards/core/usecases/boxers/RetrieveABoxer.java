package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundError;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Optional;

public class RetrieveABoxer implements UseCase<Boxer, BoxerId> {

  private final BoxerRepository boxerRepository;

  public RetrieveABoxer(BoxerRepository boxerRepository) {
    this.boxerRepository = boxerRepository;
  }

  @Override
  public void execute(PrimaryPort<Boxer> primaryPort, BoxerId boxerId) {
    Optional<Boxer> optionalBoxer = boxerRepository.get(boxerId);
    if (optionalBoxer.isPresent()) {
      primaryPort.success(optionalBoxer.get());
    } else {
      FieldErrors error = FieldErrors.newWithError("boxerId", new BoxerNotFoundError(boxerId));
      primaryPort.error(error);
    }
  }
}
