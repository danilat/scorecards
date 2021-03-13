package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAllFightsTest extends UseCaseUnitTest<Collection<FightWithBoxers>> {

  @Mock
  private PrimaryPort<Collection<FightWithBoxers>> primaryPort;
  @Mock
  private FightWithBoxersFetcher fightWithBoxersFetcher;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  private RetrieveAllFights retrieveAllFights;

  @Test
  public void givenAnExistingFightThenIsRetrieved() {
    List<FightWithBoxers> existingFightsWithBoxers = Arrays
        .asList(FightWithBoxersMother.aFightWithBoxersWithId(new FightId("irrelevant")));
    when(fightWithBoxersFetcher.all()).thenReturn(existingFightsWithBoxers);
    retrieveAllFights = new RetrieveAllFights(fightWithBoxersFetcher);

    retrieveAllFights.execute(primaryPort, new Empty());

    assertEquals(existingFightsWithBoxers, getSuccessEntity());
  }
}
