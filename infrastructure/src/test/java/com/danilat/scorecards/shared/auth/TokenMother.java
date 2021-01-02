package com.danilat.scorecards.shared.auth;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;

public class TokenMother {

  public static Token forAccount(Account account) {
    return new Token(account.name(), account.email(), account.picture());
  }
}
