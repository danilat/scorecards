package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.domain.Sort.Direction;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Collection;

public class RetrieveScoreCards implements UseCase<Collection<ScoreCardWithFightDetails>, AccountId> {

  private final ScoreCardWithFightDetailsRepository scoreCardRepository;

  public RetrieveScoreCards(ScoreCardWithFightDetailsRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public void execute(PrimaryPort<Collection<ScoreCardWithFightDetails>> primaryPort, AccountId accountId) {
    primaryPort.success(scoreCardRepository.findAllByAccountId(accountId, new Sort("scoredAt", Direction.DESC)));
  }
}
