package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
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
    public void success(Collection<ScoreCardWithFightDetails> response) {
      model.addAttribute("scorecards", response);
    }
  };

  @GetMapping("/sc/{accountId}")
  public String listByAccount(@PathVariable String accountId, Model model) {
    this.model = model;
    this.model.addAttribute("accountId", accountId);
    retrieveScoreCards.execute(retrieveScoreCardsPrimaryPort, new AccountId(accountId));
    return "list-scorecards";
  }
}
