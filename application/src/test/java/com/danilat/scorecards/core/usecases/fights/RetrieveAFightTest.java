package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveAFightTest {
  @Mock
  private FightWithBoxersRepository fightWithBoxersRepository;

  private static final FightId AN_ID = new FightId("irrelevant id");
  private FightWithBoxers existingFightWithBoxers;

  private RetrieveAFight retrieveAFight;

  @Before
  public void setup() {
    existingFightWithBoxers = FightWithBoxersMother.aFightWithBoxersWithId(AN_ID);
    when(fightWithBoxersRepository.get(AN_ID)).thenReturn(Optional.of(
        existingFightWithBoxers));

    retrieveAFight = new RetrieveAFight(fightWithBoxersRepository);
  }

  @Test
  public void givenAnExistingFightThenIsRetrieved() {
    FightWithBoxers fight = retrieveAFight.execute(AN_ID);

    assertEquals(existingFightWithBoxers, fight);
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void givenAnNonExistingFightThenIsNotRetrieved() {
    expectedException.expect(FightNotFoundException.class);
    expectedException.expectMessage(new FightId("unexisting id") + " not found");

    retrieveAFight.execute(new FightId("unexisting id"));
  }
}
