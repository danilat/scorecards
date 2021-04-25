package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountNotFoundError;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RetrieveAccountTest extends UseCaseUnitTest<Account> {
    @Mock
    private PrimaryPort primaryPort;
    @Mock
    private AccountRepository accountRepository;
    private Account existingAccount;
    private AccountId accountId = new AccountId("danilat");
    private RetrieveAccount retrieveAccount;

    @Before
    public void setup() {
        existingAccount = AccountMother.anAccountWithUsername(accountId.value());
        when(accountRepository.get(accountId)).thenReturn(Optional.of(existingAccount));
        retrieveAccount = new RetrieveAccount(accountRepository);
    }

    @Test
    public void givenAnExistingAccountThenIsRetrieved() {
        retrieveAccount.execute(getPrimaryPort(), accountId);

        Account account = getSuccessEntity();
        assertEquals(existingAccount, account);
    }

    @Test
    public void givenAnNonExistingBoxerThenIsNotRetrieved() {
        retrieveAccount.execute(getPrimaryPort(), new AccountId("un-existing id"));

        Error errors = getError();
        assertTrue(errors instanceof AccountNotFoundError);
    }

    @Override
    public PrimaryPort getPrimaryPort() {
        return primaryPort;
    }
}
