package com.danilat.scorecards.core.domain.account;

import java.util.Optional;

public interface AccountRepository {
  void save(Account account);

  Optional get(AccountId accountId);
}
