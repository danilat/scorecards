package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
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

    Map<ScoreCardId, ScoreCard> scoreCards = scoreCardRepository.all();

    assertEquals(1, scoreCards.size());
  }

  @Test
  public void findAllScoreCardsByAccount() {
    AccountId firstAccountId = new AccountId("firstAccountId");
    ScoreCard firstAccountScoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), firstAccountId);
    scoreCardRepository.save(firstAccountScoreCard);
    AccountId secondAccountId = new AccountId("secondAccountId");
    scoreCardRepository.save(ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), secondAccountId));

    Map<ScoreCardId, ScoreCard> scoreCards = scoreCardRepository.findAllByAccountId(firstAccountId);

    assertEquals(1, scoreCards.size());
    assertTrue(scoreCards.containsValue(firstAccountScoreCard));
  }
}
