package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccountRepository extends InMemoryRepository<Account, AccountId> implements AccountRepository {

}
