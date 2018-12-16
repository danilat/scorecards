package com.danilat.scorecards.core.domain.fight;

import java.util.Optional;

public interface FightRepository {

  void save(Fight fight);

  FightId nextId();

  Optional<Fight> get(FightId id);
}
