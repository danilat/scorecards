package com.danilat.scorecards.controllers;

import static org.mockito.Mockito.when;

import com.danilat.scorecards.ScorecardsApplication;
import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import javax.servlet.http.Cookie;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ScorecardsApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class BaseControllerIT {

  @MockBean
  protected TokenValidator tokenValidator;

  protected Cookie getCookieFor(Account account) {
    String theToken = "some_valid_token";
    Token token = new Token(account.name(), account.email(), account.picture());
    when(tokenValidator.validateToken(theToken)).thenReturn(token);
    Cookie cookie = new Cookie("access_token", theToken);
    return cookie;
  }

  @Autowired
  protected MockMvc mvc;
}
