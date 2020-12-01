package com.danilat.scorecards.core.usecases.scores;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardNotFoundError;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard.RetrieveAScoreCardParameters;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAScoreCardTest extends UseCaseUnitTest<ScoreCard> {

  private static final ScoreCardId AN_ID = new ScoreCardId("irrelevant");
  private static final AccountId AN_ACCOUNT_ID = new AccountId("irrelevant");
  private static final FightId A_FIGHT_ID = new FightId("irrelevant");

  @Mock
  private PrimaryPort<ScoreCard> primaryPort;
  @Mock
  private ScoreCardRepository scoreCardRepository;

  private RetrieveAScoreCard retrieveAScoreCard;
  private RetrieveAScoreCardParameters parameters;
  private ScoreCard existingScoreCard = ScoreCardMother.aScoreCardWithFightIdAndAccountId(A_FIGHT_ID, AN_ACCOUNT_ID);

  @Before
  public void setup() {
    parameters = new RetrieveAScoreCardParameters(A_FIGHT_ID, AN_ACCOUNT_ID);
    when(scoreCardRepository.findByFightIdAndAccountId(A_FIGHT_ID, AN_ACCOUNT_ID)).thenReturn(Optional.of(existingScoreCard));
    retrieveAScoreCard = new RetrieveAScoreCard(scoreCardRepository);
  }

  @Test
  public void givenAnExistingScoreCardThenIsRetrieved() {
    retrieveAScoreCard.execute(primaryPort, parameters);

    assertEquals(existingScoreCard, getSuccessEntity());
  }

  @Test
  public void givenAnNonExistingFightThenIsNotRetrieved() {
    parameters = new RetrieveAScoreCardParameters(A_FIGHT_ID, null);
    retrieveAScoreCard.execute(primaryPort, parameters);

    assertNotNull(getErrors().hasError(ScoreCardNotFoundError.class));
  }

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }
}
