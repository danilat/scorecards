package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.FightRepository;
import java.time.LocalDate;
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

  private static final String AN_ID = "irrelevant id";
  private Fight existingFight;

  @Before
  public void setup() {
    existingFight = new Fight(AN_ID, "ali", "foreman", LocalDate.now());
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
    retrieveAFight.execute("unexisting id");
  }
}
