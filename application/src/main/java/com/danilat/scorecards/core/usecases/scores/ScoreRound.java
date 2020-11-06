package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundError;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.BoxerIsNotInFightError;
import com.danilat.scorecards.core.domain.score.RoundOutOfIntervalError;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.events.RoundScored;
import com.danilat.scorecards.core.usecases.Auth;
import com.danilat.scorecards.core.usecases.ConstraintValidatorToErrorMapper;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;

import com.danilat.scorecards.shared.UseCase;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.events.DomainEventId;
import com.danilat.scorecards.shared.events.EventBus;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ScoreRound implements UseCase<ScoreFightParameters, ScoreCard> {

  private final ScoreCardRepository scoreCardRepository;
  private final FightRepository fightRepository;
  private final UniqueIdGenerator uniqueIdGenerator;
  private final Auth auth;
  private final EventBus eventBus;
  private final Clock clock;
  private Errors errors;
  private ConstraintValidatorToErrorMapper constraintValidatorToErrorMapper;

  public ScoreRound(ScoreCardRepository scoreCardRepository, FightRepository fightRepository,
      UniqueIdGenerator uniqueIdGenerator, Auth auth, EventBus eventBus, Clock clock) {
    this.scoreCardRepository = scoreCardRepository;
    this.fightRepository = fightRepository;
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.auth = auth;
    this.eventBus = eventBus;
    this.clock = clock;
    constraintValidatorToErrorMapper = new ConstraintValidatorToErrorMapper<ScoreFightParameters>();
  }

  @Override
  public void execute(ScoreFightParameters params, PrimaryPort<ScoreCard> primaryPort) {
    if (!validate(params)) {
      primaryPort.error(errors);
      return;
    }
    AccountId accountId = auth.currentAccount();
    ScoreCard scoreCard = this.scoreCardRepository.findByFightIdAndAccountId(params.getFightId(), accountId)
        .orElseGet(() -> {
          ScoreCardId id = new ScoreCardId(uniqueIdGenerator.next());
          return ScoreCard
              .create(id, accountId, params.getFightId(), params.getFirstBoxerId(), params.getSecondBoxerId());
        });
    scoreCard.scoreRound(params.getRound(), params.getFirstBoxerScore(), params.getSecondBoxerScore());
    this.scoreCardRepository.save(scoreCard);
    RoundScored roundScored = new RoundScored(scoreCard, clock.now(), new DomainEventId(uniqueIdGenerator.next()));
    this.eventBus.publish(roundScored);
    primaryPort.success(scoreCard);
  }

  private boolean validate(ScoreFightParameters params) {
    errors = new Errors();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<ScoreFightParameters>> violations = validator.validate(params);
    errors.addAll(constraintValidatorToErrorMapper.mapConstraintViolationsToErrors(violations));
    Optional<Fight> optionalFight = fightRepository.get(params.getFightId());
    if (!optionalFight.isPresent()) {
      Error error = new FightNotFoundError(params.getFightId());
      errors.add(error);
      return false;
    }

    Fight fight = optionalFight.get();
    if (params.getRound() > fight.numberOfRounds() || params.getRound() < 1) {
      Error error = new RoundOutOfIntervalError(params.getRound(), fight.numberOfRounds());
      errors.add(error);
    }
    if (!fight.firstBoxer().equals(params.getFirstBoxerId())) {
      Error error = new BoxerIsNotInFightError("firstBoxer", params.getFirstBoxerId(), params.getFightId());
      errors.add(error);
    }
    if (!fight.secondBoxer().equals(params.getSecondBoxerId())) {
      Error error = new BoxerIsNotInFightError("secondBoxer", params.getSecondBoxerId(), params.getFightId());
      errors.add(error);
    }
    return errors.size() == 0;
  }

  public static class ScoreFightParameters {

    private final FightId fightId;
    private final Integer round;
    private final BoxerId firstBoxerId;
    @NotNull(message = "firstBoxerScore is mandatory")
    @Min(value = 1, message = "scores interval is between 1 and 10")
    @Max(value = 10, message = "scores interval is between 1 and 10")
    private final Integer firstBoxerScore;
    private final BoxerId secondBoxerId;
    @NotNull(message = "secondBoxerScore is mandatory")
    @Min(value = 1, message = "scores interval is between 1 and 10")
    @Max(value = 10, message = "scores interval is between 1 and 10")
    private final Integer secondBoxerScore;

    public ScoreFightParameters(FightId fightId, Integer round, BoxerId firstBoxerId, Integer firstBoxerScore,
        BoxerId secondBoxerId, Integer secondBoxerScore) {
      this.fightId = fightId;
      this.round = round;
      this.firstBoxerId = firstBoxerId;
      this.firstBoxerScore = firstBoxerScore;
      this.secondBoxerId = secondBoxerId;
      this.secondBoxerScore = secondBoxerScore;
    }

    public FightId getFightId() {
      return fightId;
    }

    public int getRound() {
      return round;
    }

    public BoxerId getFirstBoxerId() {
      return firstBoxerId;
    }

    public int getFirstBoxerScore() {
      return firstBoxerScore;
    }

    public BoxerId getSecondBoxerId() {
      return secondBoxerId;
    }

    public int getSecondBoxerScore() {
      return secondBoxerScore;
    }

  }
}
