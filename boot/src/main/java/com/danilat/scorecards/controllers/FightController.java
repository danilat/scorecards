package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/fights")
public class FightController {

  @Autowired
  private RetrieveAFight retrieveAFight;

  private Model model;
  private String findByIdResult;

  private PrimaryPort<FightWithBoxers> fightWithBoxersPort = new PrimaryPort<FightWithBoxers>() {
    @Override
    public void success(FightWithBoxers fight) {
      model.addAttribute("fight", fight);
      findByIdResult = "show-fight";
    }

    @Override
    public void error(Errors errors) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, errors.toString());
    }
  };

  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model) {
    this.model = model;
    retrieveAFight.execute(new FightId(id), fightWithBoxersPort);
    return findByIdResult;
  }

}
