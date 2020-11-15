package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Repository;
import com.danilat.scorecards.shared.domain.Sort;

import java.util.Collection;
import java.util.Optional;

public interface ScoreCardRepository extends Repository<ScoreCard, ScoreCardId> {

  Optional<ScoreCard> findByFightIdAndAccountId(FightId fightId, AccountId accountId);

  Collection<ScoreCard> findAllByAccountId(AccountId anAccount, Sort sort);
}
