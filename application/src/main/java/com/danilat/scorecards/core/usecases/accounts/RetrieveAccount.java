package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountNotFoundError;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.usecases.UseCase;

import java.util.Optional;

public class RetrieveAccount implements UseCase<Account, AccountId> {
    private AccountRepository accountRepository;

    public RetrieveAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(PrimaryPort<Account> primaryPort, AccountId id) {
        Optional<Account> optionalAccount = accountRepository.get(id);
        if (optionalAccount.isPresent()) {
            primaryPort.success(optionalAccount.get());
        } else {
            primaryPort.error(new AccountNotFoundError(id));
        }
    }
}
