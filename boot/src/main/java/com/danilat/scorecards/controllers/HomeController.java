package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.usecases.fights.RetrieveLastPastFights;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @Autowired
  private RetrieveLastPastFights retrieveLastPastFights;
  Collection<FightWithBoxers> lastFights;

  private PrimaryPort<Collection<FightWithBoxers>> retrieveLastPastFightsPort() {
    return fights -> lastFights = fights;
  }

  @GetMapping("/")
  public String index(Model model) {
    retrieveLastPastFights.execute(retrieveLastPastFightsPort());
    model.addAttribute("lastFights", lastFights);
    return "home";
  }
}
