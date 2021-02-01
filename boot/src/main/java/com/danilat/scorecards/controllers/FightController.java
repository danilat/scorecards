package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard.RetrieveAScoreCardParameters;
import com.danilat.scorecards.core.usecases.scores.ScoreRound;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import java.util.ArrayList;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/fights")
public class FightController {

  @Autowired
  private RetrieveAFight retrieveAFight;

  @Autowired
  private ScoreRound scoreRound;

  @Autowired
  private RetrieveAScoreCard retrieveAScoreCard;

  @Autowired
  private Auth auth;

  private Model model;
  private String id;
  private String accessToken;
  private String findByIdResult;

  private PrimaryPort<FightWithBoxers> fightWithBoxersPort = new PrimaryPort<FightWithBoxers>() {
    @Override
    public void success(FightWithBoxers fight) {
      model.addAttribute("fight", fight);
      ScoreCardForm scoreCardForm = new ScoreCardForm(fight.getFirstBoxerId().value(),
          fight.getSecondBoxerId().value());
      model.addAttribute("scores", scoreCardForm);
      findByIdResult = "show-fight";
    }

    @Override
    public void error(FieldErrors errors) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, errors.toString());
    }
  };

  private PrimaryPort<ScoreCard> retrieveScoreCardPort = new PrimaryPort<ScoreCard>() {
    @Override
    public void success(ScoreCard scoreCard) {
      populateScoreCardForm(scoreCard);
    }

    @Override
    public void error(FieldErrors errors) {
      populateScoreCardForm(null);
    }

    private void populateScoreCardForm(ScoreCard scoreCard) {
      FightWithBoxers fight = (FightWithBoxers) model.getAttribute("fight");
      ScoreCardForm scoreCardForm = (ScoreCardForm) model.getAttribute("scores");
      IntStream.rangeClosed(1, fight.getNumberOfRounds()).forEach(round -> {
        ScoreForm scoreForm = new ScoreForm();
        scoreForm.setRound(round);
        scoreForm.setFirstBoxer(scoreCardForm.getFirstBoxer());
        scoreForm.setSecondBoxer(scoreCardForm.getSecondBoxer());
        if (scoreCard != null && scoreCard.isRoundScored(round)) {
          scoreForm.setFirstBoxerScore(scoreCard.firstBoxerScore(round));
          scoreForm.setSecondBoxerScore(scoreCard.secondBoxerScore(round));
        }
        scoreCardForm.add(scoreForm);
      });
    }
  };

  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model,
      @CookieValue(defaultValue = "", name = "access_token") String accessToken) {
    this.model = model;
    retrieveAFight.execute(fightWithBoxersPort, new FightId(id));
    AccountId accountId = auth.currentAccountId(accessToken);
    this.model.addAttribute("accountId", accountId);
    if (accountId != null) {
      RetrieveAScoreCardParameters parameters = new RetrieveAScoreCardParameters(new FightId(id),
          accountId);
      retrieveAScoreCard.execute(retrieveScoreCardPort, parameters);
    }
    return findByIdResult;
  }

  private String scoreResult;
  private PrimaryPort<ScoreCard> scoreRoundPort = new PrimaryPort<ScoreCard>() {
    @Override
    public void success(ScoreCard scoreCard) {
      String fightId = scoreCard.fightId().value();
      scoreResult = "redirect:/fights/" + fightId;
    }

    @Override
    public void error(FieldErrors errors) {
      model.addAttribute("errors", errors);
      scoreResult = findById(id, model, accessToken);
    }
  };

  @PostMapping("{id}/score")
  public String score(@PathVariable String id, Model model, @ModelAttribute ScoreForm scoreForm,
      @CookieValue(defaultValue = "", name = "access_token") String accessToken) {
    this.id = id;
    this.model = model;
    this.accessToken = accessToken;
    ScoreFightParameters params = new ScoreFightParameters(new FightId(id), scoreForm.getRound(),
        new BoxerId(scoreForm.getFirstBoxer()), scoreForm.getFirstBoxerScore(),
        new BoxerId(scoreForm.getSecondBoxer()), scoreForm.getSecondBoxerScore(), accessToken);
    scoreRound.execute(scoreRoundPort, params);
    return scoreResult;
  }

  protected class ScoreForm {

    private String firstBoxer;
    private String secondBoxer;
    private int round;
    private int firstBoxerScore;
    private int secondBoxerScore;

    public ScoreForm() {
    }

    public String getFirstBoxer() {
      return firstBoxer;
    }

    public void setFirstBoxer(String firstBoxer) {
      this.firstBoxer = firstBoxer;
    }

    public String getSecondBoxer() {
      return secondBoxer;
    }

    public void setSecondBoxer(String secondBoxer) {
      this.secondBoxer = secondBoxer;
    }

    public int getRound() {
      return round;
    }

    public void setRound(Integer round) {
      this.round = round;
    }

    public int getFirstBoxerScore() {
      return firstBoxerScore;
    }

    public void setFirstBoxerScore(Integer firstBoxerScore) {
      this.firstBoxerScore = firstBoxerScore;
    }

    public int getSecondBoxerScore() {
      return secondBoxerScore;
    }

    public void setSecondBoxerScore(Integer secondBoxerScore) {
      this.secondBoxerScore = secondBoxerScore;
    }
  }

  public class ScoreCardForm extends ArrayList<ScoreForm> {

    public ScoreCardForm(String firstBoxer, String secondBoxer) {
      super();
      this.firstBoxer = firstBoxer;
      this.secondBoxer = secondBoxer;
    }

    private String firstBoxer;
    private String secondBoxer;


    public String getFirstBoxer() {
      return firstBoxer;
    }

    public void setFirstBoxer(String firstBoxer) {
      this.firstBoxer = firstBoxer;
    }

    public String getSecondBoxer() {
      return secondBoxer;
    }

    public void setSecondBoxer(String secondBoxer) {
      this.secondBoxer = secondBoxer;
    }
  }
}
