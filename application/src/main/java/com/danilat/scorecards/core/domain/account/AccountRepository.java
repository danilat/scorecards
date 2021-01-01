package com.danilat.scorecards.core.domain.account;

import com.danilat.scorecards.shared.domain.Repository;
import java.util.Optional;

public interface AccountRepository extends Repository<Account, AccountId> {

  Optional<Account> findByUsername(String username);

  Optional<Account> findByEmail(String email);
}
