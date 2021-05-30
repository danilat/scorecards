package com.danilat.scorecards.shared.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.persistence.jdbc.JdbcAccountRepository;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.shared.auth.firebase.FirebaseAuth;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import com.google.firebase.auth.FirebaseAuthException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FirebaseAuth.class, JdbcConfig.class, JdbcAccountRepository.class})
public class AuthIT {

  @Autowired
  private Auth auth;
  @Autowired
  private AccountRepository accountRepository;
  @MockBean
  private TokenValidator tokenValidator;

  @Before
  public void setUp() {
    accountRepository.clear();
  }

  @Test
  public void givenATokenWithoutExistingEmailThenReturnsNothing() throws FirebaseAuthException {
    String theToken = "some_valid_token";
    TokenValidator.UserFromToken user = new TokenValidator.UserFromToken("irrelevant", "irrelevant", "irrelevant");
    when(tokenValidator.validateToken(theToken)).thenReturn(user);

    assertNull(auth.currentAccountId(theToken));
  }

  @Test
  public void givenATokenWithExistingEmailThenReturnsTheAccount() throws FirebaseAuthException {
    Account account = AccountMother.anAccountWithUsername("danilat");
    accountRepository.save(account);
    String theToken = "some_valid_token";
    TokenValidator.UserFromToken user = new TokenValidator.UserFromToken(account.name(), account.email(), account.picture());
    when(tokenValidator.validateToken(theToken)).thenReturn(user);

    assertEquals(account.id(), auth.currentAccountId(theToken));
  }
}
