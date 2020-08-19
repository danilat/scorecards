package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
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

  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model) {
    try {
      FightWithBoxers fight = retrieveAFight.execute(new FightId(id));

      model.addAttribute("fight", fight);
      return "show-fight";
    } catch (FightNotFoundException exception) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fight not found", exception);
    }
  }

}
