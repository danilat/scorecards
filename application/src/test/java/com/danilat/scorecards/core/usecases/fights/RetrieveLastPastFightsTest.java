package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Sort;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveLastPastFightsTest extends UseCaseUnitTest<Collection<FightWithBoxers>> {

  @Mock
  private FightWithBoxersFetcher fightWithBoxersFetcher;
  @Mock
  private Clock clock;

  @Mock
  private PrimaryPort<Collection<FightWithBoxers>> primaryPort;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  private Collection<FightWithBoxers> existingFightsWithBoxers;
  private RetrieveLastPastFights retrieveLastPastFights;
  private LocalDate today = LocalDate.now();
  private int limit = 50;
  private Sort sort = Sort.desc("happenedAt");

  @Before
  public void setup() {
    existingFightsWithBoxers = Arrays.asList(FightWithBoxersMother.aFightWithBoxersWithId(new FightId("irrelevant")));
    when(fightWithBoxersFetcher.findAllBefore(today, sort, limit)).thenReturn(existingFightsWithBoxers);
    when(clock.today()).thenReturn(today);

    retrieveLastPastFights = new RetrieveLastPastFights(fightWithBoxersFetcher, clock);
  }

  @Test
  public void givenAnExistingFightThenIsRetrieved() {
    retrieveLastPastFights.execute(primaryPort, new Empty());

    assertEquals(existingFightsWithBoxers, getSuccessEntity());
  }
}
