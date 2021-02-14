package com.danilat.scorecards.core.persistence.fetchers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsFetcher;
import com.danilat.scorecards.shared.domain.Sort;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryScoreCardWithFightDetailsFetcher implements ScoreCardWithFightDetailsFetcher {

  private final ScoreCardRepository scoreCardRepository;
  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;

  @Autowired
  public InMemoryScoreCardWithFightDetailsFetcher(ScoreCardRepository scoreCardRepository,
      FightRepository fightRepository, BoxerRepository boxerRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
  }

  @Override
  public Collection<ScoreCardWithFightDetails> findAllByAccountId(AccountId accountId, Sort sort) {
    return scoreCardRepository.findAllByAccountId(accountId, sort)
        .stream().map(scoreCard -> {
          Optional<Fight> fight = fightRepository.get(scoreCard.fightId());
          Optional<Boxer> firstBoxer = boxerRepository.get(scoreCard.firstBoxerId());
          Optional<Boxer> secondBoxer = boxerRepository.get(scoreCard.secondBoxerId());
          ScoreCardWithFightDetails scoreCardWithFightDetails = new ScoreCardWithFightDetails(scoreCard, fight.get(),
              firstBoxer.get(), secondBoxer.get());
          return scoreCardWithFightDetails;
        }).collect(Collectors.toList());
  }
}
