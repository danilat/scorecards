package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.usecases.accounts.RetrieveAccount;
import com.danilat.scorecards.core.usecases.scores.RetrieveScoreCards;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScoreCardController {

  private Model model;
  @Autowired
  private RetrieveScoreCards retrieveScoreCards;
  private PrimaryPort<Collection<ScoreCardWithFightDetails>> retrieveScoreCardsPrimaryPort = new PrimaryPort<Collection<ScoreCardWithFightDetails>>() {
    @Override
    public void success(Collection<ScoreCardWithFightDetails> scorecards) {
      model.addAttribute("scorecards", scorecards);
    }
  };
  @Autowired
  private RetrieveAccount retrieveAccount;
  private PrimaryPort<Account> retrieveAccountPrimaryPort = new PrimaryPort<Account>() {
    @Override
    public void success(Account account) {
      model.addAttribute("account", account);
    }
  };


  @GetMapping("/sc/{username}")
  public String listByAccount(@PathVariable String username, Model model) {
    this.model = model;
    AccountId accountId = new AccountId(username);
    retrieveAccount.execute(retrieveAccountPrimaryPort, accountId);
    retrieveScoreCards.execute(retrieveScoreCardsPrimaryPort, accountId);
    return "list-scorecards";
  }
}
