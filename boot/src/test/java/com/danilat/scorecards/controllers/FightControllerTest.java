package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FightControllerTest extends BaseControllerTest {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;

  @Test
  public void getsAnExistingFight() throws Exception {
    String fightId = "some-id";
    Fight fight = FightMother.aFightWithId(fightId);
    fightRepository.save(fight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId(fight.firstBoxer());
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId(fight.secondBoxer());
    boxerRepository.save(secondBoxer);

    this.mvc.perform(get("/fights/" + fightId))
        .andExpect(status().isOk())
        .andExpect(model().attribute("fight", notNullValue()));
  }

  @Test
  public void getsANonExistingFight() throws Exception {
    String fightId = "non-existing-id";

    this.mvc.perform(get("/fights/" + fightId))
        .andExpect(status().isNotFound());
  }


}
