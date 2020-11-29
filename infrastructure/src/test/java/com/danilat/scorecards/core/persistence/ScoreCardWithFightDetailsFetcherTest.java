package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsFetcher;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.domain.Sort.Direction;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ScoreCardWithFightDetailsFetcherTest {

  private FightRepository fightRepository;
  private BoxerRepository boxerRepository;
  private ScoreCardRepository scoreCardRepository;
  private ScoreCardWithFightDetailsFetcher scoreCardWithFightDetailsFetcher;

  @Before
  public void setup() {
    fightRepository = new InMemoryFightRepository();
    boxerRepository = new InMemoryBoxerRepository();
    scoreCardRepository = new InMemoryScoreCardRepository();
    scoreCardWithFightDetailsFetcher = new InMemoryScoreCardWithFightDetailsFetcher(scoreCardRepository,
        fightRepository, boxerRepository);
  }

  @Test
  public void findAllScoreCardsByAccountFilter() {
    AccountId firstAccountId = new AccountId("firstAccountId");
    ScoreCard firstAccountScoreCard = ScoreCardMother
        .aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), firstAccountId);
    boxerRepository.save(BoxerMother.aBoxerWithId(firstAccountScoreCard.firstBoxerId()));
    boxerRepository.save(BoxerMother.aBoxerWithId(firstAccountScoreCard.secondBoxerId()));
    fightRepository.save(FightMother
        .aFightWithIdAndBoxers(firstAccountScoreCard.fightId(), firstAccountScoreCard.firstBoxerId(),
            firstAccountScoreCard.secondBoxerId()));
    scoreCardRepository.save(firstAccountScoreCard);
    AccountId secondAccountId = new AccountId("secondAccountId");
    scoreCardRepository.save(ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), secondAccountId));

    Collection<ScoreCardWithFightDetails> scoreCardsCollection = scoreCardWithFightDetailsFetcher
        .findAllByAccountId(firstAccountId, Sort.DEFAULT);

    assertEquals(1, scoreCardsCollection.size());
  }

  @Test
  public void findAllScoreCardsByAccountOrderByScoredAtAsc() {
    Instant anInstant = Instant.now();
    AccountId accountId = new AccountId("accountId");
    ScoreCard firstScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    firstScoreCard.scoreRound(1, 10, 10, anInstant);
    boxerRepository.save(BoxerMother.aBoxerWithId(firstScoreCard.firstBoxerId()));
    boxerRepository.save(BoxerMother.aBoxerWithId(firstScoreCard.secondBoxerId()));
    fightRepository.save(FightMother.aFightWithIdAndBoxers(firstScoreCard.fightId(), firstScoreCard.firstBoxerId(),
        firstScoreCard.secondBoxerId()));
    scoreCardRepository.save(firstScoreCard);
    ScoreCard secondScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    secondScoreCard.scoreRound(1, 10, 10, anInstant.plusSeconds(60));
    boxerRepository.save(BoxerMother.aBoxerWithId(secondScoreCard.firstBoxerId()));
    boxerRepository.save(BoxerMother.aBoxerWithId(secondScoreCard.secondBoxerId()));
    fightRepository.save(FightMother.aFightWithIdAndBoxers(secondScoreCard.fightId(), secondScoreCard.firstBoxerId(),
        secondScoreCard.secondBoxerId()));
    scoreCardRepository.save(secondScoreCard);

    Sort sort = new Sort("scoredAt", Direction.ASC);
    Collection<ScoreCardWithFightDetails> scoreCards = scoreCardWithFightDetailsFetcher
        .findAllByAccountId(accountId, sort);

    assertEquals(2, scoreCards.size());
    List<ScoreCardWithFightDetails> values = new ArrayList(scoreCards);
    assertEquals(firstScoreCard.id(), values.get(0).id());
    assertEquals(secondScoreCard.id(), values.get(1).id());
  }
}
