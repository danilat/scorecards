package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundError;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.usecases.UseCase;
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
      FieldErrors errors = FieldErrors.newWithError("fightId", new FightNotFoundError(id));
      primaryPort.error(errors);
    }
  }
}
