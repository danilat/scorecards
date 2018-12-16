package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFightRepository implements FightRepository {

  private final Map<String, Fight> fights;

  public InMemoryFightRepository() {
    this.fights = new HashMap<>();
  }

  @Override
  public void save(Fight fight) {
    fights.put(fight.id(), fight);
  }

  @Override
  public String nextId() {
    return "foo";
  }

  @Override
  public Optional<Fight> get(String id) {
    return Optional.ofNullable(fights.get(id));
  }
}
