package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.util.Collection;

public class RetrieveLastPastFights implements
    UseCase<Collection<FightWithBoxers>, Empty> {

  private final FightWithBoxersFetcher fightWithBoxersFetcher;
  private final Clock clock;
  public static final int LIMIT = 20;

  public RetrieveLastPastFights(FightWithBoxersFetcher fightWithBoxersFetcher,
      Clock clock) {
    this.fightWithBoxersFetcher = fightWithBoxersFetcher;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<Collection<FightWithBoxers>> primaryPort, Empty parameters) {
    primaryPort.success(fightWithBoxersFetcher.findAllBefore(clock.today(), Sort.desc("happenAt"), LIMIT));
  }

  public void execute(PrimaryPort<Collection<FightWithBoxers>> primaryPort) {
    execute(primaryPort, new Empty());
  }
}
