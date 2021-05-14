package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;

public class AccountMother extends BaseMother {

  public static Account anAccountWithUsername(String username) {
    return new Account(new AccountId(username), username, faker().name().fullName(), faker().internet().emailAddress(),
        faker().internet().avatar(), false);
  }

  public static Account anAccountWithEmail(String email) {
    String username = faker().name().username();
    return new Account(new AccountId(username), username, faker().name().fullName(), email,
        faker().internet().avatar(), false);
  }
}
