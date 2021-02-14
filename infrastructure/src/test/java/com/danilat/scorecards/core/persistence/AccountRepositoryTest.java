package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.persistence.jdbc.JdbcAccountRepository;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcConfig.class, JdbcAccountRepository.class})
@RunWith(SpringRunner.class)
public class AccountRepositoryTest {

  @Autowired
  private AccountRepository accountRepository;
  private AccountId accountId = new AccountId("an account id");

  @Before
  public void setup() {
    accountRepository.clear();
  }

  @Test
  public void saveAnAccount() {
    Account anAccount = new Account(accountId, "username", "name", "email", "picture");

    accountRepository.save(anAccount);

    Optional retrieved = accountRepository.get(accountId);
    assertTrue(retrieved.isPresent());
    assertEquals(retrieved.get(), anAccount);
  }

  @Test
  public void findByEmailFromAnExistingAccount() {
    Account anAccount = new Account(accountId, "username", "name", "email", "picture");
    accountRepository.save(anAccount);

    Optional retrieved = accountRepository.findByEmail(anAccount.email());

    assertTrue(retrieved.isPresent());
  }

  @Test
  public void findByUsernameFromAnExistingAccount() {
    Account anAccount = new Account(accountId, "username", "name", "email", "picture");
    accountRepository.save(anAccount);

    Optional retrieved = accountRepository.findByUsername(anAccount.username());

    assertTrue(retrieved.isPresent());
  }
}
