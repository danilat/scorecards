package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.services.ScoringInFightValidator;
import com.danilat.scorecards.core.services.ScoringInFightValidator.Scoring;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.core.usecases.ConstraintValidatorToErrorMapper;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ScoreRound implements UseCase<ScoreCard, ScoreFightParameters> {

  private final ScoreCardRepository scoreCardRepository;
  private final FightRepository fightRepository;
  private final UniqueIdGenerator uniqueIdGenerator;
  private final Auth auth;
  private final EventBus eventBus;
  private final Clock clock;
  private FieldErrors errors;

  public ScoreRound(ScoreCardRepository scoreCardRepository, FightRepository fightRepository,
      UniqueIdGenerator uniqueIdGenerator, Auth auth, EventBus eventBus, Clock clock) {
    this.scoreCardRepository = scoreCardRepository;
    this.fightRepository = fightRepository;
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.auth = auth;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<ScoreCard> primaryPort, ScoreFightParameters params) {
    if (!validate(params)) {
      primaryPort.error(errors);
      return;
    }

    ScoreCard scoreCard = getOrCreateScoreCard(params);
    scoreCard.scoreRound(params.getRound(), params.getFirstBoxerScore(), params.getSecondBoxerScore(), clock.now());
    this.scoreCardRepository.save(scoreCard);
    this.eventBus.publish(scoreCard.domainEvents());
    primaryPort.success(scoreCard);
  }

  private ScoreCard getOrCreateScoreCard(ScoreFightParameters params) {
    AccountId accountId = auth.currentAccount();
    return this.scoreCardRepository.findByFightIdAndAccountId(params.getFightId(), accountId)
        .orElseGet(() -> {
          ScoreCardId id = new ScoreCardId(uniqueIdGenerator.next());
          return ScoreCard
              .create(id, accountId, params.getFightId(), params.getFirstBoxerId(), params.getSecondBoxerId());
        });
  }

  private boolean validate(ScoreFightParameters params) {
    errors = new FieldErrors();
    errors.addAll(params.validate());
    ScoringInFightValidator scoringInFightValidator = new ScoringInFightValidator(fightRepository);
    errors.addAll(scoringInFightValidator.execute(params.scoring()));
    return errors.isEmpty();
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

    public FieldErrors validate() {
      ConstraintValidatorToErrorMapper constraintValidatorToErrorMapper = new ConstraintValidatorToErrorMapper<ScoreFightParameters>();
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<ScoreFightParameters>> violations = validator.validate(this);
      return constraintValidatorToErrorMapper.mapConstraintViolationsToErrors(violations);
    }

    public Scoring scoring() {
      return new Scoring(getFightId(), getFirstBoxerId(), getSecondBoxerId(), getRound());
    }
  }
}
