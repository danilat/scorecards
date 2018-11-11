package com.danilat.scorecards.core.domain;

public interface FightRepository {

  void save(Fight fight);

  String nextId();
}
