package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.domain.ScoreCardsBusinessException;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/editor/fights")
public class EditorFightsController {

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;

  @Autowired
  private RegisterFight registerFight;

  @GetMapping("new")
  public String createForm(Model model) {
    Map<BoxerId, Boxer> boxers = retrieveAllBoxers.execute();
    model.addAttribute("boxers", boxers.values());
    return "editor/fights/new";
  }

  @PostMapping("")
  public String create(Model model, @RequestParam String firstBoxer,
      @RequestParam String secondBoxer,
      @RequestParam Integer numberOfRounds, @RequestParam String place, @RequestParam
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate happenAt) {
    try {
      RegisterFightParameters parameters = new RegisterFightParameters(new BoxerId(firstBoxer),
          new BoxerId(secondBoxer), happenAt, place, numberOfRounds);
      Fight fight = registerFight.execute(parameters);
      return "redirect:/fights/" + fight.id().value();
    } catch (ScoreCardsBusinessException e) {
      return createForm(model);
    }
  }
}
