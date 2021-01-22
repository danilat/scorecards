package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.domain.Sort.Direction;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

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
  public Collection<ScoreCard> findAllByAccountId(AccountId accountId, Sort sort) {
    return entities.values().stream()
        .filter(entry -> entry.accountId().equals(accountId))
        .sorted(sortCriteria(sort))
        .collect(Collectors.toList());
  }

  private Comparator<ScoreCard> sortCriteria(Sort sort) {
    Comparator<ScoreCard> comparator = Comparator.comparing(scoreCard -> scoreCard.id().value());
    if ("scoredAt" == sort.field()) {
      comparator = Comparator.comparing(scoreCard -> scoreCard.scoredAt());
    }
    if (sort.direction().equals(Direction.DESC)) {
      comparator = comparator.reversed();
    }
    return comparator;
  }

}
