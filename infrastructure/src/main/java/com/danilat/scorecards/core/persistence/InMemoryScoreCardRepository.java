package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Predicate;

@Repository
public class InMemoryScoreCardRepository extends InMemoryRepository<ScoreCard, ScoreCardId> implements
    ScoreCardRepository {

  @Override
  public Optional<ScoreCard> findByFightIdAndAccountId(FightId fightId, AccountId accountId) {
    Predicate<ScoreCard> predicate = (scoreCard) -> scoreCard.accountId().equals(accountId) && scoreCard.fightId()
        .equals(fightId);
    return entities.values().stream().filter(predicate).findFirst();
  }

  @Override
  public Map<ScoreCardId, ScoreCard> findAllByAccountId(AccountId accountId) {
    return entities.entrySet().stream()
        .filter(entry -> entry.getValue().accountId().equals(accountId))
        .collect(Collectors.toMap(entry -> entry.getValue().id(), entry -> entry.getValue()));
  }
}
