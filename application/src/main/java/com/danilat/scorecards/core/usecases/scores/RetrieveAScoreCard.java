package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardNotFoundError;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard.RetrieveAScoreCardParameters;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Optional;

public class RetrieveAScoreCard implements UseCase<ScoreCard, RetrieveAScoreCardParameters> {

  private final ScoreCardRepository scoreCardRepository;

  public RetrieveAScoreCard(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  public void execute(PrimaryPort<ScoreCard> primaryPort, RetrieveAScoreCardParameters parameters) {
    Optional<ScoreCard> optionalScoreCard = this.scoreCardRepository
        .findByFightIdAndAccountId(parameters.getFightId(), parameters.getAccountId());
    if (optionalScoreCard.isPresent()) {
      primaryPort.success(optionalScoreCard.get());
    } else {
      Errors errors = Errors.newWithError(new ScoreCardNotFoundError(parameters.getFightId(), parameters.getAccountId()));
      primaryPort.error(errors);
    }
  }

  public static class RetrieveAScoreCardParameters {

    private final FightId fightId;
    private final AccountId accountId;

    public RetrieveAScoreCardParameters(FightId fightId, AccountId accountId) {
      this.fightId = fightId;
      this.accountId = accountId;
    }

    public FightId getFightId() {
      return fightId;
    }

    public AccountId getAccountId() {
      return accountId;
    }
  }
}
