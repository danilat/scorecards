package com.danilat.scorecards.shared.auth;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;

public class UserFromToken {

  public static TokenValidator.UserFromToken forAccount(Account account) {
    return new TokenValidator.UserFromToken(account.name(), account.email(), account.picture());
  }
}
