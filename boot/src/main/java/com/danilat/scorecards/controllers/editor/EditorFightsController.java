package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.core.usecases.fights.RetrieveAllFights;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import com.danilat.scorecards.shared.usecases.UseCase.Empty;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/editor/fights")
public class EditorFightsController {

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;
  @Autowired
  private RegisterFight registerFight;
  @Autowired
  private RetrieveAllFights retrieveAllFights;
  @Autowired
  private RetrieveAFight retrieveAFight;

  private String createResult;

  @GetMapping("")
  public String list(Model model) {
    retrieveAllFights.execute(retrieveAllFightsPort(model), new Empty());
    return "editor/fights/list";
  }

  private PrimaryPort<Collection<FightWithBoxers>> retrieveAllFightsPort(Model model) {
    return fights -> model.addAttribute("fights", fights);
  }

  @GetMapping("new")
  public String createForm(Model model) {
    retrieveAllBoxers.execute(retrieveAllBoxersPort(model));

    return "editor/fights/new";
  }

  private PrimaryPort<Collection<Boxer>> retrieveAllBoxersPort(Model model) {
    return boxers -> {
      model.addAttribute("boxers", boxers);
      if (!model.containsAttribute("fight")) {
        model.addAttribute("fight", new FightForm());
      }
    };
  }

  private PrimaryPort<FightWithBoxers> retrieveAFightPort(Model model) {
    return fight -> {
      FightForm fightForm = new FightForm();
      fightForm.setId(fight.getId());
      fightForm.setFirstBoxer(fight.getFirstBoxerId().value());
      fightForm.setSecondBoxer(fight.getSecondBoxerId().value());
      fightForm.setPlace(fight.getPlace());
      fightForm.setHappenAt(fight.getHappenAt());
      fightForm.setNumberOfRounds(fight.getNumberOfRounds());
      model.addAttribute("fight", fightForm);
    };
  }

  @PostMapping("")
  public String create(Model model, @ModelAttribute FightForm fightForm) {
    RegisterFightParameters parameters = new RegisterFightParameters(
        new BoxerId(fightForm.getFirstBoxer()),
        new BoxerId(fightForm.getSecondBoxer()), fightForm.getHappenAt(), fightForm.getPlace(),
        fightForm.getNumberOfRounds());
    registerFight.execute(registerFightPort(model, fightForm), parameters);
    return createResult;
  }

  private PrimaryPort<Fight> registerFightPort(Model model, FightForm fightForm) {
    return new PrimaryPort<Fight>() {
      @Override
      public void success(Fight fight) {
        createResult = "redirect:/fights/" + fight.id().value();
      }

      @Override
      public void error(Error errors) {
        model.addAttribute("errors", errors);
        model.addAttribute("fight", fightForm);
        createResult = createForm(model);
      }
    };
  }

  private class FightForm {
    private String id;
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

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

  @GetMapping("{id}")
  public String edit(@PathVariable String id, Model model) {
    retrieveAllBoxers.execute(retrieveAllBoxersPort(model));
    retrieveAFight.execute(retrieveAFightPort(model), new FightId(id));

    return "editor/fights/edit";
  }


}
