package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveAFightTest {

  @Mock
  private FightWithBoxersRepository fightWithBoxersRepository;

  private static final FightId AN_ID = new FightId("irrelevant id");
  private FightWithBoxers existingFightWithBoxers;

  private RetrieveAFight retrieveAFight;

  @Mock
  private PrimaryPort<FightWithBoxers> primaryPort;

  @Captor
  ArgumentCaptor<FightWithBoxers> fightArgumentCaptor;

  private FightWithBoxers getFight() {
    verify(primaryPort).success(fightArgumentCaptor.capture());
    FightWithBoxers fight = fightArgumentCaptor.getValue();
    return fight;
  }

  @Captor
  ArgumentCaptor<Errors> errorsArgumentCaptor;

  private Errors getErrors() {
    verify(primaryPort).error(errorsArgumentCaptor.capture());
    return errorsArgumentCaptor.getValue();
  }

  @Before
  public void setup() {
    existingFightWithBoxers = FightWithBoxersMother.aFightWithBoxersWithId(AN_ID);
    when(fightWithBoxersRepository.get(AN_ID)).thenReturn(Optional.of(
        existingFightWithBoxers));

    retrieveAFight = new RetrieveAFight(fightWithBoxersRepository);
  }

  @Test
  public void givenAnExistingFightThenIsRetrieved() {
    retrieveAFight.execute(primaryPort, AN_ID);

    assertEquals(existingFightWithBoxers, getFight());
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void givenAnNonExistingFightThenIsNotRetrieved() {
    retrieveAFight.execute(primaryPort, new FightId("un-existing id"));

    assertTrue(getErrors().hasMessage(new FightId("un-existing id") + " not found"));
  }
}
