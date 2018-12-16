package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFightRepository implements FightRepository {

  private final Map<FightId, Fight> fights;

  public InMemoryFightRepository() {
    this.fights = new HashMap<>();
  }

  @Override
  public void save(Fight fight) {
    fights.put(fight.id(), fight);
  }

  @Override
  public FightId nextId() {
    return new FightId("foo");
  }

  @Override
  public Optional<Fight> get(FightId id) {
    return Optional.ofNullable(fights.get(id));
  }
}
