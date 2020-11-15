package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.FightMother;
import java.util.Collection;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class FightRepositoryTest {

  private FightRepository fightRepository;
  private FightId fightId = new FightId("some irrelevant id");

  @Before
  public void setup() {
    fightRepository = new InMemoryFightRepository();
  }

  @Test
  public void saveAFight() {
    Fight aFight = FightMother.aFightWithId(fightId);

    fightRepository.save(aFight);

    Optional retrieved = fightRepository.get(fightId);
    assertTrue(retrieved.isPresent());
  }

  @Test
  public void getAllFights() {
    Fight aFight = FightMother.aFightWithId(fightId);
    fightRepository.save(aFight);

    Collection<Fight> fights = fightRepository.all();

    assertEquals(1, fights.size());
  }
}
