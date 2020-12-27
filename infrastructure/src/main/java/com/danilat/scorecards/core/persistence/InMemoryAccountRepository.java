package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccountRepository extends InMemoryRepository<Account, AccountId> implements AccountRepository {

  @Override
  public Optional<Account> findByUsername(String username) {
    return entities.values().stream().filter(entry -> entry.username().equals(username)).findFirst();
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    return entities.values().stream().filter(entry -> entry.email().equals(email)).findFirst();
  }
}
