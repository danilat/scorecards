package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.core.usecases.scores.ScoreRound;
import com.danilat.scorecards.core.usecases.scores.ScoreRound.ScoreFightParameters;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

  private Model model;
  private String id;
  private String findByIdResult;
  private ScoreForm score;

  private PrimaryPort<FightWithBoxers> fightWithBoxersPort = new PrimaryPort<FightWithBoxers>() {
    @Override
    public void success(FightWithBoxers fight) {
      model.addAttribute("fight", fight);
      score = new ScoreForm();
      score.setFirstBoxer(fight.getFirstBoxerId().value());
      score.setSecondBoxer(fight.getSecondBoxerId().value());
      model.addAttribute("score", score);
      findByIdResult = "show-fight";
    }

    @Override
    public void error(FieldErrors errors) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, errors.toString());
    }
  };

  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model) {
    this.model = model;
    retrieveAFight.execute(fightWithBoxersPort, new FightId(id));
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
      scoreResult = findById(id, model);
    }
  };

  @PostMapping("{id}/score")
  public String score(@PathVariable String id, Model model, @ModelAttribute ScoreForm score) {
    this.id = id;
    this.model = model;
    this.score = score;
    ScoreFightParameters params = new ScoreFightParameters(new FightId(id), score.getRound(),
        new BoxerId(score.getFirstBoxer()), score.getFirstBoxerScore(),
        new BoxerId(score.getSecondBoxer()), score.getSecondBoxerScore());
    scoreRound.execute(scoreRoundPort, params);
    return scoreResult;
  }

  private class ScoreForm {

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
}
