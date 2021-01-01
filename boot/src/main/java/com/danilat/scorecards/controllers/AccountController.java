package com.danilat.scorecards.controllers;

import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/accounts")
public class AccountController {

  @Autowired
  private TokenValidator tokenValidator;

  @GetMapping("new/{idToken}")
  public String createForm(@PathVariable String idToken, Model model) throws FirebaseAuthException {
    Token token = tokenValidator.validateToken(idToken);
    model.addAttribute("name", token.getName());
    model.addAttribute("email", token.getEmail());
    model.addAttribute("picture", token.getPicture());
    return "accounts/new";
  }

}
