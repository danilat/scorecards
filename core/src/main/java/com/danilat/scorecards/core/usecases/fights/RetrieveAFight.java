package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.FightRepository;
import com.danilat.scorecards.core.usecases.UseCase;

public class RetrieveAFight implements UseCase<String>{

  private final FightRepository fightRepository;

  public RetrieveAFight(FightRepository fightRepository) {
    this.fightRepository = fightRepository;
  }

  @Override
  public Fight execute(String id) {
    return fightRepository.get(id);
  }
}
