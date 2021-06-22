package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import com.danilat.scorecards.core.persistence.jdbc.JdbcScoreCardRepository;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.domain.Sort.Direction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcConfig.class, JdbcScoreCardRepository.class, SimpleMeterRegistry.class})
@RunWith(SpringRunner.class)
public class ScoreCardRepositoryIT {

  @Autowired
  private ScoreCardRepository scoreCardRepository;
  private final ScoreCardId scoreCardId = new ScoreCardId("an id");

  @Before
  public void setup() {
    scoreCardRepository.clear();
  }

  @Test
  public void saveAScoreCard() {
    ScoreCard aScoreCard = ScoreCardMother.aScoreCardWithId(scoreCardId);

    scoreCardRepository.save(aScoreCard);

    Optional<ScoreCard> retrieved = scoreCardRepository.get(scoreCardId);
    assertTrue(retrieved.isPresent());
    assertEquals(retrieved.get(), aScoreCard);
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
    assertEquals(firstAccountScoreCard, scoreCards.toArray()[0]);
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
