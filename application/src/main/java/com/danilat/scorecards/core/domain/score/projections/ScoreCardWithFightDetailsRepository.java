package com.danilat.scorecards.core.domain.score.projections;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.shared.domain.Sort;
import java.util.Collection;

public interface ScoreCardWithFightDetailsRepository {
  Collection<ScoreCardWithFightDetails> findAllByAccountId(AccountId anAccount, Sort sort);
}
