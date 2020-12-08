package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.shared.Auth;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FightControllerTest extends BaseControllerTest {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;

  private String fightId = "some-id";
  private Boxer firstBoxer;
  private Boxer secondBoxer;

  @Before
  public void setUp() {
    Fight fight = FightMother.aFightWithId(fightId);
    fightRepository.save(fight);
    firstBoxer = BoxerMother.aBoxerWithId(fight.firstBoxer());
    boxerRepository.save(firstBoxer);
    secondBoxer = BoxerMother.aBoxerWithId(fight.secondBoxer());
    boxerRepository.save(secondBoxer);
  }

  @Test
  public void getsAnExistingFight() throws Exception {
    this.mvc.perform(get("/fights/" + fightId))
        .andExpect(status().isOk())
        .andExpect(model().attribute("fight", notNullValue()))
        .andExpect(model().attribute("score", notNullValue()));
  }

  @Test
  public void getsANonExistingFight() throws Exception {
    String fightId = "non-existing-id";

    this.mvc.perform(get("/fights/" + fightId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void postANewScoreWithValidParameters() throws Exception {
    this.mvc.perform(post("/fights/" + fightId + "/score")
        .param("firstBoxer", firstBoxer.id().value())
        .param("firstBoxerScore", "10")
        .param("secondBoxer", secondBoxer.id().value())
        .param("secondBoxerScore", "10")
        .param("round", "1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/fights/{fightId}"));
  }

  @Test
  public void postANewScoreWithInValidParameters() throws Exception {
    this.mvc.perform(post("/fights/" + fightId + "/score")
        .param("firstBoxer", firstBoxer.id().value())
        .param("firstBoxerScore", "0")
        .param("secondBoxer", secondBoxer.id().value())
        .param("secondBoxerScore", "0")
        .param("round", "1"))
        .andExpect(status().isOk())
        .andExpect(view().name("show-fight"));
  }
}