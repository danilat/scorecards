package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Map;

public class RetrieveScoreCards implements UseCase<Map<ScoreCardId, ScoreCard>, AccountId> {

  private final ScoreCardRepository scoreCardRepository;

  public RetrieveScoreCards(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public void execute(PrimaryPort<Map<ScoreCardId, ScoreCard>> primaryPort, AccountId accountId) {
    primaryPort.success(scoreCardRepository.findAllByAccountId(accountId));
  }
}
