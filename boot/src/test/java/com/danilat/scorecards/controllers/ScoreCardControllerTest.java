package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScoreCardControllerTest extends BaseControllerTest {

  private String accountId = "danilat";
  @Autowired
  private ScoreCardRepository scoreCardRepository;
  private ScoreCard scoreCard;

  @Before
  public void setUp() {
    scoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), new AccountId(accountId));
    scoreCardRepository.save(scoreCard);
  }

  @Test
  public void getsScoreCardsByAccount() throws Exception {
    this.mvc.perform(get("/" + accountId + "/scorecards/"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("scorecards", notNullValue()))
        .andExpect(model().attribute("scorecards", hasItem(scoreCard)));
  }
}
