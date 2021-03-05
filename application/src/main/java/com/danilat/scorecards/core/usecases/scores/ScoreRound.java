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
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ScoreRound extends WriteUseCase<ScoreCard, ScoreFightParameters> {

  private final ScoreCardRepository scoreCardRepository;
  private final FightRepository fightRepository;
  private final UniqueIdGenerator uniqueIdGenerator;
  private final Auth auth;
  private final Clock clock;

  public ScoreRound(ScoreCardRepository scoreCardRepository, FightRepository fightRepository,
      UniqueIdGenerator uniqueIdGenerator, Auth auth, EventBus eventBus, Clock clock) {
    super(eventBus);
    this.scoreCardRepository = scoreCardRepository;
    this.fightRepository = fightRepository;
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.auth = auth;
    this.clock = clock;
  }

  @Override
  public ScoreCard executeWhenValid(ScoreFightParameters params) {
    ScoreCard scoreCard = getOrCreateScoreCard(params);
    scoreCard.scoreRound(params.getRound(), params.getFirstBoxerScore(), params.getSecondBoxerScore(), clock.now());
    this.scoreCardRepository.save(scoreCard);
    return scoreCard;
  }

  private ScoreCard getOrCreateScoreCard(ScoreFightParameters params) {
    AccountId accountId = auth.currentAccountId(params.getAccessToken());
    return this.scoreCardRepository.findByFightIdAndAccountId(params.getFightId(), accountId)
        .orElseGet(() -> {
          ScoreCardId id = new ScoreCardId(uniqueIdGenerator.next());
          return ScoreCard
              .create(id, accountId, params.getFightId(), params.getFirstBoxerId(), params.getSecondBoxerId());
        });
  }

  protected FieldErrors validate(ScoreFightParameters params) {
    FieldErrors errors = new FieldErrors();
    errors.addAll(params.validate());
    ScoringInFightValidator scoringInFightValidator = new ScoringInFightValidator(fightRepository);
    errors.addAll(scoringInFightValidator.execute(params.scoring()));
    return errors;
  }

  public static class ScoreFightParameters extends ValidatableParameters {

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
    private String accessToken;

    public ScoreFightParameters(FightId fightId, Integer round, BoxerId firstBoxerId, Integer firstBoxerScore,
        BoxerId secondBoxerId, Integer secondBoxerScore, String accessToken) {
      this.fightId = fightId;
      this.round = round;
      this.firstBoxerId = firstBoxerId;
      this.firstBoxerScore = firstBoxerScore;
      this.secondBoxerId = secondBoxerId;
      this.secondBoxerScore = secondBoxerScore;
      this.accessToken = accessToken;
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

    public Scoring scoring() {
      return new Scoring(getFightId(), getFirstBoxerId(), getSecondBoxerId(), getRound());
    }

    public String getAccessToken() {
      return accessToken;
    }
  }
}
