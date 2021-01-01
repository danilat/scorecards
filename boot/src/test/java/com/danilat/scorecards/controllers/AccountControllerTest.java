package com.danilat.scorecards.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AccountControllerTest extends BaseControllerTest {

  @Test
  public void newAccountForm() throws Exception {
    String theToken = "some_valid_token";
    Token token = new Token("a name", "an email", "a picture");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);

    this.mvc.perform(
        get("/accounts/new/" + theToken))
        .andExpect(status().isOk());
  }
}
