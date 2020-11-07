package com.danilat.scorecards.shared;

import com.danilat.scorecards.core.domain.account.AccountId;

public interface Auth {

  AccountId currentAccount();
}
