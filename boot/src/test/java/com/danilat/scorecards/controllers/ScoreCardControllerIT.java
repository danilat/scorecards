package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScoreCardControllerIT extends BaseControllerIT {

  @Autowired
  private ScoreCardRepository scoreCardRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private AccountRepository accountRepository;

  private String accountId = "danilat";
  private ScoreCard scoreCard;
  private FightId fightId;
  private Boxer firstBoxer;
  private Boxer secondBoxer;
  private Account account;

  @Before
  public void setUp() {
    scoreCardRepository.clear();
    accountRepository.clear();
    fightRepository.clear();
    boxerRepository.clear();
    account = AccountMother.anAccountWithUsername(accountId);
    accountRepository.save(account);
    scoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), new AccountId(accountId));
    fightId = scoreCard.fightId();
    Fight fight = FightMother.aFightWithIdAndBoxers(fightId, scoreCard.firstBoxerId(), scoreCard.secondBoxerId());
    fightRepository.save(fight);
    scoreCardRepository.save(scoreCard);
    firstBoxer = BoxerMother.aBoxerWithId(fight.firstBoxer());
    boxerRepository.save(firstBoxer);
    secondBoxer = BoxerMother.aBoxerWithId(fight.secondBoxer());
    boxerRepository.save(secondBoxer);
  }

  @Test
  public void postANewScoreWithValidParameters() throws Exception {
    Cookie cookie = getCookieFor(account);

    this.mvc.perform(post("/fights/" + fightId.value() + "/score").cookie(cookie)
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
    Cookie cookie = getCookieFor(account);

    this.mvc.perform(post("/fights/" + fightId.value() + "/score").cookie(cookie)
        .param("firstBoxer", firstBoxer.id().value())
        .param("firstBoxerScore", "0")
        .param("secondBoxer", secondBoxer.id().value())
        .param("secondBoxerScore", "0")
        .param("round", "1"))
        .andExpect(status().isOk())
        .andExpect(view().name("show-fight"));
  }

  @Test
  public void getsAccountProfile() throws Exception {
    Account account = AccountMother.anAccountWithUsername("danilat");
    accountRepository.save(account);
    this.mvc.perform(get("/sc/" + account.username()))
            .andExpect(status().isOk())
            .andExpect(model().attribute("scorecards", notNullValue()))
            .andExpect(model().attribute("account", equalTo(account)));
  }
}
