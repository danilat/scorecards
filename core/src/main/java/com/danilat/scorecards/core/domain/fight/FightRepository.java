package com.danilat.scorecards.core.domain.fight;

import java.util.Optional;

public interface FightRepository {

  void save(Fight fight);

  String nextId();

  Optional<Fight> get(String id);
}
