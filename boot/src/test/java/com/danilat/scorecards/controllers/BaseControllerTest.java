package com.danilat.scorecards.controllers;

import com.danilat.scorecards.ScorecardsApplication;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ScorecardsApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

  @MockBean
  protected TokenValidator tokenValidator;

  @Autowired
  protected MockMvc mvc;
}
