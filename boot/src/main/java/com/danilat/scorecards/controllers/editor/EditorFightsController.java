package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/editor/fights")
public class EditorFightsController {

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;

  @GetMapping("new")
  public String create(Model model) {
    Map<BoxerId, Boxer> boxers = retrieveAllBoxers.execute();
    model.addAttribute("boxers", boxers.values());
    return "editor/fights/new";
  }
}
