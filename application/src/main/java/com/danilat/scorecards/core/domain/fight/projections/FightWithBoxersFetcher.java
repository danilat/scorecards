package com.danilat.scorecards.core.domain.fight.projections;

import com.danilat.scorecards.core.domain.fight.FightId;
import java.util.Optional;

public interface FightWithBoxersFetcher {

  Optional<FightWithBoxers> get(FightId id);
}