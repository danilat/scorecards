package com.danilat.scorecards.shared;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;

import java.util.Optional;

public interface Auth {

  AccountId currentAccountId(String accessToken);

  Optional<Account> currentOptionalAccount(String accessToken);

  default Account currentAccount(String accessToken){
    if(currentOptionalAccount(accessToken).isPresent()){
      return currentOptionalAccount(accessToken).get();
    }
    return null;
  }
}
