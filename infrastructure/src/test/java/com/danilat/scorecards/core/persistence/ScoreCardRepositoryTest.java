package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.shared.domain.Sort.Direction;
import java.util.Collection;
import com.danilat.scorecards.shared.domain.Sort;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScoreCardRepositoryTest {

  private ScoreCardRepository scoreCardRepository;
  private final ScoreCardId scoreCardId = new ScoreCardId("an id");

  @Before
  public void setup() {
    scoreCardRepository = new InMemoryScoreCardRepository();
  }

  @Test
  public void saveAScoreCard() {
    ScoreCard aScoreCard = ScoreCardMother.aScoreCardWithId(scoreCardId);

    scoreCardRepository.save(aScoreCard);

    Optional<ScoreCard> retrieved = scoreCardRepository.get(scoreCardId);
    assertTrue(retrieved.isPresent());
  }

  @Test
  public void getAllScoreCards() {
    ScoreCard aScoreCard = ScoreCardMother.aScoreCardWithId(scoreCardId);
    scoreCardRepository.save(aScoreCard);

    Collection<ScoreCard> scoreCards = scoreCardRepository.all();

    assertEquals(1, scoreCards.size());
  }

  @Test
  public void findAllScoreCardsByAccountFilter() {
    AccountId firstAccountId = new AccountId("firstAccountId");
    ScoreCard firstAccountScoreCard = ScoreCardMother
        .aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), firstAccountId);
    scoreCardRepository.save(firstAccountScoreCard);
    AccountId secondAccountId = new AccountId("secondAccountId");
    scoreCardRepository.save(ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), secondAccountId));

    Collection<ScoreCard> scoreCards = scoreCardRepository.findAllByAccountId(firstAccountId, Sort.DEFAULT);

    assertEquals(1, scoreCards.size());
    assertTrue(scoreCards.contains(firstAccountScoreCard));
  }

  @Test
  public void findAllScoreCardsByAccountOrderByScoredAtAsc() {
    Instant anInstant = Instant.now();
    AccountId accountId = new AccountId("accountId");
    ScoreCard firstScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    firstScoreCard.scoreRound(1, 10, 10, anInstant);
    scoreCardRepository.save(firstScoreCard);
    ScoreCard secondScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    secondScoreCard.scoreRound(1, 10, 10, anInstant.plusSeconds(60));
    scoreCardRepository.save(secondScoreCard);

    Sort sort = new Sort("scoredAt", Sort.Direction.ASC);
    Collection<ScoreCard> scoreCards = scoreCardRepository.findAllByAccountId(accountId, sort);

    assertEquals(2, scoreCards.size());
    List<ScoreCard> values = new ArrayList(scoreCards);
    assertEquals(firstScoreCard.id(), values.get(0).id());
    assertEquals(secondScoreCard.id(), values.get(1).id());
  }

  @Test
  public void findAllScoreCardsByAccountOrderByScoredAtDesc() {
    Instant anInstant = Instant.now();
    AccountId accountId = new AccountId("accountId");
    ScoreCard firstScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    firstScoreCard.scoreRound(1, 10, 10, anInstant);
    scoreCardRepository.save(firstScoreCard);
    ScoreCard secondScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), accountId);
    secondScoreCard.scoreRound(1, 10, 10, anInstant.plusSeconds(60));
    scoreCardRepository.save(secondScoreCard);

    Sort sort = new Sort("scoredAt", Direction.DESC);
    Collection<ScoreCard> scoreCards = scoreCardRepository.findAllByAccountId(accountId, sort);

    assertEquals(2, scoreCards.size());
    List<ScoreCard> values = new ArrayList(scoreCards);
    assertEquals(firstScoreCard.id(), values.get(1).id());
    assertEquals(secondScoreCard.id(), values.get(0).id());
  }
}
