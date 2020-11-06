package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UseCase;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Optional;

public class RetrieveAFight implements UseCase<FightId, FightWithBoxers> {

  private final FightWithBoxersRepository fightWithBoxersRepository;

  public RetrieveAFight(
      FightWithBoxersRepository fightWithBoxersRepository) {
    this.fightWithBoxersRepository = fightWithBoxersRepository;
  }

  public void execute(
      FightId id, PrimaryPort<FightWithBoxers> primaryPort) {
    Optional<FightWithBoxers> optionalFightWithBoxers = fightWithBoxersRepository.get(id);
    if (optionalFightWithBoxers.isPresent()) {
      primaryPort.success(optionalFightWithBoxers.get());
    } else {
      Error error = new Error("fightId", id + " not found");
      Errors errors = new Errors();
      errors.add(error);
      primaryPort.error(errors);
    }
  }
}
