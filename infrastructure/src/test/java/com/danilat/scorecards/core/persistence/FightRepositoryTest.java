package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import com.danilat.scorecards.core.persistence.jdbc.JdbcFightRepository;
import com.danilat.scorecards.core.persistence.memory.InMemoryFightRepository;
import java.util.Collection;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcConfig.class, JdbcFightRepository.class})
@RunWith(SpringRunner.class)
public class FightRepositoryTest {

  @Autowired
  private FightRepository fightRepository;
  private FightId fightId = new FightId("some irrelevant id");

  @Before
  public void setup() {
    fightRepository.clear();
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
