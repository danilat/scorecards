package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.shared.UseCase;

public class RetrieveAFight implements UseCase<FightId> {

  private final FightWithBoxersRepository fightWithBoxersRepository;

  public RetrieveAFight(FightWithBoxersRepository fightWithBoxersRepository) {
    this.fightWithBoxersRepository = fightWithBoxersRepository;
  }

  @Override
  public FightWithBoxers execute(FightId id) {
    return fightWithBoxersRepository.get(id).orElseThrow(() -> new FightNotFoundException(id));
  }
}
