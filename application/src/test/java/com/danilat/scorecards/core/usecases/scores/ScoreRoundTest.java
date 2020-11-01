package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.*;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.usecases.Auth;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScoreRoundTest {
    private ScoreRound scoreRound;
    private static final FightId A_FIGHT_ID = new FightId("1");
    private static final Fight A_FIGHT = new FightMother().aFightWithId(A_FIGHT_ID);
    private static final BoxerId FIRST_BOXER = A_FIGHT.firstBoxer();
    private static final BoxerId SECOND_BOXER = A_FIGHT.secondBoxer();
    private static final ScoreCardId AN_ID = new ScoreCardId("an scorecard id");
    private static final AccountId AN_AFICIONADO = new AccountId("an account id");

    @Spy
    private ScoreCardRepository scoreCardRepository;
    @Mock
    private UniqueIdGenerator uniqueIdGenerator;
    @Mock
    private Auth auth;
    @Mock
    private FightRepository fightRepository;

    @Before
    public void setup() {
        when(uniqueIdGenerator.next()).thenReturn(AN_ID.value());
        when(auth.currentAccount()).thenReturn(AN_AFICIONADO);
        when(fightRepository.get(A_FIGHT.id())).thenReturn(Optional.of(A_FIGHT));
        scoreRound = new ScoreRound(scoreCardRepository, fightRepository, uniqueIdGenerator, auth);
    }

    @Test
    public void givenAFightScoresARoundThenScoreIsRegistered() {
        Integer round = 1;
        Integer aliScore = 10;
        Integer foremanScore = 9;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER, foremanScore);

        ScoreCard scoreCard = scoreRound.execute(params);

        assertEquals(AN_ID, scoreCard.id());
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
    public void givenAFightScoresARoundsThenPersistTheScoreCard() {
        int round = 1;
        int aliScore = 10;
        int foremanScore = 9;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER, foremanScore);

        ScoreCard scoreCard = scoreRound.execute(params);

        verify(scoreCardRepository, times(1)).save(scoreCard);
    }

    private void givenExistingScoreCardOnFirstRound(Integer previousAliScore, Integer previousForemanScore) {
        ScoreCard existingScorecard = ScoreCardMother.aScoreCardWithIdFightIdFirstAndSecondBoxer(AN_ID, A_FIGHT_ID, FIRST_BOXER, SECOND_BOXER);
        existingScorecard.scoreRound(1, previousAliScore, previousForemanScore);
        when(scoreCardRepository.findByFightIdAndAccountId(A_FIGHT_ID, AN_AFICIONADO)).thenReturn(Optional.of(existingScorecard));
    }

    @Test
    public void givenAFightScoresSomeRoundsThenScoreIsAccumulated() {
        Integer previousAliScore = 10;
        Integer previousForemanScore = 9;
        givenExistingScoreCardOnFirstRound(previousAliScore, previousForemanScore);
        Integer round = 2;
        Integer aliScore = 10;
        Integer foremanScore = 9;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, aliScore, SECOND_BOXER, foremanScore);

        ScoreCard scoreCard = scoreRound.execute(params);

        assertEquals(AN_ID, scoreCard.id());
        assertEquals(aliScore, scoreCard.firstBoxerScore(round));
        assertEquals(foremanScore, scoreCard.secondBoxerScore(round));
        assertEquals((previousAliScore + aliScore), scoreCard.firstBoxerScore().intValue());
        assertEquals((previousForemanScore + foremanScore), scoreCard.secondBoxerScore().intValue());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void givenAScoreForUnExistingFightThenFails() {
        expectedException.expect(FightNotFoundException.class);

        ScoreFightParameters params = new ScoreFightParameters(new FightId("not-exists"), 1, FIRST_BOXER, 10, SECOND_BOXER, 9);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreForARoundGreaterOfIntervalThenFails() {
        expectedException.expect(InvalidScoreException.class);
        expectedException.expectMessage("round interval is between 1 and 12");

        int round = 13;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 9);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreForARoundLesserOfIntervalThenFails() {
        expectedException.expect(InvalidScoreException.class);
        expectedException.expectMessage("round interval is between 1 and 12");

        int round = 0;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 9);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreForARoundGreaterOfIntervalOfTheRoundThenFails() {
        Fight fight = FightMother.aFightWithIdAndNumberOfRounds(A_FIGHT_ID, 10);
        when(fightRepository.get(fight.id())).thenReturn(Optional.of(fight));
        expectedException.expect(RoundOutOfIntervalException.class);

        int roundOutOfInterval = fight.numberOfRounds() + 1;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, roundOutOfInterval, FIRST_BOXER, 10, SECOND_BOXER, 9);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreForOnlyABoxerThenFails() {
        expectedException.expect(InvalidScoreException.class);
        expectedException.expectMessage("secondBoxerScore is mandatory");

        int round = 1;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, null);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreIsLessThanMinimumTheFails() {
        expectedException.expect(InvalidScoreException.class);
        expectedException.expectMessage("scores interval is between 1 and 10");

        int round = 1;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 10, SECOND_BOXER, 0);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoreIsMoreThanMaximumTheFails() {
        expectedException.expect(InvalidScoreException.class);
        expectedException.expectMessage("scores interval is between 1 and 10");

        int round = 1;
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, round, FIRST_BOXER, 11, SECOND_BOXER, 9);

        scoreRound.execute(params);
    }

    @Test
    public void givenAScoredBoxerNotPartOfTheFightTheFails() {
        expectedException.expect(BoxerIsNotInFightException.class);

        BoxerId tyson = new BoxerId("tyson");
        ScoreFightParameters params = new ScoreFightParameters(A_FIGHT_ID, 1, FIRST_BOXER, 10, tyson, 10);

        scoreRound.execute(params);
    }
}
