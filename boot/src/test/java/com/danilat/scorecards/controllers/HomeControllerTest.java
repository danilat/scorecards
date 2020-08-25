package com.danilat.scorecards.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

public class HomeControllerTest extends BaseControllerTest {

  @Test
  public void rootPathIsAvailable() throws Exception {
    this.mvc.perform(get("/"))
        .andExpect(status().isOk());
  }
}
