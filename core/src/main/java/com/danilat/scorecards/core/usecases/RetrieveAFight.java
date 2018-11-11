package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.FightRepository;

public class RetrieveAFight {

  private final FightRepository fightRepository;

  public RetrieveAFight(FightRepository fightRepository) {
    this.fightRepository = fightRepository;
  }

  public Fight execute(String id) {
    return fightRepository.get(id);
  }
}
