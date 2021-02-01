package com.danilat.scorecards.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountControllerTest extends BaseControllerTest {

  @Autowired
  private AccountRepository accountRepository;

  @Before
  public void setUp() {
    accountRepository.clear();
  }

  @Test
  public void isNotAccessTokenCookieWhenLoginThenAskForIt() throws Exception {
    this.mvc.perform(
        get("/accounts/login/"))
        .andExpect(status().isOk())
        .andExpect(view().name("accounts/login"));
  }

  @Test
  public void theValidAccessTokenIsNotPresentWhenLoginThenRedirectsToCreate() throws Exception {
    String theToken = "some_valid_token";
    Token token = new Token("Dani", "dani@email.com", "a picture url");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);

    this.mvc.perform(
        get("/accounts/login/").cookie(cookie))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/accounts/new"));
  }

  @Test
  public void theAccessTokenIsPresentWhenLoginThenRedirectsToAccount() throws Exception {
    Account account = AccountMother.anAccountWithEmail("dani@email.com");
    accountRepository.save(account);
    String theToken = "some_valid_token";
    Token token = new Token(account.name(), account.email(), "a picture url");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);

    this.mvc.perform(
        get("/accounts/login/").cookie(cookie))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/" + account.username() + "/scorecards"));
  }

  @Test
  public void newAccountForm() throws Exception {
    String theToken = "some_valid_token";
    Token token = new Token("Dani", "dani@email.com", "a picture url");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);

    this.mvc.perform(
        get("/accounts/new/").cookie(cookie))
        .andExpect(status().isOk());
  }

  @Test
  public void postANewAccountWithValidParameters() throws Exception {
    String theToken = "some_valid_token";
    Token token = new Token("Dani", "dani@email.com", "a picture url");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);

    this.mvc.perform(post("/accounts/").cookie(cookie)
        .param("name", "Dani")
        .param("username", "danilat"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  public void postANewAccountWithInValidParameters() throws Exception {
    String theToken = "some_valid_token";
    Token token = new Token("Dani", "dani@email.com", "a picture url");
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);

    this.mvc.perform(post("/accounts/").cookie(cookie)
        .param("name", "Dani")
        .param("username", ""))
        .andExpect(status().isOk())
        .andExpect(view().name("accounts/new"));
  }
}
