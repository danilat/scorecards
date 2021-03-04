package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/editor/boxers")
public class EditorBoxersController {

  @GetMapping("new")
  public String createForm(Model model) {
    if (!model.containsAttribute("boxer")) {
      model.addAttribute("boxer", CreateBoxerParams.empty());
    }
    return "editor/boxers/new";
  }

  @Autowired
  private CreateBoxer createBoxer;
  private String createResult;

  @PostMapping("")
  public String create(Model model, @ModelAttribute CreateBoxerParams params) {
    createBoxer.execute(createBoxerPort(model), params);
    return createResult;
  }

  private PrimaryPort<Boxer> createBoxerPort(Model model) {
    return new PrimaryPort<Boxer>() {
      @Override
      public void success(Boxer boxer) {
        createResult = "redirect:/editor/boxers/" + boxer.id().value();
      }

      @Override
      public void error(FieldErrors errors) {
        model.addAttribute("errors", errors);
        createResult = createForm(model);
      }
    };
  }
}
