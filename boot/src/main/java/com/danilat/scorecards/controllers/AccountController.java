package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/accounts")
public class AccountController {

  @Autowired
  private TokenValidator tokenValidator;

  @Autowired
  private RegisterAccount registerAccount;

  @GetMapping("new/{idToken}")
  public String createForm(@PathVariable String idToken, Model model) {
    try {
      Token token = tokenValidator.validateToken(idToken);
      model.addAttribute("idToken", idToken);
      model.addAttribute("name", token.getName());
      model.addAttribute("email", token.getEmail());
      model.addAttribute("picture", token.getPicture());
    } catch (FirebaseAuthException e) {
      throw new RuntimeException(e);
    }
    return "accounts/new";
  }

  private Model model;
  private String createResult;
  private String idToken;

  private PrimaryPort<Account> registerAccountPrimaryPort = new PrimaryPort<Account>() {

    @Override
    public void success(Account account) {
      createResult = "redirect:/";
    }

    @Override
    public void error(FieldErrors errors) {
      model.addAttribute("errors", errors);
      createResult = createForm(idToken, model);
    }
  };

  @PostMapping("{idToken}")
  public String create(@PathVariable String idToken, @ModelAttribute AccountForm accountForm, Model model)
      throws FirebaseAuthException {
    this.idToken = idToken;
    this.model = model;
    Token token = tokenValidator.validateToken(idToken);
    RegisterAccountParameters params = new RegisterAccountParameters(accountForm.getUsername(), accountForm.getName(),
        token.getEmail(), token.getPicture());
    registerAccount.execute(registerAccountPrimaryPort, params);
    return createResult;
  }

  private class AccountForm {

    private String username;
    private String name;

    public AccountForm() {
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
