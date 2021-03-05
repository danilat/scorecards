package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.core.usecases.boxers.RetrieveABoxer;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import java.util.Collection;
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
@RequestMapping(value = "/editor/boxers")
public class EditorBoxersController {

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;

  @GetMapping("")
  public String list(Model model) {
    retrieveAllBoxers.execute(listBoxersPort(model));
    return "editor/boxers/list";
  }

  private PrimaryPort<Collection<Boxer>> listBoxersPort(Model model) {
    return boxers -> {
      model.addAttribute("boxers", boxers);
    };
  }

  @Autowired
  private RetrieveABoxer retrieveABoxer;

  @GetMapping("{id}")
  public String findById(@PathVariable String id, Model model) {
    retrieveABoxer.execute(findByIdPort(model), new BoxerId(id));
    return "editor/boxers/edit";
  }

  private PrimaryPort<Boxer> findByIdPort(Model model) {
    return new PrimaryPort<Boxer>() {
      @Override
      public void success(Boxer boxer) {
        BoxerForm boxerForm = new BoxerForm(boxer);
        model.addAttribute("boxer", boxerForm);
      }

      @Override
      public void error(Error errors) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errors.getMessage());
      }
    };
  }

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
        createResult = "redirect:/editor/boxers";
      }

      @Override
      public void error(Error errors) {
        model.addAttribute("errors", errors);
        createResult = createForm(model);
      }
    };
  }

  private class BoxerForm {

    private String id;
    private String name;
    private String alias;
    private String boxrecUrl;

    public BoxerForm(Boxer boxer) {
      id = boxer.id().value();
      name = boxer.name();
      alias = boxer.alias();
      boxrecUrl = boxer.boxrecUrl();
    }

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getAlias() {
      return alias;
    }

    public String getBoxrecUrl() {
      return boxrecUrl;
    }
  }
}
