package com.danilat.scorecards.core.usecases.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountCreated;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.EventBus;
import java.time.Instant;
import javax.lang.model.util.Types;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

public class RegisterAccountTest extends UseCaseUnitTest<Account> {

  @Mock
  private PrimaryPort<Account> primaryPort;
  @Mock
  private AccountRepository accountRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;

  private RegisterAccount registerAccount;

  private String AN_USERNAME = "danilat";
  private String AN_EMAIL = "danilat@danilat.com";
  private String A_NAME = "Dani Latorre";
  private String A_PICTURE_URL = "http://foobar.com/profile.jpg";
  private Instant anHappenedAt = Instant.now();
  private String anEventId = "an event id";

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  @Before
  public void setup() {
    when(clock.now()).thenReturn(anHappenedAt);
    registerAccount = new RegisterAccount(accountRepository, eventBus, clock);
  }

  @Test
  public void givenUsernameNameEmailAndPictureThenIsRegistered() {
    RegisterAccountParameters parameters = new RegisterAccountParameters(AN_USERNAME, A_NAME, AN_EMAIL, A_PICTURE_URL);

    registerAccount.execute(primaryPort, parameters);

    Account account = getSuccessEntity();
    assertEquals(AN_USERNAME, account.username());
    assertEquals(AN_EMAIL, account.email());
    assertEquals(A_NAME, account.name());
    assertEquals(A_PICTURE_URL, account.picture());
  }

  @Test
  public void givenUsernameNameEmailAndPictureThenTheIdIsTheUsername() {
    RegisterAccountParameters parameters = new RegisterAccountParameters(AN_USERNAME, A_NAME, AN_EMAIL, A_PICTURE_URL);

    registerAccount.execute(primaryPort, parameters);

    Account account = getSuccessEntity();
    assertEquals(AN_USERNAME, account.id().value());
  }

  @Test
  public void givenUsernameNameEmailAndPictureThenTheIdIsPersisted() {
    RegisterAccountParameters parameters = new RegisterAccountParameters(AN_USERNAME, A_NAME, AN_EMAIL, A_PICTURE_URL);

    registerAccount.execute(primaryPort, parameters);

    Account account = getSuccessEntity();
    verify(accountRepository, times(1)).save(account);
  }

  @Captor
  ArgumentCaptor<AccountCreated> accountCreatedArgumentCaptorCaptor;

  @Test
  public void givenUsernameNameEmailAndPictureThenAnEventIsPublished() {
    RegisterAccountParameters parameters = new RegisterAccountParameters(AN_USERNAME, A_NAME, AN_EMAIL, A_PICTURE_URL);

    registerAccount.execute(primaryPort, parameters);

    Account account = getSuccessEntity();
    verify(eventBus, times(1)).publish(accountCreatedArgumentCaptorCaptor.capture());
    AccountCreated accountCreated = accountCreatedArgumentCaptorCaptor.getValue();
    assertEquals(account, accountCreated.account());
    assertEquals(anHappenedAt, accountCreated.happenedAt());
    assertNotNull(accountCreated.eventId());
  }
}
