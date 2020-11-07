package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.*;
import com.danilat.scorecards.core.domain.score.events.RoundScored;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.events.EventBus;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ScoreRoundTest extends UseCaseUnitTest<ScoreCard> {

  private ScoreRound scoreRound;
  private static final FightId A_FIGHT_ID = new FightId("1");
  private static final Fight A_FIGHT = new FightMother().aFightWithId(A_FIGHT_ID);
  private static final BoxerId FIRST_BOXER = A_FIGHT.firstBoxer();
  private static final BoxerId SECOND_BOXER = A_FIGHT.secondBoxer();
  private static final String AN_ID = "an id";
  private static final ScoreCardId AN_SCORECARD_ID = new ScoreCardId(AN_ID);
  private static final AccountId AN_AFICIONADO = new AccountId("an account id");

  @Spy
  private ScoreCardRepository scoreCardRepository;
  @Mock
  private UniqueIdGenerator uniqueIdGenerator;
  @Mock
  private Auth auth;
  @Mock
  private FightRepository fightRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;
  private Instant anHappenedAt;
  @Mock
  private PrimaryPort<ScoreCard> primaryPort;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  @Before
  public void setup() {
    anHappenedAt = Instant.now();
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    when(auth.currentAccount()).thenReturn(AN_AFICIONADO);
    when(fightRepository.get(A_FIGHT.id())).thenReturn(Optional.of(A_FIGHT));
    when(clock.now()).thenReturn(anHappenedAt);
    scoreRound = new ScoreRound(scoreCardRepository, fightRepository, uniqueIdGenerator, auth, eventBus, clock);
  }

  @Test
  public void givenAFightScoresARoundThenScoreIsRegistered() {
    Integer round = 1;
    Integer aliScore = 10;
    Integer foremanScore = 9;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER,
        foremanScore);

    scoreRound.execute(primaryPort, params);

    ScoreCard scoreCard = getSuccessEntity();
    assertEquals(AN_SCORECARD_ID, scoreCard.id());
    assertEquals(AN_AFICIONADO, scoreCard.accountId());
    assertEquals(A_FIGHT_ID, scoreCard.fightId());
    assertEquals(FIRST_BOXER, scoreCard.firstBoxerId());
    assertEquals(SECOND_BOXER, scoreCard.secondBoxerId());
    assertEquals(aliScore, scoreCard.firstBoxerScore());
    assertEquals(foremanScore, scoreCard.secondBoxerScore());
    assertEquals(aliScore, scoreCard.firstBoxerScore(round));
    assertEquals(foremanScore, scoreCard.secondBoxerScore(round));
  }

  @Test
  public void givenAFightScoresARoundThenPersistTheScoreCard() {
    int round = 1;
    int aliScore = 10;
    int foremanScore = 9;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER,
        foremanScore);

    scoreRound.execute(primaryPort, params);

    ScoreCard scoreCard = getSuccessEntity();
    verify(scoreCardRepository, times(1)).save(scoreCard);
  }

  private void givenExistingScoreCardOnFirstRound(Integer previousAliScore, Integer previousForemanScore) {
    ScoreCard existingScorecard = ScoreCardMother
        .aScoreCardWithIdFightIdFirstAndSecondBoxer(AN_SCORECARD_ID, A_FIGHT_ID, FIRST_BOXER, SECOND_BOXER);
    existingScorecard.scoreRound(1, previousAliScore, previousForemanScore, clock.now());
    when(scoreCardRepository.findByFightIdAndAccountId(A_FIGHT_ID, AN_AFICIONADO))
        .thenReturn(Optional.of(existingScorecard));
  }

  @Test
  public void givenAFightScoresSomeRoundsThenScoreIsAccumulated() {
    Integer previousAliScore = 10;
    Integer previousForemanScore = 9;
    givenExistingScoreCardOnFirstRound(previousAliScore, previousForemanScore);
    Integer round = 2;
    Integer aliScore = 10;
    Integer foremanScore = 9;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER,
        foremanScore);

    scoreRound.execute(primaryPort, params);

    ScoreCard scoreCard = getSuccessEntity();
    assertEquals(AN_SCORECARD_ID, scoreCard.id());
    assertEquals(aliScore, scoreCard.firstBoxerScore(round));
    assertEquals(foremanScore, scoreCard.secondBoxerScore(round));
    assertEquals((previousAliScore + aliScore), scoreCard.firstBoxerScore().intValue());
    assertEquals((previousForemanScore + foremanScore), scoreCard.secondBoxerScore().intValue());
  }

  @Captor
  ArgumentCaptor<RoundScored> roundScoredArgumentCaptor;

  @Test
  public void givenAFightScoresARoundThenAnEventIsPublished() {
    Integer round = 1;
    Integer aliScore = 10;
    Integer foremanScore = 9;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER,
        foremanScore);

    scoreRound.execute(primaryPort, params);

    ScoreCard scoreCard = getSuccessEntity();
    verify(eventBus, times(1)).publish(roundScoredArgumentCaptor.capture());
    RoundScored roundScored = roundScoredArgumentCaptor.getValue();
    assertEquals(scoreCard, roundScored.scoreCard());
    assertEquals(anHappenedAt, roundScored.happenedAt());
    assertEquals(AN_ID , roundScored.eventId().value());
  }

  @Test
  public void givenAScoreForUnExistingFightThenFails() {
    FightId nonExistingFightId = new FightId("not-exists");
    ScoreFightParameters params = new ScoreFightParameters(nonExistingFightId, 1, FIRST_BOXER, 10, SECOND_BOXER,
        9);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals("Fight: " + nonExistingFightId + " not found", errors.getMessagesContentFor("fightId"));
  }

  @Test
  public void givenAScoreForARoundGreaterOfIntervalThenFails() {
    int round = 13;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 9);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals(round + " is out of the interval between 1 and 12", errors.getMessagesContentFor("round"));
  }

  @Test
  public void givenAScoreForARoundLesserOfIntervalThenFails() {
    int round = 0;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 9);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals(round + " is out of the interval between 1 and 12", errors.getMessagesContentFor("round"));
  }

  @Test
  public void givenAScoreForARoundGreaterOfIntervalOfTheRoundThenFails() {
    Fight fight = FightMother.aFightWithIdAndNumberOfRounds(A_FIGHT_ID, 10);
    when(fightRepository.get(fight.id())).thenReturn(Optional.of(fight));
    int roundOutOfInterval = fight.numberOfRounds() + 1;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, roundOutOfInterval, FIRST_BOXER, 10,
        SECOND_BOXER, 9);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals(roundOutOfInterval + " is out of the interval between 1 and " + fight.numberOfRounds(),
        errors.getMessagesContentFor("round"));
  }

  @Test
  public void givenAScoreForOnlyABoxerThenFails() {
    int round = 1;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, null);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals("secondBoxerScore is mandatory", errors.getMessagesContentFor("secondBoxerScore"));
  }

  @Test
  public void givenAScoreIsLessThanMinimumTheFails() {
    int round = 1;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 0);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals("scores interval is between 1 and 10", errors.getMessagesContentFor("secondBoxerScore"));
  }

  @Test
  public void givenAScoreIsMoreThanMaximumTheFails() {
    int round = 1;
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 11, SECOND_BOXER, 9);

    scoreRound.execute(primaryPort, params);

    Errors errors = getErrors();
    assertEquals("scores interval is between 1 and 10", errors.getMessagesContentFor("firstBoxerScore"));
  }

  @Test
  public void givenFirstBoxerScoredIsNotPartOfTheFightTheFails() {
    BoxerId tyson = new BoxerId("tyson");
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, 1, tyson, 10, SECOND_BOXER, 10);

    scoreRound.execute(primaryPort, params);
    Errors errors = getErrors();
    assertEquals(tyson + " is not in the fight" + A_FIGHT_ID, errors.getMessagesContentFor("firstBoxer"));
  }

  @Test
  public void givenSecondBoxerScoredIsNotPartOfTheFightTheFails() {
    BoxerId tyson = new BoxerId("tyson");
    ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, 1, FIRST_BOXER, 10, tyson, 10);

    scoreRound.execute(primaryPort, params);
    Errors errors = getErrors();
    assertEquals(tyson + " is not in the fight" + A_FIGHT_ID, errors.getMessagesContentFor("secondBoxer"));
  }
}
