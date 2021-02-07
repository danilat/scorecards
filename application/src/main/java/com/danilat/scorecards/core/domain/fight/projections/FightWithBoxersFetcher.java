package com.danilat.scorecards.core.domain.fight.projections;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Order;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface FightWithBoxersFetcher {

  Optional<FightWithBoxers> get(FightId id);

  Collection<FightWithBoxers> findAllBefore(LocalDate today, Order order,
      int limit);
}
