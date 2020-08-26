package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.core.usecases.fights.InvalidFightException;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    if (!model.containsAttribute("fight")) {
      model.addAttribute("fight", new FightForm());
    }
    return "editor/fights/new";
  }

  @PostMapping("")
  public String create(Model model, @ModelAttribute FightForm fightForm) {
    try {
      RegisterFightParameters parameters = new RegisterFightParameters(
          new BoxerId(fightForm.getFirstBoxer()),
          new BoxerId(fightForm.getSecondBoxer()), fightForm.getHappenAt(), fightForm.getPlace(),
          fightForm.getNumberOfRounds());
      Fight fight = registerFight.execute(parameters);
      return "redirect:/fights/" + fight.id().value();
    } catch (InvalidFightException e) {
      model.addAttribute("errors", e.getViolations());
      model.addAttribute("fight", fightForm);
      return createForm(model);
    }
  }

  private class FightForm {

    private String firstBoxer;
    private String secondBoxer;
    private Integer numberOfRounds;
    private String place;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate happenAt;

    public FightForm() {

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

    public Integer getNumberOfRounds() {
      return numberOfRounds;
    }

    public void setNumberOfRounds(Integer numberOfRounds) {
      this.numberOfRounds = numberOfRounds;
    }

    public String getPlace() {
      return place;
    }

    public void setPlace(String place) {
      this.place = place;
    }

    public LocalDate getHappenAt() {
      return happenAt;
    }

    public void setHappenAt(LocalDate happenAt) {
      this.happenAt = happenAt;
    }
  }
}
