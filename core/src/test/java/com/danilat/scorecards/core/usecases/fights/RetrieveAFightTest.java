package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.FightMother;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveAFightTest {

  @Mock
  private FightRepository fightRepository;
  private RetrieveAFight retrieveAFight;

  private static final FightId AN_ID = new FightId("irrelevant id");
  private Fight existingFight;

  @Before
  public void setup() {
    existingFight = FightMother.aFightWithId(AN_ID);
    when(fightRepository.get(AN_ID)).thenReturn(Optional.of(existingFight));

    retrieveAFight = new RetrieveAFight(fightRepository);
  }

  @Test
  public void retrieveAnExistingFight() {
    Fight fight = retrieveAFight.execute(AN_ID);

    assertEquals(existingFight, fight);
  }

  @Test(expected = FightNotFoundException.class)
  public void retrieveAnUnexistingFight() {
    retrieveAFight.execute(new FightId("unexisting id"));
  }
}
