package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.UseCase;

public class RetrieveAFight implements UseCase<String> {

  private final FightRepository fightRepository;

  public RetrieveAFight(FightRepository fightRepository) {
    this.fightRepository = fightRepository;
  }

  @Override
  public Fight execute(String id) {
    return fightRepository.get(id).orElseThrow(() -> new FightNotFoundException());
  }
}
