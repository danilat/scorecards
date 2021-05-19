package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.persistence.fetchers.JdbcFightWithBoxersFetcher;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import com.danilat.scorecards.shared.domain.Sort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcConfig.class, JdbcFightWithBoxersFetcher.class})
@RunWith(SpringRunner.class)
public class FightWithBoxersFetcherIT {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private FightWithBoxersFetcher fightWithBoxersFetcher;
  private FightId fightId = new FightId("some irrelevant id");

  @Before
  public void setup() {
    boxerRepository.clear();
    fightRepository.clear();
  }

  @Test
  public void getAFight() {
    Fight fight = FightMother.aFightWithId(fightId);
    fightRepository.save(fight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId("ali");
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId("foreman");
    boxerRepository.save(secondBoxer);

    Optional<FightWithBoxers> retrieved = fightWithBoxersFetcher.get(fightId);
    assertTrue(retrieved.isPresent());
    FightWithBoxers fightWithBoxers = retrieved.get();
    assertEquals(fight.id(), fightWithBoxers.id());
    assertEquals(fight.event().happenAt(), fightWithBoxers.getHappenAt());
    assertEquals(fight.event().place(), fightWithBoxers.getPlace());
    assertEquals(firstBoxer.name(), fightWithBoxers.getFirstBoxerName());
    assertEquals(secondBoxer.name(), fightWithBoxers.getSecondBoxerName());
    assertEquals(fight.numberOfRounds(), fightWithBoxers.getNumberOfRounds());
  }

  @Test
  public void findAllBeforeReturnOnlyPreviousFightsWithCorrectOrder() {
    Fight tomorrowFight = FightMother.aFightWithHappenAt(LocalDate.now().plusDays(1));
    Fight yesterdayFight = FightMother.aFightWithHappenAt(LocalDate.now().minusDays(1));
    Fight todayFight = FightMother.aFightWithHappenAt(LocalDate.now());
    Boxer firstBoxer = BoxerMother.aBoxerWithId("ali");
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId("foreman");
    boxerRepository.save(secondBoxer);
    fightRepository.save(tomorrowFight);
    fightRepository.save(yesterdayFight);
    fightRepository.save(todayFight);

    List<FightWithBoxers> fights = new ArrayList<>(
        fightWithBoxersFetcher.findAllBefore(LocalDate.now(), Sort.desc("happenAt"), 10));
    assertEquals(2, fights.size());
    assertEquals(todayFight.id(), fights.get(0).id());
    assertEquals(yesterdayFight.id(), fights.get(1).id());
  }

  @Test
  public void getAllFights() {
    Fight fight = FightMother.aFightWithId(fightId);
    fightRepository.save(fight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId("ali");
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId("foreman");
    boxerRepository.save(secondBoxer);

    Collection<FightWithBoxers> fightWithBoxers = fightWithBoxersFetcher.all();

    assertEquals(1, fightWithBoxers.size());
  }
}
