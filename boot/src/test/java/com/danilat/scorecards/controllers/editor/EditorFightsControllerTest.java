package com.danilat.scorecards.controllers.editor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.controllers.BaseControllerTest;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EditorFightsControllerTest extends BaseControllerTest {

  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private FightRepository fightRepository;

  private Boxer firstBoxer;
  private Boxer secondBoxer;

  @Before
  public void setup() {
    boxerRepository.clear();
    fightRepository.clear();
    firstBoxer = BoxerMother.aBoxerWithId("pacquiao");
    boxerRepository.save(firstBoxer);
    secondBoxer = BoxerMother.aBoxerWithId("mayweather");
    boxerRepository.save(secondBoxer);
  }

  @Test
  public void getTheFightsList() throws Exception {
    Fight fight = FightMother.aFightWithIdAndBoxers(new FightId("a-fight"), firstBoxer.id(), secondBoxer.id());
    fightRepository.save(fight);

    this.mvc.perform(get("/editor/fights"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("fights", notNullValue()))
        .andExpect(model().attribute("fights", hasSize(1)));
  }

  @Test
  public void getsTheFormToCreateAFight() throws Exception {
    this.mvc.perform(get("/editor/fights/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("boxers", hasItems(firstBoxer)));

  }

  @Test
  public void postANewFightWithValidParameters() throws Exception {
    this.mvc.perform(post("/editor/fights")
        .param("firstBoxer", firstBoxer.id().value())
        .param("secondBoxer", secondBoxer.id().value())
        .param("numberOfRounds", "12")
        .param("place", "MGM Grand Garden Arena")
        .param("happenAt", LocalDate.now().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/fights/{id}"));
  }

  @Test
  public void postANewFightWithInValidParameters() throws Exception {
    this.mvc.perform(post("/editor/fights")
        .param("firstBoxer", firstBoxer.id().value())
        .param("secondBoxer", "foobar")
        .param("numberOfRounds", "0")
        .param("place", "MGM Grand Garden Arena")
        .param("happenAt", LocalDate.now().toString()))
        .andExpect(status().isOk())
        .andExpect(view().name("editor/fights/new"));
  }
}
