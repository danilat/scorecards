package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.util.Collection;

public class RetrieveAllFights implements UseCase<Collection<FightWithBoxers>, Empty> {

  private final FightWithBoxersFetcher fightWithBoxersFetcher;

  public RetrieveAllFights(FightWithBoxersFetcher fightWithBoxersFetcher) {
    this.fightWithBoxersFetcher = fightWithBoxersFetcher;
  }

  @Override
  public void execute(PrimaryPort<Collection<FightWithBoxers>> primaryPort, Empty parameters) {
    primaryPort.success(fightWithBoxersFetcher.all());
  }
}
