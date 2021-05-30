package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.danilat.scorecards.shared.domain.errors.Error;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private RegisterAccount registerAccount;

    @Autowired
    private Auth auth;

    @GetMapping("login")
    public String login(@CookieValue(defaultValue = "", name = "access_token") String accessToken) {
      if ("".equals(accessToken)) {
        return "accounts/login";
      }
      TokenValidator.UserFromToken user = tokenValidator.validateToken(accessToken);
      if (user == null) {
        return "accounts/login";
      }
      AccountId accountId = auth.currentAccountId(accessToken);
      if (accountId == null) {
        return "redirect:/accounts/new";
      }
      return "redirect:/sc/" + accountId.value();
    }

    @GetMapping("new")
    public String createForm(@CookieValue(defaultValue = "", name = "access_token") String accessToken, Model model) {
        if ("".equals(accessToken)) {
            return "redirect:/accounts/login";
        }
        TokenValidator.UserFromToken user = tokenValidator.validateToken(accessToken);
        if (user == null) {
            return "redirect:/accounts/login";
        }
        model.addAttribute("idToken", accessToken);
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("picture", user.getPicture());
        return "accounts/new";
    }

    private Model model;
    private String createResult;
    private String accessToken;

    private PrimaryPort<Account> registerAccountPrimaryPort = new PrimaryPort<Account>() {

        @Override
        public void success(Account account) {
            createResult = "redirect:/";
        }

        @Override
        public void error(Error errors) {
            model.addAttribute("errors", errors);
            createResult = createForm(accessToken, model);
        }
    };

    @PostMapping("/")
    public String create(@CookieValue(defaultValue = "", name = "access_token") String accessToken,
                         @ModelAttribute AccountForm accountForm, Model model)
            throws FirebaseAuthException {
        this.accessToken = accessToken;
        TokenValidator.UserFromToken user = tokenValidator.validateToken(accessToken);
        this.model = model;
        RegisterAccountParameters params = new RegisterAccountParameters(accountForm.getUsername(), accountForm.getName(),
                user.getEmail(), user.getPicture());
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
