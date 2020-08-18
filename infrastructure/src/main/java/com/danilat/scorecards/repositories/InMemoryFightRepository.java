package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.shared.UniqueIdGenerator;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFightRepository extends InMemoryRepository<Fight, FightId> implements FightRepository {

  @Override
  public FightId nextId() {
    String unique = new UniqueIdGenerator().next();
    return new FightId(unique);
  }
}
