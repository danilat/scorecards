package com.danilat.scorecards.core.domain.account;

import java.util.Optional;

public interface AccountRepository {
  void save(Account account);

  Optional<Account> get(AccountId accountId);

  Optional<Account> findByUsername(String username);

  Optional<Account> findByEmail(String email);
}
