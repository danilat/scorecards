package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/fights")
public class FightController {

  @Autowired
  FightRepository fightRepository;
  
  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model)  {
    Fight fight = fightRepository.get(new FightId(id)).get();
    model.addAttribute("fight", fight);
    return "show-fight";
  }
  
}
