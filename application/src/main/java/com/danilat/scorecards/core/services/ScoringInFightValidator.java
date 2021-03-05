package com.danilat.scorecards.core.services;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundError;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.BoxerIsNotInFightError;
import com.danilat.scorecards.core.domain.score.RoundOutOfIntervalError;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import java.util.Optional;

public class ScoringInFightValidator {

  private FightRepository fightRepository;

  public ScoringInFightValidator(FightRepository fightRepository) {
    this.fightRepository = fightRepository;
  }

  public FieldErrors execute(Scoring scoring) {
    FieldErrors errors = new FieldErrors();
    Optional<Fight> optionalFight = fightRepository.get(scoring.getFightId());
    if (!optionalFight.isPresent()) {
      errors.add("fightId", new FightNotFoundError(scoring.getFightId()));
      return errors;
    }
    Fight fight = optionalFight.get();
    if (!fight.isRoundInInterval(scoring.getRound())) {
      errors.add("round", new RoundOutOfIntervalError(scoring.getRound(), fight.numberOfRounds()));
    }
    if (!fight.isTheFirstBoxer(scoring.getFirstBoxerId())) {
      errors.add("firstBoxer", new BoxerIsNotInFightError(scoring.getFirstBoxerId(), scoring.getFightId()));
    }
    if (!fight.isTheSecondBoxer(scoring.getSecondBoxerId())) {
      errors.add("secondBoxer", new BoxerIsNotInFightError(scoring.getSecondBoxerId(), scoring.getFightId()));
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
