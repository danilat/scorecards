package com.danilat.scorecards.core.domain.account;

import com.danilat.scorecards.shared.domain.errors.SimpleError;

public class AccountNotFoundError extends SimpleError {
    public AccountNotFoundError(AccountId accountId) {
        super("Account: " + accountId + " not found");
    }
}
