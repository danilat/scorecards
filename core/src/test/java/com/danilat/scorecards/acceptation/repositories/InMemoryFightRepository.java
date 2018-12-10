package com.danilat.scorecards.acceptation.repositories;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.FightRepository;
import java.util.Optional;

public class InMemoryFightRepository implements FightRepository{

  @Override
  public void save(Fight fight) {
    
  }

  @Override
  public String nextId() {
    return null;
  }

  @Override
  public Optional<Fight> get(String id) {
    return null;
  }
}
