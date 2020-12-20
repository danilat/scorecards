package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;

public class AccountMother {
  public static Account anAccountWithUsername(String username){
    return new Account(new AccountId(username), username, "irrelevant name", "an@email.com", "http://foobar.com/profile.jpg");
  }

  public static Account anAccountWithEmail(String email){
    return new Account(new AccountId("foobar"), "foobar", "irrelevant name", email, "http://foobar.com/profile.jpg");
  }
}
