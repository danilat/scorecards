package com.danilat.scorecards.component.repositories;

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
import com.danilat.scorecards.core.mothers.FightWithBoxersMother;
import com.danilat.scorecards.repositories.InMemoryBoxerRepository;
import com.danilat.scorecards.repositories.InMemoryFightRepository;
import com.danilat.scorecards.repositories.InMemoryFightWithBoxersRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {InMemoryFightRepository.class, InMemoryBoxerRepository.class, InMemoryFightWithBoxersRepository.class})
@RunWith(SpringRunner.class)
public class FightWithBoxersRepositoryTest {


  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private FightWithBoxersRepository fightWithBoxersRepository;
  private FightId fightId = new FightId("some irrelevant id");


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
