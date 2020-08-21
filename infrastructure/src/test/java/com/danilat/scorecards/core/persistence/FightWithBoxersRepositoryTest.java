package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class FightWithBoxersRepositoryTest {

  private FightRepository fightRepository;
  private BoxerRepository boxerRepository;
  private FightWithBoxersRepository fightWithBoxersRepository;
  private FightId fightId = new FightId("some irrelevant id");

  @Before
  public void setup() {
    fightRepository = new InMemoryFightRepository();
    boxerRepository = new InMemoryBoxerRepository();
    fightWithBoxersRepository = new InMemoryFightWithBoxersRepository(fightRepository,
        boxerRepository);
  }


  @Test
  public void getAFight() {
    Fight fight = FightMother.aFightWithId(fightId);
    fightRepository.save(fight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId("ali");
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId("foreman");
    boxerRepository.save(secondBoxer);

    Optional<FightWithBoxers> retrieved = fightWithBoxersRepository.get(fightId);
    assertTrue(retrieved.isPresent());
    FightWithBoxers fightWithBoxers = retrieved.get();
    assertEquals(fight.id(), fightWithBoxers.id());
    assertEquals(fight.event().happenAt(), fightWithBoxers.getHappenAt());
    assertEquals(fight.event().place(), fightWithBoxers.getPlace());
    assertEquals(firstBoxer.name(), fightWithBoxers.getFirstBoxerName());
    assertEquals(secondBoxer.name(), fightWithBoxers.getSecondBoxerName());
    assertEquals(fight.numberOfRounds(), fightWithBoxers.getNumberOfRounds());
  }
}
