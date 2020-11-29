package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Optional;

public class RetrieveAFight implements UseCase<FightWithBoxers, FightId> {

  private final FightWithBoxersFetcher fightWithBoxersFetcher;

  public RetrieveAFight(
      FightWithBoxersFetcher fightWithBoxersFetcher) {
    this.fightWithBoxersFetcher = fightWithBoxersFetcher;
  }

  public void execute(
      PrimaryPort<FightWithBoxers> primaryPort, FightId id) {
    Optional<FightWithBoxers> optionalFightWithBoxers = fightWithBoxersFetcher.get(id);
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
