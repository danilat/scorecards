package com.danilat.scorecards.controllers.editor;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.controllers.BaseControllerTest;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EditorFightsControllerTest extends BaseControllerTest {

  @Autowired
  private BoxerRepository boxerRepository;

  @Test
  public void getsTheFormToCreateAFight() throws Exception {
    Boxer aBoxer = BoxerMother.aBoxerWithId("pacquiao");
    boxerRepository.save(aBoxer);

    this.mvc.perform(get("/editor/fights/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("boxers", hasItems(aBoxer)));

  }
}
