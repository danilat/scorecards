package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.github.javafaker.Faker;

public class AccountMother extends BaseMother {
  public static Account anAccountWithUsername(String username){
    return new Account(new AccountId(username), username, faker().name().fullName(), faker().internet().emailAddress(), faker().internet().avatar());
  }

  public static Account anAccountWithEmail(String email){
    return new Account(new AccountId("foobar"), faker().name().username(), faker().name().fullName(), email, faker().internet().avatar());
  }
}
