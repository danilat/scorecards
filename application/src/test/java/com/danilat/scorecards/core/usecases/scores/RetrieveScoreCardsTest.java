package com.danilat.scorecards.core.usecases.scores;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Sort.Direction;
import java.util.ArrayList;
import java.util.Collection;
import com.danilat.scorecards.shared.domain.Sort;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveScoreCardsTest extends UseCaseUnitTest<Collection<ScoreCard>> {

  @Mock
  private PrimaryPort<Collection<ScoreCard>> primaryPort;
  @Mock
  private ScoreCardRepository scoreCardRepository;
  private AccountId anAccount = new AccountId("some name");
  private RetrieveScoreCards retrieveScoreCards;
  Collection<ScoreCard> existingScorecards;
  private Sort sortByStoredAt = new Sort("scoredAt", Direction.DESC);

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  @Before
  public void setUp() {
    existingScorecards = new ArrayList<>();
    when(scoreCardRepository.findAllByAccountId(eq(anAccount), eq(sortByStoredAt))).thenReturn(existingScorecards);
    retrieveScoreCards = new RetrieveScoreCards(scoreCardRepository);
  }

  @Test
  public void givenNoneScoreCardByAnAccountThenIsEmpty() {
    retrieveScoreCards.execute(primaryPort, anAccount);

    Collection<ScoreCard> scoreCards = getSuccessEntity();
    assertEquals(0, scoreCards.size());
  }

  @Test
  public void givenScoreCardsByAnAccountThenArePresent() {
    ScoreCard scoreCard = ScoreCardMother.aScoreCardWithIdAndAccount("an scoreCardId",
        anAccount.value());
    existingScorecards.add(scoreCard);

    retrieveScoreCards.execute(primaryPort, anAccount);

    Collection<ScoreCard> scoreCards = getSuccessEntity();
    assertEquals(1, scoreCards.size());
  }
}
