package com.danilat.scorecards.shared;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import java.util.Optional;

public interface Auth {

  AccountId currentAccountId(String accessToken);

  Optional<Account> currentAccount(String accessToken);
}
