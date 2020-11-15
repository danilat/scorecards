package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Collection;

public class RetrieveScoreCards implements UseCase<Collection<ScoreCard>, AccountId> {

  private final ScoreCardRepository scoreCardRepository;

  public RetrieveScoreCards(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public void execute(PrimaryPort<Collection<ScoreCard>> primaryPort, AccountId accountId) {
    primaryPort.success(scoreCardRepository.findAllByAccountId(accountId));
  }
}
