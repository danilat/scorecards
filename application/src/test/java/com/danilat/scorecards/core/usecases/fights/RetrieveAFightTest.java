package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAFightTest extends UseCaseUnitTest<FightWithBoxers> {

  @Mock
  private FightWithBoxersFetcher fightWithBoxersFetcher;

  private static final FightId AN_ID = new FightId("irrelevant id");
  private FightWithBoxers existingFightWithBoxers;

  private RetrieveAFight retrieveAFight;

  @Mock
  private PrimaryPort<FightWithBoxers> primaryPort;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  @Before
  public void setup() {
    existingFightWithBoxers = FightWithBoxersMother.aFightWithBoxersWithId(AN_ID);
    when(fightWithBoxersFetcher.get(AN_ID)).thenReturn(Optional.of(
        existingFightWithBoxers));

    retrieveAFight = new RetrieveAFight(fightWithBoxersFetcher);
  }

  @Test
  public void givenAnExistingFightThenIsRetrieved() {
    retrieveAFight.execute(primaryPort, AN_ID);

    assertEquals(existingFightWithBoxers, getSuccessEntity());
  }

  @Test
  public void givenAnNonExistingFightThenIsNotRetrieved() {
    retrieveAFight.execute(primaryPort, new FightId("un-existing id"));

    assertTrue(getErrors().hasMessage(new FightId("un-existing id") + " not found"));
  }
}
