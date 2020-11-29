package com.danilat.scorecards.core.usecases.scores;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAScoreCardTest extends UseCaseUnitTest<ScoreCard> {

  private static final ScoreCardId AN_ID = new ScoreCardId("irrelevant");
  @Mock
  private PrimaryPort<ScoreCard> primaryPort;
  @Mock
  private ScoreCardRepository scoreCardRepository;

  private RetrieveAScoreCard retrieveAScoreCard;
  private ScoreCard existingScoreCard = ScoreCardMother.aScoreCardWithId(AN_ID);

  @Before
  public void setup() {
    when(scoreCardRepository.get(AN_ID)).thenReturn(Optional.of(existingScoreCard));
    retrieveAScoreCard = new RetrieveAScoreCard(scoreCardRepository);
  }

  @Test
  public void givenAnExistingScoreCardThenIsRetrieved() {
    retrieveAScoreCard.execute(primaryPort, AN_ID);

    assertEquals(existingScoreCard, getSuccessEntity());
  }

  @Test
  public void givenAnNonExistingFightThenIsNotRetrieved() {
    retrieveAScoreCard.execute(primaryPort, new ScoreCardId("un-existing-id"));

    assertEquals("ScoreCard: " + new ScoreCardId("un-existing-id") + " not found",
        getErrors().getMessagesContentFor("scoreCardId"));
  }

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }
}
