package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.ScorecardsApplication;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(classes = ScorecardsApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class FightControllerTest {

  @Autowired
  private MockMvc mvc;

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

  @Test
  public void getsTheFormToCreateAFight() throws Exception {
    Boxer aBoxer = BoxerMother.aBoxerWithId("pacquiao");
    boxerRepository.save(aBoxer);

    this.mvc.perform(get("/editor/fights/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("boxers", hasItems(aBoxer)));

  }
}
