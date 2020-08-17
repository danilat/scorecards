package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFightRepository extends InMemoryRepository<Fight, FightId> implements FightRepository {

  @Override
  public FightId nextId() {
    String unique = new UniqueIdentifierGenerator().next();
    return new FightId(unique);
  }
}
