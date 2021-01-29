package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.persistence.memory.InMemoryAccountRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class AccountRepositoryTest {

  private AccountRepository accountRepository;
  private AccountId accountId = new AccountId("an account id");

  @Before
  public void setup() {
    accountRepository = new InMemoryAccountRepository();
  }

  @Test
  public void saveAnAccount() {
    Account anAccount = new Account(accountId, "username", "name", "email", "picture");

    accountRepository.save(anAccount);

    Optional retrieved = accountRepository.get(accountId);
    assertTrue(retrieved.isPresent());
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
