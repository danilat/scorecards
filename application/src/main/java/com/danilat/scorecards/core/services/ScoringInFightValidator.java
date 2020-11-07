package com.danilat.scorecards.core.services;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundError;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.BoxerIsNotInFightError;
import com.danilat.scorecards.core.domain.score.RoundOutOfIntervalError;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Optional;

public class ScoringInFightValidator {

  private FightRepository fightRepository;

  public ScoringInFightValidator(FightRepository fightRepository) {
    this.fightRepository = fightRepository;
  }

  public Errors execute(Scoring scoring) {
    Errors errors = new Errors();
    Optional<Fight> optionalFight = fightRepository.get(scoring.getFightId());
    if (!optionalFight.isPresent()) {
      Error error = new FightNotFoundError(scoring.getFightId());
      errors.add(error);
      return errors;
    }
    Fight fight = optionalFight.get();
    if (!fight.isRoundInInterval(scoring.getRound())) {
      Error error = new RoundOutOfIntervalError(scoring.getRound(), fight.numberOfRounds());
      errors.add(error);
    }
    if (!fight.isTheFirstBoxer(scoring.getFirstBoxerId())) {
      Error error = new BoxerIsNotInFightError("firstBoxer", scoring.getFirstBoxerId(), scoring.getFightId());
      errors.add(error);
    }
    if (!fight.isTheSecondBoxer(scoring.getSecondBoxerId())) {
      Error error = new BoxerIsNotInFightError("secondBoxer", scoring.getSecondBoxerId(), scoring.getFightId());
      errors.add(error);
    }
    return errors;
  }

  public static class Scoring {

    private FightId fightId;
    private BoxerId firstBoxerId;
    private BoxerId secondBoxerId;
    private int round;

    public Scoring(FightId fightId, BoxerId firstBoxerId,
        BoxerId secondBoxerId, int round) {
      this.fightId = fightId;
      this.firstBoxerId = firstBoxerId;
      this.secondBoxerId = secondBoxerId;
      this.round = round;
    }

    public FightId getFightId() {
      return fightId;
    }


    public BoxerId getFirstBoxerId() {
      return firstBoxerId;
    }

    public BoxerId getSecondBoxerId() {
      return secondBoxerId;
    }

    public int getRound() {
      return round;
    }
  }
}
