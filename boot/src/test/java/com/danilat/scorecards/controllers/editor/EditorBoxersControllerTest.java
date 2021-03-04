package com.danilat.scorecards.controllers.editor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.controllers.BaseControllerTest;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EditorBoxersControllerTest extends BaseControllerTest {

  @Autowired
  private BoxerRepository boxerRepository;

  @Before
  public void setup() {
    boxerRepository.clear();
  }

  @Test
  public void getTheBoxersList() throws Exception {
    boxerRepository.save(BoxerMother.aBoxerWithId("irrelevant-id"));

    this.mvc.perform(get("/editor/boxers"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("boxers", notNullValue()))
        .andExpect(model().attribute("boxers", hasSize(1)));
  }

  @Test
  public void getsTheFormToCreateABoxer() throws Exception {
    this.mvc.perform(get("/editor/boxers/new"))
        .andExpect(status().isOk());
  }

  @Test
  public void postANewBoxerWithValidParameters() throws Exception {
    this.mvc.perform(post("/editor/boxers")
        .param("name", "Manny Paqciao")
        .param("alias", "pacman")
        .param("boxrecUrl", "https://boxrec.com/en/proboxer/6129"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/editor/boxers/{id}"));
  }

  @Test
  public void postANewBoxerWithInValidParameters() throws Exception {
    this.mvc.perform(post("/editor/boxers")
        .param("name", "")
        .param("alias", "pacman")
        .param("boxrecUrl", "https://boxrec.com/en/proboxer/6129"))
        .andExpect(status().isOk())
        .andExpect(view().name("editor/boxers/new"));
  }
}
