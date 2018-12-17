package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.Repository;
import java.util.Optional;

public interface FightRepository extends Repository<Fight, FightId> {

  void save(Fight fight);

  FightId nextId();

  Optional<Fight> get(FightId id);
}
