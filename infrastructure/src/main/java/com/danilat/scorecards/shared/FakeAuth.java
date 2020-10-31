package com.danilat.scorecards.shared;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.usecases.Auth;
import org.springframework.stereotype.Component;

@Component
public class FakeAuth implements Auth {
    @Override
    public AccountId currentAccount() {
        return new AccountId("some_account_id");
    }
}
