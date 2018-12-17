package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    String unique = UUID.randomUUID().toString();
    return new FightId(unique);
  }

  @Override
  public Optional<Fight> get(FightId id) {
    return Optional.ofNullable(fights.get(id));
  }
}
