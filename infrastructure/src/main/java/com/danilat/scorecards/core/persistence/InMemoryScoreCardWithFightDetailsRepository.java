package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsRepository;
import com.danilat.scorecards.shared.domain.Sort;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryScoreCardWithFightDetailsRepository implements ScoreCardWithFightDetailsRepository {

  private final ScoreCardRepository scoreCardRepository;
  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;

  public InMemoryScoreCardWithFightDetailsRepository(ScoreCardRepository scoreCardRepository,
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
