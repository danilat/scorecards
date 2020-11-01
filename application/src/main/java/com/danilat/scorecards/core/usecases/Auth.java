package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.core.domain.account.AccountId;

public interface Auth {

  AccountId currentAccount();
}
