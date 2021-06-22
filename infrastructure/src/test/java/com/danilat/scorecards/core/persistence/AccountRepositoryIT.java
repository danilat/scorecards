package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.persistence.jdbc.JdbcAccountRepository;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;

import java.util.Optional;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcConfig.class, JdbcAccountRepository.class, SimpleMeterRegistry.class})
@RunWith(SpringRunner.class)
public class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;
    private Account anAccount = AccountMother.anAccountWithEmail("irrelevant@email.com");

    @Before
    public void setup() {
        accountRepository.clear();
    }

    @Test
    public void saveAnAccount() {
        accountRepository.save(anAccount);

        Optional retrieved = accountRepository.get(anAccount.id());
        assertTrue(retrieved.isPresent());
        assertEquals(retrieved.get(), anAccount);
    }

    @Test
    public void findByEmailFromAnExistingAccount() {
        accountRepository.save(anAccount);

        Optional retrieved = accountRepository.findByEmail(anAccount.email());

        assertTrue(retrieved.isPresent());
    }

    @Test
    public void findByUsernameFromAnExistingAccount() {
        accountRepository.save(anAccount);

        Optional retrieved = accountRepository.findByUsername(anAccount.username());

        assertTrue(retrieved.isPresent());
    }

    @Test
    public void findByUsernameFromAnExistingAccountIgnoringTheCamelCase() {
        accountRepository.save(anAccount);

        Optional retrieved = accountRepository.findByUsername(anAccount.username().toUpperCase());

        assertTrue(retrieved.isPresent());
    }
}
