package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import java.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HomeControllerIT extends BaseControllerIT {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;

  @Test
  public void rootPathIsAvailable() throws Exception {
    this.mvc.perform(get("/"))
        .andExpect(status().isOk());
  }

  @Test
  public void hasTheLastFightsThatHappened() throws Exception {
    boxerRepository.clear();
    fightRepository.clear();
    Fight fight = FightMother.aFightWithHappenAt(LocalDate.now().minusDays(1));
    boxerRepository.save(BoxerMother.aBoxerWithId(fight.firstBoxer()));
    boxerRepository.save(BoxerMother.aBoxerWithId(fight.secondBoxer()));
    fightRepository.save(fight);

    this.mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("lastFights", notNullValue()))
        .andExpect(model().attribute("lastFights", hasSize(1)));
  }
}
